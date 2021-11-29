package com.inglarna.blackeagle.ui.cardlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck

class CardListRecyclerViewAdapter (private val liveData: LiveData<List<Card>>?, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<CardListViewHolder>() {
    private var cards: List<Card> = ArrayList<Card>()
    val selectedCards: MutableList<Card> = ArrayList<Card>() //TODO: Prevent other classes from changing the content
    var select = false
        set(value){
            field = value
            selectedCards.clear()
            notifyDataSetChanged()
        }

    init {
        liveData?.observe(lifecycleOwner){
            cards = it
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val binding = ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        holder.binding.textViewAnswer.text = cards[position].question
        holder.binding.textViewQuestion.text = cards[position].answer
        holder.binding.checkBox.setOnCheckedChangeListener { checkbox, isChecked ->
            if (isChecked) {
                selectedCards.add(cards[position])
            } else {
                selectedCards.remove(cards[position])
            }
        }
        if(select){
            holder.binding.checkBox.visibility = View.VISIBLE
            holder.binding.checkBox.isChecked = selectedCards.contains(cards[position])
        }else{
            holder.binding.checkBox.visibility = View.INVISIBLE
        }
    }
    override fun getItemCount(): Int = cards.size
}