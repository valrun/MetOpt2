package com.example.metopt.nmethods;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Вектор на основе {@link List}
 */
public class Vector extends AbstractList<Double> {
    private final List<Double> coordinates;

    public Vector(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Vector() {
        this(new ArrayList<>());
    }

    /**
     * Скалярное произведение
     */
    public double scalarProduct(List<Double> vector) {
        double res = 0;
        for (int i = 0; i < vector.size(); i++) {
            res += this.coordinates.get(i) * vector.get(i);
        }
        return res;
    }

    /**
     * Умножение на скаляр
     */
    public Vector multiply(double scalar) {
        return this.stream()
                .map(x -> x * scalar)
                .collect(Collectors.toCollection(Vector::new));
    }

    /**
     * Сложение векторов
     */
    public Vector add(List<Double> vector) {
        Vector res = new Vector();
        for (int i = 0; i < vector.size(); i++) {
            res.add(this.coordinates.get(i) + vector.get(i));
        }
        return res;
    }

    /**
     * Получение одной координаты вектора
     */
    @Override
    public Double get(int index) {
        return coordinates.get(index);
    }

    /**
     * Норма вектора
     */
    public double norm() {
        return Math.sqrt(coordinates.stream().reduce(0.0, (x, y) -> x + y * y));
    }

    @Override
    public int size() {
        return coordinates.size();
    }

    @Override
    public boolean add(Double elem) {
        return this.coordinates.add(elem);
    }
}
