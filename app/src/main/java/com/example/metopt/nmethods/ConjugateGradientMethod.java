package com.example.metopt.nmethods;

public class ConjugateGradientMethod extends AbstractNMethod {

    private final int REBOOT;
    private int cnt = 0;
    private double gradientNorm = Double.POSITIVE_INFINITY;
    private Vector p;

    public ConjugateGradientMethod(QuadraticFunction fun, int reboot, String name) {
        super(fun, name);
        this.REBOOT = reboot;
    }

    public ConjugateGradientMethod(QuadraticFunction fun, String name) {
        this(fun, fun.getN(), name);
    }

    private void checkCnt(Value<Vector, Double> x) {
        if (cnt == 0) {
            grad = getFunction().getGrad(x.getVal());
            gradientNorm = grad.norm();
            p = grad.multiply(-1);
        }
        cnt = (cnt + 1) % REBOOT;
    }

    @Override
    Value<Vector,Double> iterate(Value<Vector,Double> x) {
        checkCnt(x);
        Vector mulRes = getFunction().a.multiply(p);
        double ALPHA = gradientNorm * gradientNorm / mulRes.scalarProduct(p);
        Value<Vector, Double> y = new Value<>(x.getVal().add(p.multiply(ALPHA)), getFunction());
        double newGDist = grad.norm();
        double BETA = newGDist * newGDist / (gradientNorm * gradientNorm);
        p = grad.multiply(-1).add(p.multiply(BETA));
        gradientNorm = newGDist;
        return y;
    }

    @Override
    boolean cycleCondition() {
        if (gradientNorm < EPS) {
            gradientNorm = Double.POSITIVE_INFINITY;
            cnt = 0;
            return false;
        }
        return true;
    }
}
