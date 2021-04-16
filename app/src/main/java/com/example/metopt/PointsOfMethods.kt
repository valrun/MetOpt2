package com.example.metopt

import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.example.metopt.nmethods.QuadraticFunction
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.floor
import kotlin.math.sqrt

class PointsOfMethods {
    private fun getLineOfLevel(
        f: QuadraticFunction,
        ans: Double,
        l: Double,
        r: Double,
        delta: Double = 1000.0
    ): Array<Array<DataPoint>> {
        val len = r - l
        if (f.a.size != 2 || len == 0.0) {
            return emptyArray()
        }
//        println(len)
        val del = if (len < 0.2) {
            delta / len
        } else {
            delta
        }
        val points = Array(floor(len * del).toInt()) { i -> i / del + l }
        var dataPoints1 = emptyArray<DataPoint>()
        var dataPoints2 = emptyArray<DataPoint>()
        for (x in points) {
            //a[1][1]y^2 + a1y + a2 = 0

            val a1 = (f.a[0][1] + f.a[1][0]) * x + f.b[1] * 2
            val a2 = f.a[0][0] * x * x + (f.b[0] * x + f.c - ans) * 2
            if (f.a[1][1] == 0.0) {
                val y = a2 / a1
                dataPoints1 += DataPoint(x, y)
            } else {
                val diskr = a1 * a1 - 4 * f.a[1][1] * a2
//                println("a2: $a2\td: $diskr")
                if (diskr > 0) {
                    val sqrtD = sqrt(diskr)
//                    println("x: $x\tsqrt: $sqrtD")
                    val y1 = (-a1 + sqrtD) / (2 * f.a[1][1])
                    dataPoints1 += DataPoint(x, y1)
                    val y2 = (-a1 - sqrtD) / (2 * f.a[1][1])
                    dataPoints2 += DataPoint(x, y2)
                }
            }
        }

        return if (dataPoints2.isEmpty()) {
            arrayOf(dataPoints1)
        } else {
            arrayOf(
                dataPoints1,
                dataPoints2,
                arrayOf(dataPoints1.first(), dataPoints2.first()),
                arrayOf(dataPoints1.last(), dataPoints2.last()),
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLevels(
        n: Int,
        f: QuadraticFunction,
        activity: FragmentActivity?,
        l: Double,
        r: Double
    ): Array<LineGraphSeries<DataPoint>> {
        var levelSeries: Array<LineGraphSeries<DataPoint>> = arrayOf()
        for (ans in 1..n) {
            levelSeries += getLevel(ans * 4.0, f, activity, l, r)
        }
        return levelSeries
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLevel(
        level: Double,
        f: QuadraticFunction,
        activity: FragmentActivity?,
        l: Double,
        r: Double,
        del: Double = 1000.0
    ): Array<LineGraphSeries<DataPoint>> {
        var levelSeries: Array<LineGraphSeries<DataPoint>> = arrayOf()
        val point = getLineOfLevel(f, level, l, r, del)

        point.forEach {
//            println("Size levels: " + it.size)
            val series = LineGraphSeries(it)
            series.color = Color.LTGRAY
            series.thickness = 2
            series.setOnDataPointTapListener { _, _ ->
                Toast.makeText(
                    activity,
                    "Level: $level",
                    Toast.LENGTH_SHORT
                ).show()
            }
            levelSeries += series
        }
        return levelSeries
    }
}