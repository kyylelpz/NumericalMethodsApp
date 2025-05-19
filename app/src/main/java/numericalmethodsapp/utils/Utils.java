/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.utils;

import org.matheclipse.core.eval.ExprEvaluator;

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

    public static double[][] parseEquation(String[] equations) throws IllegalArgumentException {
        int numEq = equations.length;
        String[] variables = (numEq == 2) ? new String[]{"x", "y"} : new String[]{"x", "y", "z"};
        double[][] matrix = new double[numEq][numEq + 1]; // includes constant column

        for (int i = 0; i < numEq; i++) {
            String equation = equations[i];
            if (!equation.contains("=")) {
                throw new IllegalArgumentException("Equation " + (i + 1) + " is missing '=' sign.");
            }

            String lhs = equation.substring(0, equation.indexOf('=')).trim();
            String rhs = equation.substring(equation.indexOf('=') + 1).trim();

            try {
                for (int v = 0; v < variables.length; v++) {
                    // Set the current variable to 1, others to 0
                    ExpressionBuilder builder = new ExpressionBuilder(lhs).variables(variables);
                    for (String var : variables) {
                        builder = builder.variable(var);
                    }

                    var expr = builder.build();

                    for (String var : variables) {
                        expr.setVariable(var, 0);
                    }
                    expr.setVariable(variables[v], 1);

                    matrix[i][v] = expr.evaluate();
                }

                // Parse RHS constant
                matrix[i][numEq] = Double.parseDouble(rhs);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid expression in equation " + (i + 1) + ": " + e.getMessage());
            }
        }

        return matrix;
    }

    public static String convertExprToSymjaCompatible(String expr) {
        return expr
            .replace("X", "x")
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
            .replace("X", "x")
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

    public static boolean isValidSymjaExpression(String expr) {
    try {
        ExprEvaluator evaluator = new ExprEvaluator();
        evaluator.evaluate(expr);
        return true;
    } catch (Exception e) {
        return false;
    }
}
}
