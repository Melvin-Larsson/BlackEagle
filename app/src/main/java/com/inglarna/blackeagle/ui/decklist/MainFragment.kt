package com.inglarna.blackeagle.ui.decklist

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckPagerBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.cardlist.CardListRecyclerViewAdapter
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    lateinit var binding : FragmentDeckPagerBinding
    lateinit var callback: DeckSelectedCallback

    companion object{
        fun newInstance() = MainFragment()
    }
    interface DeckSelectedCallback{
        fun onDeckSelected(deck: Deck)
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDeckPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Setup adapter
        Log.d("MainFrag", "onViewCreated")
        val adapter = DeckFragmentPagerAdapter(this)
        adapter.onDeckSelected = {
            callback.onDeckSelected(it)
        }
        binding.deckPager.adapter = adapter
        TabLayoutMediator(binding.deckTabLayout, binding.deckPager){tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.all)
                1 -> getString(R.string.folders)
                2 -> getString(R.string.favourites)
                else -> ""
            }
        }.attach()
        binding.addDeckButton.setOnClickListener{
            showCreateDeckDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DeckSelectedCallback){
            callback = context
        }
    }

    private fun showCreateDeckDialog(){
        val deckEditText = EditText(requireContext())
        deckEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.deck_to_add)
            .setView(deckEditText)
            .setPositiveButton(R.string.add_deck){dialog, _ ->
                //Add deck to database
                val deck = Deck()
                deck.name = deckEditText.text.toString()
                val deckDao = BlackEagleDatabase.getInstance(requireContext()).deckDao()
                GlobalScope.launch{
                    deckDao.insertDeck(deck)
                }
                Toast.makeText(requireContext(), "Added $deck", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }
}