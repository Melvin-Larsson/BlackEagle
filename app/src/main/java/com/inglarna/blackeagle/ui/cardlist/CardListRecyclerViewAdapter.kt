package com.inglarna.blackeagle.ui.cardlist

import android.util.Log
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

        holder.binding.textViewAnswer.text = cards[position].question
        holder.binding.textViewQuestion.text = cards[position].answer
        holder.binding.checkBox.setOnCheckedChangeListener { checkbox, isChecked ->
            if (isChecked) {
                selectedCards.add(cards[position])
            } else {
                selectedCards.remove(cards[position])
            }
        }
        val params = holder.binding.aroundQuestionAnswer.layoutParams as ConstraintLayout.LayoutParams
        val checkboxView = holder.binding.checkBox
        val aroundquestionAnswer = holder.binding.aroundQuestionAnswer

        if(select){
            checkboxView.visibility = View.VISIBLE
            checkboxView.isChecked = selectedCards.contains(cards[position])
            params.startToEnd = checkboxView.id
            params.startToStart = ConstraintLayout.LayoutParams.UNSET

            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300F, context.getResources().getDisplayMetrics()).toInt()
            val layout: LinearLayout = aroundquestionAnswer
            val params1: ViewGroup.LayoutParams = layout.layoutParams
            params1.width = width
            params1.height = FrameLayout.LayoutParams.WRAP_CONTENT
            layout.layoutParams = params1

        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = checkboxView.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
            //TODO: fixa den där width grejen på card
            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350F, context.getResources().getDisplayMetrics()).toInt()
            val layout: LinearLayout = aroundquestionAnswer
            val params1: ViewGroup.LayoutParams = layout.layoutParams
            params1.width = width
            params1.height = FrameLayout.LayoutParams.WRAP_CONTENT
            layout.layoutParams = params1
        }
    }
    override fun getItemCount(): Int = cards.size

}