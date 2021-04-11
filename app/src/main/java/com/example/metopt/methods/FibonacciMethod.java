package com.example.metopt.methods;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.function.Function;

public class FibonacciMethod extends AbstractMethod {

    ArrayList<Long> fib = new ArrayList<>();

    double x1, x2, f1, f2;

    int iteration;

    int s = -1;

    public FibonacciMethod(Function<Double, Double> fun) {
        super(fun, "Fibonacci");
    }

    void computeFib(long n) {
        fib.add(0L);
        fib.add(1L);
        while (n > fib.get(fib.size() - 1)) {
            fib.add(fib.get(fib.size() - 1) + fib.get(fib.size() - 2));
            s++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void initialize(double left, double right) {
        l = left;
        r = right;

        long n = (long) ((r - l) / EPSILON);

        computeFib(n);

        double cur = (double) fib.get(s - 1) / fib.get(s) * (r - l);
        x2 = l + cur;
        x1 = r - cur;
        f1 = function.apply(x1);
        f2 = function.apply(x2);

        iteration = s - 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void iterate() {
        if (f1 < f2) {
            r = x2;
            x2 = x1;
            f2 = f1;
            x1 = l + (double) fib.get(iteration - 2) / fib.get(iteration) * (r - l);
            f1 = function.apply(x1);
        } else {
            l = x1;
            x1 = x2;
            f1 = f2;
            x2 = l + (double) fib.get(iteration - 1) / fib.get(iteration) * (r - l);
            f2 = function.apply(x2);
        }
        iteration--;
    }

    @Override
    boolean cycleCondition() {
        return iteration > 2;
    }

    @Override
    double getAns() {
        return (x1 + x2) / 2;
    }
}
