package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.databinding.ListItemNumberSearchBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.WordNumber
//FIXME: It might be possible for the searchViewHolder to get removed from memory, hence resetting the filter and displaying all word number pairs, might need to fix
class EditNumbersListRecyclerViewAdapter(val context : Context, liveData: LiveData<List<WordNumber>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var wordNumber: List<WordNumber> = ArrayList()
    var onNumberWordClicked: ((WordNumber) -> Unit) = {}
    val selectedWordNumbers: MutableSet<WordNumber> = HashSet()
    var filter: Filter? = null
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

    companion object{
        private const val TAG = "EditNumbersAdapter"
        private const val NUMBER_VIEW_HOLDER = 0
        private const val SEARCH_VIEW_HOLDER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == SEARCH_VIEW_HOLDER){
            val binding = ListItemNumberSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EditNumbersSearchViewHolder(binding)
        }
        val binding = ListItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditNumbersListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EditNumbersListViewHolder) {
            val position = position - 1 //Searchbar is at position 0
            holder.binding.textViewNumber.text = wordNumber[position].number.toString()
            holder.binding.textViewWord.text = wordNumber[position].word
            holder.binding.root.setOnClickListener {
                if (!select) {
                    onNumberWordClicked(wordNumber[position])
                } else {
                    holder.binding.checkBoxNumbers.isChecked =
                        !holder.binding.checkBoxNumbers.isChecked
                }
            }
            holder.binding.root.setOnLongClickListener{


                true
            }
            holder.binding.checkBoxNumbers.setOnCheckedChangeListener { checkbox, ischecked ->
                if (ischecked) {
                    selectedWordNumbers.add(wordNumber[position])
                } else {
                    selectedWordNumbers.remove(wordNumber[position])
                }
            }
            val params =
                holder.binding.aroundWordNumber.layoutParams as ConstraintLayout.LayoutParams
            val checkboxView = holder.binding.checkBoxNumbers
            if (select) {
                checkboxView.visibility = View.VISIBLE
                params.startToEnd = holder.binding.checkBoxNumbers.id
                params.startToStart = ConstraintLayout.LayoutParams.UNSET
                holder.binding.checkBoxNumbers.isChecked =
                    selectedWordNumbers.contains(wordNumber[position])
            } else {
                checkboxView.visibility = View.INVISIBLE
                params.startToStart = holder.binding.wordNumberLayout.id
                params.startToEnd = ConstraintLayout.LayoutParams.UNSET
            }
        }else if(holder is EditNumbersSearchViewHolder){
            holder.binding.textInputSearch?.addTextChangedListener{
                if(filter != null){
                    filter?.filter(it.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return wordNumber.size + 1 //Always an editNumberSearchViewHolder in addition to WordNumbers
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            SEARCH_VIEW_HOLDER
        }else{
            NUMBER_VIEW_HOLDER
        }
    }

    fun selectAll(){
        if (selectedWordNumbers.size == wordNumber.size){
            selectedWordNumbers.clear()
        }else{
            selectedWordNumbers.clear()
            for (w in wordNumber){
                selectedWordNumbers.add(w)
            }
        }
        notifyDataSetChanged()
    }
}