package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.model.WordNumber

class EditNumbersListRecyclerViewAdapter(val context : Context, private val liveData: LiveData<List<WordNumber>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<EditNumbersListViewHolder>() {

    private var wordNumber: List<WordNumber> = ArrayList<WordNumber>()

    init {
        liveData?.observe(lifecycleOwner){
            wordNumber = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditNumbersListViewHolder {
        val binding = ListItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditNumbersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditNumbersListViewHolder, position: Int) {
        holder.binding.textViewNumber.text = wordNumber[position].number.toString()
        holder.binding.textViewWord.text = wordNumber[position].word
    }

    override fun getItemCount(): Int {
        return wordNumber.size
    }
}