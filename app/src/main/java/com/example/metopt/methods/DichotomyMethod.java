package com.example.metopt.methods;

import java.util.function.Function;

public class DichotomyMethod extends AbstractMethod {

    double DELTA = EPSILON / 2;
    double x1, x2;

    public DichotomyMethod(Function<Double, Double> fun) {
        super(fun, "Dichotomy");
    }

    @Override
    void initialize(double left, double right) {
        this.l = left;
        this.r = right;
    }

    @Override
    void iterate() {
        x1 = (l + r) / 2 - DELTA;
        x2 = (l + r) / 2 + DELTA;
        if (function.apply(x1) <= function.apply(x2)) {
            r = x2;
        } else {
            l = x1;
        }
    }

    @Override
    boolean cycleCondition() {
        return (r - l) / 2 > EPSILON;
    }

    @Override
    double getAns() {
        return (l + r) / 2;
    }
}
