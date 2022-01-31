package com.inglarna.blackeagle.ui.convertnumber

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentNumberConverterBinding

class NumberConverterFragment : DialogFragment() {
    private lateinit var binding: FragmentNumberConverterBinding
    private val viewModel : NumbersViewModel by viewModels()

    companion object{
        const val EXTRA_WORDS = "com.inglarna.blackeagle.words"
        const val REQUEST_WORDS = "words"
        private const val TAG = "NumbersConverterFragment"
        fun newInstance() = NumberConverterFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if(!this::binding.isInitialized){
            binding = FragmentNumberConverterBinding.inflate(inflater, container, false) //FIXME: is this an odd way of doing this
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNumberConverterBinding.inflate(layoutInflater, null, false)

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setTitle(getString(R.string.number_converter_dialog_title))
            .setPositiveButton(R.string.insert_word){_,_ ->
                val result = Bundle()
                result.putString(EXTRA_WORDS, viewModel.words.value)
                setFragmentResult(REQUEST_WORDS, result)
               // dialog?.dismiss()
            }
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}