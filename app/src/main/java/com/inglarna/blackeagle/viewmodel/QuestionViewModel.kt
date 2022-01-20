package com.inglarna.blackeagle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.ui.question.DefaultCardScrambler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application, val deckId: Long) : AndroidViewModel(application){
    private var cardRepo = CardRepo(getApplication())

    private var cardScrambler = DefaultCardScrambler()

    private var newCardCount = 40
    private var oldCardCount = 40

    private lateinit var cards: MutableList<Card>

    private val cardIndex = MutableLiveData<Int>()

    private val _card = cardIndex.map { cardIndex ->
        isAnswerShowing.value = false
        isHintShowing.value = false
        if(cardIndex < cards.size){
            cards[cardIndex]
        }else{
            Card()
        }
    }

    val done = cardIndex.map { cardIndex ->
        !_loading.value!! && cardIndex >= cards.size
    }

    private val _loading = MutableLiveData(false)

    val question = _card.map {
        it.question
    }
    val hint = _card.map {
        it.hint
    }
    val answer = _card.map {
        it.answer
    }

    val isAnswerShowing = MutableLiveData(false)
    val isHintShowing = MutableLiveData(false)

    companion object{
        const val NEW_CARDS = 0
        const val OLD_CARDS = 1
        const val ALL_CARDS = 2
    }

    /**
     * @param cardType NEW_CARDS, OLD_CARDS or ALL_CARDS
     */
    fun loadCards(cardType: Int = ALL_CARDS){
        _loading.value = true
        viewModelScope.launch {
            var newCardList: List<Card>? = null
            var oldCardList: List<Card>? = null

            if(cardType != OLD_CARDS){
                newCardList = cardRepo.getNewCardsByNextRepetition(deckId, newCardCount)
            }
            if(cardType != NEW_CARDS){
                oldCardList = cardRepo.getOldCardsByNextRepetition(deckId, oldCardCount)
            }

            cards = cardScrambler.scramble(newCardList, oldCardList)
            Log.d("Tag", "size: " + newCardList!!.size)
            cardIndex.value = 0
            _loading.value = false
        }
    }

    fun setRetrievability(retrievability: Double){
        if(_card.value != null){
            _card.value!!.repeated(retrievability)
            GlobalScope.launch {
                cardRepo.updateCard(_card.value!!)
            }
            //Card is to be repeated again today
            if(_card.value!!.isNextRepetitionToday()){
                //Move the card 10 positions back in the deck
                cards.remove(_card.value)
                cards.add(10.coerceAtMost(cards.size), _card.value!!)
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