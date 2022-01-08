package com.inglarna.blackeagle.ui.convertnumber


import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentNumbersBinding
import com.inglarna.blackeagle.ui.tutorialnumbers.TutorialNumbersActivity
import com.inglarna.blackeagle.viewmodel.wordNumberViewModel

class NumbersFragment: Fragment() {
    private lateinit var binding: FragmentNumbersBinding
    lateinit var callbacks: Callbacks
    private val wordNumberViewModel by viewModels<wordNumberViewModel>()
    private var questionMarkButton: MenuItem? = null

    interface Callbacks{
        fun onEditButtonPressed() {
        }
    }

    companion object{
        private const val TAG = "NumbersFragment"
        fun newInstance() = NumbersFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNumbersBinding.inflate(inflater, container, false)
        binding.textInputConvertNumber.addTextChangedListener{
            setWords(binding.textInputConvertNumber.text.toString())
        }
        binding.editNumbersButton.setOnClickListener(){
            callbacks.onEditButtonPressed()
        }
        return binding.root
    }

    //toobar
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.numbers_menu, menu)
        questionMarkButton = menu.findItem(R.id.questionMark)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.questionMark -> questionMark()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun questionMark() {
        startActivity(TutorialNumbersActivity.newIntent(context!!))
    }

    private fun setWords(number: String){
        //Divide the long number into smaller ones
        val numbers = ArrayList<Int>()
        var startIndex = 0
        if(number.length%3 != 0){
            numbers.add(number.substring(startIndex, startIndex + number.length%3).toInt())
            startIndex += number.length%3
        }
        while(startIndex < number.length){
            numbers.add(number.substring(startIndex, startIndex + 3).toInt())
            startIndex += 3;
        }
        //Convert to smaller numbers to words
        wordNumberViewModel.getWords(numbers).observe(this, {wordNumbers ->
            var converted = ""
            //Create the converted string
            for(number in numbers){
                for (wordNumber in wordNumbers){
                    if(wordNumber.number == number){
                        converted += " " + wordNumber.word
                        break
                    }
                }
            }
            binding.textView.text = converted
        })
    }
}