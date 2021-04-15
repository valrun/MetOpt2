package com.example.metopt.nmethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractNMethod {

    final double EPS = 1e-3;

    private final QuadraticFunction function;

    Vector grad;

    String name;

    Value<Vector, Double> x;

    protected AbstractNMethod(QuadraticFunction function, String name) {
        this.function = function;
        this.name = name;
        x = new Value<>(
                new Vector(Collections.nCopies(function.getN(), 0.)),
                function);
    }

    public QuadraticFunction getFunction() {
        return function;
    }

    public Double computeMin() {
        while (cycleCondition()) {
            x = iterate(x);
        }

        return function.apply(getAns());
    }

    public List<Value<Vector, Double>> getAllIteration() {
        List<Value<Vector, Double>> res = new ArrayList<>();

        while (cycleCondition()) {
            x = iterate(x);
            res.add(x);
        }

        return res;
    }

    abstract Value<Vector, Double> iterate(Value<Vector, Double> x);

    abstract boolean cycleCondition();

    Vector getAns() {
        return x.getVal();
    }
}
