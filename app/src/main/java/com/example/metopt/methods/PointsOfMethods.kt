package com.example.metopt.methods

import com.jjoe64.graphview.series.DataPoint
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class PointsOfMethods {
    fun getArray(met: AbstractMethod): Array<Array<DataPoint>> {
        /*val met = DichotomyMethod { x: Double ->
            x.pow(2.0) + exp(-0.35 * x)
        } */
        val points = met.getAllIteration(-2.0, 3.0)
        var dataPoints = emptyArray<Array<DataPoint>>()

        for (arr in points) {
            var tmp = emptyArray<DataPoint>()
            for (x in arr) {
                val y = x.pow(2.0) + exp(-0.35 * x)
                tmp += DataPoint(x, y)
            }
            dataPoints += tmp
        }

        return dataPoints
    }

    fun getFunction(a: Double, b: Double): Array<DataPoint> {
        val len = b - a
        val del = 1000.0
        val points = Array(floor(len * del).toInt()) { i -> i / del + a }
        points.sort()
        var dataPoints = emptyArray<DataPoint>()
        for (x in points) {
            val y = x.pow(2.0) + exp(-0.35 * x)
            dataPoints += DataPoint(x, y)
        }

        return dataPoints
    }

    fun getParabole(points: Array<DataPoint>): Array<DataPoint> {
        if (points.size != 3) {
            return emptyArray()
        }
        val x1 = points[0].x;
        val x2 = points[2].x;
        val x3 = points[1].x
        val fx1 = points[0].y;
        val fx2 = points[2].y;
        val fx3 = points[1].y

//        val a0 = fx1;
//        val a1 = (fx2 - fx1) / (x2 - x1);
//        val a2 = 1 / (x3 - x2) * ((fx3 - fx1) / (x3 - x1) - (fx2 - fx1) / (x2 - x1));
        val a2 =
            (((fx3 - fx2) * (x1 - x3) / (x3 - x2)) + fx3 - fx1) / (-x1 * x1 + (x2 + x3) * (x1 - x3) + x3 * x3)
        val a1 = (fx3 - fx2 + a2 * (x2 * x2 - x3 * x3)) / (x3 - x2)
        val a0 = fx3 - a2 * x3 * x3 - a1 * x3


        val a = -2.0;
        val b = 3.0
        //val a = x1; val b = x2

        val len = b - a
        val del = 200.0

        val newPoints = Array(floor(len * del).toInt()) { i -> i / del + a }
        newPoints.sort()
        var dataPoints = emptyArray<DataPoint>()
        for (x in newPoints) {
            val y = a2 * x.pow(2.0) + a1 * x + a0
            dataPoints += DataPoint(x, y)
        }

        return dataPoints
    }

    fun getLineOfLevel(f: MyFunction, ans: Int, l: Int, r: Int): Array<DataPoint> {
        if (f.a.size != 2) {
            return emptyArray<DataPoint>()
        }

        val len = r - l
        val del = 100.0
        val points = Array(floor(len * del).toInt()) { i -> i / del + l }
        var dataPoints = emptyArray<DataPoint>()
        for (x in points) {
            //a[1][1]x^2 + a1x + a2 = 0
            val a1 = (f.a[0][1] + f.a[1][0]) * x + f.b[1]
            val a2 = f.a[0][0] * x * x + f.b[0] * x + f.c - ans;
            if (f.a[1][1] == 0.0) {
                val y = a2 / a1
                dataPoints += DataPoint(x, y)
            } else {
                val diskr = a1 * a1 - 4 * f.a[1][1] * a2
                if (diskr > 0) {
                    val sqrtD = sqrt(diskr)
                    println("x: $x\tsqrt: $sqrtD")
                    val y1 = (-a1 + sqrtD) / (2 * f.a[1][1])
                    dataPoints += DataPoint(x, y1)
                    val y2 = (-a1 - sqrtD) / (2 * f.a[1][1])
                    dataPoints += DataPoint(x, y2)
                }
            }
        }
        return dataPoints

    }

}