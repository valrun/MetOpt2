package com.example.metopt.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.metopt.R
import com.example.metopt.SaveFunction
import com.example.metopt.nmethods.ConjugateGradientMethod
import com.example.metopt.nmethods.QuadraticFunction
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_gradient.view.*

class ConjugateGradientFragment : Fragment() {
    private lateinit var levelSeries: Array<LineGraphSeries<DataPoint>>
    private lateinit var functionSeries: Array<LineGraphSeries<DataPoint>>
    private lateinit var graph: GraphView

    private var level = true
    private var coordinateLine = true
    private var axis = true

    private var information = ""
    private lateinit var f: QuadraticFunction

    private lateinit var levelButton: AppCompatButton
    private lateinit var coordinateButton: AppCompatButton
    private lateinit var axisButton: AppCompatButton
    private lateinit var info: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        f = SaveFunction.function

        val series =
            FragmentHelper().getFunAndLvlSeries(ConjugateGradientMethod(f), f, -25.0, 25.0, this.activity)

        functionSeries = series.first
        levelSeries = series.second

        functionSeries.forEach {
            println(it.highestValueX - it.lowestValueX)
        }

        information += "Function: " + f.toString() + System.lineSeparator()
        information += "Answer: " + ConjugateGradientMethod(f).computeMin()
    }


    @SuppressLint("SetTextI18n")
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
            val last = if (level) {
                graph.series.size - levelSeries.size
            } else {
                graph.series.size
            }
            if (last > 1) {
                graph.removeSeries(graph.series[graph.series.size - 1])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = if (level) {
                graph.series.size - levelSeries.size
            } else {
                graph.series.size
            }
            if (last < functionSeries.size) {
                graph.addSeries(functionSeries[last])
            }
        }

        levelButton = view.findViewById<View>(R.id.level) as AppCompatButton
        levelButton.setOnClickListener {
            clickLevelButton()
        }

        coordinateButton =
            view.findViewById<View>(R.id.coordinateLine) as AppCompatButton
        coordinateButton.setOnClickListener {
            clickCoordinateButton()
        }

        axisButton = view.findViewById<View>(R.id.axis) as AppCompatButton
        axisButton.setOnClickListener {
            clickAxisButton()
        }

        val setEpsButton: AppCompatButton = view.findViewById<View>(R.id.setEps) as AppCompatButton
        val epsText: EditText = view.findViewById<EditText>(R.id.epsText) as EditText
        setEpsButton.setOnClickListener {
            println(epsText.text)
            val eps = epsText.text.toString().toDoubleOrNull()
            if (eps == null) {
                Toast.makeText(activity, "Incorrect eps value", Toast.LENGTH_SHORT).show()
            } else {
                setEps(eps)
            }
        }

        val nameMethod : TextView = view.findViewById(R.id.nameText)
        nameMethod.text = "Conjugate Gradient Method"

        info = view.findViewById(R.id.information)

        //GRAPH
        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level = GradientFragmentArgs.fromBundle(requireArguments()).level
        coordinateLine = GradientFragmentArgs.fromBundle(requireArguments()).coordinateLine
        axis = GradientFragmentArgs.fromBundle(requireArguments()).axis
    }

    private fun init() {
        if (level) {
            levelSeries.forEach { graph.addSeries(it) }
        }
        functionSeries.forEach { graph.addSeries(it) }

        levelButton.text = FragmentHelper().getLevelButtonText(level, resources)
        coordinateLine = !coordinateLine
        clickCoordinateButton()
        axis = !axis
        clickAxisButton()

        info.text = information
    }

    private fun clickLevelButton() {
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

    private fun clickCoordinateButton() {
        coordinateLine = !coordinateLine
        graph.gridLabelRenderer.isHorizontalLabelsVisible = coordinateLine
        graph.gridLabelRenderer.isVerticalLabelsVisible = coordinateLine
        coordinateButton.text =
            FragmentHelper().getCoordinateLineButtonText(coordinateLine, resources)
    }

    private fun clickAxisButton() {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEps(eps: Double) {
        graph.removeAllSeries()

        f = SaveFunction.function
        val series =
            FragmentHelper().getFunAndLvlSeries(
                ConjugateGradientMethod(f, eps),
                f,
                -25.0,
                25.0,
                this.activity
            )

        functionSeries = series.first
        levelSeries = series.second

        information = "Function: " + f.toString() + System.lineSeparator()
        information += "Answer: " + ConjugateGradientMethod(f, eps).computeMin()

        init()
        println("Done")
    }

}


