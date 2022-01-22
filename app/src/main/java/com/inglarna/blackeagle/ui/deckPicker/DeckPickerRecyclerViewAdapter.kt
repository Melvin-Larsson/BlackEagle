package com.inglarna.blackeagle.ui.deckPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.inglarna.blackeagle.databinding.ListItemDeckPickerBinding
import com.inglarna.blackeagle.model.Deck

class DeckPickerRecyclerViewAdapter(private val viewModel: DeckPickerViewModel, private val lifecycleOwner: LifecycleOwner) : ListAdapter<Deck, DeckPickerViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckPickerViewHolder {
        val binding = ListItemDeckPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeckPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeckPickerViewHolder, position: Int) {
        holder.binding.deck = getItem(position)
        holder.binding.viewModel = viewModel
        holder.binding.lifecycleOwner = lifecycleOwner
    }
}
class DiffCallback : DiffUtil.ItemCallback<Deck>(){
    override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean {
        return oldItem.deckId == newItem.deckId
    }

    override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
        return oldItem == newItem
    }

}