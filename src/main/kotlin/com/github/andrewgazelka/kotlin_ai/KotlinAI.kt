package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.*
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblemGraph
import com.github.andrewgazelka.kotlin_ai.search.nine.manhatten
import com.github.andrewgazelka.kotlin_ai.util.pipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
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

@ExperimentalTime
fun main() = runBlocking<Unit> {
    nineProblem()
}


