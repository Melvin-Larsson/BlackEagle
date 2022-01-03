package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.model.WordNumber

class EditNumbersListRecyclerViewAdapter(val context : Context, liveData: LiveData<List<WordNumber>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<EditNumbersListViewHolder>() {
    private var wordNumber: List<WordNumber> = ArrayList<WordNumber>()
    var onNumberWordClicked: ((WordNumber) -> Unit) = {}

    var liveData = liveData
    set(value){
        //FIXME: Unsafe question marks?, could observeLiveData be removed?
        field?.removeObservers(lifecycleOwner)
        field = value
        observeLiveData()
    }
    init {
       observeLiveData()
    }
    private fun observeLiveData(){
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
        holder.binding.root.setOnClickListener{
            onNumberWordClicked(wordNumber[position])
        }
    }

    override fun getItemCount(): Int {
        return wordNumber.size
    }
}