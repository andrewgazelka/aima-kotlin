package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblemGraph
import com.github.andrewgazelka.kotlin_ai.search.util.GraphProgressor
import java.util.*

//fun main() {
//    val file = File("data/romania.yml")
//    val graph = file.to2DGraph()
//    val problem = SimpleSearchProblem("Arad", "Bucharest", graph)
//    val solver = AStarPathSolver<String,NamedPoint>(noHeuristic())
//    println(solver.solve(problem))
//}

private val nineProblem = NineProblem.of(7, 2, 4, 5, 0, 6, 8, 3, 1)

fun main() {
    GraphProgressor(NineProblemGraph).progress(nineProblem, Scanner(System.`in`))
}
