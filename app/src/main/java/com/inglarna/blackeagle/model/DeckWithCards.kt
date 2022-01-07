package com.inglarna.blackeagle.model

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.room.Embedded
import androidx.room.Relation
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.repository.DeckRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

data class DeckWithCards(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId"
    )
    val cards: List<Card>
){
    companion object{
        private const val TAG = "DeckWithCards"
        private const val BUFFER_SIZE = 2048
        private const val DECK_FILE_NAME = "deck"
        const val FILE_TYPE = "be"

        fun import(context: Context, source: File){
            val destination = File(context.filesDir, source.name.removeSuffix("." + source.extension))
            unzip(source, destination)
            val deckWithCards = parseDeckWithCards(File(destination, DECK_FILE_NAME))
            saveDeck(context, deckWithCards, destination)
        }
        private fun unzip(source: File, destination: File){
            if(!destination.exists()){
                destination.mkdir()
            }
            val zipInputStream = ZipInputStream(BufferedInputStream(FileInputStream(source)))
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
                card.id = row.toLong()
                card.question = reader.readLine()
                card.answer = reader.readLine()
                card.hint = reader.readLine()
                card.position = reader.readLine().toInt()
                if(repetitionDataWritten){
                    card.firstRepetition = reader.readLine().toDouble()
                    card.nextRepetition = reader.readLine().toDouble()
                    card.k = reader.readLine().toDouble()
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

                val oldCardIds = ArrayList<Long>()
                for(card in deckWithCards.cards) {
                    card.deckId = deckId
                    oldCardIds.add(card.id!!)
                    card.id = null
                }
                val newCardIds = cardRepo.addCards(deckWithCards.cards)

                for(i in newCardIds.indices){
                   extractImage(imageSource, context.filesDir, oldCardIds[i], newCardIds[i])
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

    
    fun export(context: Context){
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), deck.name)
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdir()){
                Log.d(TAG, "Error creating: " + mediaStorageDir.path)
            }
        }
        writeImageFiles(context, mediaStorageDir)
        writeDeck( mediaStorageDir)
        zip(mediaStorageDir, File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), deck.name + "." + FILE_TYPE))
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
            if(card.id == cardId){
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
            outputStreamWriter.write(card.id.toString() + "\n")
            outputStreamWriter.write(card.question + "\n")
            outputStreamWriter.write(card.answer + "\n")
            outputStreamWriter.write(card.hint + "\n")
            outputStreamWriter.write(card.position.toString() + "\n")
            if(writeRepetitionData){
                outputStreamWriter.write(card.firstRepetition.toString() + "\n")
                outputStreamWriter.write(card.nextRepetition.toString() + "\n")
                outputStreamWriter.write(card.k.toString())
            }
        }
        outputStreamWriter.close()
    }
    private fun zip(source: File, destination: File){
        val zipOutputStream = ZipOutputStream(BufferedOutputStream(FileOutputStream(destination)))
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