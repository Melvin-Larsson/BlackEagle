package com.inglarna.blackeagle.ui.editnumbers

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding
import com.inglarna.blackeagle.model.WordNumber
import com.inglarna.blackeagle.viewmodel.wordNumberViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditNumbersFragment: Fragment() {
    private lateinit var binding: FragmentEditNumbersBinding
    private lateinit var wordNumberRecyclerViewAdapter: EditNumbersListRecyclerViewAdapter
    private val wordNumberViewModel by viewModels<wordNumberViewModel>()

    companion object{
        fun newInstance() = EditNumbersFragment
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