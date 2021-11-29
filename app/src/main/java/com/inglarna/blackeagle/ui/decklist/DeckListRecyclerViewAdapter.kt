package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemDeckBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckListRecyclerViewAdapter(val context : Context,
                                  private val liveData: LiveData<List<Deck>>?,
                                  private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<DeckListViewHolder>() {
    private var decks: List<Deck> = ArrayList<Deck>()
    lateinit var onDeckClicked: ((Deck) -> Unit)
    var delete = false
        set(value){
            field = value
            notifyDataSetChanged()
        }
    init {
        liveData?.observe(lifecycleOwner, {
            decks = it;
            notifyDataSetChanged()
        })
    }

    interface DeckListRecyclerViewListener{
        fun onDeckClicked(deck : Deck)
    }

    companion object{
        private const val TAG = "DeckListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListViewHolder {
        val binding = ListItemDeckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeckListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeckListViewHolder, position: Int) {

        holder.binding.textViewDeckName.text = decks[position].name
       // holder.binding.textViewCardCount.text = context.resources.getString(R.string.card_count, decks[position].cards.size)
        holder.itemView.setOnClickListener{
            onDeckClicked(decks[position])
        }
        val params = holder.binding.textViewDeckName.layoutParams as ConstraintLayout.LayoutParams
        val checkboxView = holder.binding.checkboxDeck
        if(delete){
            checkboxView.visibility = View.VISIBLE
            params.startToEnd = holder.binding.checkboxDeck.id
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = holder.binding.deckLayout.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }

    override fun getItemCount(): Int {
        return decks.size
    }
}