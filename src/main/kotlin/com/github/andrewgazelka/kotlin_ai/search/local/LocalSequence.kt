package com.github.andrewgazelka.kotlin_ai.search.local

import java.lang.Double.min

data class State<T>(val value: T, val score: Double)

interface Value {
    fun getValue(): Double
}

interface LocalSequence<T : Value> {
    fun neighbors(value: T): Sequence<T>
}

enum class OptimizeMethod {
    MAX,
    MIN
}

sealed class Optimize {
    object Worse : Optimize()
    class Better(val newValue: Double) : Optimize()
}

fun OptimizeMethod.optimize(original: Double, next: Double): Optimize {
    when (this) {
        OptimizeMethod.MAX -> if (next >= original) return Optimize.Better(next)
        OptimizeMethod.MIN -> if (next <= original) return Optimize.Better(next)
    }
    return Optimize.Worse
}


interface LocalSequenceOptimizer<T : Value> {
    fun optimize(sequence: LocalSequence<T>, start: T, method: OptimizeMethod): T
}

