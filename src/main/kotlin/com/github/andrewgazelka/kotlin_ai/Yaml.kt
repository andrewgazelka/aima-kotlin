package com.github.andrewgazelka.kotlin_ai

import com.github.andrewgazelka.kotlin_ai.search.NamedPoint
import com.github.andrewgazelka.kotlin_ai.search.Connection
import com.github.andrewgazelka.kotlin_ai.search.UndirectedGraph
import java.io.File

private val whiteSpace = """^\s.*""".toRegex()
fun File.to2DGraph(): UndirectedGraph<String, NamedPoint> {
    val connectionList = mutableListOf<Connection<String>>()
    val cityMap = HashMap<String, NamedPoint>()
    var name: String? = null
    var x = -1
    var y = -1
    for (line in this.readLines()) {
        val isName = !whiteSpace.matches(line);
        if (isName) {
            if (name != null) cityMap[name] = NamedPoint(name, x, y)
            name = line.substringBefore(":")
            continue
        }
        val (key, valueStr) = line.split(':').map { it.trim() }
        val value = valueStr.toInt()
        when (key) {
            "x" -> x = value
            "y" -> y = value
            else -> connectionList.add(Connection(name!!, key, value))
        }
    }
    cityMap[name!!] = NamedPoint(name, x, y)
    return UndirectedGraph.from(cityMap, connectionList)
}
