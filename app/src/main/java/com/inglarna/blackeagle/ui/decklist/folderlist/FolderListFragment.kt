package com.inglarna.blackeagle.ui.decklist.folderlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentFolderListBinding
import com.inglarna.blackeagle.ui.decklist.EditableListActivity
import com.inglarna.blackeagle.ui.folder.FolderActivity

class FolderListFragment : Fragment() {
    private lateinit var binding : FragmentFolderListBinding
    private val viewModel by viewModels<FolderListViewModel>()
    private lateinit var adapter: FolderListRecyclerViewAdapter

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
        adapter = FolderListRecyclerViewAdapter(requireContext(), viewModel, viewLifecycleOwner)
        adapter.onFolderSelected = { folder ->
            startActivity(FolderActivity.newIntent(requireContext(), folder.folderId!!))
        }
        binding.folderRecyclerView.adapter = adapter
        binding.folderRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.folders.observe(viewLifecycleOwner){
            adapter.folders = it
        }
        viewModel.select.observe(viewLifecycleOwner){select ->
            (requireActivity() as EditableListActivity).setSelectVisibility(select)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> viewModel.removeSelectedFolders()
            R.id.selectAll -> viewModel.toggleSelectAll()
            R.id.closeSelect -> viewModel.setSelect(false)
        }
        return true
    }

}