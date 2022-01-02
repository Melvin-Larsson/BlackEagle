package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.model.WordNumber

class EditNumbersListRecyclerViewAdapter(val context : Context) : RecyclerView.Adapter<EditNumbersListViewHolder>() {

    private var wordNumber: List<WordNumber> = ArrayList<WordNumber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditNumbersListViewHolder {
        val binding = ListItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditNumbersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditNumbersListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return wordNumber.size
    }


}