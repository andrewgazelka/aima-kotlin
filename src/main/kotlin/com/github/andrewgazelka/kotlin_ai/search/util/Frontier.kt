package com.github.andrewgazelka.kotlin_ai.search.util

import com.github.andrewgazelka.kotlin_ai.search.To
import com.github.andrewgazelka.kotlin_ai.util.removeFirst
import com.github.andrewgazelka.kotlin_ai.util.sortedQueue

data class Node<K, V>(var key: K, var value: V, var parent: Node<K, V>? = null)

class Frontier<T>(init: T? = null, private val sortedBy: (Double, T) -> Comparable<*>) {
    private val queue = sortedQueue<Node<T, Int>> { (key, dist, _) ->
        return@sortedQueue sortedBy(dist.toDouble(), key)
    }

    init {
        if (init != null) queue.add(Node(init, 0))
    }

    fun isEmpty() = queue.isEmpty()
    fun pollNode(): Node<T, Int>? = queue.poll()

    private fun removeByKey(key: T) = queue.removeFirst { it.key == key }
    private fun getNodeByKey(key: T) = queue.firstOrNull { it.key == key }

    fun progress(parentNode: Node<T, Int>?, to: To<T>, restrictOnFrontier: Boolean) {
        val child = to.end
        val childNode = getNodeByKey(child)
        if (childNode == null && restrictOnFrontier) return // prevent loops
        val newValue = (parentNode?.value ?: 0) + to.distance
        if (childNode != null && childNode.value <= newValue) return // existing path is shorter
        removeByKey(child)
        queue.add(Node(child, newValue, parentNode))
    }

    private fun backwardSequence(endNode: Node<T, Int>) = generateSequence(endNode) { node -> node.parent }

    fun fullPath(endNode: Node<T, Int>) = fullNodePath(endNode).map { it.key }
    fun fullNodePath(endNode: Node<T, Int>) = backwardSequence(endNode).toList().reversed()
}
