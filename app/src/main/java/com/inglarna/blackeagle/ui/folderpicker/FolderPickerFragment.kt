package com.inglarna.blackeagle.ui.folderpicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentFolderPickerBinding
import com.inglarna.blackeagle.viewmodel.FolderPickerViewModel

class FolderPickerFragment: Fragment() {
    private lateinit var binding: FragmentFolderPickerBinding
    private lateinit var adapter: FolderPickerRecyclerViewAdapter
    private val viewModel by viewModels<FolderPickerViewModel>()

    companion object{
        public const val FOLDER_ID = "folderId"
        fun newInstance(): FolderPickerFragment = FolderPickerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFolderPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FolderPickerRecyclerViewAdapter()
        adapter.onFolderPressed = {
            endActivity(it.folderId!!)
        }
        binding.folderPickerRecyclerview.adapter = adapter
        binding.folderPickerRecyclerview.layoutManager = LinearLayoutManager(context)

        observeData()

    }
    private fun observeData(){
        viewModel.folders.observe(this, {
            adapter.folders = it
        })
    }
    private fun endActivity(folderId: Long){
        val result = Intent()
        result.putExtra(FOLDER_ID, folderId)
        activity!!.setResult(Activity.RESULT_OK, result)
        activity!!.finish()
    }
}