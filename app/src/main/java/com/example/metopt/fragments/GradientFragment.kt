package com.example.metopt.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.metopt.R
import com.example.metopt.nmethods.GradientOpt
import com.example.metopt.nmethods.QuadraticFunction
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_gradient.view.*

class GradientFragment : Fragment() {
    lateinit var levelSeries: Array<LineGraphSeries<DataPoint>>
    lateinit var functionSeries: Array<LineGraphSeries<DataPoint>>
    lateinit var graph: GraphView

    private var level = true
    private var coordinateLine = true
    private var axis = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val f = QuadraticFunction(
            listOf(listOf(20.0, 2.0), listOf(2.0, 3.0)),
            listOf(-5.0, 3.0),
            2.0
        )

        val series =
            FragmentHelper().getFunAndLvlSeries(GradientOpt(f), f, -25.0, 25.0, this.activity)

        functionSeries = series.first
        levelSeries = series.second
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
            val last = if (level) {graph.series.size - levelSeries.size} else {graph.series.size}
            if (last > 1) {
                graph.removeSeries(graph.series[graph.series.size - 1])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = if (level) {graph.series.size - levelSeries.size} else {graph.series.size}
            if (last < functionSeries.size) {
                graph.addSeries(functionSeries[last])
            }
        }

        val levelButton: AppCompatButton = view.findViewById<View>(R.id.level) as AppCompatButton
        levelButton.setOnClickListener {
            clickLevelButton(levelButton)
        }

        val coorButton: AppCompatButton =
            view.findViewById<View>(R.id.coordinateLine) as AppCompatButton
        coorButton.setOnClickListener {
            clickCoordinateButton(coorButton)
        }

        val axisButton: AppCompatButton = view.findViewById<View>(R.id.axis) as AppCompatButton
        axisButton.setOnClickListener {
            clickAxisButton(axisButton)
        }

        //GRAPH
        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        if (level) {levelSeries.forEach { graph.addSeries(it) } }
        functionSeries.forEach { graph.addSeries(it) }

        levelButton.text = FragmentHelper().getLevelButtonText(level, resources)
        coordinateLine = !coordinateLine
        clickCoordinateButton(coorButton)
        axis = !axis
        clickAxisButton(axisButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level = GradientFragmentArgs.fromBundle(requireArguments()).level
        coordinateLine = GradientFragmentArgs.fromBundle(requireArguments()).coordinateLine
        axis = GradientFragmentArgs.fromBundle(requireArguments()).axis
    }

    private fun clickLevelButton(levelButton: AppCompatButton) {
        level = !level
        val lastFun = if (level) {
            graph.series.size - 1
        } else {
            graph.series.size - levelSeries.size - 1
        }
        graph.removeAllSeries()
        if (level) {
            levelSeries.forEach { graph.addSeries(it) }
        }
        for (i in 0..lastFun) {
            graph.addSeries(functionSeries[i])
        }
        levelButton.text = FragmentHelper().getLevelButtonText(level, resources)
    }

    private fun clickCoordinateButton(coordinateButton: AppCompatButton) {
        coordinateLine = !coordinateLine
        graph.gridLabelRenderer.isHorizontalLabelsVisible = coordinateLine
        graph.gridLabelRenderer.isVerticalLabelsVisible = coordinateLine
        coordinateButton.text =
            FragmentHelper().getCoordinateLineButtonText(coordinateLine, resources)
    }

    private fun clickAxisButton(axisButton: AppCompatButton) {
        axis = !axis
        graph.gridLabelRenderer.isHighlightZeroLines = axis
        if (axis) {
            graph.gridLabelRenderer.horizontalAxisTitle = "- ось Ox -"
            graph.gridLabelRenderer.verticalAxisTitle = "- ось Oy -"
        } else {
            graph.gridLabelRenderer.horizontalAxisTitle = ""
            graph.gridLabelRenderer.verticalAxisTitle = ""
        }
        axisButton.text = FragmentHelper().getAxisButtonText(axis, resources)
    }
}


