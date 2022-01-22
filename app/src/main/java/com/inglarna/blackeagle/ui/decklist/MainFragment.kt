package com.inglarna.blackeagle.ui.decklist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentDeckPagerBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Deck
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    lateinit var binding : FragmentDeckPagerBinding
    private val viewModel by viewModels<DeckListViewModel>()

    private var isFabExpanded = false

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
        binding.deckPager.adapter = adapter
        TabLayoutMediator(binding.deckTabLayout, binding.deckPager){tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.all)
                1 -> getString(R.string.folders)
                2 -> getString(R.string.favourites)
                else -> ""
            }
        }.attach()
        //Expand fab menu button
        binding.expandFab.setOnClickListener{
            if(isFabExpanded) {
                collapseFabMenu()
            }else{
                expandFabMenu()
            }
        }
        binding.addDeckFab.setOnClickListener{
            addDeck()
            collapseFabMenu()
        }
        binding.addFolderFab.setOnClickListener{
            addFolder()
            collapseFabMenu()
        }
    }
    private fun expandFabMenu(){
        isFabExpanded = true
        //Add deck button
        binding.addDeckFab.animate().translationY(-180f)
        binding.addDeckActionText.animate().translationY(-180f).alpha(1f)
        //Add folder button
        binding.addFolderFab.animate().translationY(-340f)
        binding.addFolderActionText.animate().translationY(-340f).alpha(1f)
    }
    private fun collapseFabMenu(){
        isFabExpanded = false
        //Add deck button
        binding.addDeckFab.animate().translationY(0f)
        binding.addDeckActionText.animate().translationY(0f).alpha(0f)
        //Add folder button
        binding.addFolderFab.animate().translationY(0f)
        binding.addFolderActionText.animate().translationY(0f).alpha(0f)
    }

    private fun addDeck(){
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
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun addFolder(){
        val folderEditText = EditText(requireContext())
        folderEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.folder_to_add))
            .setView(folderEditText)
            .setPositiveButton(R.string.add_folder){dialog, _ ->
                viewModel.addFolder(folderEditText.text.toString())
                dialog.dismiss()
            }
            .create()
            .show()
    }
}