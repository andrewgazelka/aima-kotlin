package com.github.andrewgazelka.kotlin_ai.search

import com.github.andrewgazelka.kotlin_ai.util.removeFirst
import com.github.andrewgazelka.kotlin_ai.util.sortedQueue

data class Node<K, V>(var key: K, var value: V, var parent: Node<K, V>? = null)

class Frontier<K>(init: K? = null, private val sortedBy: (Double, K) -> Comparable<*>) {
    private val queue = sortedQueue<Node<K, Int>> { (key, dist, _) ->
        return@sortedQueue sortedBy(dist.toDouble(), key)
    }

    init {
        if (init != null) queue.add(Node(init, 0))
    }

    fun isEmpty() = queue.isEmpty()
    fun pollNode(): Node<K,Int>? = queue.poll()

    private fun removeByKey(key: K) = queue.removeFirst { it.key == key }
    private fun getNodeByKey(key: K) = queue.firstOrNull { it.key == key }

    fun progress(parentNode: Node<K, Int>?, to: To<K>, restrictOnFrontier: Boolean) {
        val child = to.end
        val childNode = getNodeByKey(child)
        if (childNode == null && restrictOnFrontier) return // prevent loops
        val newValue = (parentNode?.value ?: 0) + to.distance
        if (childNode != null && childNode.value <= newValue) return // existing path is shorter
        removeByKey(child)
        queue.add(Node(child, newValue, parentNode))
    }

    private fun backwardSequence(endNode: Node<K, Int>) = generateSequence(endNode) { node -> node.parent }

    fun fullPath(endNode: Node<K, Int>) = fullNodePath(endNode).map { it.key }
    fun fullNodePath(endNode: Node<K, Int>) = backwardSequence(endNode).toList().reversed()
}
