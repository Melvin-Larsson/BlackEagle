package com.inglarna.blackeagle.ui.folder

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditFolderBinding

class EditFolderFragment: Fragment() {

    private lateinit var binding: FragmentEditFolderBinding
    private lateinit var viewModel: EditFolderViewModel

    companion object{
        private const val FOLDER_ID = "folderId"
        fun newInstance(folderId: Long): EditFolderFragment{
            val fragment = EditFolderFragment()
            val bundle = Bundle()
            bundle.putLong(FOLDER_ID, folderId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = arguments!!.getLong(FOLDER_ID, -1)
        viewModel = ViewModelProvider(this, EditFolderViewModelFactory(activity!!.application, folderId))[EditFolderViewModel::class.java]

        binding.viewModel = viewModel

        binding.renameFolderButton.setOnClickListener{
            editFolderName()
        }

        viewModel.folder.observe(viewLifecycleOwner){ folder ->
            if(folder == null){
                requireActivity().finish()
            }else{
                requireActivity().title = folder.name
            }
        }
    }

    private fun editFolderName(){
        val folderNameEditText = EditText(requireContext()) //TODO: requireContext() or getContext() (in the whole project)
        folderNameEditText.setText(viewModel.folder.value!!.name) //FIXME: This safe?
        folderNameEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.rename_folder_dialog_title)
            .setView(folderNameEditText)
            .setPositiveButton(R.string.edit){dialog, _ ->
                val newName = folderNameEditText.text.toString()
                viewModel.renameFolder(newName)
                dialog.dismiss()
            }
            .create()
            .show()
    }
}