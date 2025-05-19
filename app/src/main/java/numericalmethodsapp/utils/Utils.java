/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author lopez
 */
public class Utils {
    public static int getDecimalPlacesFromTolerance(double tolerance) {
        // Get the number of decimal places by determining the magnitude of tolerance
        String[] parts = String.format("%.10f", tolerance).split("\\.");
        String decimalPart = parts.length > 1 ? parts[1] : "";
        // Trim trailing zeros
        decimalPart = decimalPart.replaceAll("0+$", "");
        return decimalPart.length();
    }
    
    public static double round(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    public static double[][] parseEquation(String[] equations){
        double[][] matrix = new double[3][4];
        for(int i = 0; i < equations.length; i++){
            String[] parsedEquation = new String[2];
            equations[i].indexOf('=');
            parsedEquation[0] = equations[i].substring(0, equations[i].indexOf('=')).trim();
            parsedEquation[1] = equations[i].substring(equations[i].indexOf('=') + 1, equations[i].length()).trim();
            
            matrix[i][0] = new ExpressionBuilder(parsedEquation[0])
                                .variables("x", "y", "z")
                                .build()
                                .setVariable("x", 1)
                                .setVariable("y", 0)
                                .setVariable("z", 0)
                                .evaluate();
            matrix[i][1] = new ExpressionBuilder(parsedEquation[0])
                                .variables("x", "y", "z")
                                .build()
                                .setVariable("x", 0)
                                .setVariable("y", 1)
                                .setVariable("z", 0)
                                .evaluate();
            matrix[i][2] = new ExpressionBuilder(parsedEquation[0])
                                .variables("x", "y", "z")
                                .build()
                                .setVariable("x", 0)
                                .setVariable("y", 0)
                                .setVariable("z", 1)
                                .evaluate();
            matrix[i][3] = Double.parseDouble(parsedEquation[1]);                  
        }
        return matrix;

    }

    public static String convertExprToSymjaCompatible(String expr) {
        return expr
            .replace("exp(", "Exp(")
            .replace("log(", "Log(")
            .replace("ln(", "Log(")
            .replace("sin(", "Sin(")
            .replace("cos(", "Cos(")
            .replace("tan(", "Tan(")
            .replace("1/tan(", "Cot(")
            .replace("1/sin(", "Csc(")
            .replace("1/cos(", "Sec(")
            .replace("abs(", "Abs(")
            .replace("sqrt(", "Sqrt(")
            .replace(" ", "");
        }

    public static String convertExprToExp4jCompatible(String expr) {
    
        expr = expr.replaceAll("E\\^([^\\s\\)]+)", "exp($1)");
        expr = expr.replaceAll("e\\^([^\\s\\)]+)", "exp($1)");
        expr = expr.replaceAll("(?i)exp\\(", "exp(");
        
        return expr
            .replace("ln(", "log(")
            .replace("Ln(", "log(")
            .replace("Log(", "log(")
            .replace("Sin(", "sin(")
            .replace("Cos(", "cos(")
            .replace("Tan(", "tan(")
            .replace("Cot(", "1/tan(")
            .replace("Csc(", "1/sin(")
            .replace("Sec(", "1/cos(")
            .replace("Abs(", "abs(")
            .replace("Sqrt(", "sqrt(")
            .replace(" ", "");
    }

    public static double evaluateFunction(String function, double x, int decimalPlaces) {
        try {
            Expression expr = new ExpressionBuilder(function)
                    .variable("x")
                    .build()
                    .setVariable("x", x);

            double result = expr.evaluate();
            return Utils.round(result, decimalPlaces);
        } catch (ArithmeticException e) {
            System.out.println("Math Error: " + e.getMessage());
            System.exit(1);
            return Double.NaN;
        } catch (Exception e) {
            System.out.println("Invalid expression: " + e.getMessage());
            System.exit(1);
            return Double.NaN;
        }
    }
}
