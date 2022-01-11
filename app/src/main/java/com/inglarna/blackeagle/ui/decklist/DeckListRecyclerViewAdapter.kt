package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

class DeckListRecyclerViewAdapter(val context : Context) : RecyclerView.Adapter<DeckListViewHolder>(), PopupMenu.OnMenuItemClickListener {
    var decks: List<DeckWithCards> = ArrayList()
    set(value){
        field = value
        notifyDataSetChanged()
    }
    private var longClickPosition = -1
    val selectedDecks: MutableSet<Deck> = HashSet()
    lateinit var onDeckClicked: ((Deck) -> Unit)
    var onSelectionStarted: (() -> Unit) = {}
    var onDeleteDeckClicked: ((Deck) -> Unit) = {}
    var select = false
        set(value){
            field = value
            selectedDecks.clear()
            notifyDataSetChanged()
        }

    companion object{
        private const val TAG = "DeckListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListViewHolder {
        val binding = ListItemDeckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeckListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeckListViewHolder, position: Int) {
        holder.binding.deckWithCards = decks[position]
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
                select = true
                selectedDecks.add(decks[position].deck)
                onSelectionStarted()
            }
            true
        }
        holder.binding.checkboxDeck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
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
            checkboxView.isChecked = selectedDecks.contains(decks[position].deck)
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            params.startToEnd = checkboxView.id
        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = checkboxView.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
        holder.binding.aroundDeck.layoutParams = params
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
            R.id.deleteDeck -> deleteDeck()
        }
        return true
    }

    private fun deleteDeck() {
        onDeleteDeckClicked(decks[longClickPosition].deck)
    }
}