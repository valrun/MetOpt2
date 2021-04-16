package com.example.metopt.nmethods;

/**
 * Абстрактный класс для методов одномерной оптимизации
 */
public abstract class AbstractOneDimMethod {
    abstract double findMin(double l, double r, double EPS);
}
