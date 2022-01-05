package com.inglarna.blackeagle.ui.editnumbers

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.WordNumber
import com.inglarna.blackeagle.viewmodel.wordNumberViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditNumbersFragment: Fragment() {
    private lateinit var binding: FragmentEditNumbersBinding
    private lateinit var wordNumberRecyclerViewAdapter: EditNumbersListRecyclerViewAdapter
    private val wordNumberViewModel by viewModels<wordNumberViewModel>()
    private var resetButton: MenuItem? = null
    private var checkBoxButton: MenuItem? = null

    companion object{
        fun newInstance() = EditNumbersFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNumbersBinding.inflate(inflater, container, false)
        binding.textInputSearch?.addTextChangedListener{
            wordNumberRecyclerViewAdapter.liveData = wordNumberViewModel.getWords(binding.textInputSearch?.text.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordNumberRecyclerViewAdapter = EditNumbersListRecyclerViewAdapter(requireContext(), wordNumberViewModel.getNumberViews(), this)
        wordNumberRecyclerViewAdapter.onNumberWordClicked = {
            showEditNumberWordDialog(it)
        }
        binding.editNumbersRecyclerview.adapter = wordNumberRecyclerViewAdapter
        binding.editNumbersRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    //toolbar
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.edit_numbers_menu, menu)
        resetButton = menu.findItem(R.id.reset)
        checkBoxButton = menu.findItem(R.id.checkBoxNumbers)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.reset -> resetWordNumber()
            R.id.checkBoxNumbers -> checkBoxVisibility()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetWordNumber() {
        var selectedNumbers = wordNumberRecyclerViewAdapter.selectedWordNumbers
        var defaultWords = BlackEagleDatabase.loadDefaultNumberWords(context!!)
        for(selectedNumber in selectedNumbers){
            selectedNumber.word = defaultWords[selectedNumber.number]
        }
        GlobalScope.launch {
            wordNumberViewModel.updateWords(selectedNumbers)
        }
    }

    private fun checkBoxVisibility() {
        Toast.makeText(context, "checkbox", Toast.LENGTH_SHORT).show()
        wordNumberRecyclerViewAdapter.select = !wordNumberRecyclerViewAdapter.select

    }

    private fun showEditNumberWordDialog(wordNumber: WordNumber){
        val wordNumberEditText = EditText(context)
        wordNumberEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(context)
            .setTitle(context?.resources?.getString(R.string.title_edit_number_word, wordNumber.number))
            .setView(wordNumberEditText)
            .setPositiveButton(R.string.update){ dialog, _ ->
                GlobalScope.launch {
                    wordNumber.word = wordNumberEditText.text.toString()
                    wordNumberViewModel.updateWord(wordNumber)
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }
}