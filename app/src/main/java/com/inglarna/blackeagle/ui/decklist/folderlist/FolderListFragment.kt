package com.inglarna.blackeagle.ui.decklist.folderlist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentFolderListBinding
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.ui.cardlist.EditDeckActivity
import com.inglarna.blackeagle.ui.decklist.EditableListActivity
import com.inglarna.blackeagle.ui.folder.FolderActivity
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

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentFolderListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FolderListRecyclerViewAdapter(context!!, viewModel, viewLifecycleOwner)
        adapter.onFolderSelected = { folder ->
            startActivity(FolderActivity.newIntent(context!!, folder.folderId!!))
        }
        binding.folderRecyclerView.adapter = adapter
        binding.folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.folders.observe(this, {
            adapter.folders = it
        })
        viewModel.select.observe(this){select ->
            (requireActivity() as EditableListActivity).setSelectVisibility(select)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
            R.id.delete -> viewModel.removeSelectedFolders()
            R.id.selectAll -> viewModel.toggleSelectAll()
            R.id.closeSelect -> viewModel.setSelect(false)
        }
        return true
    }

}