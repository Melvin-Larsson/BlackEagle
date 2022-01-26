package com.inglarna.blackeagle.ui.cardlist

import android.app.Application
import androidx.lifecycle.*
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.repository.DeckRepo
import getCurrentDay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardListViewModel (application: Application, val deckId: Long): AndroidViewModel(application){
    private var cardRepo: CardRepo = CardRepo(getApplication())
    private var deckRepo: DeckRepo = DeckRepo(getApplication())

    val cards = cardRepo.getFullDeck(deckId)

    private val _select = MutableLiveData(false)
    val select: LiveData<Boolean>
        get() = _select

    //TODO: This correct? correct solution?
    //Livedata observers are only notified when the livedata gets a new value, i.e.
    //when livedata.setValue() is called but not when the contents of
    //the list are changed. To solve this the list is stored separately and put into
    //the livedata (setValue()) everytime the list contents change
    //https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata
    private var selectedCardsSet = mutableSetOf<Card>()
    private val _selectedCards: MutableLiveData<Set<Card>> = MutableLiveData(selectedCardsSet)
    val selectedCards: LiveData<Set<Card>> //TODO: write about this maybe?
        get() = _selectedCards

    val deck: LiveData<Deck> = deckRepo.getLiveDeck(deckId)

    //getCurrentDay() + 1.0 because getCurrentDay() returns the lowest possible value a card that should be studied today can have
    val deckFinished = cardRepo.getFullDeckByNextRepetition(deckId, getCurrentDay() + 1.0).map { cards ->
        cards.isEmpty()
    }

    fun deleteSelectedCards(){
        _select.value = false
        GlobalScope.launch { //FIXME: should Globalscope be used here? selectedCardsList.clear() on one hand, deleteCards() on the other
            cardRepo.deleteCards(selectedCardsSet)
            selectedCardsSet.clear()
        }
    }

    fun updateCards(cards: Set<Card>){
        cardRepo.updateCards(cards)
    }

    fun onCardLongClicked(card: Card): Boolean{
        setSelect(true)
        setSelectionState(card, true)
        return true //if method is called in onLongClick (in list_item_card.xml for example), a boolean return value is required
    }

    fun setSelect(select: Boolean): Boolean{ //TODO: is it necessary to return true here?
        _select.value = select
        selectedCardsSet.clear()
        return true
    }
    /**
     * Toggles the selection status of the card if the selection mode is on (select = true).
     * If the card is selected it becomes unselected and if the card is not selected it
     * becomes selected.
     */
    fun setSelectionState(card: Card, selected: Boolean){
        if(select.value!!){
            if(selected){
                selectedCardsSet.add(card)
            }else{
                selectedCardsSet.remove(card)
            }
            _selectedCards.value = selectedCardsSet
        }
    }

    fun toggleSelectAll(){
        selectedCardsSet = if(selectedCardsSet.size == cards.value!!.size){
            mutableSetOf()
        }else{
           cards.value!!.toMutableSet()
        }
        _selectedCards.value = selectedCardsSet
    }
}