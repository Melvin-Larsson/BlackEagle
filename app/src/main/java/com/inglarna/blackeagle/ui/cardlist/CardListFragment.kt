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
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class CardListFragment : Fragment() {
    private lateinit var binding : FragmentCardListBinding
    private lateinit var cardListViewModel : CardListViewModel
    private lateinit var adapter : CardListRecyclerViewAdapter
    //Toolbar buttons
    private lateinit var deleteButton: MenuItem
    private lateinit var selectAllButton: MenuItem
    private lateinit var closeSelectButton: MenuItem
    private lateinit var moreButton: MenuItem

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
        setHasOptionsMenu(true)
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_list_menu, menu)
        selectAllButton = menu.findItem(R.id.selectAllCards)
        deleteButton = menu.findItem((R.id.delete))
        moreButton = menu.findItem(R.id.more)
        closeSelectButton = menu.findItem(R.id.closeSelect)

        /*Must observe select after menu has been created,
          otherwise the visibility of menuOptions will be toggled before they reference anything
          causing an exception*/
        cardListViewModel.select.observe(viewLifecycleOwner){ isSelecting ->
            setToolbarVisibility(isSelecting)
        }
    }
    //when clicking in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                activity!!.finish()
                return true
            }
            R.id.delete -> cardListViewModel.deleteSelectedCards()
            R.id.selectAllCards -> cardListViewModel.toggleSelectAll()
            R.id.more -> startActivity(EditDeckActivity.newIntent(context!!, cardListViewModel.deckId))
            R.id.closeSelect -> cardListViewModel.setSelect(false)
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Retrieve ViewModel
        val deckId = arguments!!.getLong(DECK_ID, -1)
        cardListViewModel = ViewModelProvider(this, CardListViewModelFactory(activity!!.application, deckId))[CardListViewModel::class.java]
        binding.viewModel = cardListViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //End activity if no deck can be found, set the toolbar title otherwise
        cardListViewModel.deck.observe(this){ deck ->
            if (deck == null){
                activity?.finish()
            }else{
                activity?.title = deck.name
            }
        }

        //Recyclerview
        initializeRecyclerView()

        //Add card button
        binding.buttonAddCard.setOnClickListener{
            startActivity(CardActivity.newIntent(context!!, cardListViewModel.deckId))
        }

        //Start study button
        cardListViewModel.deckFinished.observe(viewLifecycleOwner){ deckFinished ->
            binding.startStudyButton.setOnClickListener{
                if(deckFinished){
                    showConfirmExtraStudyDialog()
                }else{
                    startStudyForResult.launch(QuestionActivity.newIntent(context!!, cardListViewModel.deckId, false))
                }
            }
        }
    }
    private fun initializeRecyclerView(){
        //Setup adapter
        adapter = CardListRecyclerViewAdapter(cardListViewModel, viewLifecycleOwner)
        binding.recyclerViewCard.adapter = adapter
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())

        //Observer cards
        cardListViewModel.cards.observe(viewLifecycleOwner, {
            adapter.cards = it
        })

        //Initialize moving of cards
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
        //Card selected listener
        cardListViewModel.select.observe(viewLifecycleOwner){ select ->
            adapter.onCardSelected = { card, isSelected ->
                if(select){
                    cardListViewModel.setSelectionState(card, !isSelected)
                }else{
                    startActivity(CardActivity.newIntent(requireContext(), card.deckId, card.cardId))
                }
            }


        }
    }

    private fun showConfirmExtraStudyDialog(){
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.study_beforehand_question))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok){_, _ ->
                startStudyForResult.launch(QuestionActivity.newIntent(context!!, cardListViewModel.deckId, true))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setToolbarVisibility(showSelect: Boolean){
        //visibility of toolbar and checkbox
        deleteButton.isVisible = showSelect
        selectAllButton.isVisible = showSelect
        closeSelectButton.isVisible = showSelect
        moreButton.isVisible = !showSelect
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