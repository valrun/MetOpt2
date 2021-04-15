package com.example.metopt.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.metopt.PointsOfMethods
import com.example.metopt.R
import com.example.metopt.nmethods.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_gradient.view.*


class GradientFragment : Fragment() {
    lateinit var levelSeries: Array<LineGraphSeries<DataPoint>>
    lateinit var functionSeries: Array<LineGraphSeries<DataPoint>>
    lateinit var graph: GraphView

    var level = true
    var coordinateLine = true
    var axis = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        levelSeries = emptyArray()
        functionSeries = emptyArray()

        val f = QuadraticFunction(
            listOf(listOf(20.0, 0.0), listOf(0.0, 3.0)),
            listOf(-7.0, 3.0),
            2.0
        )
        val method = GradientOpt(f)
        PointsOfMethods().getLevels(2, f, activity, -20.0, 20.0).forEach {
            levelSeries += it
        }
        PointsOfMethods().getLevel(1.0, f, activity, -20.0, 20.0).forEach {
            levelSeries += it
        }
        PointsOfMethods().getLevel(0.50, f, activity, -20.0, 20.0).forEach {
            levelSeries += it
        }


        var l = -20.0;
        var r = 20.0

        var i = 0;
        var first = true

        val points = method.allIteration
        var prevPoint = points[0]

        points.forEach {
            println(it.fVal)
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
                series.color = Color.rgb(
                    255f * i / points.size,
                    255f * i / points.size,
                    128f + 127f * i / points.size
                )
                i += 2
            } else {
                series.color = Color.rgb(
                    165f + 60f * i / points.size,
                    42f + 183f * i / points.size,
                    42f + 183f * i / points.size
                )
                i -= 2
            }

            series.setOnDataPointTapListener { series, dataPoint ->
                Toast.makeText(
                    activity,
                    "Current Answer\n x: ${it.`val`[0]} \n y: ${it.`val`[1]} \n z: ${it.fVal} \n",
                    Toast.LENGTH_SHORT
                ).show()
            }

            functionSeries += series
            levelSeries += level

            if (2 * i > points.size) {
                first = false
            }

            prevPoint = it
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_gradient, container, false)

        //BUTTONS
        val prevButton: AppCompatButton = view.findViewById<View>(R.id.prev) as AppCompatButton
        prevButton.setOnClickListener {
            val last = graph.series.size - levelSeries.size
            if (last > 0) {
                graph.removeSeries(graph.series[graph.series.size - 1])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = graph.series.size - levelSeries.size
            if (last < functionSeries.size) {
                graph.addSeries(functionSeries[last])
            }
        }

        val levelButton: AppCompatButton = view.findViewById<View>(R.id.level) as AppCompatButton
        levelButton.setOnClickListener {
            val lastFun = if (level) {
                graph.series.size - levelSeries.size - 1
            } else {
                graph.series.size - 1
            }
            graph.removeAllSeries()
            if (!level) {
                levelSeries.forEach { graph.addSeries(it) }
            }
            for (i in 0..lastFun) {
                graph.addSeries(functionSeries[i])
            }
            levelButton.text = if (level) {
                resources.getString(R.string.showLevel)
            } else {
                resources.getString(R.string.hideLevel)
            }
            level = !level
        }

        val coorButton: AppCompatButton = view.findViewById<View>(R.id.coordinateLine) as AppCompatButton
        coorButton.setOnClickListener {
            graph.gridLabelRenderer.isHorizontalLabelsVisible = !coordinateLine
            graph.gridLabelRenderer.isVerticalLabelsVisible = !coordinateLine
            coorButton.text = if (coordinateLine) {
                resources.getString(R.string.showCoordinateLine)
            } else {
                resources.getString(R.string.hideCoordinateLine)
            }
            coordinateLine = !coordinateLine
        }

        val axisButton: AppCompatButton = view.findViewById<View>(R.id.axis) as AppCompatButton
        axisButton.setOnClickListener {
            graph.gridLabelRenderer.isHighlightZeroLines = !axis
            if (axis) {
                graph.gridLabelRenderer.horizontalAxisTitle = ""
                graph.gridLabelRenderer.verticalAxisTitle = ""
                axisButton.text = resources.getString(R.string.showAxis)
            } else {
                graph.gridLabelRenderer.horizontalAxisTitle = "- ось Ox -"
                graph.gridLabelRenderer.verticalAxisTitle = "- ось Oy -"
                axisButton.text = resources.getString(R.string.hideAxis)
            }
            axis = !axis
        }

        //GRAPH
        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)

        graph.gridLabelRenderer.horizontalAxisTitle = "- ось Ox -"
        graph.gridLabelRenderer.verticalAxisTitle = "- ось Oy -"

        levelSeries.forEach { graph.addSeries(it) }
        functionSeries.forEach { graph.addSeries(it) }


        return view
    }
}


