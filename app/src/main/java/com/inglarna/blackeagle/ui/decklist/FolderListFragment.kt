package com.inglarna.blackeagle.ui.decklist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentFolderListBinding
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.viewmodel.FolderListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FolderListFragment : Fragment() {
    private lateinit var binding : FragmentFolderListBinding
    private val viewModel by viewModels<FolderListViewModel>()
    private lateinit var adapter: FolderListRecyclerViewAdapter
    private lateinit var addFolderMenuItem: MenuItem
    companion object{
        fun newInstance(): FolderListFragment = FolderListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentFolderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FolderListRecyclerViewAdapter(context!!)
        adapter.onDeckClicked = {
            startActivity(CardListActivity.newIntent(context!!, it.deckId))
        }
        binding.folderRecyclerView.adapter = adapter
        binding.folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeData()
    }
    private fun observeData(){
        viewModel.folderWithDecks.observe(this, {
            adapter.folders = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.folder_list_menu, menu)
        addFolderMenuItem = menu.findItem(R.id.button_add_folder)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.button_add_folder -> addFolder()
        }
        return true
    }
    private fun addFolder(){
        val folderEditText = EditText(requireContext())
        folderEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.folder_to_add))
            .setView(folderEditText)
            .setPositiveButton(R.string.add_folder){dialog, _ ->
                //Add folder to database
                GlobalScope.launch {
                    val folder = Folder()
                    folder.name = folderEditText.text.toString()
                    viewModel.addFolder(folder)
                }
                dialog.dismiss()
            }
            .create()
            .show()
    }
}