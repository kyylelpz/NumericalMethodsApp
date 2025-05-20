/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class FalsePosition {
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
        expression = Utils.convertExprToExp4jCompatible(expression);
        
        //Enter initial guesses
        double x0 = 0.0, x1 = 0.0;
        while (true) {
            try {
                System.out.print("Enter x0: ");
                x0 = input.nextDouble();
                Math.abs(Utils.evaluateFunction(expression, x0, decimalPlaces));
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (Exception e) {
                System.out.println("Evaluation error: " + e.getMessage());
                input.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("Enter x1: ");
                x1 = input.nextDouble();
                Math.abs(Utils.evaluateFunction(expression, x1, decimalPlaces));
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (Exception e) {
                System.out.println("Evaluation error: " + e.getMessage());
                input.nextLine();
            }
        }
        
        //Start iteration
        LinkedList<LinkedList<Double>> iterations = new LinkedList<>();
        try {
            Double solution = falsePosition(expression, x0, x1, tolerance, decimalPlaces, 1, iterations, null);

            for (int i = 0; i < iterations.size(); i++) {
                LinkedList<Double> row = iterations.get(i);
                System.out.println("Iteration #" + (i+1) + ": a = " + row.get(0) + " b = " + row.get(1) + " c = "
                                    + row.get(2) + " f(c) = " + row.get(3));
            }

            if (solution == null) {
                System.out.println("Method diverged. No approximate solution found.");
            } else {
                System.out.printf("The approximate solution is: " + solution);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //False Position Start/Recursive
    public static Double falsePosition(String function, double a, double b, double tolerance,
                    int decimalPlaces, int iteration, LinkedList<LinkedList<Double>> iterations, StringBuilder sb) {
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.");
            return null;
        }

        //Evaluate functions
        double fa = Utils.evaluateFunction(function, a, decimalPlaces);
        double fb = Utils.evaluateFunction(function, b, decimalPlaces);

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            System.out.println("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.");
            sb.append("");
            return null;
        }
        
        if (fa * fb >= 0) {
            System.out.println("f(a) and f(b) must have opposite signs.");
            sb.append("f(a) and f(b) must have opposite signs.");
        }

        // Compute c using Regula Falsi formula
        double c = a + (((b-a)*(-fa))/(fb-fa));
        c = Utils.round(c, decimalPlaces);

        double fc = Utils.evaluateFunction(function, c, decimalPlaces);

        if (Double.isNaN(c) || Double.isInfinite(c)) {
            System.out.println("Invalid function evaluation at x(n+1) (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at x(n+1) (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (Math.abs(c) > 1e10) {
            System.out.println("Divergence detected due to very large x(n+1) value. Iteration stopped.");
            sb.append("Divergence detected due to very large x(n+1) value. Iteration stopped.");
            return null;
        }

        //System.out.printf("Iteration %d: a=%.6f, b=%.6f, c=%.6f, f(c)=%.6f%n", iteration, a, b, c, fc);

        LinkedList<Double> row = new LinkedList<>();
        row.offer(a);
        row.offer(b);
        row.offer(c);
        row.offer(fc);

        iterations.add(row);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(fc) <= tolerance || c == a || c == b) { // check if kailangan pa ung Math.abs(c - a) <= tolerance || Math.abs(c - b) <= tolerance
            return c;
        }
        // 

        // Recursive step
        if (fa * fc < 0) {
            return falsePosition(function, a, c, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        } else {
            return falsePosition(function, c, b, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        }

    }

    public static String solve(String fx, double a, double b, double tolerance) {
        StringBuilder sb = new StringBuilder();

        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        fx = Utils.convertExprToExp4jCompatible(fx);

        LinkedList<LinkedList<Double>> iterations = new LinkedList<>();
        try {
            Double solution = falsePosition(fx, a, b, tolerance, decimalPlaces, 1, iterations, sb);

            if (!iterations.isEmpty()) {
                sb.append("Iterations:\n\n");
                for (int i = 0; i < iterations.size(); i++) {
                    LinkedList<Double> row = iterations.get(i);
                    sb.append("Iteration #").append(i + 1)
                        .append(": a = ").append(row.get(0))
                        .append(" b = ").append(row.get(1))
                        .append(" c = ").append(row.get(2))
                        .append(" f(c) = ").append(row.get(3))
                        .append("\n");
                }
                sb.append("\n");
            }

            if (solution == null) {
                sb.append("Method diverged or failed. No approximate solution found.");
            } else {
                sb.append("The approximate solution is: ").append(solution);
            }
        } catch (IllegalArgumentException e) {
            sb.append("Error: ").append(e.getMessage());
        }

        return sb.toString();
    }

}
