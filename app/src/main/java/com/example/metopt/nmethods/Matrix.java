package com.example.metopt.nmethods;

import java.util.AbstractList;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix extends AbstractList<List<Double>> {
    private final List<Vector> a;

    public Matrix(List<List<Double>> a) {
        this.a = a.stream().map(Vector::new).collect(Collectors.toList());
        if (a.stream().anyMatch(list -> list.size() != a.size())) {
            throw new IllegalArgumentException("Non-quadratic matrix");
        }
    }

    public Vector multiply(Vector vector) {
        Vector res = new Vector();
        for (Vector v : a) {
            res.add(v.scalarProduct(vector));
        }
        return res;
    }

    public double get(int r, int c) {
        return a.get(r).get(c);
    }

    @Override
    public Vector get(int index) {
        return new Vector(a.get(index));
    }

    @Override
    public int size() {
        return a.size();
    }
}
