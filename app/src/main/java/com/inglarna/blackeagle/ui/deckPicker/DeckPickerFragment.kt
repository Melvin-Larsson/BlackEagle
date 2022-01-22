package com.inglarna.blackeagle.ui.deckPicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentDeckPickerBinding

class DeckPickerFragment : Fragment() {
    private lateinit var binding: FragmentDeckPickerBinding
    private lateinit var adapter: DeckPickerRecyclerViewAdapter
    private val viewModel by viewModels<DeckPickerViewModel>()

    companion object{
        private const val FOLDER_ID = "folderId"
        const val SELECTED_DECKS = "selectedDecks"
        fun newInstance(folderId: Long): DeckPickerFragment{
            val fragment = DeckPickerFragment()
            val bundle = Bundle()
            bundle.putLong(FOLDER_ID, folderId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /*Required because if the default implementation of the back button is used, the previous activity will be destroyed and recreated
              losing all of its data even if the data is saved in viewModel/saveInstanceState*/
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeckPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model
        val folderId = arguments!!.getLong(FOLDER_ID, -1)
        viewModel.setExcludedFolder(folderId)

        //Setup recyclerview
        adapter = DeckPickerRecyclerViewAdapter(viewModel, viewLifecycleOwner)
        binding.recyclerViewDeck.adapter = adapter
        binding.recyclerViewDeck.layoutManager = LinearLayoutManager(context)
        viewModel.decks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        //Add to folder button
        binding.buttonAddToFolder.setOnClickListener{
            endActivity()
        }
    }

    private fun endActivity(){
        //Retrieve deck ids
        val selectedDecks = viewModel.selectedDecks.value!!.toList()
        val selectedDecksIds = LongArray(selectedDecks.size){ i -> //TODO: source https://kotlinlang.org/docs/basic-types.html#primitive-type-arrays
            selectedDecks[i].deckId!!
        }
        //end activity
        val result = Intent()
        result.putExtra(SELECTED_DECKS, selectedDecksIds)
        activity!!.setResult(Activity.RESULT_OK, result)
        activity!!.finish()

    }
}