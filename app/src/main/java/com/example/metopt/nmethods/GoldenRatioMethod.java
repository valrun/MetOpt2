package com.example.metopt.nmethods;

import java.util.function.Function;

public class GoldenRatioMethod extends AbstractOneDimMethod {

    protected final Function<Double, Double> func;

    private final double TAU = (Math.sqrt(5) - 1) / 2;
    double x1, x2, fx1, fx2, curEps;


    public GoldenRatioMethod(Function<Double, Double> func) {
        this.func = func;
    }

    public double findMin(double l, double r, double EPS) {
        x1 = l + ((3 - Math.sqrt(5)) / 2 * (r - l));
        x2 = l + ((Math.sqrt(5) - 1) / 2 * (r - l));
        fx1 = func.apply(x1);
        fx2 = func.apply(x2);
        curEps = (r - l) / 2;
        while (curEps > EPS) {
            if (fx1 < fx2) {
                r = x2;
                x2 = x1;
                fx2 = fx1;
                x1 = r - TAU * (r - l);
                fx1 = func.apply(x1);
            } else {
                l = x1;
                x1 = x2;
                fx1 = fx2;
                x2 = l + TAU * (r - l);
                fx2 = func.apply(x2);
            }
            curEps = TAU * curEps;
        }
        return (l + r) / 2;
    }
}
