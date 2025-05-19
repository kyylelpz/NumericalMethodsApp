/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.utils;

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
}
