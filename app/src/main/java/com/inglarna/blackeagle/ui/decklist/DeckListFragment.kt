package com.inglarna.blackeagle.ui.decklist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.viewmodel.DeckViewModel

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var onDeckSelected: ((Deck) -> Unit)
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var deckRecyclerViewAdapter: DeckListRecyclerViewAdapter
    private var pageId = -1

    companion object{
        private const val PAGE_ID = "pageId"
        fun newInstance(deckPage: Int): DeckListFragment{
            val fragment = DeckListFragment()
            val bundle = Bundle()

            bundle.putInt(PAGE_ID, deckPage)
            fragment.arguments = bundle
            return fragment
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
        val data : LiveData<List<Deck>>? = if(pageId == 2){
            deckViewModel.getFavoriteDecks()
        }
        //Other
        else{
            deckViewModel.getDecks()
        }
        deckRecyclerViewAdapter = DeckListRecyclerViewAdapter(requireActivity(), data, requireActivity())
        deckRecyclerViewAdapter.onDeckClicked = onDeckSelected
        binding.deckRecyclerView.adapter = deckRecyclerViewAdapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.deck_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.checkbox -> checkboxVisibility()

        }
        return true
    }
    private fun checkboxVisibility(){
        deckRecyclerViewAdapter.delete = !deckRecyclerViewAdapter.delete
    }
}