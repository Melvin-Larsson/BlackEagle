package com.inglarna.blackeagle.ui.question

import android.app.Application
import androidx.lifecycle.*
import com.inglarna.blackeagle.QueryPreferences
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.repository.StatRepo
import getCurrentDay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

class QuestionViewModel(application: Application, val deckId: Long) : AndroidViewModel(application){
    private val cardRepo = CardRepo(getApplication())
    private val statRepo = StatRepo(getApplication())

    private var cardScrambler = DefaultCardScrambler()

    private var newCardQuota = 0.5

    private lateinit var cards: MutableList<Card>

    private val cardIndex = MutableLiveData<Int>()

    private val _card = cardIndex.map { cardIndex ->
        isAnswerShowing.value = false
        isHintShowing.value = false
        if(cardIndex < cards.size){
            cards[cardIndex]
        }else{
            null
        }
    }

    val done = cardIndex.map { cardIndex ->
        !_loading.value!! && cardIndex >= cards.size
    }

    private val _loading = MutableLiveData(false)

    val question = _card.map {
        it?.question ?: ""
    }
    val hint = _card.map {
        it?.hint ?: ""
    }
    val answer = _card.map {
        it?.answer ?: ""
    }

    val isAnswerShowing = MutableLiveData(false)
    val isHintShowing = MutableLiveData(false)

    /**
     * @param cardType NEW_CARDS, OLD_CARDS or ALL_CARDS
     */
    fun loadCards(cardsToReview : Int = 40){
        val cardsToReview = if(cardsToReview < 0){
            40
        }else{
            cardsToReview
        }
        _loading.value = true
        viewModelScope.launch {
            //New cards
            val tempNewCardCount = (cardsToReview * newCardQuota).roundToInt()
            val tempNewCardList = cardRepo.getNewCardsByNextRepetition(deckId, cardsToReview)
            //Old cards
            val oldCardCount = cardsToReview - min(tempNewCardList.size, tempNewCardCount)
            val oldCardList = cardRepo.getOldCardsByNextRepetition(deckId, oldCardCount)

            val newCardCount = min(tempNewCardList.size, cardsToReview - oldCardList.size)
            val newCardList = tempNewCardList.subList(0, newCardCount)

            cards = cardScrambler.scramble(newCardList, oldCardList)
            cardIndex.value = 0
            _loading.value = false
        }
    }
    fun setRetrievability(retrievability: Double){
        _card.value?.let { card ->
            card.repeated(retrievability)
            GlobalScope.launch {
                cardRepo.updateCard(card)
                statRepo.incrementCurrentStat()
            }
            //Card is to be repeated again today
            if(card.isNextRepetitionToday()){
                //Move the card 10 positions back in the deck
                cards.remove(card)
                cards.add(10.coerceAtMost(cards.size), card)
                cardIndex.value = cardIndex.value
            }else{
                //Select next card
                cardIndex.value = (cardIndex.value)?.inc()
            }
        }
    }

    fun showHint(){
        isHintShowing.value = true
    }
    fun showAnswer(){
        isAnswerShowing.value = true
    }
}