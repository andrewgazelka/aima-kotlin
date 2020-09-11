package com.github.andrewgazelka.kotlin_ai.search.nine

import com.github.andrewgazelka.kotlin_ai.search.Graph
import com.github.andrewgazelka.kotlin_ai.search.Heuristic
import com.github.andrewgazelka.kotlin_ai.search.To
import com.github.andrewgazelka.kotlin_ai.util.cachedFunc
import com.github.andrewgazelka.kotlin_ai.util.swap
import kotlin.math.absoluteValue

private fun Int.adjacent(end: Int) = sequence {
    when (this@adjacent) {
        0 -> yield(1)
        end -> yield(end - 1)
        else -> {
            yield(this@adjacent - 1)
            yield(this@adjacent + 1)
        }
    }
}

data class Index2D(val x: Int, val y: Int) {
    fun manhatten(other: Index2D): Int {
        return (other.x - x).absoluteValue + (other.y - y).absoluteValue
    }

    companion object {
        fun from(index: Int, rowLength: Int): Index2D {
            return Index2D(index % rowLength, index / rowLength);
        }
    }
}

data class NineProblem(val collection: List<Int>) {

    operator fun get(x: Int, y: Int): Int {
        return collection[x + y * 3]
    }

    fun rows() = collection.run {
        listOf(subList(0, 3), subList(3, 6), subList(6, 9))
    }


    override fun toString() = buildString {
        appendLine()
        for (y in 0 until 3) {
            for (x in 0 until 3) {
                val get = get(x, y)
                append(if (get == 0) " " else get.toString())
            }
            appendLine()
        }
    }

    operator fun get(index2D: Index2D): Int {
        return get(index2D.x, index2D.y)
    }

    private fun emptyIndexRaw(): Int {
        return collection.indexOf(0)
    }

    private fun emptyIndex(): Index2D {
        val idx = emptyIndexRaw()
        return Index2D(idx % 3, idx / 3);
    }

    private fun swap(a: Index2D, b: Index2D): NineProblem {
        val aIdx = a.x + a.y * 3
        val bIdx = b.x + b.y * 3
        val newList = collection.toMutableList().apply { swap(aIdx, bIdx) }
        return NineProblem(newList)
    }

    private fun possibleMoves(): Sequence<Index2D> {
        val (x, y) = emptyIndex()
        return x.adjacent(2).map { Index2D(it, y) } + y.adjacent(2).map { Index2D(x, it) }
    }

    fun possibleNewStates(): Sequence<NineProblem> {
        val original = emptyIndex()
        return possibleMoves().map { idx ->
            swap(original, idx)
        }
    }

    companion object {
        fun random(): NineProblem {
            return NineProblem((0..9).toList().shuffled())
        }

        fun of(vararg nums: Int): NineProblem {
            return NineProblem(nums.toList())
        }
    }
}

object NineProblemGraph : Graph<NineProblem> {
    override fun connections(key: NineProblem): Sequence<To<NineProblem>> {
        return key.possibleNewStates().map { To(it, 1) } // all moves have a value of 1
    }
}

private val valueToIndex = cachedFunc<NineProblem, IntArray> { nineProblem ->
    val arr = IntArray(9)
    nineProblem.collection.forEachIndexed { index, value ->
        arr[value] = index
    }
    arr
}

fun manhatten(goal: NineProblem): Heuristic<NineProblem> = { x ->
    val map = valueToIndex(goal)
    var sum = 0
    x.collection.forEachIndexed { index, value ->
        val otherIndex = map[value]
        val otherIndex2D = Index2D.from(otherIndex, 3)
        val currentIndex2D = Index2D.from(index, 3)
        sum += otherIndex2D.manhatten(currentIndex2D)
    }
    sum.toDouble()
}

fun main() {
    println(NineProblem.random())
}
