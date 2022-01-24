package com.inglarna.blackeagle

import android.text.Spanned
import android.widget.TextView
import androidx.core.text.toSpanned
import androidx.databinding.InverseBindingAdapter

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getText(view: TextView): CharSequence{
    return view.text
}