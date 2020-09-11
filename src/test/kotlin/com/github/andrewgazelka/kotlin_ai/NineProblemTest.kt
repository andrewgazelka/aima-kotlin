package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.util.GraphProgressor
import org.junit.jupiter.api.Test

private val nineProblem = NineProblem((0..9).toList())

class NineProblemTest {


    @Test
    fun `test adjacent`() {
        val possibleNewStates = nineProblem.possibleNewStates().toList()
        println(possibleNewStates)
    }

    @Test
    fun test() {
    }
}

