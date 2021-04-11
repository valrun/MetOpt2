package com.example.metopt.methods;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Evaluator {

    Set<AbstractMethod> methods;

    public Evaluator(Function<Double, Double> fun) {
        methods = new HashSet<>(List.of(new DichotomyMethod(fun),
                new FibonacciMethod(fun), new GoldenRatioMethod(fun),
                new ParabolicMethod(fun), new BrentsMethods(fun)));
    }

    private void print(AbstractMethod method, PrintWriter pw) {
        pw.println(method.name + " method");
        pw.printf("%.4f%n", method.computeMin(-2.0, 3.0));
    }

    public void evaluate() {
        PrintWriter pw = new PrintWriter(System.out);
        for (AbstractMethod method : methods) {
            print(method, pw);
        }
        pw.close();
    }

    public static void main(String[] args) {
        Evaluator evaluator = new Evaluator((x) -> Math.pow(x,2) + Math.exp(-0.35 * x));
        evaluator.evaluate();
    }
}
