package com.inglarna.blackeagle.ui.question

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentQuestionBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class QuestionFragment : Fragment() {
    private lateinit var binding: FragmentQuestionBinding
    private var deckId: Long = -1
    private var forceStudy = false
    private var cardsInitialized = false
    private var cards: MutableList<Card> = ArrayList()
    private val cardViewModel by viewModels<CardViewModel>()
    private lateinit var cardRepo : CardRepo

    companion object{
        const val DECK_FINISHED = "deckFinished"
        private const val DECK_ID = "deckId"
        private const val FORCE_STUDY = "forceStudy"
        private const val TAG = "Question"
        fun newInstance(deckId: Long, forceStudy: Boolean = false): QuestionFragment {
            val bundle = Bundle()
            bundle.putLong(DECK_ID, deckId)
            bundle.putBoolean(FORCE_STUDY, forceStudy)
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
        forceStudy = arguments!!.getBoolean(FORCE_STUDY, false)

        val maxDate = if(forceStudy){Double.MAX_VALUE} else{ceil(Date().time / (1000.0*3600.0*24.0))}
        cardViewModel.getDeckByNextRepetition(deckId, maxDate).observe(this){
            if(!forceStudy || !cardsInitialized){
                cards = it.toMutableList()
                cardsInitialized = true
            }
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
                binding.buttonShowAnswer.visibility = View.INVISIBLE
                binding.buttonShowHint.visibility = View.INVISIBLE
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
            //Remove card from study queue or put it last in queue if necessary
            if(forceStudy){
                cards.remove(selectedCard)
                if(view.id == binding.buttonDifficult.id){
                    cards.add(selectedCard)
                }
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

    val imageGetter = object: Html.ImageGetter {
        override fun getDrawable(source: String): Drawable{
            val drawable = BitmapDrawable(resources, BitmapFactory.decodeFile(File(context?.filesDir, source).path))
            drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
    }
    private fun resetFields(){
        //Set text
        binding.textViewQuestion.text = cards[0].question
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            binding.textViewQuestion.text = Html.fromHtml(cards[0].question, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
            binding.textViewAnswer.text = Html.fromHtml(cards[0].answer, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
            binding.textViewHint.text = Html.fromHtml(cards[0].hint, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
        }else{
            binding.textViewQuestion.text = Html.fromHtml(cards[0].question, imageGetter,null).trim()
            binding.textViewAnswer.text = Html.fromHtml(cards[0].answer, imageGetter,null).trim()
            binding.textViewHint.text = Html.fromHtml(cards[0].hint, imageGetter,null).trim()
        }
        //Reset visibilities
        binding.textViewHint.visibility = View.GONE
        binding.textViewAnswer.visibility = View.GONE
        binding.difficultyButtonsContainer.visibility = View.GONE
        binding.buttonShowAnswer.visibility = View.VISIBLE
        if(cards[0].hint.isNotEmpty()){
            binding.buttonShowHint.visibility = View.VISIBLE
        }
    }

    private fun endActivity(deckFinished: Boolean){
        val result = Intent()
        result.putExtra(DECK_FINISHED, deckFinished)
        activity!!.setResult(Activity.RESULT_OK, result)
        activity!!.finish()
    }
}
