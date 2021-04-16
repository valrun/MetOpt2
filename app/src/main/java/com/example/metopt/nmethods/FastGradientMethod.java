package com.example.metopt.nmethods;

import java.util.function.Function;

/**
 * Метод наискорейшего спуска основывается на выборе шага из следующего
 * соображения. Из точки  x^{[k]} будем двигаться в направлении антиградиента
 * до тех пор пока не достигнем минимума функции f на этом направлении,
 * т. е. на луче
 *
 * Другими словами, alpha^{[k]} выбирается так, чтобы следующая итерация
 * была точкой минимума функции f на луче L. Заметим, кстати, что в этом
 * методе направления соседних шагов ортогональны.
 *
 * Метод наискорейшего спуска требует решения на каждом шаге задачи одномерной
 * оптимизации. Практика показывает, что этот метод часто требует меньшего
 * числа операций, чем градиентный метод с постоянным шагом.
 */

public class FastGradientMethod extends AbstractNMethod {

    Function<Double, Vector> fun;

    public FastGradientMethod(QuadraticFunction fun, String name) {
        super(fun, name);
    }

    public FastGradientMethod(QuadraticFunction func) {
        super(func, "Fast gradient descent");
    }

    public FastGradientMethod(QuadraticFunction func, String name, double eps) {
        super(func, name, eps);
    }

    public FastGradientMethod(QuadraticFunction func, double eps) {
        super(func, "Gradient descent", eps);
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
