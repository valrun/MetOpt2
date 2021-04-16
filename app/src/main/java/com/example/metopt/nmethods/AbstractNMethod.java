package com.example.metopt.nmethods;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Абстрактный класс для градиентных методов многомерной оптимизации
 */
public abstract class AbstractNMethod {

    /**
     * Заданная точность
     */
    final double EPS;

    private final QuadraticFunction function;

    /**
     * Градиент данной квадратичной функции
     */
    Vector grad;

    String name;

    Value<Vector, Double> x;

    protected AbstractNMethod(QuadraticFunction function, String name) {
        this.function = function;
        this.name = name;
        this.EPS = 1e-3;
        x = new Value<>(
                new Vector(Collections.nCopies(function.getN(), 0.)),
                function);
    }

    protected AbstractNMethod(QuadraticFunction function, String name, double eps) {
        this.function = function;
        this.name = name;
        this.EPS = eps;
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
        System.err.println(getAns());
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
