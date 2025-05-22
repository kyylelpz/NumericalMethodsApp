/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.ArrayList;

import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class Bisection {

     public static Double bisection(String expression, double a, double b, double tolerance, int decimalPlaces,
                                   int iteration, ArrayList<ArrayList<Double>> iterations, StringBuilder sb, Character var) {

        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append("\n");
        expression = expression.replaceAll("(?<=[0-9])"+var, "*"+var);
        
        double mp = (a + b) / 2;
        mp = Utils.round(mp, decimalPlaces);

        String substituted1 = expression.replaceAll("\\b"+var+"\\b", Double.toString(a));
        String substituted2 = expression.replaceAll("\\b"+var+"\\b", Double.toString(b));
        String substituted3 = expression.replaceAll("\\b"+var+"\\b", Double.toString(mp));
        
        //Evaluate functions
        double fa = Utils.evaluateFunction(expression, a, decimalPlaces, var);
        double fb = Utils.evaluateFunction(expression, b, decimalPlaces, var);
        double fmp = Utils.evaluateFunction(expression, mp, decimalPlaces, var);

        sb.append("f(").append(var).append("L) = ").append(substituted1).append(" = ").append(fa).append("\n");
        sb.append("f(").append(var).append("R) = ").append(substituted2).append(" = ").append(fb).append("\n");

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            sb.append("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.\n");
            return null;
        }

        if (fa * fb > 0) {
            sb.append("f(").append(var).append("L) and f(").append(var).append("R) must have opposite signs.\n");
            return null;
        }

        sb.append("f(").append(var).append("M) = ").append(substituted3).append(" = ").append(fmp).append("\n");

        if (Double.isNaN(fmp) || Double.isInfinite(fmp)) {
            sb.append("Invalid function evaluation at midpoint (NaN or Infinity). Iteration stopped.\n");
            return null;
        }

        if (Math.abs(mp) > 1e10) {
            sb.append("Divergence detected due to very large midpoint value. Iteration stopped.\n");
            return null;
        }

        ArrayList<Double> row = new ArrayList<>();
        row.add(a);
        row.add(b);
        row.add(mp);
        row.add(fmp);

        iterations.add(row);

        // PAGHIWALAYIN NALANG THIS
        double check = Math.abs(fmp);

        if (Math.abs(fmp) <= tolerance){
            sb.append("| f(").append(var).append("M) | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        double check1 = Math.abs(mp - a);

        if (Math.abs(mp - a) <= tolerance){
            sb.append("| ").append(var).append("M - ").append(var).append("L | = ").append(" | (").append(mp).append(" - ").append(a).append(") | = ").append(check1).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        double check2 = Math.abs(mp - b);

        if (Math.abs(mp - b) <= tolerance){
            sb.append("| ").append(var).append("M - ").append(var).append("R | = ").append(" | (").append(mp).append(" - ").append(b).append(") | = ").append(check2).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        sb.append("| f(").append(var).append("M) | = ").append(check).append(" is greater than tolerance.\n");

        double nextIteration = fa*fmp;
        double nextIteration2 = fb*fmp;

        if (nextIteration < 0) {
            sb.append("f(").append(var).append("L)*f(").append(var).append("M) = ").append(fa).append(" * ").append(fmp).append("\n\t= ").append(nextIteration).append(" is less than 0.\n");
            sb.append("Setting ").append(var).append("R = ").append(var).append("M\n");
            sb.append("Continuing to next iteration...\n\n");
            
            return bisection(expression, a, mp, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
        } else {
            sb.append("f(").append(var).append("R)*f(").append(var).append("M) = ").append(fb).append(" * ").append(fmp).append("\n\t= ").append(nextIteration2).append(" is less than 0.\n");
            sb.append("Setting ").append(var).append("L = ").append(var).append("M\n");
            sb.append("Continuing to next iteration...\n\n");

            return bisection(expression, mp, b, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
        }
    }
    

    public static String solve(String expression, double a, double b, double tolerance, StringBuilder sb, Character var) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        expression = Utils.convertExprToExp4jCompatible(expression);

        sb.append("f(").append(var).append(") = ").append(expression).append("\n");
        sb.append(var).append("(L) = ").append(a).append("\n");
        sb.append(var).append("(R) = ").append(b).append("\n\n");

        ArrayList<ArrayList<Double>> iterations = new ArrayList<>();

        sb.append("Start of Bisection Method:\n\n");

        Double solution;
        try {
            solution = bisection(expression, a, b, tolerance, decimalPlaces, 1, iterations, sb, var);
        } catch (IllegalArgumentException e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
            return sb.toString();
        }

        if (!iterations.isEmpty()) {
            sb.append("Summary of Iterations:\n\n");

            sb.append(String.format("%-12s%-15s%-15s%-15s%-15s\n", 
                "Iteration", var + "L", var + "R", var + "M", "F(" + var + "M)"));

            String format = String.format("%%-12d%%-15.%df%%-15.%df%%-15.%df%%-15.%df\n",
                decimalPlaces, decimalPlaces, decimalPlaces, decimalPlaces);
                
            for (int i = 0; i < iterations.size(); i++) {
                ArrayList<Double> row = iterations.get(i);
                sb.append(String.format(format, i + 1, row.get(0), row.get(1), row.get(2), row.get(3)));
            }

            sb.append("\n");
        }

        if (solution == null) {
            sb.append("Method diverged or stopped due to a mathematical error. No approximate root found.\n");
        } else {
            sb.append("The approximate solution is: ").append(solution).append("\n");
        }

        return sb.toString();
    }

}
