/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class NewtonRaphson {
    public static void run (Scanner input){
        input.nextLine();

        //Enter f(x)
        String expression = "";
        while (true) {
            System.out.print("Enter f(x): ");
            expression = input.nextLine();
            expression = Utils.convertExprToSymjaCompatible(expression);

            if (Utils.isValidSymjaExpression(expression)) {
                break;
            } else {
                System.out.println("Invalid mathematical expression. Please check your syntax (e.g., unmatched parentheses, invalid functions). Try again.");
            }
        }

        expression = Utils.convertExprToSymjaCompatible(expression);

        //Enter tolerance
        double tolerance = 0.001;
        while (true) {
            try {
                System.out.print("Enter tolerance: ");
                tolerance = input.nextDouble();
                if (tolerance <= 0) throw new IllegalArgumentException("Tolerance must be positive.");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }

        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        //Enter initial guess
        double iGuess = 0.0;
        while (true) {
            try {
                System.out.print("Enter initial guess: ");
                iGuess = input.nextDouble();
                Math.abs(Utils.evaluateFunction(expression, iGuess, decimalPlaces));
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (Exception e) {
                System.out.println("Evaluation error: " + e.getMessage());
                input.nextLine();
            }
        }


        // Compute the derivative
        ExprEvaluator util = new ExprEvaluator();
        IExpr derivative = util.evaluate("D(" + expression + ", x)");
        String derivativeStr = Utils.convertExprToExp4jCompatible(derivative.toString());
        System.out.println("f'(x): " + derivativeStr);

        // Start iteration
        LinkedList<Double> iterations = new LinkedList<>();
        Double solution = newtonRaphson(expression, derivativeStr, iGuess, tolerance, decimalPlaces, 1, iterations);

        // Print results
        System.out.println("\nIterations:");
        System.out.println();

        for (int i = 0; i < iterations.size()-1; i++) {
            System.out.println("Iteration #" + (i + 1) + ":\tx(n) = " + iterations.get(i) + "\tx(n+1) = " + iterations.get(i+1));
        }

        System.out.println();
        
        if (solution == null) {
            System.out.println("Method diverged. No approximate solution found.");
        } else {
            System.out.printf("The approximate solution is: " + solution);
        }
    }

    public static Double newtonRaphson (String expression, String derivativeStr, double x, double tolerance, int decimalPlaces, int iteration, LinkedList<Double> iterations){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            return null;
        }
        
        double fx = Utils.evaluateFunction(expression, x, decimalPlaces);
        double fdx = Utils.evaluateFunction(derivativeStr, x, decimalPlaces);

        if (Double.isNaN(fx) || Double.isInfinite(fx) ||
            Double.isNaN(fdx) || Double.isInfinite(fdx)) {
            System.out.println("Invalid function or derivative evaluation (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (fdx == 0.0) {
            System.out.println("Zero derivative detected at x = " + x + ". Cannot proceed with Newton-Raphson.");
            return null;
        }

        double nextGuess = x - fx/fdx;
        nextGuess = Utils.round(nextGuess, decimalPlaces);

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            return null;
        }

        iterations.add(nextGuess);

        if (Math.abs(nextGuess-x) <= tolerance){
            return nextGuess;
        }

        return newtonRaphson(expression, derivativeStr, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations);
    }

}