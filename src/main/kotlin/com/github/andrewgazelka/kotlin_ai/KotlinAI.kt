package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.*
import com.github.andrewgazelka.kotlin_ai.search.local.*
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblemGraph
import com.github.andrewgazelka.kotlin_ai.search.nine.manhatten
import com.github.andrewgazelka.kotlin_ai.util.pipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.time.ExperimentalTime

suspend fun romania() {
    val file = File("data/romania.yml")
    val graph = file.to2DGraph()
    val problem = SimpleSearchProblem("Arad", "Bucharest", graph)

    val goal = graph["Bucharest"]
    val heuristic = straightLineHeuristic(goal)
    val solver = AStarPathSolver(heuristic = graph::get.pipe(heuristic))
    println(solver.solve(problem))
}

@ExperimentalTime
suspend fun nineProblem() = coroutineScope {
    val nineProblemStart = NineProblem.of(7, 2, 4, 5, 0, 6, 8, 3, 1)
    val nineProblemEnd = NineProblem((0 until 9).toList())
    val graph = NineProblemGraph
    val solver = AStarPathSolver(manhatten(nineProblemEnd))
    solver.solve(graph.problem(start = nineProblemStart, end = nineProblemEnd))
}

suspend fun hillClimb(channel: Channel<TSP>, count: Int) {
    val start = TSP.random(count)
    val climb = HillClimb(1_000_000, channel)
    val optimize = climb.optimize(TSPSequence, start, OptimizeMethod.MIN)
}

suspend fun anneal(channel: Channel<TSP>, count: Int) {
    val start = TSP.random(count)
    val tempSequence = generateSequence(100.0) { it * 0.995 }
//        .takeWhile { it > 0.0001 }
        .map {
            it * it // squaring because we are using dist2
        }
    val climb = SimulatedAnnealing(tempSequence, channel)
    climb.optimize(TSPSequence, start, OptimizeMethod.MIN)
}

@ExperimentalTime
fun main() = runBlocking<Unit> {
//    hillClimb()
}


