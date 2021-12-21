package com.inglarna.blackeagle.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentQuestionBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel

class QuestionFragment : Fragment() {
    lateinit var binding: FragmentQuestionBinding
    private var deckId: Long = -1
    private var index = 0
    private var cards: List<Card> = ArrayList<Card>()
    private val cardViewModel by viewModels<CardViewModel>()

    companion object{
        private const val DECK_ID = "deckId"
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
        cardViewModel.getDeckViews(deckId).observe(this){
            cards = it
            if(cards.isNotEmpty()){
                resetFields()
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
                binding.buttonNextQuestion.visibility = View.VISIBLE
                binding.buttonShowAnswer.visibility = View.GONE
            }
        }
        binding.buttonNextQuestion.setOnClickListener{
            index ++
            if(index >= cards.size){
                activity!!.finish()
            }else{
                resetFields()
            }
        }
    }
    private fun resetFields(){
        //Set text
        binding.textViewQuestion.text = cards[index].question
        binding.textViewAnswer.text = cards[index].answer
        binding.textViewHint.text = cards[index].hint
        //Reset visibilities
        binding.textViewHint.visibility = View.INVISIBLE
        binding.textViewAnswer.visibility = View.INVISIBLE
        binding.buttonNextQuestion.visibility = View.GONE
        binding.buttonShowAnswer.visibility = View.VISIBLE
    }
}