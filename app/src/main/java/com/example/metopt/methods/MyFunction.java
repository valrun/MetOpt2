package com.example.metopt.methods;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MyFunction {
    final ArrayList<ArrayList<Double>> a;
    final ArrayList<Double> b;
    final Double c;
    private boolean isDiag;

    public MyFunction(ArrayList<ArrayList<Double>> a, ArrayList<Double> b, double c, boolean isDiag) {
        this.a = (ArrayList<ArrayList<Double>>) a.clone();
        this.b = (ArrayList<Double>) b.clone();
        this.c = c;
        this.isDiag = isDiag;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Double> gradient(ArrayList<Double> x) {
        return VplusV(MxV(a, x), b);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static ArrayList<Double> MxV(ArrayList<ArrayList<Double>> m, ArrayList<Double> v) {
        if (m.isEmpty() || m.get(0).size() != v.size()) {
            throw new IllegalArgumentException("You are invalid");
        }
        return (ArrayList<Double>) m.stream().map(i -> scalar(i, v)).collect(Collectors.toList());
    }

    private static Double scalar(ArrayList<Double> v1, ArrayList<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("You are invalid");
        }
        double sum = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            sum += (v1.get(i) * v2.get(i));
        }
        return sum;
    }

    private static ArrayList<Double> VplusV(ArrayList<Double> v1, ArrayList<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("You are invalid");
        }
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < v1.size(); i++) {
            result.add(v1.get(i) + v2.get(i));
        }
        return result;
    }

    public ArrayList<Double> VminusV(ArrayList<Double> v1, ArrayList<Double> v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("You are invalid");
        }
        ArrayList<Double> result = new ArrayList<>(v1.size());
        for (int i = 0; i < v1.size(); i++) {
            result.add(v1.get(i) - v2.get(i));
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Double> Vxa(ArrayList<Double> v, Double a) {
        return (ArrayList<Double>) v.stream().map(i -> i*a).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Double modV(ArrayList<Double> v) {
        return Math.sqrt(v.stream().mapToDouble(i -> i*i).sum());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Double getValue(ArrayList<Double> x) {
        return 0.5*scalar(MxV(a, x), x) + scalar(b, x) + c;
    }
}
