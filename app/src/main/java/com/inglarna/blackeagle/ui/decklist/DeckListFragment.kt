package com.inglarna.blackeagle.ui.decklist

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.text.InputType
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.ui.card.CardActivity
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var deckSelectedCallback: DeckSelectedCallback
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var deckRecyclerViewAdapter: DeckListRecyclerViewAdapter
    private var deleteButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var pageId = -1

    companion object{
        private const val TAG = "DeckListFragment"
        private const val PAGE_ID = "pageId"
        fun newInstance(deckPage: Int): DeckListFragment{
            val fragment = DeckListFragment()
            val bundle = Bundle()
            bundle.putInt(PAGE_ID, deckPage)
            fragment.arguments = bundle
            return fragment
        }
    }

    interface DeckSelectedCallback{
        fun onDeckSelected(deck: Deck)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DeckSelectedCallback){
            deckSelectedCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pageId = arguments!!.getInt(PAGE_ID, -1)
        //Favorites
        val data : LiveData<List<DeckWithCards>>? = if(pageId == 2){
            deckViewModel.getFavoriteDecks()
        }
        //Other
        else{
            deckViewModel.getDecks()
        }
        deckRecyclerViewAdapter = DeckListRecyclerViewAdapter(requireActivity(), data, requireActivity())
        binding.deckRecyclerView.adapter = deckRecyclerViewAdapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        deckRecyclerViewAdapter.onDeckClicked = {
                deckSelectedCallback.onDeckSelected(it)
        }
        deckRecyclerViewAdapter.onDeleteDeckClicked={ deck ->
            GlobalScope.launch {
                deckViewModel.deleteDeck(deck)
            }
        }
        deckRecyclerViewAdapter.onEditDeckClicked={ deck ->
            editDeckName(deck)
        }
    }

    private fun editDeckName(deck: Deck) {
        val deckEditText = EditText(requireContext())
        deckEditText.setText(deck.name)
        deckEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle("Your new name of the deck")
            .setView(deckEditText)
            .setPositiveButton("Edit"){dialog, _ ->
                deck.name = deckEditText.text.toString()
                val deckDao = BlackEagleDatabase.getInstance(requireContext()).deckDao()
                GlobalScope.launch {
                    deckDao.updateDeck(deck)
                }
                Toast.makeText(requireContext(), "edited $deck", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.deck_list_menu, menu)
        deleteButton = menu.findItem((R.id.delete))
        selectAllButton = menu.findItem((R.id.selectAll))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.checkbox -> checkboxVisibility()
            R.id.delete -> delete()
            R.id.selectAll -> selectAll()
            R.id.importDeck -> import()
        }
        return true
    }

    private fun checkboxVisibility(){
        deckRecyclerViewAdapter.select = !deckRecyclerViewAdapter.select
        deleteButton?.isVisible = deckRecyclerViewAdapter.select
        selectAllButton?.isVisible = deckRecyclerViewAdapter.select
    }

    private fun delete(){
        val selectedDecks = deckRecyclerViewAdapter.selectedDecks.toMutableList()
        GlobalScope.launch {
            for (deck in selectedDecks){
                deckViewModel.deleteDeck(deck)
            }
        }
        deckRecyclerViewAdapter.select = false
        deleteButton?.isVisible = false
        selectAllButton?.isVisible = false
    }

    private fun selectAll() {
        deckRecyclerViewAdapter.selectAll()
    }

    private val startFileExplorerForResult = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        //FIXME: This probably does not work in all instances
        var path = uri.path
        if(path!!.contains("raw:")){
            path = path!!.substring(path.indexOf("raw:") + 4)
        }
        val deckWithCards = DeckWithCards.import(context!!, File(path))
    }
    private fun import(){
        startFileExplorerForResult.launch("*/*")
    }
}