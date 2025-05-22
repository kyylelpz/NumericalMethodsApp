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
public class FalsePosition {

    public static Double falsePosition(String function, double a, double b, double tolerance,
                    int decimalPlaces, int iteration, LinkedList<LinkedList<Double>> iterations, StringBuilder sb, Character var) {

        if (iteration > 1000) {
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append(":\n");
        function = function.replaceAll("(?<=[0-9])"+var, "*"+var);

        String substituted1 = function.replaceAll("\\b"+var+"\\b", Double.toString(a));
        String substituted2 = function.replaceAll("\\b"+var+"\\b", Double.toString(b));

        double fa = Utils.evaluateFunction(function, a, decimalPlaces, var);
        double fb = Utils.evaluateFunction(function, b, decimalPlaces, var);

        sb.append("f(").append(var).append("L) = ").append(substituted1).append(" = ").append(fa).append("\n");
        sb.append("f(").append(var).append("R) = ").append(substituted2).append(" = ").append(fb).append("\n");

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            sb.append("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped\n");
            return null;
        }
        
        if (fa * fb >= 0) {
            sb.append("f(xL) and f(xR) must have opposite signs.\n");
            return null;
        }

        if (Math.abs(fb - fa) < 1e-10) {
            sb.append("Possible division by zero: f(xR) and f(xL) are too close. Iteration stopped.\n");
            return null;
        }
        
        double i = (((b-a)*(-fa))/(fb-fa));
        i = Utils.round(i, decimalPlaces);
        double c = a + i;
        c = Utils.round(c, decimalPlaces);

        String substituted3 = function.replaceAll("\\b"+var+"\\b", Double.toString(c));
        double fc = Utils.evaluateFunction(function, c, decimalPlaces, var);

        sb.append(var).append("M = xL + I = ").append(a).append(" + ").append(i).append(" = ").append(c).append("\n");
        sb.append("f(").append(var).append("M) = ").append(substituted3).append(" = ").append(fc).append("\n");
        

        if (Double.isNaN(c) || Double.isInfinite(c)) {
            sb.append("Invalid function evaluation at ").append(var).append("(n+1) (NaN or Infinity). Iteration stopped.");
            return null;
        }

        if (Math.abs(c) > 1e10) {
            sb.append("Divergence detected due to very large ").append(var).append("(n+1) value. Iteration stopped.");
            return null;
        }

        LinkedList<Double> row = new LinkedList<>();
        row.offer(a);
        row.offer(b);
        row.offer(c);
        row.offer(fc);

        iterations.add(row);

        if (Math.abs(fc) <= tolerance){ //|| c == a || c == b) { // check if kailangan pa ung Math.abs(c - a) <= tolerance || Math.abs(c - b) <= tolerance
            sb.append("| f(").append(var).append("M) | = | ").append(Math.abs(fc)).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }
        
        if (c == a){
            sb.append(var).append("M == ").append(var).append("L (").append(c).append(" == ").append(a).append(")\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        if (c == b){
            sb.append(var).append("M == ").append(var).append("R (").append(c).append(" == ").append(b).append(")\n");
            sb.append("Stopping the iteration...\n\n\n");
            return c;
        }

        sb.append("| f(").append(var).append("M) | = ").append(Math.abs(fc)).append(" is greater than tolerance.\n");

        double nextIteration = Utils.round((fa*fc), decimalPlaces);
        double nextIteration2 = Utils.round((fb*fc), decimalPlaces);

        if (fa * fc < 0) {
            sb.append("f(").append(var).append("L)*f(").append(var).append("M) = ").append(fa).append(" * ").append(fc).append("\n\t= ").append(nextIteration).append(" is less than 0.\n");
            sb.append("Setting ").append(var).append("R = ").append(var).append("M\n");
            sb.append("Continuing to next iteration...\n\n");

            return falsePosition(function, a, c, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
        } else {
            sb.append("f(").append(var).append("R)*f(").append(var).append("M) = ").append(fb).append(" * ").append(fc).append("\n\t= ").append(nextIteration2).append(" is less than 0.\n");
            sb.append("Setting ").append(var).append("L = ").append(var).append("M\n");
            sb.append("Continuing to next iteration...\n\n");

            return falsePosition(function, c, b, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
        }

    }

    public static String solve(String fx, double a, double b, double tolerance, StringBuilder sb, Character var) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        fx = Utils.convertExprToExp4jCompatible(fx);

        sb.append("f(").append(var).append(") = ").append(fx).append("\n");
        sb.append(var).append("L = ").append(a).append("\n");
        sb.append(var).append("R = ").append(b).append("\n\n");
        sb.append("I (interpolation term) = [(").append(var).append("R-").append(var).append("L)(-f(").append(var).append("L)]/[f(").append(var).append("R)-f(").append(var).append("L)]\n")
        .append("will be evaluated without detailed solution.").append("\n\n");

        sb.append("Start of False Position or Regula-Falsi Method:\n\n");

        LinkedList<LinkedList<Double>> iterations = new LinkedList<>();
        try {
            Double solution = falsePosition(fx, a, b, tolerance, decimalPlaces, 1, iterations, sb, var);

            if (!iterations.isEmpty()) {
                sb.append("Iterations:\n\n");

                sb.append(String.format("%-12s%-15s%-15s%-15s%-15s\n", 
                    "Iteration", var + "L", var + "R", var + "(n+1)", "F(n+1)"));

                String format = "%-12d%-15." + decimalPlaces + "f%-15." + decimalPlaces + "f%-15." + decimalPlaces + "f%-15." + decimalPlaces + "f\n";

                for (int i = 0; i < iterations.size(); i++) {
                    LinkedList<Double> row = iterations.get(i);
                    sb.append(String.format(format,
                        i + 1, row.get(0), row.get(1), row.get(2), row.get(3)));
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
