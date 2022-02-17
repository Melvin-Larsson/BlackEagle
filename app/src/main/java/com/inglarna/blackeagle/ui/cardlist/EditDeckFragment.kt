package com.inglarna.blackeagle.ui.cardlist

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditDeckBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.ui.folderpicker.FolderPickerActivity
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.folderpicker.FolderPickerFragment
import com.inglarna.blackeagle.viewmodel.EditDeckViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//TODO: lägg till confirm popup när delete trycks
class EditDeckFragment : Fragment() {
    lateinit var binding: FragmentEditDeckBinding
    private lateinit var viewModel: EditDeckViewModel
    private lateinit var deck: Deck
    private val startFileExplorerForResult = registerForActivityResult(ActivityResultContracts.CreateDocument()){ uri->
        if(uri != null){
            viewModel.export(uri)
        }
    }
    private val startFolderPickerForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data != null){
                val folderId = result.data?.getLongExtra(FolderPickerFragment.FOLDER_ID, -1)
                if(folderId!! >= 0){
                    viewModel.addDeckToFolder(folderId)
                }
            }
        }
    }
    companion object{
        private const val DECK_ID = "deckID"
        fun newInstance(deckId: Long): EditDeckFragment {
            val fragment = EditDeckFragment()
            val bundle = Bundle()
            bundle.putLong(DECK_ID, deckId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditDeckBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deckId = arguments!!.getLong(DECK_ID, -1)
        viewModel = ViewModelProvider(this, EditDeckViewModelFactory(activity!!.application, deckId)).get(
            EditDeckViewModel::class.java)
        viewModel.deck.observe(this){
            if (it == null){
                activity?.finish()
            }else{
                deck = it
                activity?.title = getString(R.string.edit_deck, deck.name)
                setFavoriteIcon(it.favorite)
            }
        }
        binding.exportButton.setOnClickListener{
            startFileExplorerForResult.launch(deck.name + ".be")
        }
        binding.deleteDeckButton.setOnClickListener{
            deleteDeck()
        }
        binding.addToFolderButton.setOnClickListener{
            startFolderPickerForResult.launch(FolderPickerActivity.newIntent(context!!))
        }
        binding.addToFavouritesButton.setOnClickListener{
            addToFavourites()
        }
        binding.editDeckNameButton.setOnClickListener{
            editDeckName(deck)
        }
    }

    private fun setFavoriteIcon(favorite: Boolean){
        if (favorite){
            binding.addToFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_fill, 0, 0, 0)
        }else{
            binding.addToFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border, 0, 0, 0)
        }
    }

    private fun deleteDeck() {
        viewModel.deleteDeck()
        activity?.finish()
    }

    private fun addToFavourites() {
        viewModel.updateDeck {
            it.favorite = !it.favorite
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
                dialog.dismiss()
            }
            .create()
            .show()
    }


}