package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.addcard.AddCardActivity
import com.inglarna.blackeagle.ui.decklist.MainFragment
import com.inglarna.blackeagle.ui.question.QuestionActivity
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding
    lateinit var onAddCardClicked: (()-> Unit)
    lateinit var editCardSelectedCallBack: EditCardSelectedCallBack
    private val cardViewModel by viewModels<CardViewModel>()
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var adapter : CardListRecyclerViewAdapter
    private var favoriteButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var deckId: Long= -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newInstance(deckId: Long) : CardListFragment{
            val fragment = CardListFragment()
            val bundle = Bundle()
            
            bundle.putLong(DECK_ID, deckId)
            fragment.arguments = bundle
            return fragment
        }
    }
    interface EditCardSelectedCallBack{
        fun onEditCardSelected(card: Card)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_list_menu, menu)
        favoriteButton = menu.findItem(R.id.favoriteStudy)
        deleteButton = menu.findItem((R.id.delete))
        deckViewModel.getDecks()?.observe(this,{ decks->
            for(deckWithCards in decks){
                if (deckWithCards.deck.id == deckId){
                    setFavoriteIcon(deckWithCards.deck.favorite)
                }
            }
        })

    }
    //when clicking in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
            R.id.startStudy -> startStudy()
            R.id.favoriteStudy -> favorites()
            R.id.select -> select()
            R.id.delete -> delete()
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckId = arguments!!.getLong(DECK_ID, -1)
        adapter = CardListRecyclerViewAdapter(cardViewModel.getDeckViews(deckId), this, this)
        binding.recyclerViewCard.adapter = adapter
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            onAddCardClicked()
        }
        //Actionbar title
        deckViewModel.getDeck(deckId).observe(this, {deck->
            getActivity()!!.setTitle("title")
            /*val actionbar = activity
            actionbar!!.title = deck.name*/
        })

        adapter.onEditCardClicked = { card ->
            editCardSelectedCallBack.onEditCardSelected(card)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is EditCardSelectedCallBack){
            editCardSelectedCallBack = context
        }
    }
    private fun startStudy(){
        startActivity(QuestionActivity.newIntent(requireContext(), deckId))
    }
    private fun favorites() {
        val deckDao = BlackEagleDatabase.getInstance(activity!!).deckDao()

        GlobalScope.launch {
            val deck = deckDao.getDeck(deckId)
            deck.favorite = !deck.favorite
            deckDao.updateDeck(deck)
        }
    }
    private fun select(){
        adapter.select = !adapter.select
        deleteButton?.isVisible = adapter.select
    }
    private fun delete(){
        val selectedCards = adapter.selectedCards.toMutableList()
        GlobalScope.launch {
            for(card in selectedCards){
                cardViewModel.deleteCard(card)
            }
        }
        adapter.select = false
        deleteButton?.isVisible = false
    }
    private fun setFavoriteIcon(favorite: Boolean){
        if (favorite){
            favoriteButton?.setIcon(R.drawable.ic_favorite_study)
        }else{
            favoriteButton?.setIcon(R.drawable.ic_favorite_study_border)
        }
    }
}