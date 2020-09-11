package com.github.andrewgazelka.kotlin_ai.search.local
import com.github.andrewgazelka.kotlin_ai.util.allOneSwaps
import kotlin.math.pow
import kotlin.random.Random


data class Point2D(val x: Double, val y: Double){
    fun dist2(other: Point2D): Double {
        return (x-other.x).pow(2) + (y-other.y).pow(2)
    }
}
data class TSP(val points: List<Point2D>): Value{
    val distance2: Double get(){
        var prev = points.first()
        var sum = 0.0
        points.asSequence().drop(1).forEach { next ->
            sum += prev.dist2(next)
            prev = next
        }
        sum += prev.dist2(points.first())
        return sum
    }

    fun oneSwaps() = points.allOneSwaps().map { TSP(it) }

    override fun getValue(): Double {
        return distance2
    }

    companion object {
        fun random(count: Int, xRange: Double = 100.0, yRange: Double = 100.0): TSP {
            val points = (0 until count).map {
                Point2D(Random.nextDouble(xRange), Random.nextDouble(yRange))
            }
            return TSP(points)
        }
    }

}

object TSPSequence : LocalSequence<TSP> {
    override fun neighbors(value: TSP) = value.oneSwaps()
}
