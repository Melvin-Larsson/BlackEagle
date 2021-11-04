package com.inglarna.blackeagle.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card

class CardListRecyclerViewAdapter: RecyclerView.Adapter<CardListViewHolder>() {
    private val cards = arrayOf(Card("Question1", "Answer1", "Hint1", 1), Card("Question2", "Answer2", "Hint2", 2), Card("Question3", "Answer3", "Hint3", 3))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val binding = ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        holder.binding.textViewAnswer.text = cards[position].question
        holder.binding.textViewQuestion.text = cards[position].answer
    }

    override fun getItemCount(): Int = cards.size
}