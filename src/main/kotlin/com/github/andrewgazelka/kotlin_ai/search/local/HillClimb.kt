package com.github.andrewgazelka.kotlin_ai.search.local

import kotlinx.coroutines.channels.SendChannel

class HillClimb<T : Value>(private val maxIterations: Int, private val channel: SendChannel<T>? = null) : LocalSequenceOptimizer<T> {

    override suspend fun optimize(sequence: LocalSequence<T>, start: T, method: OptimizeMethod): T {
        var on = start
        var value = start.getValue()
        channel?.send(on)
        var iteration = 0
        while (iteration++ < maxIterations) {
            var changed = false
            sequence.neighbors(on).forEach { elem ->
                val elemValue = elem.getValue()
                val opt = method.optimize(value, elemValue) as? Optimize.Better ?: return@forEach
                value = opt.newValue
                on = elem
                changed = true
            }
            if (!changed) break
            else channel?.send(on)
        }
        return on
    }
}
