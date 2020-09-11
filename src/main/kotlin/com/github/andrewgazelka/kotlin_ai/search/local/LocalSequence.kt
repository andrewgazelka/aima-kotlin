package com.github.andrewgazelka.kotlin_ai.search.local

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

fun OptimizeMethod.optimize(original: Double?, next: Double, temp: Double = 0.0): Optimize {
    when (this) {
        OptimizeMethod.MAX -> if (original == null || next + temp >= original) return Optimize.Better(next)
        OptimizeMethod.MIN -> if (original == null || next - temp <= original) return Optimize.Better(next)
    }
    return Optimize.Worse
}


interface LocalSequenceOptimizer<T : Value> {
    suspend fun optimize(sequence: LocalSequence<T>, start: T, method: OptimizeMethod): T
}

