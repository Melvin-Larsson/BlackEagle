package com.inglarna.blackeagle.ui.question

import com.inglarna.blackeagle.model.Card

interface CardScrambler {
    fun scramble (newCards: List<Card>?, oldCards: List<Card>?): MutableList<Card>
}