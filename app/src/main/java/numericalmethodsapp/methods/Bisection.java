/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class Bisection {
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

        //Get decimal places
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        System.out.println(decimalPlaces);
        
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
        
        // Start iterations
        ArrayList<ArrayList<Double>> iterations = new ArrayList<>();
        try {
            Double solution = bisection(expression, x0, x1, tolerance, decimalPlaces, 1, iterations, null);

            for (int i = 0; i < iterations.size(); i++) {
                ArrayList<Double> row = iterations.get(i);
                System.out.println("Iteration #" + (i+1) + ": a = " + row.get(0) + " b = " + row.get(1) + " mp = "
                                    + row.get(2) + " f(mp) = " + row.get(3));
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

     public static Double bisection(String expression, double a, double b, double tolerance, int decimalPlaces,
                                   int iteration, ArrayList<ArrayList<Double>> iterations, StringBuilder sb) {
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.");
            return null;
        }
        
        double mp = (a + b) / 2;
        mp = Utils.round(mp, decimalPlaces);
        
        //Evaluate functions
        double fa = Utils.evaluateFunction(expression, a, decimalPlaces);
        double fb = Utils.evaluateFunction(expression, b, decimalPlaces);
        double fmp = Utils.evaluateFunction(expression, mp, decimalPlaces);

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            System.out.println("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.");
            return null;
        }

        //CHECK LATER
        
        if (fa * fb >= 0) {
            System.out.println("f(a) and f(b) must have opposite signs.");
            sb.append("f(a) and f(b) must have opposite signs.");
            return null;
        }

        if (Double.isNaN(fmp) || Double.isInfinite(fmp)) {
            System.out.println("Invalid function evaluation at midpoint (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at midpoint (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (Math.abs(mp) > 1e10) {
            System.out.println("Divergence detected due to very large midpoint value. Iteration stopped.");
            sb.append("Divergence detected due to very large midpoint value. Iteration stopped.");
            return null;
        }

        ArrayList<Double> row = new ArrayList<>();
        row.add(a);
        row.add(b);
        row.add(mp);
        row.add(fmp);

        iterations.add(row);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(fmp) <= tolerance || Math.abs(mp - a) <= tolerance || Math.abs(mp - b) <= tolerance) {
            return mp;
        }

        // Recursive step
        if (fa * fmp < 0) {
            return bisection(expression, a, mp, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        } else {
            return bisection(expression, mp, b, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        }
    }

    public static String solve(String expression, double a, double b, double tolerance) {
        StringBuilder output = new StringBuilder();
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        expression = Utils.convertExprToExp4jCompatible(expression);

        ArrayList<ArrayList<Double>> iterations = new ArrayList<>();

        Double solution;
        try {
            solution = bisection(expression, a, b, tolerance, decimalPlaces, 1, iterations, output);
        } catch (IllegalArgumentException e) {
            output.append("Error: ").append(e.getMessage()).append("\n");
            return output.toString();
        }

        if (!iterations.isEmpty()) {
            output.append("Iterations:\n\n");
            for (int i = 0; i < iterations.size(); i++) {
                ArrayList<Double> row = iterations.get(i);
                output.append(String.format("Iteration #%d: a = %.8f, b = %.8f, mp = %.8f, f(mp) = %.8f\n",
                        i + 1, row.get(0), row.get(1), row.get(2), row.get(3)));
            }
            output.append("\n");
        }

        if (solution == null) {
            output.append("Method diverged. No approximate solution found.\n");
        } else {
            output.append("The approximate solution is: ").append(solution).append("\n");
        }

        return output.toString();
    }

}
