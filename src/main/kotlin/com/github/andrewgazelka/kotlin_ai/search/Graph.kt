package com.github.andrewgazelka.kotlin_ai.search

import kotlin.math.absoluteValue

data class Connection<T>(val from: T, val to: T, val distance: Int)
data class To<T>(val end: T, val distance: Int){
    fun addDistance(dx: Int) = To(end, distance + dx)
}

fun <K,V> Graph<K,V>.grabTo(from: K, to:K ): To<K>? = this.connections(from).firstOrNull { it.end  == to}

interface Graph<K, V> {
    fun connections(key: K): Sequence<To<K>>
    operator fun get(key: K): V?

    companion object {
        fun <K, V> from(map: Map<K, V>, connections: Iterable<Connection<K>>): Graph<K, V> {

            val connectionMap = HashMap<K, ArrayList<To<K>>>()
            connections.forEach { con ->
                connectionMap.getOrPut(con.from) { ArrayList() }.add(To(con.to, con.distance))
                connectionMap.getOrPut(con.to) { ArrayList() }.add(To(con.from, con.distance))
            }

            return object : Graph<K, V> {
                override fun connections(key: K): Sequence<To<K>> {
                    return connectionMap[key]?.asSequence() ?: emptySequence()
                }


                override fun get(key: K): V? = map[key]
            }
        }
    }
}

interface Vector<T> {
    fun asVector(): List<T>
}

fun Vector<Int>.l1norm(other: Vector<Int>): Int = asVector().zip(other.asVector()).map { (a,b) -> a-b }.sum().absoluteValue

data class NamedPoint(val name: String, val x: Int, val y: Int) : Vector<Int> {
    override fun asVector(): List<Int> = listOf(x, y)
}




