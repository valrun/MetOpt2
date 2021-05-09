package com.example.metopt.fragments

import android.content.res.Resources
import android.graphics.Color
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.metopt.PointsOfMethods
import com.example.metopt.R
import com.example.metopt.nmethods.*
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
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

    fun clickCoordinateButton(
        coordinateLine: Boolean,
        gridLabelRenderer: GridLabelRenderer,
        coordinateButton: Button,
        resources: Resources
    ) {
        gridLabelRenderer.isHorizontalLabelsVisible = coordinateLine
        gridLabelRenderer.isVerticalLabelsVisible = coordinateLine
        coordinateButton.text =
            FragmentHelper().getCoordinateLineButtonText(coordinateLine, resources)
    }

    fun clickAxisButton(
        axis: Boolean,
        gridLabelRenderer: GridLabelRenderer,
        axisButton: Button,
        resources: Resources
    ) {
        gridLabelRenderer.isHighlightZeroLines = axis
        if (axis) {
            gridLabelRenderer.horizontalAxisTitle = "- ось Ox1 -"
            gridLabelRenderer.verticalAxisTitle = "- ось Ox2 -"
        } else {
            gridLabelRenderer.horizontalAxisTitle = ""
            gridLabelRenderer.verticalAxisTitle = ""
        }
        axisButton.text = FragmentHelper().getAxisButtonText(axis, resources)
    }

    fun getMethod(i: Int, f: QuadraticFunction, eps: Double? = null): AbstractNMethod {
        return when (i) {
            1 -> if (eps == null) GradientMethod(f) else GradientMethod(f, eps)
            2 -> if (eps == null) FastGradientMethod(f) else FastGradientMethod(f, eps)
            3 -> if (eps == null) ConjugateGradientMethod(f) else ConjugateGradientMethod(f, eps)
            else -> GradientMethod(f)
        }
    }

    fun getNameMethod(i: Int): String {
        return when (i) {
            1 -> "Gradient Method"
            2 -> "Fast Gradient Method"
            3 ->  "Conjugate Gradient Method"
            else -> "Method"
        }
    }

    fun getFunAndLvlSeries(
        method: AbstractNMethod,
        f: QuadraticFunction,
        startL: Double,
        startR: Double,
        activity: FragmentActivity?
    ): Triple<Array<LineGraphSeries<DataPoint>>, Array<LineGraphSeries<DataPoint>>, Pair<PointsGraphSeries<DataPoint>, Double>> {
        var levelSeries = emptyArray<LineGraphSeries<DataPoint>>()
        var functionSeries = emptyArray<LineGraphSeries<DataPoint>>()

        var i = 0
        var first = true
        var l = startL
        var r = startR

        val points = method.allIteration
        if (points.isEmpty()) {
            return Triple(functionSeries, levelSeries, Pair(PointsGraphSeries<DataPoint>(), 0.0))
        }
        var prevPoint = points.first()
//        println(points.size)

        points.forEach {
//            println("VAL:" + it.fVal)
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
                    l = maxOf(startL, l); r = minOf(r, startR)
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

        val answerSeries = PointsGraphSeries(
            arrayOf(
                DataPoint(
                    points.last().`val`[0],
                    points.last().`val`[1]
                )
            )
        )
        answerSeries.color = Color.RED
        answerSeries.setOnDataPointTapListener { _, _ ->
            Toast.makeText(
                activity,
                "MINIMUM \n x1: ${points.last().`val`[0]} \n x2: ${points.last().`val`[1]} \n ans: ${points.last().fVal} \n",
                Toast.LENGTH_SHORT
            ).show()
        }

        return Triple(functionSeries, levelSeries, Pair(answerSeries, points.last().fVal))
    }
}


