package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.*
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import java.io.File

class HelloWorld : View() {

    private val problem = run {
        val file = File("data/romania.yml")
        val graph = file.to2DGraph()
        SimpleSearchProblem("Arad", "Bucharest", graph)
    }

    override val root = group {
        val graph = problem.graph
        graph.allConnections.forEach { (from, to, _) ->
            line {
                val fromPoint = graph[from]!!
                val toPoint = graph[to]!!
                startX = fromPoint.x.toDouble()
                startY = fromPoint.y.toDouble()
                endX = toPoint.x.toDouble()
                endY = toPoint.y.toDouble()
            }
        }
        graph.entries.forEach { (key, point) ->
            circle {
                centerX = point.x.toDouble()
                centerY = point.y.toDouble()
                println("x $centerX, y $centerY")
                fill = when {
                    problem.isEnd(key) -> Color.RED
                    problem.start == key -> Color.LIMEGREEN
                    else -> Color.BLACK
                }
                radius = 5.0
            }
        }
        val (traj, dist) =
            AStarPathSolver<String, NamedPoint>(noHeuristic()).solve(problem) as? SolverResult.Found ?: return@group
        path {
            stroke = Color.AQUA
            strokeWidth = 2.0
            val (_,x,y) = graph[traj.first()]!!
            moveTo(x, y)
            traj.asSequence().drop(1).forEach {key ->
                val (_,x,y) = graph[key]!!
                lineTo(x, y)
            }
        }
    }
}

class HelloWorldApp : App(HelloWorld::class, Styles::class)

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }

}

fun main(args: Array<String>) {
    launch<HelloWorldApp>(args)
}
