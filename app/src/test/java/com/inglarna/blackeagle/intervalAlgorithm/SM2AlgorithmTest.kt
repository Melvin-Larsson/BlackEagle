package com.inglarna.blackeagle.intervalAlgorithm

import com.inglarna.blackeagle.model.Card
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test
import java.lang.IllegalArgumentException

class SM2AlgorithmTest{
    companion object{
        private const val EASY = 0.9
        private const val MEDIUM = 0.7
        private const val DIFFICULT = 0.0
    }
    @Test
    fun repeated_EasyEasyEasy_nextRepetitionDelayIncrease(){
        val repetitions = listOf(
            Repetition(EASY),
            Repetition(EASY),
            Repetition(EASY)
        )

        val results = repeat(repetitions)

        assertThat(results[1], greaterThan(results[0]))
    }
    @Test
    fun repeated_MediumMediumMedium_nextRepetitionDelayIncrease(){
        val repetitions = listOf(
            Repetition(MEDIUM),
            Repetition(MEDIUM),
            Repetition(MEDIUM)
        )

        val results = repeat(repetitions)

        assertThat(results[1], greaterThan(results[0]))
    }
    @Test
    fun repeated_DifficultDifficultDifficult_lessThanOneNextRepetitionDelay(){
        val repetitions = listOf(
            Repetition(DIFFICULT),
            Repetition(DIFFICULT),
            Repetition(DIFFICULT)
        )

        val results = repeat(repetitions)

        assertThat(results[0].toInt(), `is`(0))
        assertThat(results[1].toInt(), `is`(0))

    }
    @Test
    fun repeated_EasyEasyDifficult_lessThanOneNextRepetitionDelay(){
        val repetitions = listOf(
            Repetition(EASY),
            Repetition(EASY),
            Repetition(DIFFICULT)
        )

        val results = repeat(repetitions)

        assertThat(results[1].toInt(), `is`(0))
    }
    @Test(expected = IllegalArgumentException::class)
    fun repeated_negativeRetrievability_Exception(){
        val repetitions = listOf(
            Repetition(-0.5 )
        )

        repeat(repetitions)
    }
    @Test(expected = IllegalArgumentException::class)
    fun repeated_retrievabilityGreaterThanOne_Exception(){
        val repetitions = listOf(
            Repetition(2.0 )
        )
        repeat(repetitions)
    }
    @Test
    fun repeated_EasyEasyEasyWithAndWithoutExtraTime_repetitionDelayGreaterWithExtraTime(){
        val repetitionsDefaultTime = listOf(
            Repetition(EASY),
            Repetition(EASY),
            Repetition(EASY),
            Repetition(EASY)
        )
        val repetitionsExtraTime = listOf(
            Repetition(EASY),
            Repetition(EASY,2),
            Repetition(EASY, 3),
            Repetition(EASY, 4)
        )

        val resultsDefault = repeat(repetitionsDefaultTime)
        val resultsExtra = repeat(repetitionsExtraTime)

        assertThat(resultsDefault[0], lessThan(resultsExtra[0]))
        assertThat(resultsDefault[1], lessThan(resultsExtra[1]))
        assertThat(resultsDefault[2], lessThan(resultsExtra[2]))
    }
    @Test
    fun repeated_EasyEasyEasyWithAndWithoutLessTime_repetitionsDelayLessWithLessTime(){
        val repetitionsDefaultTime = listOf(
            Repetition(EASY),
            Repetition(EASY),
            Repetition(EASY),
            Repetition(EASY)
        )
        val repetitionsExtraTime = listOf(
            Repetition(EASY),
            Repetition(EASY,),
            Repetition(EASY, -1),
            Repetition(EASY, -2)
        )

        val resultsDefault = repeat(repetitionsDefaultTime)
        val resultsExtra = repeat(repetitionsExtraTime)

        assertThat(resultsDefault[0], `is`(resultsExtra[0]))
        assertThat(resultsDefault[1], greaterThan(resultsExtra[1]))
        assertThat(resultsDefault[2], greaterThan(resultsExtra[2]))
    }
    @Test
    fun repeated_EasyEasyEasySameDay_sameNextRepetition(){
        val card = Card()
        card.nextRepetition = 0.0
        val algorithm = SM2Algorithm()
        val result = mutableListOf<Double>()
        for(i in 1..3){
            algorithm.cardRepeated(card, EASY, 0)
            result.add(card.nextRepetition)
        }
        assertThat(result[0], `is`(result[1]))
        assertThat(result[1], `is`(result[2]))
    }

    /**
     * Calls repeated() on a card with the data specified in the passed list
     * A list containing the calculated delay between repetitions is returned
     */
    private fun repeat(repetitions: List<Repetition>): List<Double>{
        val card = Card()
        card.nextRepetition = 0.0
        var lastRepetition: Double = -1.0
        val nextRepetitionResults = mutableListOf<Double>()
        val algorithm = SM2Algorithm()
        for(repetition in repetitions){
            algorithm.cardRepeated(card, repetition.retrievability, card.nextRepetition.toLong() + repetition.nextRepetitionVariation)
            //Calculate delay
            if(lastRepetition >= 0){
                nextRepetitionResults.add(card.nextRepetition - lastRepetition)
            }
            lastRepetition = card.nextRepetition
        }
        return nextRepetitionResults
    }
    data class Repetition(val retrievability: Double, val nextRepetitionVariation: Long = 0)
}