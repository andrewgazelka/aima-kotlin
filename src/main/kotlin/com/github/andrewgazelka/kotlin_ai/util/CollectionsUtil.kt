package com.github.andrewgazelka.kotlin_ai.util

import java.util.*

/**
 *  Create priority queue from elems
 */
fun <T> pQueueOf(vararg elems: T, sort: (T) -> Comparable<*>): PriorityQueue<T> {
    val queue = PriorityQueue<T>(compareBy { x -> sort(x) })
    queue.addAll(elems)
    return queue
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


inline fun loop(block: () -> Unit){
    while (true){
        block()
    }
}
