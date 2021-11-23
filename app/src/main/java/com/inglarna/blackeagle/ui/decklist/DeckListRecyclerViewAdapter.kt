package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
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

class DeckListRecyclerViewAdapter(val context : Context, private val liveData: LiveData<List<DeckViewModel.DeckView>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<DeckListViewHolder>() {
    private var decks: List<DeckViewModel.DeckView> = ArrayList<DeckViewModel.DeckView>()
    lateinit var onDeckClicked: ((DeckViewModel.DeckView) -> Unit)

    init {
        liveData?.observe(lifecycleOwner){
            decks = it
            notifyDataSetChanged()
        }
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
    }

    override fun getItemCount(): Int {
        return decks.size
    }
}