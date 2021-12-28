package com.inglarna.blackeagle.ui.cardlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card

import android.widget.LinearLayout
import android.util.TypedValue
import android.widget.FrameLayout
import com.inglarna.blackeagle.R


class CardListRecyclerViewAdapter(private val liveData: LiveData<List<Card>>?, private val lifecycleOwner: LifecycleOwner, private val context: CardListFragment): RecyclerView.Adapter<CardListViewHolder>() {
    private var cards: List<Card> = ArrayList<Card>()
    lateinit var onEditCardClicked: ((Card) -> Unit)
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
        holder.itemView.setOnClickListener{
            onEditCardClicked(cards[position])
        }


        holder.binding.textViewQuestion.text = context.resources.getString(R.string.card_question, cards[position].question)
        holder.binding.textViewAnswer.text = context.resources.getString(R.string.card_answer, cards[position].answer)
        val cardNumber = position + 1
        holder.binding.cardNumber.text = cardNumber.toString()
        holder.binding.checkBox.setOnCheckedChangeListener { checkbox, isChecked ->
            if (isChecked) {
                selectedCards.add(cards[position])
            } else {
                selectedCards.remove(cards[position])
            }
        }
        val params = holder.binding.aroundQuestionAnswer.layoutParams as ConstraintLayout.LayoutParams
        val checkboxView = holder.binding.checkBox

        if(select){
            checkboxView.visibility = View.VISIBLE
            checkboxView.isChecked = selectedCards.contains(cards[position])
            params.startToEnd = checkboxView.id
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = checkboxView.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }
    override fun getItemCount(): Int = cards.size

}