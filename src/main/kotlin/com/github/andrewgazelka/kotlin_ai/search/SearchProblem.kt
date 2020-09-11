package com.github.andrewgazelka.kotlin_ai.search

interface SearchProblem<T> {
    val start: T
    fun isEnd(value: T): Boolean
    val graph: Graph<T>
}

data class SimpleSearchProblem<T>(override val start: T, val end: T, override val graph: Graph<T>) : SearchProblem<T> {
    override fun isEnd(value: T) = value == end
}

fun <T> Graph<T>.problem(start: T, end: T): SimpleSearchProblem<T> {
    return SimpleSearchProblem(start, end, this)
}
