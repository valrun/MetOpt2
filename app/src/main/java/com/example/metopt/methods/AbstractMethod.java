package com.example.metopt.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractMethod {

    final double EPSILON = 0.00001;

    double l, r;

    String name;

    final Function<Double, Double> function;

    public AbstractMethod(Function<Double, Double> function, String name) {
        this.function = function;
        this.name = name;
    }

    public double computeMin(double left, double right) {
        initialize(left, right);

        while (cycleCondition()) {
            iterate();
        }
        return function.apply(getAns());
    }

    public List<List<Double>> getAllIteration(double left, double right) {
        initialize(left, right);
        List<List<Double>> res = new ArrayList<>();

        while (cycleCondition()) {
            iterate();
            res.add(List.of(l, getAns(), r));
        }

        return res;
    }

    abstract void initialize(double left, double right);

    abstract void iterate();

    abstract boolean cycleCondition();

    abstract double getAns();

}
