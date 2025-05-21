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
        Double solution = newtonRaphson(expression, derivativeStr, iGuess, tolerance, decimalPlaces, 1, iterations, null);

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

    public static Double newtonRaphson (String expression, String derivativeStr, double currGuess, double tolerance,
                                        int decimalPlaces, int iteration, LinkedList<Double> iterations, StringBuilder sb){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append(":\n");

        expression = expression.replaceAll("(?<=[0-9])x", "*x");
        String substitutedExpr = expression.replaceAll("\\bx\\b", Double.toString(currGuess));
        derivativeStr = derivativeStr.replaceAll("(?<=[0-9])x", "*x");
        String substitutedDStr = derivativeStr.replaceAll("\\bx\\b", Double.toString(currGuess));
        
        double fx = Utils.evaluateFunction(expression, currGuess, decimalPlaces);
        double fdx = Utils.evaluateFunction(derivativeStr, currGuess, decimalPlaces);

        if (Double.isNaN(fx) || Double.isInfinite(fx) ||
            Double.isNaN(fdx) || Double.isInfinite(fdx)) {
            System.out.println("Invalid function or derivative evaluation (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function or derivative evaluation (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (fdx == 0.0) {
            System.out.println("Zero derivative detected at x = " + currGuess + ". Cannot proceed with Newton-Raphson due to DivisionByZero Error.");
            sb.append("Zero derivative detected at x = ").append(currGuess).append(". Cannot proceed with Newton-Raphson due to DivisionByZero Error.\n");
            return null;
        }

        double nextGuess = currGuess - fx/fdx;
        nextGuess = Utils.round(nextGuess, decimalPlaces);

        sb.append("x(n+1) = (").append(currGuess).append(")-((").append(substitutedExpr).append(")/(").append(substitutedDStr).append(")\n");
        sb.append("x(n+1) = ").append(nextGuess).append("\n");

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            sb.append("Divergence detected. Iteration stopped.\n");
            return null;
        }

        iterations.add(nextGuess);

        double check = Utils.round(Math.abs(nextGuess - currGuess), decimalPlaces);

        if (Math.abs(nextGuess-currGuess) <= tolerance){
            sb.append("| x(n+1) - x(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return nextGuess;
        }

        sb.append("| x(n+1) - x(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is greater than tolerance.\n");
        sb.append("Continuing to next iteration...\n\n");

        return newtonRaphson(expression, derivativeStr, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, sb);
    }

    public static String solve(String expression, String derivativeStr, double tolerance, double initialGuess, StringBuilder sb) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        sb.append("f(x) = ").append(expression).append("\n");
        sb.append("f'(x) = ").append(derivativeStr).append("\n\n");
        
        LinkedList<Double> iterations = new LinkedList<>();

        iterations.add(initialGuess);
        
        sb.append("Set tolerance to: ").append(tolerance).append("\n");
        sb.append("Set decimal figures to: ").append(decimalPlaces).append("\n\n");

        sb.append("Start of Newton-Raphson Method:\n\n");

        Double result = newtonRaphson(expression, derivativeStr, initialGuess, tolerance, decimalPlaces, 1, iterations, sb);

        if (result == null) {
            sb.append("Method diverged or encountered an error. No approximate solution found.\n");
        } else {
            sb.append("Summary of Iterations:\n\n");
            for (int i = 0; i < iterations.size() - 1; i++) {
                sb.append(String.format("Iteration #%2d", i+1)).append(":     x(n+1) = ").append(iterations.get(i+1)).append("\n");
            }
            sb.append("\nThe approximate root is: ").append(result);
        }

        return sb.toString();
    }

}