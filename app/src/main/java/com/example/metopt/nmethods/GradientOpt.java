package com.example.metopt.nmethods;

import java.util.function.Function;

public class GradientOpt extends AbstractNMethod {

    Function<Double, Vector> fun;

    public GradientOpt(QuadraticFunction fun) {
        super(fun, "Fast gradient descent");
    }

    public GradientOpt(QuadraticFunction fun, String name) {
        super(fun, name);
    }

    @Override
    Value<Vector, Double> iterate(Value<Vector,Double> x) {
        fun = t -> x.getVal().add(grad.multiply(-t));
        AbstractOneDimMethod oneDimMethod = new GoldenRatioMethod(fun.andThen(getFunction()));
        double alpha = oneDimMethod.findMin(0.0, 1.0, EPS / 100);
        return new Value<>(fun.apply(alpha), getFunction());
    }

    @Override
    boolean cycleCondition() {
        grad = getFunction().getGrad(x.getVal());
        return !(grad.norm() < EPS);
    }
}
