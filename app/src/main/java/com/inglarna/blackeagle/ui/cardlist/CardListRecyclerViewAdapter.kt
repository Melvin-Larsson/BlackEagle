package com.inglarna.blackeagle.ui.cardlist

import android.util.Log
import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card

import android.widget.LinearLayout
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import com.inglarna.blackeagle.R
import java.util.*
import kotlin.collections.ArrayList
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.collections.HashSet
import kotlin.math.absoluteValue


class CardListRecyclerViewAdapter(liveData: LiveData<List<Card>>?,lifecycleOwner: LifecycleOwner,private val context: CardListFragment):
    RecyclerView.Adapter<CardListViewHolder>(), SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {
    private var cards: List<Card> = ArrayList()
    val selectedCards: MutableSet<Card> = HashSet() //TODO: Prevent other classes from changing the content
    val movedCards: MutableSet<Card> = HashSet()
    var onEditCardClicked: ((Card) -> Unit) = {}
    var onDeleteCardClicked: ((Card) -> Unit) = {}
    var selectMultipleCallback: (() -> Unit) = {}
    var onStartDrag : ((RecyclerView.ViewHolder) -> Unit) = {}
    private var longClickPosition = -1
    var select = false
        set(value){
            field = value
            selectedCards.clear()
            notifyDataSetChanged()
        }
    companion object{
        const val TAG = "CardListRecyclerViewAdapter"
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
    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        holder.binding.dragHandle.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if(event.action == MotionEvent.ACTION_DOWN){
                    onStartDrag(holder)
                }
                return false
            }
        })
        //Click listener
        holder.itemView.setOnClickListener{

            if (select){
                holder.binding.checkBox.isChecked = !holder.binding.checkBox.isChecked
            }else{
                onEditCardClicked(cards[position])
            }
        }
        //Long click
        holder.itemView.setOnLongClickListener {
            if (!select){
                select = !select
                selectMultipleCallback()
            }
            true
        }

        //Data on card
        holder.binding.textViewQuestion.text = context.resources.getString(R.string.card_question, cards[position].question)
        holder.binding.textViewAnswer.text = context.resources.getString(R.string.card_answer, cards[position].answer)
        val cardNumber = position + 1
        holder.binding.cardNumber.text = cardNumber.toString()
        //Checkbox
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
            Log.d(TAG, "hej")
            checkboxView.visibility = View.VISIBLE
            checkboxView.isChecked = selectedCards.contains(cards[position])
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            params.startToEnd = checkboxView.id

        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = checkboxView.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }
    override fun getItemCount(): Int = cards.size

    fun selectAll(){
        if (selectedCards.size == cards.size){
            selectedCards.clear()
        }else{
            selectedCards.clear()
            for (c in cards){
                selectedCards.add(c)
            }
        }
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {

        var step = if (fromPosition < toPosition) {
            1
        } else {
            -1
        }
        for (i in fromPosition..toPosition - step) {
            //Swap card positions
            val temp = cards[i].position
            cards[i].position = cards[i + step].position
            cards[i + step].position = temp
            Collections.swap(cards, i, i + step)
            //Add to moved set
            movedCards.add(cards[i])
            movedCards.add(cards[i + step])
        }
        notifyItemMoved(fromPosition, toPosition)

    }
}