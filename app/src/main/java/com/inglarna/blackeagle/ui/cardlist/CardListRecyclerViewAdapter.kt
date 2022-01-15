package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card
import android.view.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import java.io.File
import kotlin.collections.HashSet


class CardListRecyclerViewAdapter(private val context: Context):
    RecyclerView.Adapter<CardListViewHolder>(), SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {
    var cards: List<Card> = ArrayList()
    set(value){
        field = value
        notifyDataSetChanged()
    }
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val binding = ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        holder.binding.card = cards[position]
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
                select = true
                selectedCards.add(cards[position])
                selectMultipleCallback()
            }
            true
        }

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
            checkboxView.visibility = View.VISIBLE
            checkboxView.isChecked = selectedCards.contains(cards[position])
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            params.startToEnd = checkboxView.id

        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = checkboxView.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
        holder.binding.aroundQuestionAnswer.layoutParams = params
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
        val step = if (fromPosition < toPosition) {
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