package com.inglarna.blackeagle.ui.question
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inglarna.blackeagle.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var  questionViewModel : QuestionViewModel

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

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?) : View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val deckId = arguments!!.getLong(DECK_ID, -1)
        val forceStudy = arguments!!.getBoolean(FORCE_STUDY, false)
        questionViewModel = ViewModelProvider(this, QuestionViewModelFactory(activity!!.application, deckId)).get(
            QuestionViewModel::class.java)

        binding.viewmodel = questionViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        questionViewModel.done.observe(viewLifecycleOwner){ done ->
            if(done){
                endActivity()
            }
        }

        if(forceStudy){
            questionViewModel.loadCards(QuestionViewModel.ALL_CARDS)
        }else{
            questionViewModel.loadCards(QuestionViewModel.NEW_CARDS)
        }
    }

    private fun endActivity(){
        val result = Intent()
        result.putExtra(DECK_FINISHED, true)
        activity!!.setResult(Activity.RESULT_OK, result)
        activity!!.finish()
    }
}
