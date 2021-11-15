package com.inglarna.blackeagle.ui.cardlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card

class CardListRecyclerViewAdapter: RecyclerView.Adapter<CardListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val binding = ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        /*holder.binding.textViewAnswer.text = cards[position].question
        holder.binding.textViewQuestion.text = cards[position].answer*/
    }

    override fun getItemCount(): Int = 0
}