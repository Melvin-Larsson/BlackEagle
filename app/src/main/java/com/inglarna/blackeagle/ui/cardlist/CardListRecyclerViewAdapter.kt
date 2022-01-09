package com.inglarna.blackeagle.ui.cardlist

import android.util.Log
import android.annotation.SuppressLint
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
import java.io.File
import kotlin.collections.HashSet
import kotlin.math.absoluteValue


class CardListRecyclerViewAdapter(liveData: LiveData<List<Card>>?,lifecycleOwner: LifecycleOwner,private val context: Context):
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

    private val imageGetter = object: Html.ImageGetter {
        override fun getDrawable(source: String): Drawable {
            val drawable = BitmapDrawable(context.resources, BitmapFactory.decodeFile(File(context.filesDir, source).path))
            drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
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
        //Data on card
        val question = replaceImages(cards[position].question)
        val answer = replaceImages(cards[position].answer)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            holder.binding.textViewQuestion.text = Html.fromHtml(question, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
            holder.binding.textViewAnswer.text = Html.fromHtml(answer, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
        }else{
            holder.binding.textViewQuestion.text = Html.fromHtml(question,imageGetter,null).trim()
            holder.binding.textViewAnswer.text = Html.fromHtml(answer, imageGetter,null).trim()

        }
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

    private fun replaceImages(string: String): String{
        val imageRegex = Regex("<img src=\".*\">")
        return string.replace(imageRegex, "(img)")
    }
}