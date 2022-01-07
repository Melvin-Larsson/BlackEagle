package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.repository.CardRepo

class DeckListRecyclerViewAdapter(val context : Context,
                                  private val liveData: LiveData<List<DeckWithCards>>?,
                                  private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<DeckListViewHolder>(), PopupMenu.OnMenuItemClickListener {
    private var cardRepo = CardRepo(context)
    private var decks: List<DeckWithCards> = ArrayList<DeckWithCards>()
    private var longClickPosition = -1
    val selectedDecks: MutableSet<Deck> = HashSet<Deck>()
    lateinit var onDeckClicked: ((Deck) -> Unit)
    var onEditDeckClicked: ((Deck) -> Unit) = {}
    var onDeleteDeckClicked: ((Deck) -> Unit) = {}
    var select = false
        set(value){
            field = value
            selectedDecks.clear()
            notifyDataSetChanged()
        }
    init {
        liveData?.observe(lifecycleOwner, {
            decks = it
            notifyDataSetChanged()
        })
    }

    companion object{
        private const val TAG = "DeckListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListViewHolder {
        val binding = ListItemDeckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeckListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeckListViewHolder, position: Int) {
        holder.binding.textViewDeckName.text = decks[position].deck.name
        holder.binding.textViewCardCount.text = context.resources.getString(R.string.card_count, decks[position].cards.size)
        holder.itemView.setOnClickListener{
            if(!select){
                onDeckClicked(decks[position].deck)
            }else{
                holder.binding.checkboxDeck.isChecked = !holder.binding.checkboxDeck.isChecked
            }
        }

        //long click
        holder.itemView.setOnLongClickListener {
            if (!select){
                val popup = PopupMenu(holder.itemView.context, it)
                popup.setOnMenuItemClickListener(this)
                popup.inflate(R.menu.deck_long_click_menu)
                popup.show()
                longClickPosition = holder.adapterPosition
            }
            true
        }
        holder.binding.checkboxDeck.setOnCheckedChangeListener { checkbox, ischecked ->
            if (ischecked){
                selectedDecks.add(decks[position].deck)
            }else{
                selectedDecks.remove(decks[position].deck)
            }
        }
        //checkbox visibility
        val params = holder.binding.aroundDeck.layoutParams as ConstraintLayout.LayoutParams
        val checkboxView = holder.binding.checkboxDeck
        if(select){
            checkboxView.visibility = View.VISIBLE
            params.startToEnd = holder.binding.checkboxDeck.id
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            holder.binding.checkboxDeck.isChecked = selectedDecks.contains(decks[position].deck)
        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = holder.binding.deckLayout.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }

    override fun getItemCount(): Int {
        return decks.size
    }

    fun selectAll(){
        if (selectedDecks.size == decks.size) {
            selectedDecks.clear()
        }else{
            selectedDecks.clear()
            for (d in decks) {
                selectedDecks.add(d.deck)
            }
        }
        notifyDataSetChanged()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.editDeck -> editDeck()
            R.id.deleteDeck -> deleteDeck()
        }
        return true
    }

    private fun deleteDeck() {
        onDeleteDeckClicked(decks[longClickPosition].deck)
    }

    private fun editDeck() {
        onEditDeckClicked(decks[longClickPosition].deck)
    }

}