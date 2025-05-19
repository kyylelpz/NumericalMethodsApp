/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;
import java.util.Scanner;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import numericalmethodsapp.utils.Utils;
/**
 *
 * @author lopez
 */
public class NewtonRaphson {
    public static void run (Scanner input){
        input.nextLine();

        System.out.print("Enter f(x): ");
        String equation = input.nextLine();

        System.out.print("Enter initial guess: ");
        double guess = input.nextDouble();

        System.out.print("Enter tolerance (1e-3): ");
        double tolerance = input.nextDouble();

        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        String function = equation.split("=")[0].trim();  // Get the expression before '='

        System.out.println(function);

        // Compute the derivative
        ExprEvaluator util = new ExprEvaluator();
        IExpr derivative = util.evaluate("D(" + function + ", x)");
        String derivativeStr = derivative.toString();

        double solution = newtonRaphson(function, derivativeStr, guess, tolerance, decimalPlaces, decimalPlaces);

        System.out.println("The approximated solution is: " + solution);
    }

    public static double newtonRaphson (String function, String derivativeStr, double x, double tolerance, int decimalPlaces, int iteration){
        double fx = Utils.evaluateFunction(function, x, decimalPlaces);
        double fdx = Utils.evaluateFunction(derivativeStr, x, decimalPlaces);

        double nextGuess = x - fx/fdx;
        nextGuess = Utils.round(nextGuess, decimalPlaces);

        System.out.println("Iteration #" + iteration + ": x(n) = " + x + " x(n+1) = " + nextGuess);

        if (Math.abs(nextGuess-x) <= tolerance){
            return nextGuess;
        }

        return newtonRaphson(function, derivativeStr, nextGuess, tolerance, decimalPlaces, iteration+1);
    }

