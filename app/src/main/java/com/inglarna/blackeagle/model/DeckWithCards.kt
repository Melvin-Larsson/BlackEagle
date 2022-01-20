package com.inglarna.blackeagle.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.room.Embedded
import androidx.room.Relation
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.repository.DeckRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.collections.ArrayList

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "deckId"
    )
    val cards: List<Card>
){
    companion object{
        private const val TAG = "DeckWithCards"
        private const val BUFFER_SIZE = 2048
        private const val DECK_FILE_NAME = "deck"
        const val FILE_TYPE = "be"

        fun import(context: Context, source: Uri){
            val destination = File(context.filesDir, UUID.randomUUID().toString())
            unzip(context, source, destination)
            val deckWithCards = parseDeckWithCards(File(destination, DECK_FILE_NAME))
            saveDeck(context, deckWithCards, destination)
        }
        private fun unzip(context: Context, source: Uri, destination: File){
            if(!destination.exists()){
                destination.mkdir()
            }
            val zipInputStream = ZipInputStream(BufferedInputStream(context.contentResolver.openInputStream(source)))
            var filename: String
            var zipEntry: ZipEntry?
            val buffer = ByteArray(BUFFER_SIZE)
            var count: Int
            while(true){
                zipEntry = zipInputStream.nextEntry
                if(zipEntry == null){
                    break
                }
                filename = zipEntry.name
                if(zipEntry.isDirectory){
                    val directory = File(destination, filename)
                    directory.mkdir()
                    continue
                }

                val fileOutputStream = FileOutputStream(File(destination, filename))
                while(true){
                    count = zipInputStream.read(buffer)
                    if(count == -1){
                        break
                    }
                    fileOutputStream.write(buffer, 0, count)
                }
                fileOutputStream.close()
                zipInputStream.closeEntry()
            }
        }

        private fun parseDeckWithCards(source: File): DeckWithCards{
            val deck = Deck()
            val cards = ArrayList<Card>()
            val reader = BufferedReader(FileReader(source))

            deck.name = reader.readLine()
            val repetitionDataWritten = reader.readLine().toBoolean()

            var row: String?
            while(true){
                row = reader.readLine()
                if(row == null){
                    break
                }
                val card = Card()
                card.cardId = row.toLong()
                card.question = reader.readLine()
                card.answer = reader.readLine()
                card.hint = reader.readLine()
                card.position = reader.readLine().toInt()
                if(repetitionDataWritten){
                    card.lastRepetition = reader.readLine().toDouble()
                    card.nextRepetition = reader.readLine().toDouble()
                    card.repetitions = reader.readLine().toInt()
                    card.easinessFactor = reader.readLine().toDouble()
                }
                cards.add(card)
            }
            reader.close()
            return DeckWithCards(deck, cards)
        }

        private fun saveDeck(context: Context, deckWithCards: DeckWithCards, imageSource: File){
            GlobalScope.launch {
                val cardRepo = CardRepo(context)
                val deckRepo = DeckRepo(context)

                val deckId = deckRepo.addDeck(deckWithCards.deck)

                for(card in deckWithCards.cards) {
                    card.deckId = deckId
                    val oldCardId = card.cardId!!
                    card.cardId = null
                    cardRepo.addCard(card)
                    extractImage(imageSource, context.filesDir, oldCardId, card.cardId!!)

                    //Rename image references in text fields
                    val imageFiles = PictureUtils.getImageFilesFromId(imageSource, oldCardId)
                    val idRegex = Regex("^\\d+")
                    for(image in imageFiles){
                        val oldImgTag = "<img src=\"${image.name}\">"
                        val newImgTag = "<img src=\"${card.cardId}${image.name.replace(idRegex, "")}\">"
                        card.question = card.question.replace(oldImgTag, newImgTag)
                        card.answer = card.answer.replace(oldImgTag, newImgTag)
                        card.hint = card.hint.replace(oldImgTag, newImgTag)
                        cardRepo.updateCard(card)
                    }
                }
                deleteDirectory(imageSource)
            }
        }
        private fun extractImage(sourceFolder: File, destinationFolder: File, oldImageId: Long, newImageId: Long){
            val imageFiles = PictureUtils.getImageFilesFromId(sourceFolder, oldImageId)
            val cardIdRegex = Regex("^$oldImageId")

            for (file in imageFiles){
                val newFileName = file.name.replace(cardIdRegex, newImageId.toString())
                val destination = File(destinationFolder, newFileName)
                file.copyTo(destination)
            }
        }
        private fun deleteDirectory(directory: File){
            val files = directory.listFiles()
            if(files != null){
                for(file in files){
                    if(file.isDirectory){
                        deleteDirectory(file)
                    }else{
                        file.delete()
                    }
                }
            }
            directory.delete()
        }
    }

    
    fun export(context: Context, destinationFolder: Uri){
        val tempDir = File(context.filesDir, UUID.randomUUID().toString())
        if(!tempDir.exists()){
            tempDir.mkdir()
        }
        writeImageFiles(context, tempDir)
        writeDeck(tempDir)
        zip(context, tempDir, destinationFolder)
        deleteDirectory(tempDir)
    }
    private fun writeImageFiles(context: Context, destinationFolder: File){
        val imageFiles = getImageFiles(context.filesDir)
        for(imageFile in imageFiles){
            Log.d(TAG, "file: " + imageFile.name)
            val destinationFile = File(destinationFolder, imageFile.name)
            if(destinationFile.exists()){
                destinationFile.delete()
            }
            imageFile.copyTo(destinationFile)
        }
    }
    private fun getImageFiles(source: File): List<File>{
        val imageFiles = ArrayList<File>()
        val files = source.listFiles()
        if(files != null){
            val cardIdRegex = Regex("^\\d+")
            for (file in files){
                val cardId = cardIdRegex.find(file.name)
                if(cardId != null && isCardInDeck(cardId.value.toLong())){
                    imageFiles.add(file)
                }
            }
        }
        return imageFiles
    }
    private fun isCardInDeck(cardId: Long): Boolean{
        for(card in cards){
            if(card.cardId == cardId){
                return true
            }
        }
        return false
    }

    private fun writeDeck(destinationFolder: File, writeRepetitionData: Boolean = false){
        val outputStreamWriter = OutputStreamWriter(FileOutputStream(File(destinationFolder, DECK_FILE_NAME)))
        outputStreamWriter.write(deck.name + "\n")
        outputStreamWriter.write(writeRepetitionData.toString() + "\n")
        for(card in cards){
            outputStreamWriter.write(card.cardId.toString() + "\n")
            outputStreamWriter.write(card.question + "\n")
            outputStreamWriter.write(card.answer + "\n")
            outputStreamWriter.write(card.hint + "\n")
            outputStreamWriter.write(card.position.toString() + "\n")
            if(writeRepetitionData){
                outputStreamWriter.write(card.lastRepetition.toString() + "\n")
                outputStreamWriter.write(card.nextRepetition.toString() + "\n")
                outputStreamWriter.write(card.repetitions.toString() + "\n")
                outputStreamWriter.write(card.easinessFactor.toString())

            }
        }
        outputStreamWriter.close()
    }
    private fun zip(context: Context, source: File, destination: Uri){
        val zipOutputStream = ZipOutputStream(BufferedOutputStream(context.contentResolver.openOutputStream(destination)))
        val data = ByteArray(BUFFER_SIZE)
        val files = source.listFiles()
        if(files != null){
            for(file in files){
                val origin = BufferedInputStream(FileInputStream(file), BUFFER_SIZE)
                val entry = ZipEntry(file.name)
                zipOutputStream.putNextEntry(entry)
                var count: Int
                while(true){
                    count = origin.read(data, 0, BUFFER_SIZE)
                    if(count == -1){
                        break
                    }
                    zipOutputStream.write(data, 0, count)
                }
                origin.close()
            }
            zipOutputStream.close()
        }
    }
}