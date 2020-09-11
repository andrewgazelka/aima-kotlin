package com.github.andrewgazelka.kotlin_ai.gui

import com.github.andrewgazelka.kotlin_ai.hillClimb
import com.github.andrewgazelka.kotlin_ai.nineProblem
import com.github.andrewgazelka.kotlin_ai.search.SolverResult
import com.github.andrewgazelka.kotlin_ai.search.local.TSP
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import javafx.scene.Group
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import tornadofx.*
import kotlin.math.pow
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
            val channel = Channel<TSP>()
            launch {
                channel.consumeAsFlow().collect {
                    fire(UpdateTSP(it))
                    delay(1)
                }
            }
            while(true){
                hillClimb(channel, 250)
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
                subscribe<UpdateTSP> { (tsp) ->
                    grp.children.clear()
                    path {
                        stroke = Color.BLACK
                        val points = tsp.points.map { it * 5.0 }
                        val first = points.first()
                        moveTo(first.x, first.y)
                        points.asSequence().drop(1).forEach { (x, y) ->
                            lineTo(x, y)
                        }
                        lineTo(first.x, first.y)
                    }
                    val num = "%.2f".format(tsp.getValue().pow(0.5))
                    text(num) {
                        stroke = Color.RED
                    }

                }
            }

        }
    }

}

@ExperimentalTime
class MyApp : App(TSPProblem::class) {
    override fun start(stage: Stage) {
        stage.minHeight = 400.0
        stage.minWidth = 800.0
        super.start(stage)
    }
}

@ExperimentalTime
fun main(args: Array<String>) {
    launch<MyApp>(args)
}
