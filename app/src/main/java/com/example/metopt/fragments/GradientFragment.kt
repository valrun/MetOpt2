package com.example.metopt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.example.metopt.R
import com.example.metopt.nmethods.QuadraticFunction
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.fragment_gradient.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradientFragment : Fragment() {
    private var nMethod = 0

    private lateinit var levelSeries: Array<LineGraphSeries<DataPoint>>
    private lateinit var functionSeries: Array<LineGraphSeries<DataPoint>>
    private lateinit var answerSeries: PointsGraphSeries<DataPoint>
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

    private val scope = lifecycle.coroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        level = GradientFragmentArgs.fromBundle(requireArguments()).level
        coordinateLine = GradientFragmentArgs.fromBundle(requireArguments()).coordinateLine
        axis = GradientFragmentArgs.fromBundle(requireArguments()).axis
        nMethod = GradientFragmentArgs.fromBundle(requireArguments()).nMethod

        f = QuadraticFunction(
            listOf(listOf(40.0, 0.0), listOf(0.0, 2.0)),
            listOf(-7.0, 3.0),
            2.0
        )

        setSeries(false)
    }

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
            if (last > 2) {
                graph.removeSeries(graph.series[graph.series.size - 1])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = if (level) {
                graph.series.size - levelSeries.size
            } else {
                graph.series.size
            } - 1
            if (last < functionSeries.size) {
                graph.addSeries(functionSeries[last])
            }
        }

        levelButton = view.findViewById<View>(R.id.level) as AppCompatButton
        levelButton.setOnClickListener {
            init(true)
        }

        coordinateButton =
            view.findViewById<View>(R.id.coordinateLine) as AppCompatButton
        coordinateButton.setOnClickListener {
            coordinateLine = !coordinateLine
            FragmentHelper().clickCoordinateButton(
                coordinateLine,
                graph.gridLabelRenderer,
                coordinateButton,
                resources
            )

        }

        axisButton = view.findViewById<View>(R.id.axis) as AppCompatButton
        axisButton.setOnClickListener {
            axis = !axis
            FragmentHelper().clickAxisButton(axis, graph.gridLabelRenderer, axisButton, resources)
        }

        val setEpsButton: AppCompatButton = view.findViewById<View>(R.id.setEps) as AppCompatButton
        val epsText: EditText = view.findViewById(R.id.epsText) as EditText
        setEpsButton.setOnClickListener {
            val eps = epsText.text.toString().toDoubleOrNull()
            if (eps == null) {
                Toast.makeText(activity, "Incorrect eps value", Toast.LENGTH_SHORT).show()
            } else {
                setSeries(true, eps)
            }
        }

        val a11 = view.findViewById<EditText>(R.id.a11)
        val a12 = view.findViewById<EditText>(R.id.a12)
        val a21 = view.findViewById<EditText>(R.id.a21)
        val a22 = view.findViewById<EditText>(R.id.a22)
        val b1 = view.findViewById<EditText>(R.id.b1)
        val b2 = view.findViewById<EditText>(R.id.b2)
        val c = view.findViewById<EditText>(R.id.c)

        val set = view.findViewById<AppCompatButton>(R.id.setFun)
        set.setOnClickListener {
            f = try {
                QuadraticFunction(
                    listOf(
                        listOf(a11.text.toString().toDouble(), a12.text.toString().toDouble()),
                        listOf(a21.text.toString().toDouble(), a22.text.toString().toDouble())
                    ),
                    listOf(b1.text.toString().toDouble(), b2.text.toString().toDouble()),
                    c.text.toString().toDouble()
                )
            } catch (e: Exception) {
                f
            }
            setSeries(true)
        }

        info = view.findViewById(R.id.information)

        val nameMethod: TextView = view.findViewById(R.id.nameText)
        nameMethod.text = FragmentHelper().getNameMethod(nMethod)

        //GRAPH
        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        init(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level = GradientFragmentArgs.fromBundle(requireArguments()).level
        coordinateLine = GradientFragmentArgs.fromBundle(requireArguments()).coordinateLine
        axis = GradientFragmentArgs.fromBundle(requireArguments()).axis
        nMethod = GradientFragmentArgs.fromBundle(requireArguments()).nMethod
    }

    private fun init(changeLvl: Boolean = false) {
        val lastFun =
            if (changeLvl)
                (if (level) {
                    graph.series.size - levelSeries.size
                } else {
                    graph.series.size
                } - 2)
            else (functionSeries.size - 1)

        graph.removeAllSeries()
        if (changeLvl) level = !level

        if (level) {
            levelSeries.forEach { graph.addSeries(it) }
        }
        graph.addSeries(answerSeries)
        for (i in 0..lastFun) {
            graph.addSeries(functionSeries[i])
        }

        levelButton.text = FragmentHelper().getLevelButtonText(level, resources)
        FragmentHelper().clickCoordinateButton(
            coordinateLine,
            graph.gridLabelRenderer,
            coordinateButton,
            resources
        )
        FragmentHelper().clickAxisButton(axis, graph.gridLabelRenderer, axisButton, resources)

        info.text = information
    }

    private fun setSeries(isInit: Boolean, eps: Double? = null) {
        val activity = this.activity
        if (isInit) Toast.makeText(activity, "Start counting", Toast.LENGTH_SHORT).show()

        val dispatcher = if (isInit) Dispatchers.IO else Dispatchers.Main
        scope.launch(dispatcher) {
            val method = FragmentHelper().getMethod(nMethod, f, eps)

            val series =
                FragmentHelper().getFunAndLvlSeries(
                    method,
                    f,
                    -25.0,
                    25.0,
                    activity
                )

            withContext(Dispatchers.Main) {
                functionSeries = series.first
                levelSeries = series.second
                answerSeries = series.third.first

                information = "Function: $f"
                information += if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    System.lineSeparator()
                } else {
                    ". "
                }
                information += "Answer: ${series.third.second}"
                information += if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    System.lineSeparator()
                } else {
                    ". "
                }
                information += "Iterations: " + functionSeries.size

                if (isInit) {
                    init(false)
                    Toast.makeText(activity, "Done!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


