package com.inglarna.blackeagle.ui.convertnumber

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentNumbersBinding
import com.inglarna.blackeagle.ui.editnumbers.EditNumbersActivity
import com.inglarna.blackeagle.ui.tutorialnumbers.TutorialNumbersActivity

class NumbersFragment: Fragment() {
    private lateinit var binding: FragmentNumbersBinding
    private val viewModel by viewModels<NumbersViewModel>()

    private var questionMarkButton: MenuItem? = null

    companion object{
        private const val TAG = "NumbersFragment"
        fun newInstance() = NumbersFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Child fragment
        val childFragment = NumberConverterFragment.newInstance()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.number_converter_container, childFragment).commit()

        //Edit numbers button
        binding.editNumbersButton.setOnClickListener(){
            startActivity(EditNumbersActivity.newIntent(requireContext()))
        }
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
        startActivity(TutorialNumbersActivity.newIntent(requireContext()))
    }
}