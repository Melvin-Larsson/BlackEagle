package com.inglarna.blackeagle.ui.question

import com.inglarna.blackeagle.model.Card
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class DefaultCardScramblerTest{

    lateinit var scrambler: DefaultCardScrambler

    @Before
    fun setUP(){
        scrambler = DefaultCardScrambler()
    }

    @Test
    fun scramble_onlyNew(){
        val newList = listOf(
            Card(),
            Card(),
            Card(),
        )
        val oldList = listOf<Card>()

        val res = scrambler.scramble(newList, oldList)

        assertThat(res.size, `is`(newList.size))
    }

    @Test
    fun scramble_onlyOld(){
        val newList = listOf<Card>()

        val oldList = listOf(
            Card(),
            Card(),
            Card(),
        )

        val res = scrambler.scramble(newList, oldList)

        assertThat(res.size, `is`(oldList.size))
    }

    @Test
    fun scramble_OldSizeEqualNewSize(){
        val newList = listOf(
            Card(),
            Card(),
            Card(),
        )

        val oldList = listOf(
            Card(),
            Card(),
            Card(),
        )

        val res = scrambler.scramble(newList, oldList)

        assertThat(res.size, `is`(oldList.size + newList.size))
    }

    @Test
    fun scramble_fiveNewThreeOld(){
        val newList = listOf(
            Card(),
            Card(),
            Card(),
            Card(),
            Card(),
        )

        val oldList = listOf(
            Card(),
            Card(),
            Card(),
        )

        val res = scrambler.scramble(newList, oldList)

        assertThat(res.size, `is`(oldList.size + newList.size))
        assertThat(res[6], `is`(newList[3]))
        assertThat(res[7], `is`(newList[4]))
    }

    @Test
    fun scramble_threeNewFiveOld(){
        val newList = listOf(
            Card(),
            Card(),
            Card(),
        )

        val oldList = listOf(
            Card(),
            Card(),
            Card(),
            Card(),
            Card(),
        )

        val res = scrambler.scramble(newList, oldList)

        assertThat(res.size, `is`(oldList.size + newList.size))
        assertThat(res[6], `is`(oldList[3]))
        assertThat(res[7], `is`(oldList[4]))

    }
}