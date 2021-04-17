package com.example.metopt.nmethods;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.function.Function;

/**
 * Квадратичная функция, заданная в матричной форме
 */

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

    /**
     * Применение квадратичной функции к какому-то аргументу
     */
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

    /**
     * Вычисление градиента
     */
    public Vector getGrad(Vector point) {
        return a.multiply(point).add(b);
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int n = a.size();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res.append(a.get(i, j) / 2).append("*x").append(i + 1).append("x").append(j + 1).append(" + ");
            }
        }
        n = b.size();
        for (int i = 0; i < n; i++) {
            res.append(b.get(i)).append("*x").append(i + 1).append(" + ");
        }
        res.append(c);
        return res.toString();
    }
}
