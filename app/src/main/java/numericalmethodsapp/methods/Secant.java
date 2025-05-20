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
public class Secant {
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
        ArrayList<Double> iterations = new ArrayList<>();
        iterations.add(x0);
        iterations.add(x1);
        
        try {
            Double solution = secant(expression, x0, x1, tolerance, decimalPlaces, 1, iterations, null);

            // Print results
            System.out.println("\nIterations:");
            System.out.println();

            for (int i = 0; i < iterations.size()-2; i++) {
                System.out.println("Iteration #" + (i+1) + ": a = " + iterations.get(i) + " b = " + iterations.get(i+1) + " c = " + iterations.get(i+2));
            }

            System.out.println();
            
            if (solution == null) {
                System.out.println("Method diverged. No approximate solution found.");
            } else {
                System.out.printf("The approximate solution is: " + solution);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Double secant(String function, double a, double b, double tolerance,
                            int decimalPlaces, int iteration, ArrayList<Double> iterations,
                            StringBuilder sb) {
        if (iteration >= 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        //Evaluate functions
        double fa = Utils.evaluateFunction(function, a, decimalPlaces);
        double fb = Utils.evaluateFunction(function, b, decimalPlaces);

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            System.out.println("Invalid function evaluation at initial points (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at initial points (NaN or Infinity). Iteration stopped.\n");
            return null;
        }

        if (Math.abs(fb - fa) < 1e-10) {
            System.out.println("Possible division by zero: f(b) and f(a) are too close. Iteration stopped.");
            sb.append("Possible division by zero: f(b) and f(a) are too close. Iteration stopped.\n");
            return null;
        }

        double c = b - fb * (b-a) / (fb-fa);
        c = Utils.round(c, decimalPlaces);

        iterations.add(c);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(c-b) < tolerance || Math.abs(c - a) <= tolerance || Math.abs(c - b) <= tolerance) {
            return c;
        }

        return secant(function, b, c, tolerance, decimalPlaces, iteration + 1, iterations, sb);
    }

    public static String solve(String expression, double tolerance, double x0, double x1) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        ArrayList<Double> iterations = new ArrayList<>();
        iterations.add(x0);
        iterations.add(x1);

        StringBuilder sb = new StringBuilder();
        Double solution = secant(expression, x0, x1, tolerance, decimalPlaces, 1, iterations, sb);

        if (iterations.size() > 2) {
            sb.append("Iterations:\n\n");
            for (int i = 0; i < iterations.size() - 2; i++) {
                sb.append(String.format("Iteration #%d:\ta = %.6f\tb = %.6f\tc = %.6f\n",
                        i + 1, iterations.get(i), iterations.get(i + 1), iterations.get(i + 2)));
            }
            sb.append("\n");
        }

        if (solution == null) {
            sb.append("\n");
            sb.append("Method diverged or stopped due to a mathematical error.\n");
        } else {
            sb.append(String.format("The approximate solution is: %.6f", solution));
        }

        return sb.toString();
    }

}
