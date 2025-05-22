/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.utils;

import java.util.HashSet;
import java.util.Set;

import org.matheclipse.core.eval.ExprEvaluator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author lopez
 */
public class Utils {
    public static int getDecimalPlacesFromTolerance(double tolerance) {
        
        String[] parts = String.format("%.10f", tolerance).split("\\.");
        String decimalPart = parts.length > 1 ? parts[1] : "";
        
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
        double[][] matrix = new double[numEq][numEq + 1];

        for (int i = 0; i < numEq; i++) {
            String equation = equations[i];
            if (!equation.contains("=")) {
                throw new IllegalArgumentException("Equation " + (i + 1) + " is missing '=' sign.");
            }

            String lhs = equation.substring(0, equation.indexOf('=')).trim();
            String rhs = equation.substring(equation.indexOf('=') + 1).trim();

            try {
                for (int v = 0; v < variables.length; v++) {
                    
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
            throw new IllegalArgumentException("Math error during evaluation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression during evaluation: " + e.getMessage(), e);
        }
    }

    public static double evaluateFunction(String function, double x, int decimalPlaces, char variable) {
        try {
            Expression expr = new ExpressionBuilder(function)
                    .variable(Character.toString(variable))
                    .build()
                    .setVariable(Character.toString(variable), x);

            double result = expr.evaluate();
            return Utils.round(result, decimalPlaces);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Math error during evaluation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression during evaluation: " + e.getMessage(), e);
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

    public static Set<Character> extractVariables(String expression) {
    Set<Character> variables = new HashSet<>();
    final Set<String> FUNCTIONS = Set.of("sin", "cos", "tan", "log", "ln", "sqrt", "exp");

    int n = expression.length();
    
    for (int i = 0; i < n; i++) {
        char c = expression.charAt(i);

        // Case 1: Check for variable in the form (x)
        if (c == '(' && i + 2 < n && Character.isLetter(expression.charAt(i + 1)) && expression.charAt(i + 2) == ')') {
            variables.add(expression.charAt(i + 1));
            i += 2;
        }

        // Case 2: Check for function name (sin, cos, etc.)
        else if (Character.isLetter(c)) {
            StringBuilder word = new StringBuilder();
            int j = i;

            while (j < n && Character.isLetter(expression.charAt(j))) {
                word.append(expression.charAt(j));
                j++;
            }

            String wordStr = word.toString();

            if (FUNCTIONS.contains(wordStr)) {
                i = j - 1; // Skip over the function
            } else if (wordStr.length() == 1 && wordStr.charAt(0) != 'e') {
                variables.add(wordStr.charAt(0)); // Single-letter variable (not 'e')
            }

            i = j - 1; // Move to last char of the word
        }
    }

    return variables;
}


}
