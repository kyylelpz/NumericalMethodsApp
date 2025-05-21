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

        sb.append("Iteration #").append(iteration).append(":\n");
        function = function.replaceAll("(?<=[0-9])x", "*x");

        String substituted1 = function.replaceAll("\\bx\\b", Double.toString(a));
        String substituted2 = function.replaceAll("\\bx\\b", Double.toString(b));

        //Evaluate functions
        double fa = Utils.evaluateFunction(function, a, decimalPlaces);
        double fb = Utils.evaluateFunction(function, b, decimalPlaces);

        sb.append("f(xL) = ").append(substituted1).append(" = ").append(fa).append("\n");
        sb.append("f(xR) = ").append(substituted2).append(" = ").append(fb).append("\n");

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            System.out.println("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.");
            sb.append("");
            return null;
        }
        
        if (fa * fb >= 0) {
            System.out.println("f(xL) and f(xR) must have opposite signs.");
            sb.append("f(xL) and f(xR) must have opposite signs.\n");
            return null;
        }

        if (Math.abs(fb - fa) < 1e-10) {
            String error = "Possible division by zero: f(xR) and f(xL) are too close. Iteration stopped.";
            System.out.println(error);
            sb.append(error).append("\n");
            return null;
        }
        
        // Compute c using Regula Falsi formula
        double i = (((b-a)*(-fa))/(fb-fa));
        i = Utils.round(i, decimalPlaces);
        double c = a + i;
        c = Utils.round(c, decimalPlaces);

        String substituted3 = function.replaceAll("\\bx\\b", Double.toString(c));
        double fc = Utils.evaluateFunction(function, c, decimalPlaces);

        sb.append("xM = xL + I = ").append(a).append(" + ").append(i).append(" = ").append(c).append("\n");
        sb.append("f(xM) = ").append(substituted3).append(" = ").append(fc).append("\n");
        

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

        LinkedList<Double> row = new LinkedList<>();
        row.offer(a);
        row.offer(b);
        row.offer(c);
        row.offer(fc);

        iterations.add(row);

        if (Math.abs(fc) <= tolerance){ //|| c == a || c == b) { // check if kailangan pa ung Math.abs(c - a) <= tolerance || Math.abs(c - b) <= tolerance
            sb.append("| f(xM) | = | ").append(Math.abs(fc)).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }
        
        if (c == a){
            sb.append("xM == xL (").append(c).append(" == ").append(a).append(")\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        if (c == b){
            sb.append("xM == xR (").append(c).append(" == ").append(b).append(")\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        sb.append("| f(xM) | = ").append(Math.abs(fc)).append(" is greater than tolerance.\n");

        double nextIteration = Utils.round((fa*fc), decimalPlaces);
        double nextIteration2 = Utils.round((fb*fc), decimalPlaces);

        if (fa * fc < 0) {
            sb.append("f(xL)*f(xM) = ").append(fa).append(" * ").append(fc).append("\n\t= ").append(nextIteration).append(" is less than 0.\n");
            sb.append("Setting xR = xM\n");
            sb.append("Continuing to next iteration...\n\n");

            return falsePosition(function, a, c, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        } else {
            sb.append("f(xR)*f(xM) = ").append(fb).append(" * ").append(fc).append("\n\t= ").append(nextIteration2).append(" is less than 0.\n");
            sb.append("Setting xL = xM\n");
            sb.append("Continuing to next iteration...\n\n");

            return falsePosition(function, c, b, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        }

    }

    public static String solve(String fx, double a, double b, double tolerance, StringBuilder sb) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        fx = Utils.convertExprToExp4jCompatible(fx);

        sb.append("f(x) = ").append(fx).append("\n");
        sb.append("xL = ").append(a).append("\n");
        sb.append("xR = ").append(b).append("\n\n");
        sb.append("I (interpolation term) = [(xR-XL)(-f(xL)]/[f(xR)-f(xL)] will be evaluated without detailed solution.").append("\n\n");

        sb.append("Start of False Position or Regula-Falsi Method:\n\n");

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
