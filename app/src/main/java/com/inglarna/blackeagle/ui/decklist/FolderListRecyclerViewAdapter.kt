package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ListItemFolderBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.FolderWithDecks

class FolderListRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<FolderListViewHolder>() {
    var onDeckClicked : ((Deck) -> Unit) = {}
    var folders : List<FolderWithDecks> = ArrayList()
    set(value){
        field = value
        notifyDataSetChanged()
    }
    private var expandedFolders = ArrayList<FolderWithDecks>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    companion object{
        private const val TAG = "FolderRecyclerAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderListViewHolder {
        val binding = ListItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderListViewHolder, position: Int) {
        holder.binding.folderWithDecks = folders[position]
        if(expandedFolders.contains(folders[position])){
            holder.binding.deckContainer.removeAllViews()
            val inflater = LayoutInflater.from(context)
            for(deck in folders[position].decks){
                val view = inflater.inflate(R.layout.list_item_deck, holder.binding.deckContainer, false)
                view.setOnClickListener{
                    onDeckClicked(deck)
                }
                val textView = view.findViewById(R.id.textViewDeckName) as TextView
                textView.text = deck.name
                holder.binding.deckContainer.addView(view)
            }
        }
        holder.itemView.setOnClickListener{
            //Shrink
            if(expandedFolders.contains(folders[position])){
                expandedFolders.remove(folders[position])
                holder.binding.deckContainer.removeAllViews()
                holder.binding.arrowImageDropDown.setImageDrawable(context.resources.getDrawable(R.drawable.ic_drop_up_arrow))
            }
            //Expand
            else{
                expandedFolders.add(folders[position])
                holder.binding.arrowImageDropDown.setImageDrawable(context.resources.getDrawable(R.drawable.ic_drop_down_arrow))

            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = folders.size
}