package com.jovanovic.stefan.mytestapp.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentTutorialNumbers2Binding
import com.inglarna.blackeagle.utilities.ImageZoom

class SecondScreen : Fragment() {

    lateinit var binding: FragmentTutorialNumbers2Binding
    var imageZoom: ImageZoom = ImageZoom()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTutorialNumbers2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting the length of the animation
        imageZoom.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        //on image 1 clicked
        binding.convertNumbersImage1.setOnClickListener{
            //image zoom
            imageZoom.zoomImageFromThumb(binding.convertNumbersImage1,
                R.drawable.convert_numbers_guide_swedish1,
                binding.expandedConvertNumbersImage,
                binding.container)
        }

        //on image 2 clicked
        binding.convertNumbersImage2.setOnClickListener{
            //image zoom
            imageZoom.zoomImageFromThumb(binding.convertNumbersImage2,
                R.drawable.convert_numbers_guide_swedish2,
                binding.expandedConvertNumbersImage,
                binding.container)
        }
    }

}