package com.github.andrewgazelka.kotlin_ai.util

import com.github.andrewgazelka.kotlin_ai.search.To
import com.github.andrewgazelka.kotlin_ai.search.Graph
import com.github.andrewgazelka.kotlin_ai.search.grabTo
import java.util.*

/**
 *  Create priority queue from elems
 */
fun <T> pQueueOf(vararg elems: T, sort: (T) -> Comparable<*>): PriorityQueue<T> {
    val queue = PriorityQueue<T>(compareBy { x -> sort(x) })
    queue.addAll(elems)
    return queue
}

fun <T> sortedQueue(sort: (T) -> Comparable<*>): PriorityQueue<T> {
    return PriorityQueue(compareBy { x -> sort(x) })
}

fun <T> pQueue(vararg elems: T, sort: (T) -> Comparable<*>): PriorityQueue<T> {
    val queue = PriorityQueue<T>(compareBy { x -> sort(x) })
    queue.addAll(elems)
    return queue
}

fun <K, V> Map<K, V>.sortBy(sort: (K, V) -> Comparable<*>) = this.toSortedMap(
    compareBy { key ->
        sort(key, this[key]!!)
    })

fun <T> MutableIterable<T>.removeFirst(block: (T) -> Boolean) = with(iterator()) {
    while (hasNext()) {
        val next = next()
        if (block(next)) {
            remove()
            return@with next
        }
    }
    return@with null
}

// TODO: does not work NPE
fun <K, V> List<K>.toPath(graph: Graph<K, V>): List<To<K>> {
    val start = listOf(To(first(), 0))
    return this.foldRight(start) { k, acc ->
        val to = graph.grabTo(start.last().end, k)
        acc + listOf(to!!)
    }
}

fun <A, B> Sequence<A>.cartesianProduct(other: Sequence<B>) = sequence<Pair<A, B>> {
    for (a in this@cartesianProduct) {
        for (b in other) {
            yield(Pair(a, b))
        }
    }
}

fun <T> MutableList<T>.swap(indexA: Int, indexB: Int){
    val temp = this[indexA]
    this[indexA] = this[indexB]
    this[indexB] = temp
}

fun <T> Sequence<T>.random(): T {
    return this.toList().random()
}
