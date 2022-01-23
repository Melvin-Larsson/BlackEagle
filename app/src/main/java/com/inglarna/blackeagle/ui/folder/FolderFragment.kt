package com.inglarna.blackeagle.ui.folder

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentFolderBinding
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.ui.cardlist.CardListViewModel
import com.inglarna.blackeagle.ui.cardlist.CardListViewModelFactory
import com.inglarna.blackeagle.ui.deckPicker.DeckPickerActivity
import com.inglarna.blackeagle.ui.deckPicker.DeckPickerFragment

class FolderFragment : Fragment() {
    lateinit var binding : FragmentFolderBinding
    lateinit var viewModel: FolderViewModel
    lateinit var adapter: FolderDeckRecyclerViewAdapter

    //Menu buttons
    private lateinit var deleteButton: MenuItem
    private lateinit var selectAllButton: MenuItem
    private lateinit var closeSelectButton: MenuItem
    private lateinit var addDeckButton: MenuItem
    private lateinit var editFolderButton: MenuItem

    private val startDeckPickerForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val selectedDecks = intent!!.getLongArrayExtra(DeckPickerFragment.SELECTED_DECKS)
            if(selectedDecks != null){
                viewModel.addDecks(selectedDecks!!)
            }
        }

    }

    companion object {
        private const val FOLDER_ID = "folderId"
        fun newInstance(folderId: Long): FolderFragment{
            val fragment = FolderFragment()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.folder_menu, menu)
        deleteButton = menu.findItem(R.id.delete)
        selectAllButton = menu.findItem(R.id.selectAllDecks)
        closeSelectButton = menu.findItem(R.id.closeSelect)
        addDeckButton = menu.findItem(R.id.addDeck)
        editFolderButton = menu.findItem(R.id.more)

        /*Must observe select after menu has been created,
          otherwise the visibility of menuOptions will be toggled before they reference anything
          causing an exception*/
        viewModel.select.observe(viewLifecycleOwner){ isSelecting ->
            setToolbarVisibility(isSelecting)
        }
    }
    private fun setToolbarVisibility(showSelect: Boolean){
        deleteButton.isVisible = showSelect
        selectAllButton.isVisible = showSelect
        closeSelectButton.isVisible = showSelect
        addDeckButton.isVisible = !showSelect
        editFolderButton.isVisible = !showSelect
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /*Required because if the default implementation of the back button is used, the previous activity will be destroyed and recreated
              losing all its data even if the data is saved in viewModel/saveInstanceState*/
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
            R.id.delete -> viewModel.removeSelectedDecks()
            R.id.selectAllDecks -> viewModel.toggleSelectAll()
            R.id.closeSelect -> viewModel.setSelect(false)
            R.id.addDeck -> startDeckPickerForResult.launch(DeckPickerActivity.newIntent(context!!, viewModel.folderId))
            R.id.more -> startActivity(EditFolderActivity.newIntent(requireContext(), viewModel.folderId))

        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val folderId = arguments!!.getLong(FOLDER_ID, -1)
        viewModel = ViewModelProvider(this, FolderViewModelFactory(activity!!.application, folderId))[FolderViewModel::class.java]

        adapter = FolderDeckRecyclerViewAdapter(viewModel, viewLifecycleOwner)
        adapter.onDeckSelected = { deck ->
            startActivity(CardListActivity.newIntent(context!!, deck.deckId))
        }

        binding.recyclerViewDeck.adapter = adapter
        binding.recyclerViewDeck.layoutManager = LinearLayoutManager(context)
        viewModel.folderWithDeck.observe(viewLifecycleOwner){ folderWithDecks ->
            if(folderWithDecks == null){
                requireActivity().finish()
            }else{
                adapter.submitList(folderWithDecks.decks)
                requireActivity().title = folderWithDecks.folder.name

            }
        }
    }
}