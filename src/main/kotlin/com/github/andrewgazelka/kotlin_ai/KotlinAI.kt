package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.*
import com.github.andrewgazelka.kotlin_ai.search.local.HillClimb
import com.github.andrewgazelka.kotlin_ai.search.local.OptimizeMethod
import com.github.andrewgazelka.kotlin_ai.search.local.TSP
import com.github.andrewgazelka.kotlin_ai.search.local.TSPSequence
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblemGraph
import com.github.andrewgazelka.kotlin_ai.search.nine.manhatten
import com.github.andrewgazelka.kotlin_ai.util.pipe
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

fun hillClimb() {
    val start = TSP.random(20)
    val climb = HillClimb<TSP>(100)
    val optimize = climb.optimize(TSPSequence, start, OptimizeMethod.MIN)
    println("optimized $optimize")
}

@ExperimentalTime
fun main() = runBlocking<Unit> {
    hillClimb()
}


