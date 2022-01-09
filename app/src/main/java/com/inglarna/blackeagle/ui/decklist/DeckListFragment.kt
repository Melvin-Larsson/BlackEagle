package com.inglarna.blackeagle.ui.decklist

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var deckSelectedCallback: DeckSelectedCallback
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var adapter: DeckListRecyclerViewAdapter
    private var deleteButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var closeButton: MenuItem? = null
    private var importButton: MenuItem? = null
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
        adapter = DeckListRecyclerViewAdapter(requireActivity(), data, requireActivity())
        binding.deckRecyclerView.adapter = adapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onDeckClicked = {
                deckSelectedCallback.onDeckSelected(it)
        }
        adapter.onDeleteDeckClicked={ deck ->
            GlobalScope.launch {
                deckViewModel.deleteDeck(deck)
            }
        }
        adapter.selectMultipleCallback = {
            toolbarVisibility()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.deck_list_menu, menu)
        deleteButton = menu.findItem((R.id.delete))
        selectAllButton = menu.findItem((R.id.selectAll))
        closeButton = menu.findItem((R.id.closeSelect))
        importButton = menu.findItem((R.id.importDeck))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> delete()
            R.id.selectAll -> selectAll()
            R.id.importDeck -> import()
            R.id.closeSelect -> closeSelect()
        }
        return true
    }
    private fun closeSelect() {
        adapter.select = !adapter.select
        toolbarVisibility()
    }
    private fun toolbarVisibility(){
        deleteButton?.isVisible = adapter.select
        selectAllButton?.isVisible = adapter.select
        closeButton?.isVisible = adapter.select
        importButton?.isVisible = !adapter.select
    }

    private fun delete(){
        val selectedDecks = adapter.selectedDecks.toMutableList()
        GlobalScope.launch {
            for (deck in selectedDecks){
                deckViewModel.deleteDeck(deck)
            }
        }
        adapter.select = false
        toolbarVisibility()
    }

    private fun selectAll() {
        adapter.selectAll()
    }

    private val startFileExplorerForResult = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            DeckWithCards.import(context!!, uri)
        }
    }
    private fun import(){
        startFileExplorerForResult.launch("*/*")
    }
}