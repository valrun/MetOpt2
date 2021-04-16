package com.example.metopt.nmethods;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Вычисляет минимум квадратичных функций, используя три метода:
 * градиентного спуска, наискорейшего спуска, сопряженных градиентов
 */
public class Evaluator {

    Set<AbstractNMethod> methods;

    public Evaluator(QuadraticFunction fun) {
        methods = new HashSet<>(List.of(
                new GradientMethod(fun, "Gradient descent"),
                new FastGradientMethod(fun, "Fast gradient descent"),
                new ConjugateGradientMethod(fun, "Conjugate gradient descent")));
    }

    private void print(AbstractNMethod method, PrintWriter pw) {
        pw.println(method.name + " method");
        pw.printf("%.4f%n", method.computeMin());
    }

    public void evaluate() {
        PrintWriter pw = new PrintWriter(System.out);
        for (AbstractNMethod method : methods) {
            print(method, pw);
        }
        pw.close();

    }

    /**
     * Генерирует типовую квадратичную функцию для тестирования программы
     */
    private static QuadraticFunction getQuadraticFunction() {
        // 20x^2 + y^2 -7x + 3y + 2
        List<List<Double>> a = Arrays.asList(Arrays.asList(40.0, 0.0), Arrays.asList(0.0, 2.0));
        List<Double> b = Arrays.asList(-7.0, 3.0);
        double c = 2.0;
        return new QuadraticFunction(a, b, c);
    }

    public static void main(String[] args) {
        QuadraticFunction fun = getQuadraticFunction();
        Evaluator evaluator = new Evaluator(fun);
        evaluator.evaluate();
    }
}
