/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.Arrays;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class GaussianElimination {
    public static void run (Scanner input){
        //pwede tong naka increment na number pero max ay 3 equations AHHAHAHAHA
        System.out.println("Enter number of linear equations: ");
        int numEq = input.nextInt();

        if (numEq > 3 || numEq < 2){
            System.out.println("Number of linear equations should be at least 2 or not more than 3.");
            return;
        }

        String[] equations = new String[numEq];
        input.nextLine();

        System.out.println("Enter each equation (e.g., 3x + 2y - z = 4):");
        for (int i = 0; i < numEq; i++) {
            System.out.print("Equation " + (i + 1) + ": ");
            equations[i] = input.nextLine();
        }

        double[][] matrix = Utils.parseEquation(equations);

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

        for(int i = 0; i < matrix.length; i++){
            for(int j = i + 1; j < matrix.length; j++){
                if(Math.abs(matrix[j][i]) > Math.abs(matrix[i][i])){
                    double[] temp = matrix[i];
                    matrix[i] = matrix[j];
                    matrix[j] = temp;
                }
            }

            for (int j = i + 1; j < matrix.length; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= matrix.length; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
            
        }

        // Back Substitution
        double[] solution = new double[matrix.length];
        for (int i = matrix.length - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < matrix.length; j++) {
                sum += matrix[i][j] * solution[j];
            }
            solution[i] = (matrix[i][matrix.length] - sum) / matrix[i][i];
        }

        return solution;
    }
}
