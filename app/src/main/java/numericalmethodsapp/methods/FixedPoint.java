/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        System.out.print("Enter g(x): ");
        String gofx = input.nextLine();

        gofx = Utils.convertExprToSymjaCompatible(gofx);

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
        ArrayList<Double> iterations = new ArrayList<>();
        Double solution = fixedPoint(gofx, iGuess, tolerance, decimalPlaces, 1, iterations);

        // Print results
        System.out.println("\nIterations:");
        System.out.println();

        for (int i = 0; i < iterations.size(); i++) {
            System.out.println("Iteration #" +  (i + 1) + ": " + "x =\t" + iterations.get(i));
        }

        System.out.println();

        if (solution == null) {
            System.out.println("Method diverged. No approximate solution found.");
        } else {
            System.out.printf("The approximate solution is: " + solution);
}
    }

    public static Double fixedPoint (String gofx, double currGuess, double tolerance, int decimalPlaces, int iteration, ArrayList<Double> iterations){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            return null;
        }
        
        double nextGuess = Utils.evaluateFunction(gofx, currGuess, decimalPlaces);

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            return null;
        }

        iterations.add(nextGuess);

        if (Math.abs(nextGuess - currGuess) <= tolerance) {
            return nextGuess;
        }

        return fixedPoint(gofx, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations);
    }
}
