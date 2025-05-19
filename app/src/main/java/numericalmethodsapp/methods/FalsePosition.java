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
public class FalsePosition {
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
            double root = falsePosition(exp, x0, x1, tolerance, decimalPlaces, 1);
            System.out.printf("Approximate root: %.6f%n", root);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //False Position Start/Recursive
    public static double falsePosition(String function, double a, double b, double tolerance, int decimalPlaces, int iteration) {
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
        
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("f(a) and f(b) must have opposite signs.");
        }

        // Compute c using Regula Falsi formula
        double c = a + (((b-a)*(-fa))/(fb-fa));
        c = Utils.round(c, decimalPlaces);

        //System.out.println("c: " + c);
        double fc = evaluateFunction(function, c, tolerance, decimalPlaces);
        //System.out.println("Function of c: " + fc);

        if (Double.isNaN(fc)) {
            throw new IllegalArgumentException("Function evaluation returned NaN at c.");
        }

        System.out.printf("Iteration %d: a=%.6f, b=%.6f, c=%.6f, f(c)=%.6f%n", iteration, a, b, c, fc);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(fc) < tolerance) {
            return c;
        }

        if (c == a || c == b){
            return c;
        }

        // Recursive step
        if (fa * fc < 0) {
            return falsePosition(function, a, c, tolerance, decimalPlaces, iteration + 1);
        } else {
            return falsePosition(function, c, b, tolerance, decimalPlaces, iteration + 1);
        }

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
