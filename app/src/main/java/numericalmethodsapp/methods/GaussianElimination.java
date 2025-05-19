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
public class GaussianElimination {
    public static void run (Scanner input){
        int numEq = 0;

        while (true){
            try {
                System.out.print("Enter number of linear equations (2 or 3): ");
                numEq = input.nextInt();

                if (numEq < 2 || numEq > 3) {
                    System.out.println("Error: Number of linear equations should be 2 or 3.");
                    continue;
                }
                
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer.");
                input.nextLine(); // clear buffer
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

        // Gaussian Elimination
        double[] solution = gaussianElimination(matrix);

        System.out.println("The solutions are: " + Arrays.toString(solution));
    }

    public static double[] gaussianElimination (double[][] matrix){
        int n = matrix.length;
        
        for(int i = 0; i < n; i++){
            // Partial pivoting
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }

            if (maxRow != i) {
                double[] temp = matrix[i];
                matrix[i] = matrix[maxRow];
                matrix[maxRow] = temp;
            }

            // Check for 0 or very small pivot value
            if (Math.abs(matrix[i][i]) < 1e-12) {
                throw new ArithmeticException("Zero pivot encountered, system may have no unique solution.");
            }

            // Eliminate elements below diagonal
            for (int j = i + 1; j < matrix.length; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= matrix.length; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
            
        }

        // Back Substitution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
            }
            if (Math.abs(matrix[i][i]) < 1e-12) {
                throw new ArithmeticException("Division by zero during back substitution.");
            }
            solution[i] = (matrix[i][matrix.length] - sum) / matrix[i][i];
        }

        return solution;
    }
}
