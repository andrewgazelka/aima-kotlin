package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.*
import java.io.File

fun main() {
    val file = File("data/romania.yml")
    val graph = file.to2DGraph()
    val problem = SimpleSearchProblem("Arad", "Bucharest", graph)
    val solver = AStarPathSolver<String,NamedPoint>(noHeuristic())
    println(solver.solve(problem))
}
