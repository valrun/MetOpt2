package com.example.metopt.nmethods;

import java.util.List;
import java.util.function.Function;

public class QuadraticFunction implements Function<Vector, Double> {

    private final int n;
    public final Matrix a;
    public final Vector b;
    public final double c;

    public QuadraticFunction(List<List<Double>> a, List<Double> b, double c) {
        this.n = b.size();
        if (a.stream().anyMatch(list -> list.size() != n) || a.size() != n) {
            throw new IllegalArgumentException("Invalid lists' sizes " + n);
        }
        this.a = new Matrix(a);
        this.b = new Vector(b);
        this.c = c;
    }

    public Double apply(Vector arg) {
        double res = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                res += a.get(i, j) * arg.get(i) * arg.get(j);
            }
        }
        res /= 2;
        for (int i = 0; i < n; ++i) {
            res += b.get(i) * arg.get(i);
        }
        res += c;
        return res;
    }

    public Vector getGrad(Vector point) {
        return a.multiply(point).add(b);
    }

    public int getN() {
        return n;
    }

}
