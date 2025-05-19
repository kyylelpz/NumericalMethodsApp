/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.Scanner;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class Secant {
    public static void run (Scanner input){
        //Enter equation
        System.out.print("Enter equation: ");
        input.nextLine();
        String exp = input.nextLine();

        //clear string********
        
        //Enter initial guesses
        System.out.print("Enter x0: ");
        double x0 = input.nextDouble();

        System.out.print("Enter x1: ");
        double x1 = input.nextDouble();
        
        //Enter tolerance
        System.out.print("Enter tolerance (1e-3): ");
        double tolerance = input.nextDouble();

        //Get decimal places
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        System.out.println(decimalPlaces);
        
        try {
            double root = secant(exp, x0, x1, tolerance, decimalPlaces, 1);
            System.out.printf("Approximate root: %.6f%n", root);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static double secant (String function, double a, double b, double tolerance, int decimalPlaces, int iteration) {
        //Pause 1 second
        try {
            Thread.sleep(250); // wait 1 second
        }
            
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Evaluate functions
        double fa = evaluateFunction(function, a, tolerance, decimalPlaces);
        //System.out.println("Function of a: " + fa);
        double fb = evaluateFunction(function, b, tolerance, decimalPlaces);
        //System.out.println("Function of b: " + fb);

        if (Double.isNaN(fa) || Double.isNaN(fb)) {
            throw new IllegalArgumentException("Function evaluation returned NaN at the initial points.");
        }

        //INSERT TRY FOR DIVISION BY ZERO ***************
        double c = b - fb * (b-a) / (fb-fa);
        c = Utils.round(c, decimalPlaces);

        double fc = evaluateFunction(function, c, tolerance, decimalPlaces);

        if (Double.isNaN(fc)) {
            throw new IllegalArgumentException("Function evaluation returned NaN at x(n+1).");
        }

        //CHANGE THE FORMAT
        System.out.printf("Iteration %d: a=%.6f, b=%.6f, c=%.6f, f(c)=%.6f%n", iteration, a, b, c, fc);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(c-b) < tolerance) {
            return c;
        }

        if (c == a || c == b){
            return c;
        }

        // Recursive step
        return secant(function, b, c, tolerance, decimalPlaces, iteration + 1);
    }

    public static double evaluateFunction(String function, double x, double tolerance, int decimalPlaces) {
        Expression expr = new ExpressionBuilder(function)
                .variable("x")
                .build()
                .setVariable("x", x);

        double result = expr.evaluate();
        return Utils.round(result, decimalPlaces);
    }
}
