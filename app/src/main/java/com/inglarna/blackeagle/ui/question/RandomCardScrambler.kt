package com.inglarna.blackeagle.ui.question

import com.inglarna.blackeagle.model.Card

class RandomCardScrambler: CardScrambler {
    override fun scramble(newCards: List<Card>?, oldCards: List<Card>?): MutableList<Card> {
        val result = mutableListOf<Card>()
        result.addAll(newCards ?: emptyList())
        result.addAll(oldCards ?: emptyList())
        result.shuffle()
        return result
    }
}