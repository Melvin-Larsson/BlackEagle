package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.WordNumber

class EditNumbersListRecyclerViewAdapter(val context : Context, liveData: LiveData<List<WordNumber>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<EditNumbersListViewHolder>() {
    private var wordNumber: List<WordNumber> = ArrayList()
    var onNumberWordClicked: ((WordNumber) -> Unit) = {}
    val selectedWordNumbers: MutableList<WordNumber> = ArrayList()
    var select = false
        set(value){
            field = value
            selectedWordNumbers.clear()
            notifyDataSetChanged()
        }

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
            if (!select) {
                onNumberWordClicked(wordNumber[position])
            }else{
                holder.binding.checkBoxNumbers.isChecked = !holder.binding.checkBoxNumbers.isChecked
            }
        }
        holder.binding.checkBoxNumbers.setOnCheckedChangeListener { checkbox, ischecked ->
            if (ischecked){
                selectedWordNumbers.add(wordNumber[position])
            }else{
                selectedWordNumbers.remove(wordNumber[position])
            }
        }
        val params = holder.binding.aroundWordNumber.layoutParams as ConstraintLayout.LayoutParams
        val checkboxView = holder.binding.checkBoxNumbers
        if(select){
            checkboxView.visibility = View.VISIBLE
            params.startToEnd = holder.binding.checkBoxNumbers.id
            params.startToStart = ConstraintLayout.LayoutParams.UNSET
            holder.binding.checkBoxNumbers.isChecked = selectedWordNumbers.contains(wordNumber[position])
        }else{
            checkboxView.visibility = View.INVISIBLE
            params.startToStart = holder.binding.wordNumberLayout.id
            params.startToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }

    override fun getItemCount(): Int {
        return wordNumber.size
    }
}