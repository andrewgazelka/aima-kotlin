package com.github.andrewgazelka.kotlin_ai.search.nine

import com.github.andrewgazelka.kotlin_ai.search.Graph
import com.github.andrewgazelka.kotlin_ai.search.To
import com.github.andrewgazelka.kotlin_ai.util.swap

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

data class Index2D(val x: Int, val y: Int)
data class NineProblem(val collection: List<Int>) {

    operator fun get(x: Int, y: Int): Int {
        return collection[x + y * 3]
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

    fun emptyIndexRaw(): Int {
        return collection.indexOf(0)
    }

    fun emptyIndex(): Index2D {
        val idx = emptyIndexRaw()
        return Index2D(idx % 3, idx / 3);
    }

    fun swap(a: Index2D, b: Index2D): NineProblem {
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

object NineProblemGraph : Graph<NineProblem, Nothing> {
    override fun connections(key: NineProblem): Sequence<To<NineProblem>> {
        return key.possibleNewStates().map { To(it, 1) } // all moves have a value of 1
    }

    override fun get(key: NineProblem): Nothing? {
        TODO("Not yet implemented")
    }

}

fun main() {
    println(NineProblem.random())
}
