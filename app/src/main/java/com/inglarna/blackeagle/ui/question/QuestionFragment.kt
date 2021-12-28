package com.inglarna.blackeagle.ui.question

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentQuestionBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class QuestionFragment : Fragment() {
    lateinit var binding: FragmentQuestionBinding
    private var deckId: Long = -1
    private var cards: List<Card> = ArrayList<Card>()
    private val cardViewModel by viewModels<CardViewModel>()
    private lateinit var cardRepo : CardRepo

    companion object{
        public const val DECK_FINISHED = "deckFinished"
        private const val DECK_ID = "deckId"
        private const val TAG = "Question"
        fun newInstance(deckId: Long): QuestionFragment {
            val bundle = Bundle()
            bundle.putLong(DECK_ID, deckId)
            val fragment = QuestionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckId = arguments!!.getLong(DECK_ID, -1)
        cardViewModel.getDeckByNextRepetition(deckId, ceil(Date().time / (1000.0*3600.0*24.0))).observe(this){
            cards = it
            if(cards.isNotEmpty()){
                resetFields()
            }else{
                endActivity(true)
            }
        }
        binding.buttonShowHint.setOnClickListener {
            if (cards.isNotEmpty()) {
                binding.textViewHint.visibility = View.VISIBLE
            }
        }
        binding.buttonShowAnswer.setOnClickListener {
            if(cards.isNotEmpty()){
                //Show answer
                binding.textViewAnswer.visibility = View.VISIBLE
                //Switch buttons
                binding.difficultyButtonsContainer.visibility = View.VISIBLE
                binding.buttonShowAnswer.visibility = View.GONE
            }
        }
        var difficultyButtonListener = View.OnClickListener{view ->
            //Inform card about repetition
            var retrievability: Double = when(view.id){
                binding.buttonEasy.id -> 0.9
                binding.buttonMedium.id -> 0.5
                else -> 0.0
            }
            var selectedCard = cards[0]
            selectedCard.repeated(retrievability)
            GlobalScope.launch {
                cardRepo.updateCard(selectedCard)
            }
            //Next question
            if(cards.isEmpty()){
                endActivity(true)
            }else{
                resetFields()
            }

        }
        binding.buttonEasy.setOnClickListener(difficultyButtonListener)
        binding.buttonMedium.setOnClickListener(difficultyButtonListener)
        binding.buttonDifficult.setOnClickListener(difficultyButtonListener)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardRepo = CardRepo(context)
    }

    private fun resetFields(){
        //Set text
        binding.textViewQuestion.text = cards[0].question
        binding.textViewAnswer.text = cards[0].answer
        binding.textViewHint.text = cards[0].hint
        //Reset visibilities
        binding.textViewHint.visibility = View.INVISIBLE
        binding.textViewAnswer.visibility = View.INVISIBLE
        binding.difficultyButtonsContainer.visibility = View.GONE
        binding.buttonShowAnswer.visibility = View.VISIBLE
    }
    private fun endActivity(doneStudying: Boolean){
        val result = Intent()
        result.putExtra(DECK_FINISHED, doneStudying)
        activity!!.setResult(Activity.RESULT_OK, result)
        activity!!.finish()
    }
}