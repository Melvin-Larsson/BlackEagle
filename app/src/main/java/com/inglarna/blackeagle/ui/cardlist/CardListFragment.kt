package com.inglarna.blackeagle.ui.cardlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.ui.question.QuestionActivity
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.inglarna.blackeagle.ui.question.QuestionFragment
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.math.ceil

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding
    lateinit var onAddCardClicked: (()-> Unit)
    lateinit var editCardSelectedCallBack: EditCardSelectedCallBack
    private val cardViewModel by viewModels<CardViewModel>()
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var adapter : CardListRecyclerViewAdapter
    private var favoriteButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var startStudyButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var deckId: Long= -1
    private var deckFinishedToday = false

    companion object{
        private const val TAG = "CardListFragment"
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
        //toolbar
        setHasOptionsMenu(true)
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_list_menu, menu)
        selectAllButton = menu.findItem(R.id.selectAllCards)
        favoriteButton = menu.findItem(R.id.favoriteStudy)
        deleteButton = menu.findItem((R.id.delete))
        startStudyButton = menu.findItem(R.id.startStudy)
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
            R.id.select -> select()
            R.id.delete -> delete()
            R.id.startStudy -> study()
            R.id.favoriteStudy -> favorites()
            R.id.selectAllCards -> selectAll()
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //creating adapter
        deckId = arguments!!.getLong(DECK_ID, -1)
        adapter = CardListRecyclerViewAdapter(cardViewModel.getDeckViews(deckId), this, this)
        binding.recyclerViewCard.adapter = adapter
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            onAddCardClicked()
        }
        adapter.onDeleteCardClicked={ card ->
            GlobalScope.launch {
                cardViewModel.deleteCard(card)
            }
        }
        //Actionbar title
        deckViewModel.getDeck(deckId).observe(this, {deck->
            getActivity()!!.setTitle("title")
            /*val actionbar = activity
            actionbar!!.title = deck.name*/
        })
        cardViewModel.getDeckByNextRepetition(deckId, ceil(Date().time / (1000 * 3600 * 24).toDouble())).observe(this, {
            deckFinishedToday = it.isEmpty()
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

    private val startStudyForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            if(intent!!.getBooleanExtra(QuestionFragment.DECK_FINISHED, false)){
                showConfetti()
            }
        }
    }
    private fun study(){
        if(deckFinishedToday){
            showConfirmExtraStudyDialog()
        }else{
            startStudyForResult.launch(QuestionActivity.newIntent(context!!, deckId, false))
        }
    }
    private fun showConfirmExtraStudyDialog(){
        AlertDialog.Builder(context)
            .setTitle("Confirm")
            .setMessage("Do you really want to study?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes){dialog, _ ->
                startStudyForResult.launch(QuestionActivity.newIntent(context!!, deckId, true))
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun selectAll() {
        adapter.selectAll()
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
        //visibility of toolbar and checkbox
        adapter.select = !adapter.select
        deleteButton?.isVisible = adapter.select
        selectAllButton?.isVisible = adapter.select
        favoriteButton?.isVisible = !adapter.select
        startStudyButton?.isVisible = !adapter.select
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
        selectAllButton?.isVisible = false
    }
    private fun setFavoriteIcon(favorite: Boolean){
        if (favorite){
            favoriteButton?.setIcon(R.drawable.ic_favorite_study)
        }else{
            favoriteButton?.setIcon(R.drawable.ic_favorite_study_border)
        }
    }
    private fun showConfetti(){
        binding.viewKonfetti.build()
            .setSpeed(1f, 5f)
            .setTimeToLive(2000L)
            .setFadeOutEnabled(true)
            .setDirection(0.0, 359.0)
            .addSizes(Size(12))
            .addShapes(Shape.Square, Shape.Circle)
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }
}