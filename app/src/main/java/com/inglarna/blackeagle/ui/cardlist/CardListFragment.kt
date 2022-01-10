package com.inglarna.blackeagle.ui.cardlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.ui.question.QuestionActivity
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.card.CardActivity
import com.inglarna.blackeagle.ui.question.QuestionFragment
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.math.ceil

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding
    private val cardViewModel by viewModels<CardViewModel>()
    private val deckViewModel by viewModels<DeckViewModel>()
    lateinit var onAddCardClicked: (()-> Unit)
    private lateinit var adapter : CardListRecyclerViewAdapter
    private var favoriteButton: MenuItem? = null
    private var deleteButton: MenuItem? = null
    private var selectAllButton: MenuItem? = null
    private var closeSelectButton: MenuItem? = null
    private var moreButton: MenuItem? = null
    private lateinit var deck: Deck
    private var deckId: Long= -1
    private var deckFinishedToday = false
    private val startStudyForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            if(intent!!.getBooleanExtra(QuestionFragment.DECK_FINISHED, false)){
                showConfetti()
            }
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //toolbar
        setHasOptionsMenu(true)
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_list_menu, menu)
        selectAllButton = menu.findItem(R.id.selectAllCards)
        deleteButton = menu.findItem((R.id.delete))
        moreButton = menu.findItem(R.id.more)
        closeSelectButton = menu.findItem(R.id.closeSelect)
    }
    //when clicking in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
            R.id.delete -> delete()
            R.id.selectAllCards -> adapter.selectAll()
            R.id.more -> startActivity(EditDeckActivity.newIntent(context!!, deck.id))
            R.id.closeSelect -> closeSelect()
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
        deckViewModel.getDeck(deckId).observe(this){
            if (it == null){
                activity?.finish()
            }else{
                deck = it
                activity?.title = deck.name
            }
        }
        adapter = CardListRecyclerViewAdapter(cardViewModel.getDeckViews(deckId), this, context!!)
        binding.recyclerViewCard.adapter = adapter
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            onAddCardClicked()
        }
        binding.startStudyButton.setOnClickListener{
            if(deckFinishedToday){
                showConfirmExtraStudyDialog()
            }else{
                startStudyForResult.launch(QuestionActivity.newIntent(context!!, deckId, false))
            }
        }
        adapter.onDeleteCardClicked={ card ->
            GlobalScope.launch {
                cardViewModel.deleteCard(card)
            }

        }
        adapter.onEditCardClicked = { card ->
            startActivity(CardActivity.newIntent(context!!, card.deckId, card.id))
        }

        adapter.selectMultipleCallback = {
            toolbarVisibility()
        }
        initializeCardMoving()
        //Actionbar title
        cardViewModel.getDeckByNextRepetition(deckId, ceil(Date().time / (1000 * 3600 * 24).toDouble())).observe(this, {
            deckFinishedToday = it.isEmpty()
        })



    }
    private fun initializeCardMoving(){
        val touchHelperCallback = SimpleItemTouchHelperCallback()
        touchHelperCallback.touchHelperAdapter = adapter
        touchHelperCallback.clearViewCallback = SimpleItemTouchHelperCallback.ClearViewCallback{
            GlobalScope.launch {
                cardViewModel.updateCards(adapter.movedCards)
                adapter.movedCards.clear()
            }
        }
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        adapter.onStartDrag = {viewHolder ->
            touchHelper.startDrag(viewHolder)
        }
        touchHelper.attachToRecyclerView(binding.recyclerViewCard)
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

    private fun toolbarVisibility(){
        //visibility of toolbar and checkbox
        deleteButton?.isVisible = adapter.select
        selectAllButton?.isVisible = adapter.select
        closeSelectButton?.isVisible = adapter.select
        moreButton?.isVisible = !adapter.select
    }

    private fun closeSelect() {
        adapter.select = !adapter.select
        toolbarVisibility()
    }
    private fun delete(){
        val selectedCards = adapter.selectedCards.toMutableList()
        GlobalScope.launch {
            for(card in selectedCards){
                cardViewModel.deleteCard(card)
            }
        }
        adapter.select = false
        toolbarVisibility()
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