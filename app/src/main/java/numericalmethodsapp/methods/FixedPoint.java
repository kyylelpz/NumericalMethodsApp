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
public class FixedPoint {
    public static void run (Scanner input){
         //Enter equation
        System.out.print("Enter equation: ");
        input.nextLine();
        String equation = input.nextLine();

        System.out.print("Enter g(x): ");
        String gofx = input.nextLine();

        System.out.print("Enter tolerance: ");
        double tolerance = input.nextDouble();

        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        ExprEvaluator util = new ExprEvaluator();
        IExpr dgofx = util.evaluate("D(" + gofx + ", x)");
        String dgofxStr = dgofx.toString();

        System.out.println(dgofxStr);

        dgofxStr = dgofxStr.replace("E^x", "exp(x)");
        System.out.println(dgofxStr);

        double iGuess;
        do{
            System.out.print("Enter intial guess: ");
            iGuess = input.nextDouble();
        } while (evaluateFunction(dgofxStr, iGuess, tolerance, decimalPlaces) >= 1);

        if (evaluateFunction(dgofxStr, iGuess, tolerance, decimalPlaces) < 1){
            fixedPoint(dgofxStr, iGuess, tolerance, decimalPlaces, 1);
        }
    }

    public static double fixedPoint (String derivativeStr, double currGuess, double tolerance, int decimalPlaces, int iteration){
        double nextGuess = evaluateFunction(derivativeStr, currGuess, tolerance, decimalPlaces);

        if (Math.abs(nextGuess-currGuess) <= tolerance){
            return nextGuess;
        }

        return fixedPoint(derivativeStr, nextGuess, tolerance, decimalPlaces, iteration);
    }

    public static double evaluateFunction (String function, double x, double tolerance, int decimalPlaces) {
        
    }

    
}
