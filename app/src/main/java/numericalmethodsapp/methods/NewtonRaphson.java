/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.LinkedList;

import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class NewtonRaphson {

    public static Double newtonRaphson (String expression, String derivativeStr, double currGuess, double tolerance,
                                        int decimalPlaces, int iteration, LinkedList<Double> iterations, StringBuilder sb, Character var){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append(":\n");

        expression = expression.replaceAll("(?<=[0-9])"+var, "*"+var);
        String substitutedExpr = expression.replaceAll("\\b"+var+"\\b", Double.toString(currGuess));
        derivativeStr = derivativeStr.replaceAll("(?<=[0-9])"+var, "*"+var);
        String substitutedDStr = derivativeStr.replaceAll("\\b"+var+"\\b", Double.toString(currGuess));
        
        double fx = Utils.evaluateFunction(expression, currGuess, decimalPlaces, var);
        double fdx = Utils.evaluateFunction(derivativeStr, currGuess, decimalPlaces, var);

        if (Double.isNaN(fx) || Double.isInfinite(fx) ||
            Double.isNaN(fdx) || Double.isInfinite(fdx)) {
            System.out.println("Invalid function or derivative evaluation (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function or derivative evaluation (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (fdx == 0.0) {
            System.out.println("Zero derivative detected at "+var+" = " + currGuess + ". Cannot proceed with Newton-Raphson due to DivisionByZero Error.");
            sb.append("Zero derivative detected at ").append(var).append(" = ").append(currGuess).append(". Cannot proceed with Newton-Raphson due to DivisionByZero Error.\n");
            return null;
        }

        double nextGuess = currGuess - fx/fdx;
        nextGuess = Utils.round(nextGuess, decimalPlaces);

        sb.append(var).append("(n+1) = (").append(currGuess).append(")-((").append(substitutedExpr).append(")/(").append(substitutedDStr).append(")\n");
        sb.append(var).append("(n+1) = ").append(nextGuess).append("\n");

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            sb.append("Divergence detected. Iteration stopped.\n");
            return null;
        }

        iterations.add(nextGuess);

        double check = Math.abs(Utils.round(Math.abs(nextGuess - currGuess), decimalPlaces));

        if (Math.abs(nextGuess-currGuess) <= tolerance){
            sb.append("| ").append(var).append("(n+1) - ").append(var).append("(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return nextGuess;
        }

        sb.append("| ").append(var).append("(n+1) - ").append(var).append("(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is greater than tolerance.\n");
        sb.append("Continuing to next iteration...\n\n");

        return newtonRaphson(expression, derivativeStr, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
    }

    public static String solve(String expression, String derivativeStr, double tolerance, double initialGuess, StringBuilder sb, Character var) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        sb.append("f(").append(var).append(") = ").append(expression).append("\n");
        sb.append("f'(").append(var).append(") = ").append(derivativeStr).append("\n");
        sb.append(var).append("(n) = ").append(initialGuess).append("\n\n");
        
        LinkedList<Double> iterations = new LinkedList<>();

        iterations.add(initialGuess);
        
        sb.append("Set tolerance to: ").append(tolerance).append("\n");
        sb.append("Set decimal figures to: ").append(decimalPlaces).append("\n\n");

        sb.append("Start of Newton-Raphson Method:\n\n");

        Double result = newtonRaphson(expression, derivativeStr, initialGuess, tolerance, decimalPlaces, 1, iterations, sb, var);

        if (result == null) {
            sb.append("Method diverged or stopped due to a mathematical error. No approximate root found.\n");
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