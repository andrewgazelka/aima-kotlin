package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.AStarPathSolver
import com.github.andrewgazelka.kotlin_ai.search.SimpleSearchProblem
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.noHeuristic
import java.io.File

fun main() {
    val file = File("data/romania.yml")
    val graph = file.to2DGraph()
    val problem = SimpleSearchProblem("Arad", "Bucharest", graph)
    val solver = AStarPathSolver<String>(noHeuristic())
    println(solver.solve(problem))
}

private val nineProblemStart = NineProblem.of(7, 2, 4, 5, 0, 6, 8, 3, 1)
private val nineProblemEnd = NineProblem((0 until 9).toList())

//fun main() {
//
//    val solver = AStarPathSolver<NineProblem>(manhatten(nineProblemEnd))
//    GraphProgressor(NineProblemGraph).progress(nineProblemStart, Scanner(System.`in`))
//}
