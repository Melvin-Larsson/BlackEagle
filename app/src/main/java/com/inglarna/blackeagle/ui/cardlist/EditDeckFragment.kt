package com.inglarna.blackeagle.ui.cardlist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentEditDeckBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//TODO: lägg till confirm popup när delete trycks
class EditDeckFragment : Fragment() {
    lateinit var binding: FragmentEditDeckBinding
    private val deckViewModel by viewModels<DeckViewModel>()
    private var deckId: Long= -1
    private lateinit var deck: Deck
    private val startFileExplorerForResult = registerForActivityResult(ActivityResultContracts.CreateDocument()){ uri->
        if(uri != null){
            GlobalScope.launch {
                deckViewModel.getDeckWithCards(deckId).export(context!!, uri)
            }
        }
    }
    companion object{
        private const val DECK_ID = "deckID"
        fun newInstance(deckId: Long): EditDeckFragment {
            val EditDeckFragment = EditDeckFragment()
            val bundle = Bundle()
            bundle.putLong(DECK_ID, deckId)
            EditDeckFragment.arguments = bundle
            return EditDeckFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditDeckBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deckId = arguments!!.getLong(DECK_ID, -1)
        deckViewModel.getDeck(deckId).observe(this){

            if (it == null){
                activity?.finish()
            }else{
                deck = it
                setFavoriteIcon(it.favorite)
            }
        }
        binding.exportButton.setOnClickListener{
            startFileExplorerForResult.launch(deck.name + ".be")
        }
        binding.deleteDeckButton.setOnClickListener{
            deleteDeck()
        }
        binding.addToFavouritesButton.setOnClickListener{
            addToFavourites()
        }
        binding.editDeckNameButton.setOnClickListener{
            editDeckName(deck)
        }
        deckViewModel.getDecks()?.observe(this,{ decks->
            for(deckWithCards in decks){
                if (deckWithCards.deck.id == deckId){
                    setFavoriteIcon(deckWithCards.deck.favorite)
                }
            }
        })
    }

    private fun setFavoriteIcon(favorite: Boolean){
        if (favorite){
            binding.addToFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_fill, 0, 0, 0)
        }else{
            binding.addToFavouritesButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border, 0, 0, 0)
        }
    }

    private fun addToFavourites() {
        val deckDao = BlackEagleDatabase.getInstance(activity!!).deckDao()

        GlobalScope.launch {
            val deck = deckDao.getDeck(deckId)
            deck.favorite = !deck.favorite
            deckDao.updateDeck(deck)
        }
    }

    private fun deleteDeck() {
        GlobalScope.launch {
            deckViewModel.deleteDeck(deck)
        }
        activity?.finish()
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
                Toast.makeText(requireContext(), "edited $deck", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

}