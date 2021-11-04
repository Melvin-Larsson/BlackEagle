package com.inglarna.blackeagle.ui.decklist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckPagerBinding
import com.inglarna.blackeagle.model.Deck

class MainFragment : Fragment() {

    lateinit var binding : FragmentDeckPagerBinding
    lateinit var onDeckSelected: ((Deck) -> Unit)

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDeckPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Setup adapter
        val adapter = DeckFragmentPagerAdapter(this)
        adapter.onDeckSelected = onDeckSelected
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
    private fun showCreateDeckDialog(){
        val deckEditText = EditText(requireContext())
        deckEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.deck_to_add)
            .setView(deckEditText)
            .setPositiveButton(R.string.add_deck){dialog, _ ->
                val deck = deckEditText.text.toString()
                //TODO add deck
                Toast.makeText(requireContext(), "Added $deck", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }
}