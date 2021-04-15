package com.example.metopt.nmethods;

public class GradientMethod extends AbstractNMethod {
    double ALPHA = 1.0;
    double gradNorm;

    public GradientMethod(QuadraticFunction func) {
        super(func, "Gradient descent");
    }

    public GradientMethod(QuadraticFunction func, String name) {
        super(func, name);
    }

    @Override
    Value<Vector, Double> iterate(Value<Vector, Double> x) {
        while (true) {
            Value<Vector, Double> y = new Value<>(
                    x.getVal().add(grad.multiply(-ALPHA / gradNorm)),
                    getFunction());
            if (y.getFVal() < x.getFVal()) {
                return y;
            } else {
                ALPHA /= 2;
            }
        }
    }

    @Override
    boolean cycleCondition() {
        grad = getFunction().getGrad(x.getVal());
        gradNorm = grad.norm();
        return !(gradNorm < EPS);
    }
}
