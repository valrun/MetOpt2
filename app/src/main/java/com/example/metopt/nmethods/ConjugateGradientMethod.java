package com.example.metopt.nmethods;

/**
 * Метод сопряжённых градиентов — итерационный метод для безусловной оптимизации
 * в многомерном пространстве. Основным достоинством метода является то, что он
 * решает квадратичную задачу оптимизации за конечное число шагов.
 * Метод сопряженных градиентов является дальнейшим развитием метода наискорейшего
 * спуска, который сочетает в себе два понятия: градиент целевой функции и
 * сопряженное направление векторов.
 * <p>
 * В соответствии с представленными  выражениями новое сопряженное направление
 * получается сложением градиента (антиградиента) в точке поворота и предыдущего
 * направления движения, умноженного на коэффициент. Таким образом, метод сопряженных
 * градиентов формирует направление поиска к оптимальному значению используя
 * информацию о поиске полученную на предыдущих этапах спуска.
 */
public class ConjugateGradientMethod extends AbstractNMethod {

    private final int REBOOT;

    /**
     * Текущее количество итераций
     */
    private int cnt = 0;

    private double gradientNorm = Double.POSITIVE_INFINITY;
    /**
     * Следующее направление
     */
    private Vector p;

    public ConjugateGradientMethod(QuadraticFunction fun, int reboot, String name) {
        super(fun, name);
        this.REBOOT = reboot;
    }

    public ConjugateGradientMethod(QuadraticFunction fun, int reboot, String name, double eps) {
        super(fun, name, eps);
        this.REBOOT = reboot;
    }

    public ConjugateGradientMethod(QuadraticFunction fun, String name) {
        this(fun, fun.getN(), name);
    }

    public ConjugateGradientMethod(QuadraticFunction fun) {
        this(fun, fun.getN(), "Conjugate gradient descent");
    }

    public ConjugateGradientMethod(QuadraticFunction fun, double eps) {
        this(fun, fun.getN(), "Conjugate gradient descent", eps);
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
    Value<Vector, Double> iterate(Value<Vector, Double> x) {
        checkCnt(x);
        Vector mulRes = getFunction().a.multiply(p);
        double ALPHA = gradientNorm * gradientNorm / mulRes.scalarProduct(p);
        Value<Vector, Double> y = new Value<>(x.getVal().add(p.multiply(ALPHA)), getFunction());
        double newGDist = grad.norm();
        // параметр сопряжённости
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
