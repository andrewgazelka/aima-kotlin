package com.github.andrewgazelka.kotlin_ai.gui

import com.github.andrewgazelka.kotlin_ai.nineProblem
import com.github.andrewgazelka.kotlin_ai.search.SolverResult
import com.github.andrewgazelka.kotlin_ai.search.nine.NineProblem
import javafx.scene.Group
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*
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

@ExperimentalTime
class TSPProblem : View() {

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

@ExperimentalTime
class MyApp : App(MyView::class) {
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
