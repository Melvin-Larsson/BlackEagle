package com.inglarna.blackeagle.ui.question

import com.inglarna.blackeagle.model.Card

class DefaultCardScrambler : CardScrambler {
    override fun scramble(newCards: List<Card>?, oldCards: List<Card>?): MutableList<Card> {
        val cardList1 = newCards ?: emptyList()
        val cardList2 = oldCards ?: emptyList()
        val longList: List<Card>
        val shortList: List<Card>
        if(cardList1.size >= cardList2.size){
            longList = cardList1
            shortList = cardList2
        }else{
            longList = cardList2
            shortList = cardList1
        }

        val ratio = if(shortList.isNotEmpty()) {
            (longList.size / shortList.size).toDouble()
        }else{
            0.0
        }
        val result = mutableListOf<Card>()
        var longListIndex = 0
        for (card in shortList){
            result.add(card)
            var i = 0
            while (i < ratio){
                result.add(longList[i])
                i++
                longListIndex++
            }
        }
        for(i in longListIndex until longList.size){
            result.add(longList[i])
        }
        return result
    }
}