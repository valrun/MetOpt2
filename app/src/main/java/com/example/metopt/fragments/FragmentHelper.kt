package com.example.metopt.fragments

import android.content.res.Resources
import android.graphics.Color
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.metopt.PointsOfMethods
import com.example.metopt.R
import com.example.metopt.nmethods.AbstractNMethod
import com.example.metopt.nmethods.QuadraticFunction
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.abs

class FragmentHelper {
    fun getLevelButtonText(level: Boolean, resources: Resources): String {
        return if (!level) {
            resources.getString(R.string.showLevel)
        } else {
            resources.getString(R.string.hideLevel)
        }
    }

    fun getCoordinateLineButtonText(coordinateLine: Boolean, resources: Resources): String {
        return if (!coordinateLine) {
            resources.getString(R.string.showCoordinateLine)
        } else {
            resources.getString(R.string.hideCoordinateLine)
        }
    }

    fun getAxisButtonText(axis: Boolean, resources: Resources): String {
        return if (!axis) {
            resources.getString(R.string.showAxis)
        } else {
            resources.getString(R.string.hideAxis)
        }
    }

    fun getFunAndLvlSeries(
        method: AbstractNMethod,
        f: QuadraticFunction,
        startL: Double,
        startR: Double,
        activity: FragmentActivity?
    ): Pair<Array<LineGraphSeries<DataPoint>>, Array<LineGraphSeries<DataPoint>>> {
        var levelSeries = emptyArray<LineGraphSeries<DataPoint>>()
        var functionSeries = emptyArray<LineGraphSeries<DataPoint>>()

        var i = 0
        var first = true
        var l = startL
        var r = startR

        val points = method.allIteration
        if (points.isEmpty()) {
            return Pair(functionSeries, levelSeries)
        }
        var prevPoint = points.first()

        points.forEach {
//            println(it.fVal)
            var firstPoint = true
            val level = PointsOfMethods().getLevel(it.fVal, f, activity, l, r)
            level.forEach { lvl ->
                if (!lvl.isEmpty) {
                    levelSeries += lvl
                    if (firstPoint) {
                        l = lvl.lowestValueX; r = lvl.highestValueX
                        firstPoint = false
                    } else {
                        l = minOf(l, lvl.lowestValueX); r = maxOf(r, lvl.highestValueX)
                    }
                }
            }
            l -= abs(l)
            r += abs(r)

            val series = if (prevPoint.`val`[0] < it.`val`[0]) {
                LineGraphSeries(
                    arrayOf(
                        DataPoint(prevPoint.`val`[0], prevPoint.`val`[1]),
                        DataPoint(it.`val`[0], it.`val`[1])
                    )
                )
            } else {
                LineGraphSeries(
                    arrayOf(
                        DataPoint(it.`val`[0], it.`val`[1]),
                        DataPoint(prevPoint.`val`[0], prevPoint.`val`[1])
                    )
                )
            }

            series.thickness = 12

            if (first) {
                series.color =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Color.rgb(
                            255f * i / points.size,
                            255f * i / points.size,
                            128f + 127f * i / points.size
                        )
                    } else {
                        Color.RED
                    }
                i += 2
            } else {
                series.color =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Color.rgb(
                            165f + 60f * i / points.size,
                            42f + 183f * i / points.size,
                            42f + 183f * i / points.size
                        )
                    } else {
                        Color.RED
                    }
                i -= 2
            }

            series.setOnDataPointTapListener { _, _ ->
                Toast.makeText(
                    activity,
                    "x1: ${it.`val`[0]} \n x2: ${it.`val`[1]} \n ans: ${it.fVal} \n",
                    Toast.LENGTH_SHORT
                ).show()
            }

            functionSeries += series

            if (2 * i > points.size) {
                first = false
            }

            prevPoint = it
        }

        return Pair(functionSeries, levelSeries)
    }
}


