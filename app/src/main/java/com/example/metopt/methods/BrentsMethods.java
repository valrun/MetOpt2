package com.example.metopt.methods;

import java.util.function.Function;

public class BrentsMethods extends AbstractMethod {

    double x, w, v;

    double fx, fw, fv;

    double d, e;

    double K = (3 - Math.sqrt(5)) / 2;

    public BrentsMethods(Function<Double, Double> fun) {
        super(fun, "Brent's");
    }


    @Override
    void initialize(double left, double right) {
        this.l = left;
        this.r = right;

        x = w = v = (l + r) / 2;

        fx = fw = fv = function.apply(x);

        d = e = r - l;
    }

    @Override
    void iterate() {
        double g;
        g = e;
        e = d;
        double u;
        if (!(fx == fw || fx == fv || fv == fw)) {
            u = x - (Math.pow((x - w), 2) * (fx - fv) - Math.pow((x - v), 2) * (fx - fw)) / (2 * ((x - w) * (fx - fv) - (x - v) * (fx - fw)));
            if (u >= l + EPSILON && u <= r - EPSILON && Math.abs(u - x) < g / 2) {
                d = Math.abs(u - x);
            } else {
                if (x < (r - l) / 2) {
                    u = l + K * (r - x);
                    d = r - x;
                } else {
                    u = r - K * (x - l);
                    d = x - l;
                }
            }
        } else {
            if (x < (r - l) / 2) {
                u = x + K * (r - x);
                d = r - x;

            } else {
                u = x - K * (x - l);
                d = x - l;
            }
            if (Math.abs(u - x) < EPSILON) {
                u = x + Math.signum(u - x) * EPSILON;
            }
        }
        double fu = function.apply(u);
        if (fu <= fx) {
            if (u >= x) {
                l = x;
            } else {
                r = x;
            }
            v = w;
            w = x;
            x = u;
            fv = fw;
            fw = fx;
            fx = fu;
        } else {
            if (u >= x) {
                r = u;
            } else {
                l = u;
            }
            if (fu <= fw || w == x) {
                v = w;
                w = u;
                fv = fw;
                fw = fu;
            } else {
                if (fu <= fv || v == x || v == w) {
                    v = u;
                    fv = fu;
                }
            }
        }
    }

    @Override
    boolean cycleCondition() {
        return d > EPSILON;
    }

    @Override
    double getAns() {
        return x;
    }
}
