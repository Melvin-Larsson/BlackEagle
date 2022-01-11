package com.inglarna.blackeagle.ui.folderpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemFolderPickerBinding
import com.inglarna.blackeagle.model.Folder

class FolderPickerRecyclerViewAdapter: RecyclerView.Adapter<FolderPickerViewHolder>() {
    var folders : List<Folder> = ArrayList()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    var onFolderPressed: ((Folder) -> Unit) = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderPickerViewHolder {
        val binding = ListItemFolderPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderPickerViewHolder, position: Int) {
        holder.binding.folder = folders[position]
        holder.itemView.setOnClickListener{
            onFolderPressed(folders[position])
        }
    }

    override fun getItemCount(): Int = folders.size
}