package com.inglarna.blackeagle.ui.editnumbers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterResults
import android.widget.Filterable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.ListItemNumberBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.WordNumber

class EditNumbersListRecyclerViewAdapter(val context : Context, liveData: LiveData<List<WordNumber>>?, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<EditNumbersListViewHolder>(),
    Filterable {
    private var wordNumber: List<WordNumber> = ArrayList()
    private var searchWordNumber: MutableList<WordNumber> = ArrayList()
    var onNumberWordClicked: ((WordNumber) -> Unit) = {}
    var selectMultipleCallback: (() -> Unit) = {}
    val selectedWordNumbers: MutableSet<WordNumber> = HashSet<WordNumber>()


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
            searchWordNumber.addAll(wordNumber)
            notifyDataSetChanged()
        }
    }
    companion object{
        private const val TAG = "adapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditNumbersListViewHolder {
        val binding = ListItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditNumbersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditNumbersListViewHolder, position: Int) {
        holder.binding.textViewNumber.text = searchWordNumber[position].number.toString()
        holder.binding.textViewWord.text = searchWordNumber[position].word
        holder.itemView.setOnClickListener{
            if (!select) {
                onNumberWordClicked(searchWordNumber[position])
            }else{
                holder.binding.checkBoxNumbers.isChecked = !holder.binding.checkBoxNumbers.isChecked
            }
        }
        holder.itemView.setOnLongClickListener {
            Log.d(TAG, "hej")
            if (!select){
                select = !select
                selectMultipleCallback()
            }
            true
        }
        holder.binding.checkBoxNumbers.setOnCheckedChangeListener { checkbox, ischecked ->
            if (ischecked){
                selectedWordNumbers.add(searchWordNumber[position])
            }else{
                selectedWordNumbers.remove(searchWordNumber[position])
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
        return searchWordNumber.size
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

    override fun getFilter(): Filter {
        return exampleFilter
    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<WordNumber> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                Log.d(TAG, "tjingeling")
                filteredList.addAll(wordNumber)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in wordNumber) {
                    if (item.word.toLowerCase().contains(filterPattern) || item.number.toString().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            searchWordNumber.clear()
            searchWordNumber.addAll(results.values as Collection<WordNumber>)
            notifyDataSetChanged()
        }
    }
}
