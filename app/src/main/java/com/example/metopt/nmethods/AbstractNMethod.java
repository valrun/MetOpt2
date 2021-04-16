package com.example.metopt.nmethods;

import java.util.Collections;

/**
 * Абстрактный класс для градиентных методов многомерной оптимизации
 */
public abstract class AbstractNMethod {

    /**
     * Заданная точность
     */
    final double EPS = 1e-3;

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

    abstract Value<Vector, Double> iterate(Value<Vector, Double> x);

    abstract boolean cycleCondition();

    Vector getAns() {
        return x.getVal();
    }
}
