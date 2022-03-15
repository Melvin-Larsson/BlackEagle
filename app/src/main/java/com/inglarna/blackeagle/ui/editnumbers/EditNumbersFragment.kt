package com.inglarna.blackeagle.ui.editnumbers

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.WordNumber
import com.inglarna.blackeagle.viewmodel.wordNumberViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditNumbersFragment: Fragment() {
    private lateinit var binding: FragmentEditNumbersBinding
    private lateinit var adapter: EditNumbersListRecyclerViewAdapter
    private val wordNumberViewModel by viewModels<wordNumberViewModel>()
    private var resetButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var closeSelectButton: MenuItem? = null


    val timer = object: CountDownTimer(2000, 1) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            binding.scrollTop.visibility = View.GONE
            binding.scrollBottom.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "korvMedMos"
        fun newInstance() = EditNumbersFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EditNumbersListRecyclerViewAdapter(
            requireContext(),
            wordNumberViewModel.getNumberViews(),
            this
        )
        adapter.onNumberWordClicked = {
            showEditNumberWordDialog(it)
        }
        adapter.selectMultipleCallback = {
            toolbarVisibility()
        }
        binding.scrollBottom.setOnClickListener {
            binding.editNumbersRecyclerview.scrollToPosition(adapter.itemCount - 1)
            timer.cancel()
            timer.start()
        }
        binding.scrollTop.setOnClickListener{
            binding.editNumbersRecyclerview.scrollToPosition(0)
            timer.cancel()
            timer.start()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.editNumbersRecyclerview.setOnScrollChangeListener { view, _, scrollY, _, oldScrollY ->
                //when starting the activity the scrollChangeListener heard someone talk, therefore we implemented this if that fixes that
                setScrollButton(oldScrollY - scrollY)
            }
        }else{
            binding.editNumbersRecyclerview.setOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, scrollY: Int) {
                    super.onScrolled(recyclerView, dx, scrollY)
                    setScrollButton(scrollY)
                }
            })
        }

        adapter.filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = wordNumberViewModel.getWords(constraint.toString())
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                adapter.liveData = (results?.values as LiveData<List<WordNumber>>)
            }

        }
        binding.editNumbersRecyclerview.adapter = adapter
        binding.editNumbersRecyclerview.layoutManager = LinearLayoutManager(requireContext())

    }

    //toolbar
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.edit_numbers_menu, menu)
        resetButton = menu.findItem(R.id.reset)
        closeSelectButton = menu.findItem(R.id.closeSelect)
        selectAllButton = menu.findItem(R.id.selectAllNumbers)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.selectAllNumbers -> selectAll()
            R.id.reset -> resetWordNumber()
            R.id.closeSelect -> closeSelect()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectAll() {
        adapter.selectAll()
    }

    private fun resetWordNumber() {
        //Update words
        val selectedNumbers = adapter.selectedWordNumbers.toHashSet()
        val defaultWords = BlackEagleDatabase.loadDefaultNumberWords(context!!)
        for (selectedNumber in selectedNumbers) {
            selectedNumber.word = defaultWords[selectedNumber.number]
        }
        GlobalScope.launch {
            wordNumberViewModel.updateWords(selectedNumbers)
        }
        //Unselect
        adapter.select = false
        toolbarVisibility()
    }

    private fun toolbarVisibility() {
        resetButton?.isVisible = adapter.select
        selectAllButton?.isVisible = adapter.select
        closeSelectButton?.isVisible = adapter.select
    }

    private fun closeSelect() {
        adapter.select = !adapter.select
        toolbarVisibility()
    }

    private fun showEditNumberWordDialog(wordNumber: WordNumber) {
        val wordNumberEditText = EditText(context)
        wordNumberEditText.setText(wordNumber.word)
        wordNumberEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(context)
            .setTitle(
                context?.resources?.getString(
                    R.string.title_edit_number_word,
                    wordNumber.number
                )
            )
            .setView(wordNumberEditText)
            .setPositiveButton(R.string.update) { dialog, _ ->
                GlobalScope.launch {
                    wordNumber.word = wordNumberEditText.text.toString()
                    wordNumberViewModel.updateWord(wordNumber)
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun setScrollButton(scrollY: Int){
        if (scrollY != 0) {
            binding.scrollTop.visibility = View.VISIBLE
            binding.scrollBottom.visibility = View.VISIBLE
            timer.start()
        }
    }
}