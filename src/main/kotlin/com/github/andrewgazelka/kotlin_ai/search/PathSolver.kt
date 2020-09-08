package com.github.andrewgazelka.kotlin_ai.search

import com.github.andrewgazelka.kotlin_ai.util.pQueueOf
import com.github.andrewgazelka.kotlin_ai.util.removeFirst
import java.util.*
import kotlin.collections.ArrayList

sealed class SolverResult<K> {
    class Fail<K> : SolverResult<K>()
    data class Found<K>(val path: List<To<K>>, val dist: Int) : SolverResult<K>()
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
        return SolverResult.Found(trajectory, trajectory.distance)
    }
}

typealias Heuristic<V> = (current: V) -> Double // TODO: good practice?

fun straightLineHeuristic(goal: Vector<Int>): Heuristic<Vector<Int>> = { current -> current.l1norm(goal).toDouble() }
fun <T> noHeuristic(): Heuristic<T> = { 0.0 }

fun <K> PriorityQueue<To<K>>.removeByKey(key: K) = this.removeFirst { it.end == key }
fun <K> PriorityQueue<To<K>>.containsKey(key: K) = this.any { it.end == key }
fun <K> PriorityQueue<To<K>>.getByKey(key: K): To<K>? = this.firstOrNull { it.end == key }


class AStarPathSolver<K, V>(private val heuristic: Heuristic<V>) : PathSolver<K, V> {

    // https://stackoverflow.com/questions/29872664/add-key-and-value-into-an-priority-queue-and-sort-by-key-in-java
    override fun solve(problem: SearchProblem<K, V>): SolverResult<K> {

        val graph = problem.graph
        var key = problem.start

        val frontier = pQueueOf(To(key, 0)) { (key, dist) ->
            dist + heuristic(graph[key]!!)
        }


        val explored = HashSet<K>()

        val trajectory = ArrayList<To<K>>()

        do {
            if (frontier.isEmpty()) return SolverResult.Fail()
            val on = frontier.poll()
            key = on.end
            if (problem.isEnd(key)) return SolverResult.Found(trajectory, on.distance)
            explored.add(key)
            for (localTo in graph.connections(key)) {
                val globalTo = localTo.addDistance(on.distance)
                val child = localTo.end
                val frontierChild by lazy { frontier.getByKey(child) }
                if (child !in explored && frontierChild != null) {
                    frontier.add(globalTo) // keep track of previous distances
                } else if (frontierChild == null) {
                    frontier.add(globalTo)
                } else if (frontierChild!!.distance > globalTo.distance) {
                    frontier.removeByKey(child)
                }
            }
        } while (true)
    }

}



