package com.github.andrewgazelka.kotlin_ai.search.local

class HillClimb<T : Value>(private val maxIterations: Int) : LocalSequenceOptimizer<T> {

    override fun optimize(sequence: LocalSequence<T>, start: T, method: OptimizeMethod): T {
        var on = start
        var value = start.getValue()
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
        }
        return on
    }
}
