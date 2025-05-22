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
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        sb.append("Iteration #").append(iteration).append("\n");
        expression = expression.replaceAll("(?<=[0-9])x", "*x");
        
        double mp = (a + b) / 2;
        mp = Utils.round(mp, decimalPlaces);

        String substituted1 = expression.replaceAll("\\bx\\b", Double.toString(a));
        String substituted2 = expression.replaceAll("\\bx\\b", Double.toString(b));
        String substituted3 = expression.replaceAll("\\bx\\b", Double.toString(mp));
        
        //Evaluate functions
        double fa = Utils.evaluateFunction(expression, a, decimalPlaces);
        double fb = Utils.evaluateFunction(expression, b, decimalPlaces);
        double fmp = Utils.evaluateFunction(expression, mp, decimalPlaces);

        sb.append("f(xL) = ").append(substituted1).append(" = ").append(fa).append("\n");
        sb.append("f(xR) = ").append(substituted2).append(" = ").append(fb).append("\n");

        if (Double.isNaN(fa) || Double.isInfinite(fa) ||
            Double.isNaN(fb) || Double.isInfinite(fb)) {
            System.out.println("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at interval endpoints (NaN or Infinity). Iteration stopped.\n");
            return null;
        }

        //CHECK LATER

        if (fa * fb > 0) {
            System.out.println("f(xL) and f(xR) must have opposite signs.");
            sb.append("f(xL) and f(xR) must have opposite signs.\n");
            return null;
        }

        sb.append("f(xM) = ").append(substituted3).append(" = ").append(fmp).append("\n");

        if (Double.isNaN(fmp) || Double.isInfinite(fmp)) {
            System.out.println("Invalid function evaluation at midpoint (NaN or Infinity). Iteration stopped.");
            sb.append("Invalid function evaluation at midpoint (NaN or Infinity). Iteration stopped.\n");
            return null;
        }

        if (Math.abs(mp) > 1e10) {
            System.out.println("Divergence detected due to very large midpoint value. Iteration stopped.");
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
            sb.append("| f(xM) | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        double check1 = Math.abs(mp - a);

        if (Math.abs(mp - a) <= tolerance){
            sb.append("| xM - xL | = ").append(" | (").append(mp).append(" - ").append(a).append(") | = ").append(check1).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        double check2 = Math.abs(mp - b);

        if (Math.abs(mp - b) <= tolerance){
            sb.append("| xM - xR | = ").append(" | (").append(mp).append(" - ").append(b).append(") | = ").append(check2).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return mp;
        }

        sb.append("| f(xM) | = ").append(check).append(" is greater than tolerance.\n");

        double nextIteration = fa*fmp;
        double nextIteration2 = fb*fmp;

        if (nextIteration < 0) {
            sb.append("f(xL)*f(xM) = ").append(fa).append(" * ").append(fmp).append("\n\t= ").append(nextIteration).append(" is less than 0.\n");
            sb.append("Setting xR = xM\n");
            sb.append("Continuing to next iteration...\n\n");
            
            return bisection(expression, a, mp, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        } else {
            sb.append("f(xR)*f(xM) = ").append(fb).append(" * ").append(fmp).append("\n\t= ").append(nextIteration2).append(" is less than 0.\n");
            sb.append("Setting xL = xM\n");
            sb.append("Continuing to next iteration...\n\n");

            return bisection(expression, mp, b, tolerance, decimalPlaces, iteration + 1, iterations, sb);
        }
    }
    

    public static String solve(String expression, double a, double b, double tolerance, StringBuilder sb) {
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        expression = Utils.convertExprToExp4jCompatible(expression);

        sb.append("f(x) = ").append(expression).append("\n");
        sb.append("x(L) = ").append(a).append("\n");
        sb.append("x(R) = ").append(b).append("\n\n");

        ArrayList<ArrayList<Double>> iterations = new ArrayList<>();

        sb.append("Start of Bisection Method:\n\n");

        Double solution;
        try {
            solution = bisection(expression, a, b, tolerance, decimalPlaces, 1, iterations, sb);
        } catch (IllegalArgumentException e) {
            sb.append("Error: ").append(e.getMessage()).append("\n");
            return sb.toString();
        }

        if (!iterations.isEmpty()) {
            sb.append("Summary of Iterations:\n\n");
            for (int i = 0; i < iterations.size(); i++) {
                ArrayList<Double> row = iterations.get(i);
                String format = String.format("Iteration #%%d:\txL = %%.%df\txR = %%.%df\txM = %%.%df\t f(xM) =  %%.%df\n",
                              decimalPlaces, decimalPlaces, decimalPlaces, decimalPlaces, decimalPlaces);
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
