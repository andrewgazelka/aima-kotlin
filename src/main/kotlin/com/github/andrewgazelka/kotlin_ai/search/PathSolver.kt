package com.github.andrewgazelka.kotlin_ai.search

import com.github.andrewgazelka.kotlin_ai.util.random
import kotlinx.coroutines.channels.SendChannel
import java.util.*
import kotlin.collections.ArrayList

sealed class SolverResult<K> {
    class Fail<K> : SolverResult<K>()
    data class Found<K>(val path: List<K>, val dist: Int) : SolverResult<K>()
}

interface PathSolver<K, V> {
    fun solve(problem: SearchProblem<K, V>): SolverResult<K>
}

val List<To<*>>.distance get() = foldRight(0) { x, acc -> acc + x.distance }

class RandomPathSolver<K, V> : PathSolver<K, V> {
    override fun solve(problem: SearchProblem<K, V>): SolverResult<K> {
        var state = problem.start
        val trajectory = ArrayList<To<K>>()
        do {
            val randomAction = problem.graph.connections(state).random()
            trajectory.add(randomAction)
            state = randomAction.end
        } while (!problem.isEnd(state))
        return SolverResult.Found(trajectory.map { it.end }, trajectory.distance)
    }
}

typealias Heuristic<V> = (current: V) -> Double // TODO: good practice?

fun straightLineHeuristic(goal: Vector<Int>): Heuristic<Vector<Int>> = { current -> current.l1norm(goal).toDouble() }
fun <T> noHeuristic(): Heuristic<T> = { 0.0 }


class AStarPathSolver<K, V>(private val heuristic: Heuristic<V>, private val channel: SendChannel<K>? = null) : PathSolver<K, V> {

    // https://stackoverflow.com/questions/29872664/add-key-and-value-into-an-priority-queue-and-sort-by-key-in-java
    override fun solve(problem: SearchProblem<K, V>): SolverResult<K> {

        val graph = problem.graph
        var keyOn = problem.start

        val frontier = Frontier(init = keyOn) { dist, key ->
            dist + heuristic(graph[key]!!)
        }

        val explored = HashSet<K>()

        do {
            val on = frontier.pollNode() ?: return SolverResult.Fail()
            keyOn = on.key
            if (problem.isEnd(keyOn)) return SolverResult.Found(frontier.fullPath(on), on.value)
            explored.add(keyOn)
            for (to in graph.connections(keyOn)) {
                channel?.offer(to.end)
                val restrictOnFrontier = to.end in explored
                frontier.progress(on, to, restrictOnFrontier)
            }
        } while (true)
    }

}



