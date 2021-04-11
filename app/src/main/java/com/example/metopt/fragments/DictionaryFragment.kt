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
import com.example.metopt.methods.DichotomyMethod
import com.example.metopt.methods.PointsOfMethods
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.fragment_dichotomy.view.*
import kotlin.math.exp
import kotlin.math.pow

class dichotomyFragment : Fragment() {
    var pointsSeries: Array<PointsGraphSeries<DataPoint>> = arrayOf()
    lateinit var functionSeries: LineGraphSeries<DataPoint>
    lateinit var graph: GraphView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        //FUNCTION
        functionSeries = LineGraphSeries(
            PointsOfMethods().getFunction(-2.0, 3.0)
        )
        functionSeries.color = Color.GRAY

        val points = PointsOfMethods().getArray(DichotomyMethod { x: Double ->
            x.pow(2.0) + exp(-0.35 * x)
        })
        var i = 0
        var firstPart = true
        for (point in points) {
            val series = PointsGraphSeries(point)
            if (firstPart) {
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
            series.size = 12f
            series.setOnDataPointTapListener { series, dataPoint ->
                Toast.makeText(
                    activity,
                    "Left\n x: ${point[0].x} \n y: ${point[0].y} \n" +
                            "Current Answer\n x: ${point[1].x} \n y: ${point[1].y} \n" +
                            "Right\n x: ${point[2].x} \n y: ${point[2].y} \n",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //graph.addSeries(series)
            pointsSeries += series

            if (2 * i > points.size) {
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
        val view = inflater.inflate(R.layout.fragment_dichotomy, container, false)

        //BUTTON
        val prevButton: AppCompatButton = view.findViewById<View>(R.id.prev) as AppCompatButton
        prevButton.setOnClickListener {
            val last = graph.series.size - 1
            if (last > 0) {
                graph.removeSeries(graph.series[last])
            }
        }

        val nextButton: AppCompatButton = view.findViewById<View>(R.id.next) as AppCompatButton
        nextButton.setOnClickListener {
            val last = graph.series.size - 1
            if (last < pointsSeries.size) {
                graph.addSeries(pointsSeries[last])
            }
        }

        graph = view.graph as GraphView
        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.addSeries(functionSeries)

        return view
    }
}