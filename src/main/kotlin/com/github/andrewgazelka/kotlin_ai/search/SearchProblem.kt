package com.github.andrewgazelka.kotlin_ai.search

interface SearchProblem<K, V> {
    val start: K
    fun isEnd(value: K): Boolean
    val graph: Graph<K, V>
}

data class SimpleSearchProblem<K, V>(override val start: K, val end: K, override val graph: Graph<K, V>) :
    SearchProblem<K, V> {
    override fun isEnd(value: K) = value == end
}
