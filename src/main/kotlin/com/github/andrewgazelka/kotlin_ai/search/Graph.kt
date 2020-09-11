package com.github.andrewgazelka.kotlin_ai.search

import kotlin.math.absoluteValue

data class Connection<T>(val from: T, val to: T, val distance: Int)
data class To<T>(val end: T, val distance: Int)

fun <T> Graph<T>.grabTo(from: T, to: T): To<T>? = this.connections(from).firstOrNull { it.end == to }

operator fun <K, V> GraphWithData<K, V>.get(value: K): V = getValue(value)!!
interface GraphWithData<K, V> : Graph<K> {
    fun getValue(value: K): V?

    companion object {
        fun <K, V> wrap(graph: Graph<K>, map: Map<K, V>): GraphWithData<K, V> {
            return object : GraphWithData<K, V> {
                override fun getValue(value: K): V? {
                    return map[value]
                }

                override fun connections(key: K): Sequence<To<K>> {
                    return graph.connections(key)
                }

            }
        }
    }
}

interface Graph<T> {
    fun connections(key: T): Sequence<To<T>>

    companion object {
        fun <T> from(connections: Iterable<Connection<T>>): Graph<T> {

            val connectionMap = HashMap<T, ArrayList<To<T>>>()
            connections.forEach { con ->
                connectionMap.getOrPut(con.from) { ArrayList() }.add(To(con.to, con.distance))
                connectionMap.getOrPut(con.to) { ArrayList() }.add(To(con.from, con.distance))
            }

            return object : Graph<T> {
                override fun connections(key: T): Sequence<To<T>> {
                    return connectionMap[key]?.asSequence() ?: emptySequence()
                }
            }
        }
    }
}

interface Vector<T> {
    fun asVector(): List<T>
}

fun Vector<Int>.l1norm(other: Vector<Int>): Int =
    asVector().zip(other.asVector()).map { (a, b) -> a - b }.sum().absoluteValue

data class NamedPoint(val name: String, val x: Int, val y: Int) : Vector<Int> {
    override fun asVector(): List<Int> = listOf(x, y)
}




