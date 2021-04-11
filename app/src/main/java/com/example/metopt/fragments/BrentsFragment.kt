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
import com.example.metopt.R
import com.example.metopt.methods.MyFunction
import com.example.metopt.methods.PointsOfMethods
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.fragment_brents.view.*

class BrentsFragment : Fragment() {
    var pointsSeries: Array<PointsGraphSeries<DataPoint>> = arrayOf()
    //    lateinit var functionSeries: LineGraphSeries<DataPoint>
    lateinit var graph: GraphView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        //FUNCTION
//        functionSeries = LineGraphSeries(
//            PointsOfMethods().getFunction(-2.0, 3.0)
//        )
//        functionSeries.color = Color.GRAY

        var i = 0
        val n = 5
        var firstPart = true

        //POINTS
        for (ans in 1..n) {
            val level = ans * 4
            val point = PointsOfMethods().getLineOfLevel(
                MyFunction(
                    arrayListOf(arrayListOf(1.0, 0.0), arrayListOf(0.0, 1.0)),
                    arrayListOf(0.0, 0.0), 0.0, false
                ), level, -10, 10
            )

            val series = PointsGraphSeries(point)
            if (firstPart) {
                series.color = Color.rgb(
                    255f * i / n,
                    255f * i / n,
                    128f + 127f * i / n
                )
                i += 2
            } else {
                series.color = Color.rgb(
                    165f + 60f * i / n,
                    42f + 183f * i / n,
                    42f + 183f * i / n
                )
                i -= 2
            }
            series.size = 4f
            series.setOnDataPointTapListener { series, dataPoint ->
                Toast.makeText(
                    activity,
                    "Level: $level",
                    Toast.LENGTH_SHORT
                ).show()
            }
            pointsSeries += series

            if (2 * i > n) {
                firstPart = false
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_brents, container, false)

        //BUTTON
        val prevButton: AppCompatButton = view.findViewById<View>(R.id.prev) as AppCompatButton
        prevButton.setOnClickListener {
            val last = graph.series.size - 1
            if (last >= 0) {
                graph.removeSeries(graph.series[last])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = graph.series.size
            if (last < pointsSeries.size) {
                graph.addSeries(pointsSeries[last])
            }
        }

        //GRAPH
        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
//        graph.addSeries(functionSeries)

        return view
    }
}


