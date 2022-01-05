package com.inglarna.blackeagle.ui.cardlist

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


class CardListRecyclerViewAdapter(liveData: LiveData<List<Card>>?,lifecycleOwner: LifecycleOwner,private val context: CardListFragment):
    RecyclerView.Adapter<CardListViewHolder>(), PopupMenu.OnMenuItemClickListener, SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {
    private var cards: List<Card> = ArrayList()
    val selectedCards: MutableList<Card> = ArrayList() //TODO: Prevent other classes from changing the content
    val movedCards: MutableSet<Card> = HashSet()
    var onEditCardClicked: ((Card) -> Unit) = {}
    var onStartDrag : ((RecyclerView.ViewHolder) -> Unit) = {}

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
            onEditCardClicked(cards[position])
            if (select){
                holder.binding.checkBox.isChecked = !holder.binding.checkBox.isChecked
            }
        }
        //Long click
        holder.itemView.setOnLongClickListener {
            if (!select){
                val popup = PopupMenu(holder.itemView.context, it)
                popup.setOnMenuItemClickListener(this)
                popup.inflate(R.menu.card_long_click_menu)
                popup.show()
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


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.editCard-> editCard()
            R.id.deleteCard-> delete()
        }
        return true
    }

    private fun editCard() {
        //Toast.makeText(itemView.context, "edit", Toast.LENGTH_LONG).show()

    }

    private fun delete(){
        //Toast.makeText(holder.itemView.context, "delete", Toast.LENGTH_LONG).show()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        var step = if(fromPosition < toPosition) {
            1
        }else{
            -1
        }
        for (i in fromPosition..toPosition - step){
            //Add to moved set
            movedCards.add(cards[i])
            movedCards.add(cards[i+1])
            //Swap card positions
            val temp = cards[i].position
            cards[i].position = cards[i + step].position
            cards[i + step].position = temp
            Collections.swap(cards, i, i + step)
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}