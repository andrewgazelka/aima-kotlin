package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.AStarPathSolver
import com.github.andrewgazelka.kotlin_ai.search.SimpleSearchProblem
import com.github.andrewgazelka.kotlin_ai.search.get
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.straightLineHeuristic
import com.github.andrewgazelka.kotlin_ai.util.pipe
import java.io.File

fun main() {
    val file = File("data/romania.yml")
    val graph = file.to2DGraph()
    val problem = SimpleSearchProblem("Arad", "Bucharest", graph)

    val goal = graph["Bucharest"]
    val heuristic = straightLineHeuristic(goal)
    val solver = AStarPathSolver(heuristic = graph::get.pipe(heuristic))
    println(solver.solve(problem))
}

private val nineProblemStart = NineProblem.of(7, 2, 4, 5, 0, 6, 8, 3, 1)
private val nineProblemEnd = NineProblem((0 until 9).toList())

