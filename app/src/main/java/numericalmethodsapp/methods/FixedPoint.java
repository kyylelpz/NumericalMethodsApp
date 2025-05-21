package numericalmethodsapp.methods;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class FixedPoint {
    public static void run (Scanner input){
        input.nextLine(); //burn input

        //Enter g(x)
        String gofx = "";
        while (true) {
            System.out.print("Enter g(x): ");
            gofx = input.nextLine();
            gofx = Utils.convertExprToSymjaCompatible(gofx);

            if (Utils.isValidSymjaExpression(gofx)) {
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

        //Get the derivative for convergence checking
        ExprEvaluator util = new ExprEvaluator();
        IExpr dgofx = util.evaluate("D(" + gofx + ", x)");

        //Convert expression to exp4j compatible formats
        String dgofxStr = Utils.convertExprToExp4jCompatible(dgofx.toString());
        System.out.println("g'(x): " + dgofxStr);

        double iGuess = 0;

        //Enter initial guess
        while (true) {
            try {
                System.out.print("Enter initial guess: ");
                iGuess = input.nextDouble();

                //Check for convergence in derivative
                double absDerivative = Math.abs(Utils.evaluateFunction(dgofxStr, iGuess, decimalPlaces));
                if (absDerivative >= 1) {
                    System.out.printf("g'(%.4f) = %.4f which is >= 1. Try a different initial guess.\n", iGuess, absDerivative);
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (Exception e) {
                System.out.println("Evaluation error: " + e.getMessage());
                input.nextLine();
            }
        }

        //Start iteration
        gofx = Utils.convertExprToExp4jCompatible(gofx);
        Queue<Double> iterations = new LinkedList<>();
        Double solution = fixedPoint(gofx, iGuess, tolerance, decimalPlaces, 1, iterations, null);

        // Print results
        System.out.println("\nIterations:");
        System.out.println();

        for (int i = 0; i < iterations.size(); i++) {
            System.out.println("Iteration #" +  (i + 1) + ":\t" + "x = " + iterations.poll());
        }

        System.out.println();

        if (solution == null) {
            System.out.println("Method diverged. No approximate solution found.");
        } else {
            System.out.printf("The approximate solution is: " + solution);
}
    }

    public static Double fixedPoint (String gofx, double currGuess, double tolerance, int decimalPlaces, int iteration, Queue<Double> iterations, StringBuilder sb){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }

        gofx = gofx.replaceAll("(?<=[0-9])x", "*x"); // turns 2x into 2*x
        String substituted = gofx.replaceAll("\\bx\\b", Double.toString(currGuess));
        double nextGuess = Utils.evaluateFunction(gofx, currGuess, decimalPlaces);

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            sb.append("Divergence detected. Iteration stopped.\n");
            return null;
        }

        iterations.offer(nextGuess);
        sb.append("Iteration #").append(iteration).append(": \n");
        sb.append("x(n+1) = ").append(substituted).append(" = ").append(nextGuess).append("\n");

        double check = Utils.round(Math.abs(nextGuess - currGuess), decimalPlaces);

        if (check <= tolerance) {
            sb.append("| x(n+1) - x(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return nextGuess;
        }

        sb.append("| x(n+1) - x(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(" | = ").append(check).append(" is greater than tolerance.\n");
        sb.append("Continuing to next iteration...\n\n");

        return fixedPoint(gofx, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, sb);
    }

    public static String solve(String gofx, double tolerance, double iGuess, String dgofxStr, double absDerivative) {
        StringBuilder output = new StringBuilder();

        output.append("g(x) = ").append(gofx).append("\n");
        output.append("g'(x) = ").append(dgofxStr).append("\n");
        
        output.append("x(n) = ").append(iGuess).append("\n");
        

        output.append("| g'(").append(iGuess).append(") | = ").append(absDerivative).append(" < 1: Iterations will likely converge.\n\n");
    
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        gofx = Utils.convertExprToExp4jCompatible(gofx);

        output.append("Set tolerance to: ").append(tolerance).append("\n");
        output.append("Set decimal figures to: ").append(decimalPlaces).append("\n\n");

        output.append("Start of Fixed-Point Iteration Method:\n\n");

        Queue<Double> iterations = new LinkedList<>();
        Double solution = fixedPoint(gofx, iGuess, tolerance, decimalPlaces, 1, iterations, output);

        if (iterations.size() > 1) {
            output.append("Summary of Iterations:\n\n");
            int i = 0;
            while(!iterations.isEmpty()) {
                output.append(String.format("Iteration #%2d", i + 1)).append(":     x = ").append(iterations.poll()).append("\n");
                i++;
            }
            output.append("\n");
        }

        if (solution == null) {
            output.append("Method diverged. No approximate solution found.");
        } else {
            output.append("The approximate root is: ").append(solution);
        }

        return output.toString();

    }
    
    
}
