/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;


import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class CramersRule {
    public static void run(Scanner input){
        int numEq = 0;

        while(true){
            try {
                System.out.print("Enter number of linear equations (2 or 3 only): ");
                numEq = input.nextInt();

                if (numEq < 2 || numEq > 3) {
                    System.out.println("Error: Number of linear equations must be 2 or 3.");
                    continue;
                }

                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer (2 or 3).");
                input.nextLine();
            }
        }

        String[] equations = new String[numEq];
        input.nextLine();

        System.out.println("Enter each equation (e.g., 3x + 2y - z = 4):");
        for (int i = 0; i < numEq; i++) {
            System.out.print("Equation " + (i + 1) + ": ");
            equations[i] = input.nextLine();
        }

        double[][] matrix;
        
        try {
            matrix = Utils.parseEquation(equations);
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing equations: " + e.getMessage());
            return;
        }

        // Display matrix (for testing)
        System.out.println("\nParsed Matrix:");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }

        double[] answer = null;

        // Main Cramer's Rule Algorithm
        if(matrix.length == 2){
            answer = cramer2(matrix);
        }
        else if (matrix.length == 3){
            answer = cramer3(matrix);
        }

        if (answer != null) {
            System.out.println("Solution: " + Arrays.toString(answer));
        } else {
            System.out.println("The system has no unique solution (determinant is zero).");
        }
    }

    public static double[] cramer3(double[][] matrix){
        double d = determinant3x3(matrix[0][0], matrix[0][1], matrix[0][2],
                                  matrix[1][0], matrix[1][1], matrix[1][2],
                                  matrix[2][0], matrix[2][1], matrix[2][2]);

        if (Math.abs(d) < 1e-9) { // Determinant is zero
            throw new ArithmeticException("Determinant is zero. System has no unique solution.");
        }

        double dx = determinant3x3(matrix[0][3], matrix[0][1], matrix[0][2],
                                   matrix[1][3], matrix[1][1], matrix[1][2],
                                   matrix[2][3], matrix[2][1], matrix[2][2]);

        double dy = determinant3x3(matrix[0][0], matrix[0][3], matrix[0][2],
                                   matrix[1][0], matrix[1][3], matrix[1][2],
                                   matrix[2][0], matrix[2][3], matrix[2][2]);

        double dz = determinant3x3(matrix[0][0], matrix[0][1], matrix[0][3],
                                   matrix[1][0], matrix[1][1], matrix[1][3],
                                   matrix[2][0], matrix[2][1], matrix[2][3]);

        double x = dx / d;
        double y = dy / d;
        double z = dz / d;
        
        return new double[]{cleanZero(x), cleanZero(y), cleanZero(z)};
    }

    private static double determinant3x3(double a, double b, double c,
                                         double d, double e, double f,
                                         double g, double h, double i) {
        return a * (e * i - f * h)
             - b * (d * i - f * g)
             + c * (d * h - e * g);
    }

    public static double[] cramer2(double[][] matrix) {
        double a1 = matrix[0][0], b1 = matrix[0][1], c1 = matrix[0][2];
        double a2 = matrix[1][0], b2 = matrix[1][1], c2 = matrix[1][2];

        double D  = a1 * b2 - a2 * b1;
        double Dx = c1 * b2 - c2 * b1;
        double Dy = a1 * c2 - a2 * c1;

        if (Math.abs(D) < 1e-9) { // Determinant is zero
            throw new ArithmeticException("Determinant is zero. System has no unique solution.");
        }

        double x = Dx / D;
        double y = Dy / D;

        return new double[] { cleanZero(x), cleanZero(y) };
    }

    public static double cleanZero(double val) {
        return val == -0.0 ? 0.0 : val;
    }

    public static String solve(String[] equations) {
        StringBuilder output = new StringBuilder();

        int numEq = equations.length;
        if (numEq < 2 || numEq > 3) {
            return "Error: Number of linear equations must be 2 or 3.";
        }

        output.append("Solving system with ").append(numEq).append(" equations...\n\n");

        double[][] matrix;
        try {
            matrix = Utils.parseEquation(equations);
        } catch (IllegalArgumentException e) {
            return "Error parsing equations: " + e.getMessage();
        }

        output.append("Parsed augmented matrix:\n");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                output.append(String.format("%10.4f", matrix[i][j])).append(" ");
            }
            output.append("\n");
        }
        output.append("\n");

        double[] solution;
        try {
            if (numEq == 2) {
                solution = cramer2(matrix);
            } else { // numEq == 3
                solution = cramer3(matrix);
            }
        } catch (ArithmeticException ex) {
            return "No unique solution: " + ex.getMessage();
        }

        output.append("Solution:\n");
        if (numEq == 2) {
            output.append("x = ").append(solution[0]).append("\n");
            output.append("y = ").append(solution[1]).append("\n");
        } else {
            output.append("x = ").append(solution[0]).append("\n");
            output.append("y = ").append(solution[1]).append("\n");
            output.append("z = ").append(solution[2]).append("\n");
        }

        return output.toString();
    }
}
