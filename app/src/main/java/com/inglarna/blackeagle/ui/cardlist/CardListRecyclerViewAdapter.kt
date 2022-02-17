package com.inglarna.blackeagle.ui.cardlist

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card
import android.view.*
import java.util.*
import android.view.MotionEvent
import kotlin.collections.HashSet


class CardListRecyclerViewAdapter(private val viewModel: CardListViewModel, private val lifecycleOwner: LifecycleOwner):
    RecyclerView.Adapter<CardListViewHolder>(), SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {
    var onCardSelected: (Card, Boolean) -> Unit = {_,_ -> }
    var cards: List<Card> = listOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    val movedCards: MutableSet<Card> = HashSet()
    var onStartDrag : ((RecyclerView.ViewHolder) -> Unit) = {}

    companion object{
        const val TAG = "CardListRecyclerViewAdapter"

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        val binding = ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {
        holder.binding.card = cards[position]
        holder.binding.position = position + 1
        holder.binding.viewModel = viewModel
        holder.binding.lifecycleOwner = lifecycleOwner
        holder.binding.dragHandle.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if(event.action == MotionEvent.ACTION_DOWN){
                    onStartDrag(holder)
                }
                return false
            }
        })
        holder.itemView.setOnClickListener {
            onCardSelected(cards[position], holder.binding.checkBox.isChecked)
        }
    }
    override fun getItemCount(): Int = cards.size

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