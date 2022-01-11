package com.inglarna.blackeagle.ui.cardlist

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.ui.question.QuestionActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.inglarna.blackeagle.ui.card.CardActivity
import com.inglarna.blackeagle.ui.question.QuestionFragment
import com.inglarna.blackeagle.viewmodel.CardListViewModel
import com.inglarna.blackeagle.viewmodel.CardListViewModelFactory
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.math.ceil

class CardListFragment : Fragment() {
    private lateinit var binding : FragmentCardListBinding
    private lateinit var cardListViewModel : CardListViewModel
    private lateinit var adapter : CardListRecyclerViewAdapter
    private lateinit var deleteButton: MenuItem
    private lateinit var selectAllButton: MenuItem
    private lateinit var closeSelectButton: MenuItem
    private lateinit var moreButton: MenuItem
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
            R.id.more -> startActivity(EditDeckActivity.newIntent(context!!, cardListViewModel.deckId))
            R.id.closeSelect -> closeSelect()
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val deckId = arguments!!.getLong(DECK_ID, -1)
        cardListViewModel = ViewModelProvider(this, CardListViewModelFactory(activity!!.application, deckId)).get(CardListViewModel::class.java)
        //creating adapter
        cardListViewModel.deck.observe(this){
            if (it == null){
                activity?.finish()
            }else{
                activity?.title = it.name
            }
        }
        adapter = CardListRecyclerViewAdapter(context!!)
        binding.recyclerViewCard.adapter = adapter
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            startActivity(CardActivity.newIntent(context!!, cardListViewModel.deckId))
        }
        binding.startStudyButton.setOnClickListener{
            if(deckFinishedToday){
                showConfirmExtraStudyDialog()
            }else{
                startStudyForResult.launch(QuestionActivity.newIntent(context!!, cardListViewModel.deckId, false))
            }
        }
        adapter.onDeleteCardClicked={ card ->
            cardListViewModel.deleteCard(card)
        }
        adapter.onEditCardClicked = { card ->
            startActivity(CardActivity.newIntent(context!!, card.deckId, card.cardId))
        }
        adapter.selectMultipleCallback = {
            toolbarVisibility()
        }
        observeData()
        initializeCardMoving()

        cardListViewModel.getCardsByNextRepetition(ceil(Date().time / (1000 * 3600 * 24).toDouble())).observe(this, {
            deckFinishedToday = it.isEmpty()
        })

    }
    private fun observeData(){
        cardListViewModel.cards.observe(this, {
            adapter.cards = it
        })
    }
    private fun initializeCardMoving(){
        val touchHelperCallback = SimpleItemTouchHelperCallback()
        touchHelperCallback.touchHelperAdapter = adapter
        touchHelperCallback.clearViewCallback = SimpleItemTouchHelperCallback.ClearViewCallback{
            GlobalScope.launch {
                cardListViewModel.updateCards(adapter.movedCards)
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
                startStudyForResult.launch(QuestionActivity.newIntent(context!!, cardListViewModel.deckId, true))
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun toolbarVisibility(){
        //visibility of toolbar and checkbox
        deleteButton.isVisible = adapter.select
        selectAllButton.isVisible = adapter.select
        closeSelectButton.isVisible = adapter.select
        moreButton.isVisible = !adapter.select
    }

    private fun closeSelect() {
        adapter.select = !adapter.select
        toolbarVisibility()
    }
    private fun delete(){
        val selectedCards = adapter.selectedCards.toMutableList()
        cardListViewModel.deleteCards(selectedCards)
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