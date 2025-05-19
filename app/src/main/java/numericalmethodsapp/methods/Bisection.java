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
public class Bisection {
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
            double root = bisection(exp, x0, x1, tolerance, decimalPlaces, 1);
            System.out.printf("Approximate root: %.6f%n", root);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static double bisection (String function, double a, double b, double tolerance, int decimalPlaces, int iteration) {
        //Pause 1 second
        try {
            Thread.sleep(250); // wait 1 second
        }
            
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        double mp = (a + b) / 2;
        mp = Utils.round(mp, decimalPlaces);

        
        //Evaluate functions
        double fa = evaluateFunction(function, a, tolerance, decimalPlaces);
        //System.out.println("Function of a: " + fa);
        double fb = evaluateFunction(function, b, tolerance, decimalPlaces);
        //System.out.println("Function of b: " + fb);
        double fmp = evaluateFunction(function, mp, tolerance, decimalPlaces);


        if (Double.isNaN(fa) || Double.isNaN(fb)) {
            throw new IllegalArgumentException("Function evaluation returned NaN at the initial points.");
        }

        //CHECK LATER
        
        if (fa * fb >= 0) {
            throw new IllegalArgumentException("f(a) and f(b) must have opposite signs.");
        }

        if (Double.isNaN(fmp)) {
            throw new IllegalArgumentException("Function evaluation returned NaN at midpoint.");
        }

        System.out.printf("Iteration %d: a=%.6f, b=%.6f, mp=%.6f, f(mp)=%.6f%n", iteration, a, b, mp, fmp);

        // Base case: If the root is found or maximum iterations reached
        if (Math.abs(fmp) < tolerance) {
            return mp;
        }

        if (mp == a || mp == b){
            return mp;
        }

        // Recursive step
        if (fa * fmp < 0) {
            return bisection(function, a, mp, tolerance, decimalPlaces, iteration + 1);
        } else {
            return bisection(function, mp, b, tolerance, decimalPlaces, iteration + 1);
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
