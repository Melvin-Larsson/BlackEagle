package com.inglarna.blackeagle.ui.decklist.decklist

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
import com.inglarna.blackeagle.ui.decklist.EditableListActivity
import com.inglarna.blackeagle.viewmodel.DeckListViewModel

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var deckSelectedCallback: DeckSelectedCallback
    private val deckListViewModel by viewModels<DeckListViewModel>()
    private lateinit var adapter: DeckListRecyclerViewAdapter
    private var deleteButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var closeButton: MenuItem? = null
    private var importButton: MenuItem? = null
    private var pageId = -1

    companion object{
        private const val TAG = "DeckListFragment"
        private const val PAGE_ID = "pageId"
        fun newInstance(deckPage: Int): DeckListFragment {
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

        adapter = DeckListRecyclerViewAdapter(requireActivity())
        binding.deckRecyclerView.adapter = adapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.onDeckClicked = {
            deckSelectedCallback.onDeckSelected(it)
        }
        adapter.onDeleteDeckClicked={ deck ->
            deckListViewModel.deleteDeck(deck)
        }
        adapter.onSelectionStarted = {
            (requireActivity() as EditableListActivity).setSelectVisibility(adapter.select)
        }
        observeData()
    }
    private fun observeData(){
        //Favorites
        val data : LiveData<List<DeckWithCards>> = if(pageId == 2){
            deckListViewModel.favoriteDecks
        }
        //Other
        else{
            deckListViewModel.decks
        }
        data.observe(this, {
            adapter.decks = it
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> delete()
            R.id.selectAll -> selectAll()
            //R.id.importDeck -> import()
            R.id.closeSelect -> closeSelect()
        }
        return true
    }
    private fun closeSelect() {
        adapter.select = false
        (requireActivity() as EditableListActivity).setSelectVisibility(false)
    }

    private fun delete(){
        val selectedDecks = adapter.selectedDecks.toList()
        deckListViewModel.deleteDecks(selectedDecks)
        adapter.select = false
        (requireActivity() as EditableListActivity).setSelectVisibility(false)
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