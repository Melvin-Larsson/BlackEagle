package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.model.Deck

class DeckListRecyclerViewAdapter(val context : Context) : RecyclerView.Adapter<DeckListViewHolder>() {
    private val decks = arrayOf(Deck("Kortlek 1"), Deck("Kortlek 2"), Deck("Kortlek 3"))
    lateinit var onDeckClicked: ((Deck) -> Unit)

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
        holder.binding.textViewDeckName.text = decks[position].name
        holder.binding.textViewCardCount.text = context.resources.getString(R.string.card_count, decks[position].cards.size)
        holder.itemView.setOnClickListener{
            onDeckClicked(decks[position])
        }
    }

    override fun getItemCount(): Int {
        return decks.size
    }
}