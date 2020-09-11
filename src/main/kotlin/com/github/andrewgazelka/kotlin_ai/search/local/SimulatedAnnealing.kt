package com.github.andrewgazelka.kotlin_ai.search.local

import kotlinx.coroutines.channels.SendChannel

class SimulatedAnnealing<T : Value>(
    private val tempSequence: Sequence<Double>,
    private val channel: SendChannel<T>? = null
) : LocalSequenceOptimizer<T> {

    override suspend fun optimize(sequence: LocalSequence<T>, start: T, method: OptimizeMethod): T {
        var on = start
        var value = start.getValue()
        channel?.send(on)
        for (temp in tempSequence) {
            val underThreshold = ArrayDeque<T>()
            sequence.neighbors(on).forEach { elem ->
                val elemValue = elem.getValue()
                if (method.optimize(value, elemValue, temp) is Optimize.Worse) return@forEach
                underThreshold.add(elem)
            }
            if (underThreshold.isEmpty()) break
            val randChoice = underThreshold.random()
            on = randChoice
            channel?.send(on)
            value = randChoice.getValue()
        }
        return on
    }
}
