package com.example.metopt.nmethods;

import java.util.function.Function;

/**
 * Пара из вектора и результата применения к нему отображения f: R^n -> R
 */

public class Value<T, R> {
    private final T val;
    private final R fVal;

    public Value(T val, Function<T, R> fun) {
        this.val = val;
        this.fVal = fun.apply(val);
    }

    public T getVal() {
        return this.val;
    }

    public R getFVal() {
        return fVal;
    }
}

