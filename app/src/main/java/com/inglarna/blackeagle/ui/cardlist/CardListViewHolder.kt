package com.inglarna.blackeagle.ui.cardlist

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemCardBinding
import com.inglarna.blackeagle.model.Card

class CardListViewHolder(val binding: ListItemCardBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }


    override fun onClick(p0: View?) {
        /*
        inspektera och edit
         */
    }

    override fun onLongClick(p0: View?): Boolean {
        Toast.makeText(itemView.context, "long click", Toast.LENGTH_LONG).show()
        return true
    }
}