package com.inglarna.blackeagle.ui.tutorialnumbers.viewPager.screens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentTutorialNumbers1Binding
import com.inglarna.blackeagle.utilities.ImageZoom

class FirstScreen: Fragment() {
    lateinit var binding: FragmentTutorialNumbers1Binding
    var imageZoom: ImageZoom = ImageZoom()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTutorialNumbers1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageZoom.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        //empty title
        //on image clicked
        binding.convertNumbersTable.setOnClickListener{
            //image zoom
            imageZoom.zoomImageFromThumb(binding.convertNumbersTable,
                R.drawable.convert_numbers_table,
                binding.expandedConvertNumbersTable,
                binding.container)
        }
    }


}