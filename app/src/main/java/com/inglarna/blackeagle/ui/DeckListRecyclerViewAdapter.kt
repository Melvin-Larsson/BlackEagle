package com.inglarna.blackeagle.ui

import android.content.Context
import android.content.res.Resources
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck

class DeckListRecyclerViewAdapter(val context : Context) : RecyclerView.Adapter<DeckListViewHolder>() {
    private val decks = arrayOf(Deck("Kortlek 1"), Deck("Kortlek 2"), Deck("Kortlek 3"))

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
    }

    override fun getItemCount(): Int {
        return decks.size
    }
}