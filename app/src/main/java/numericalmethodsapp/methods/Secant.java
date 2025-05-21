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
            String error = "Method did not converge after 1000 iterations.";
            System.out.println(error);
            sb.append(error).append("\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append("\n");
        function = function.replaceAll("(?<=[0-9])x", "*x");

        String substituted1 = function.replaceAll("\\bx\\b", Double.toString(a));
        String substituted2 = function.replaceAll("\\bx\\b", Double.toString(b));

        //Evaluate functions
        double fa = Utils.evaluateFunction(function, a, decimalPlaces);
        double fb = Utils.evaluateFunction(function, b, decimalPlaces);

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            String error = "Invalid function evaluation at initial points (NaN or Infinity). Iteration stopped.";
            System.out.println(error);
            sb.append(error).append("\n");
            return null;
        }

        if (Math.abs(fb - fa) < 1e-10) {
            String error = "Possible division by zero: f(b) and f(a) are too close. Iteration stopped.";
            System.out.println(error);
            sb.append(error).append("\n");
            return null;
        }

        double c = b - fb * (b-a) / (fb-fa);
        c = Utils.round(c, decimalPlaces);

        sb.append("x(n+1) = ").append(b).append(" - ").append(substituted2).append(" *\n\t(").append(b).append(" - ").append(a).append(") / (").append(substituted2).append(" - ").append(substituted1).append("\n");
        sb.append("x(n+1) = ").append(c).append("\n");

        iterations.add(c);

        double check = Math.abs(Utils.round(Math.abs(c - b), decimalPlaces));
        
        if (Math.abs(c-b) <= tolerance) {
            sb.append("| x(n+1) - x(n) | = ").append(" | (").append(c).append(" - ").append(b).append(") | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        double check1 = Math.abs(Utils.round(Math.abs(c - a), decimalPlaces));

        if (Math.abs(c-a) <= tolerance){
            sb.append("| x(n+1) - x(n-1) | = ").append(" | (").append(c).append(" - ").append(a).append(") | = ").append(check1).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        sb.append("| x(n+1) - x(n) | = ").append(" | (").append(c).append(" - ").append(b).append(") | = ").append(check).append(" is greater than tolerance.\n");
        sb.append("Continuing to next iteration...\n\n");

        return secant(function, b, c, tolerance, decimalPlaces, iteration + 1, iterations, sb);
    }

    public static String solve(String expression, double tolerance, double x0, double x1, StringBuilder sb) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        sb.append("f(x) = ").append(expression).append("\n");
        sb.append("x(0) = ").append(x0).append("\n");
        sb.append("x(1) = ").append(x1).append("\n\n");

        ArrayList<Double> iterations = new ArrayList<>();
        iterations.add(x0);
        iterations.add(x1);

        sb.append("Start of Secant Method:\n\n");

        Double solution = secant(expression, x0, x1, tolerance, decimalPlaces, 1, iterations, sb);
    
        if (iterations.size() > 2) {
            sb.append("Summary of Iterations:\n\n");
            String format = String.format("Iteration #%%d:\ta = %%.%df\tb = %%.%df\tc = %%.%df\n",
                              decimalPlaces, decimalPlaces, decimalPlaces);
            for (int i = 0; i < iterations.size() - 2; i++) {
                sb.append(String.format(format, i + 1, iterations.get(i), iterations.get(i + 1), iterations.get(i + 2)));
            }
            sb.append("\n");
        }

        if (solution == null) {
            sb.append("\n");
            sb.append("Method diverged or stopped due to a mathematical error.\n");
        } else {
            String format = String.format("The approximate solution is: %%.%df\n", decimalPlaces);
            sb.append(String.format(format, solution));
        }

        return sb.toString();
    }

}
