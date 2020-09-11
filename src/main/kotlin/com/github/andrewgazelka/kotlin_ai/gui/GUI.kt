package com.github.andrewgazelka.kotlin_ai.gui

import com.github.andrewgazelka.kotlin_ai.anneal
import com.github.andrewgazelka.kotlin_ai.nineProblem
import com.github.andrewgazelka.kotlin_ai.search.SolverResult
import com.github.andrewgazelka.kotlin_ai.search.local.TSP
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import javafx.scene.Group
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.text.Font
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import tornadofx.*
import java.lang.Double.min
import kotlin.random.Random
import kotlin.time.ExperimentalTime

fun HBox.numBox(num: Int) = stackpane {
    hboxConstraints {
        hGrow = Priority.ALWAYS
    }
    style {
        borderColor += box(Color.BLACK)
    }
    rectangle {
        fill = Color.BEIGE
        width = 50.0
        height = 50.0
    }

    text(if (num == 0) " " else num.toString()) {
        stroke = Color.BLACK
        font = Font.font(20.0)
    }

}


fun Group.displayNineProblem(nineProblem: NineProblem) {
    vbox {
        nineProblem.rows().map { row ->
            hbox {
                row.forEach { elem -> numBox(elem) }
            }
        }
    }

}

data class UpdateNineProblem(val nineProblem: NineProblem) : FXEvent(EventBus.RunOn.ApplicationThread)

@ExperimentalTime
class NineProblemView : View() {

    init {
        GlobalScope.launch {
            val result = nineProblem() as? SolverResult.Found ?: return@launch
            while (true) {
                fire(UpdateNineProblem(result.path.first()))
                delay(2_000)
                result.path.asSequence().drop(1).forEach { problem ->
                    delay(100)
                    fire(UpdateNineProblem(problem))
                }
                delay(2_000)
            }
        }
    }

    lateinit var grp: Group
    override val root = borderpane {
        style {
            padding = box(20.px)
        }
        center {
            grp = group {
                subscribe<UpdateNineProblem> { (nineProblem) ->
                    grp.children.clear()
                    displayNineProblem(nineProblem)
                }
            }

        }
    }

}

data class UpdateTSP(val tsp: TSP) : FXEvent(EventBus.RunOn.ApplicationThread)

@ExperimentalTime
class TSPProblem : View() {

    init {
        GlobalScope.launch {
            val channel = Channel<TSP>(Channel.UNLIMITED)
            launch {
                channel.consumeAsFlow()
                    .collectIndexed { i, tsp ->
                        fire(UpdateTSP(tsp))
//                        delay(100)
                    }
            }
            while (true) {
                anneal(channel, 100)
//                delay(500)
            }
        }
    }

    lateinit var grp: Group
    override val root = borderpane {
        style {
            padding = box(20.px)
            backgroundColor += Color.BLACK
        }
        center {
            grp = group {
                subscribe<UpdateTSP> { (tsp) ->
                    grp.children.clear()
                    path {
                        val range = 1000 * 10
                        val time = System.currentTimeMillis() % range
                        val proportion = time.toDouble() / range
                        val scale = min(currentStage!!.width, currentStage!!.height) * 0.7
//                        strokeLineCap = StrokeLineCap.ROUND
//                        strokeLineJoin = StrokeLineJoin.MITER
                        stroke = Color.hsb(proportion * 360.0, 1.0, 1.0, 0.8)
                        strokeWidth = 15.0
                        val points = tsp.points.map { it * scale }
                        val first = points.first()
                        moveTo(first.x, first.y)
                        points.asSequence().drop(1).forEach { (x, y) ->
//                            val controlX = Random.nextDouble(scale)
//                            val controlY = Random.nextDouble(scale)
                            this.lineTo(x, y)
//                            lineTo(x, y)
                        }
                        lineTo(first.x, first.y)
                    }
//                    val num = "%.2f".format(tsp.getValue().pow(0.5))
//                    text(num) {
//                        stroke = Color.RED
//                    }

                }
            }

        }
    }

}

@ExperimentalTime
class MyApp : App(TSPProblem::class) {
    override fun start(stage: Stage) {
        stage.minHeight = 400.0
        stage.isFullScreen = true
        stage.minWidth = 800.0
        super.start(stage)
    }
}

@ExperimentalTime
fun main(args: Array<String>) {
    launch<MyApp>(args)
}
