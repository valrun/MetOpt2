package com.example.metopt.methods;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Function;

public class ParabolicMethod extends AbstractMethod {

    double fl, fr, x, fx;

    public ParabolicMethod(Function<Double, Double> fun) {
        super(fun, "Parabolic");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void initialize(double left, double right) {
        l = left;
        r = right;
        fl = function.apply(l);
        fr = function.apply(r);
        x = (l + r) / 2;
        fx = function.apply(x);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void iterate() {
        double u =  x - (Math.pow(x - l, 2) *
                (fx - fr) - Math.pow(x - r, 2) * (fx - fl)) /
                (2 * ((x - l) * (fx - fr) - (x - r) * (fx - fl)));
        double fu = function.apply(u);
        if (fu > fx) {
            if (u > x) {
                r = u;
                fr = fu;
            } else {
                l = u;
                fl = fu;
            }
        } else {
            if (x > u) {
                r = x;
                fr = fx;
            } else {
                l = x;
                fl = fx;
            }
            x = u;
            fx = fu;
        }
    }

    @Override
    boolean cycleCondition() {
        return r - l > EPSILON;
    }

    @Override
    double getAns() {
        return (l + r) / 2;
    }
}
