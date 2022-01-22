package com.inglarna.blackeagle.ui.folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.inglarna.blackeagle.databinding.ListItemFolderDeckBinding
import com.inglarna.blackeagle.model.Deck

class FolderDeckRecyclerViewAdapter(private val viewModel: FolderViewModel, private val lifecycleOwner: LifecycleOwner) : ListAdapter<Deck,FolderDeckViewHolder>(DiffCallback()) {
    var onDeckSelected: (Deck) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderDeckViewHolder {
        val binding = ListItemFolderDeckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderDeckViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderDeckViewHolder, position: Int) {
        holder.binding.deck = getItem(position)
        holder.binding.viewModel = viewModel
        holder.binding.lifecycleOwner = lifecycleOwner
        holder.itemView.setOnClickListener{
            onDeckSelected(getItem(position))
        }
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