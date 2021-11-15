package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckListRecyclerViewAdapter(val context : Context) : RecyclerView.Adapter<DeckListViewHolder>() {
    private var decks: List<DeckWithCards> = ArrayList<DeckWithCards>()
    lateinit var onDeckClicked: ((Deck) -> Unit)

    init {
        GlobalScope.launch {
            val database = BlackEagleDatabase.getInstance(context)
            val deckDao = database.deckDao()
            decks = deckDao.getDecks()
        }
    }

    interface DeckListRecyclerViewListener{
        fun onDeckClicked(deck : Deck)
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
            onDeckClicked(decks[position].deck)
        }
    }

    override fun getItemCount(): Int {
        return decks.size
    }
}