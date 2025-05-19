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
public class GaussSeidel {
    public static void run(Scanner input){
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

        System.out.println("Enter tolerance (1e-3): ");
        double tolerance = input.nextDouble();

        //Get decimal places
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);
        System.out.println(decimalPlaces);

        double[][] matrix = Utils.parseEquation(equations);

        // Display matrix (for testing)
        System.out.println("\nParsed Matrix:");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }

        // Start Jacobi Method
        // check if equations are diagonally dominant
        matrix = diagonallyDominant(matrix);

        // Display matrix (for testing)
        System.out.println("\nDiagonally Dominant Matrix:");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }

        double[] initialGuess = {0,0,0};

        double[] solutions = gaussSeidel(matrix, initialGuess, tolerance, decimalPlaces, 1);

        System.out.println("The approximated solutions are: " + Arrays.toString(solutions));
        
    }

    public static double[][] diagonallyDominant (double[][] matrix){
        //boolean row1 = true;
        //boolean row2 = true;
        //boolean row3 = true;

        //check row1
        if (!(matrix[0][0] > Math.abs(matrix[0][1]) + Math.abs(matrix[0][2]))){
            //row1 = false;
            double[] temp = matrix[0];
            if (matrix[1][0] > Math.abs(matrix[1][1]) + Math.abs(matrix[1][2])){
                matrix[0] = matrix[1];
                matrix[1] = temp;
                //row1 = true;
            }
            else if (matrix[2][0] > Math.abs(matrix[2][1]) + Math.abs(matrix[2][2])){
                matrix[0] = matrix[2];
                matrix[2] = temp;
                //row1 = true;
            }
        }

        //check row2
        if (!(matrix[1][1] > Math.abs(matrix[1][0]) + Math.abs(matrix[1][2]))){
            //row2 = false;
            double[] temp = matrix[1];
            if (matrix[0][1] > Math.abs(matrix[0][0]) + Math.abs(matrix[0][2])){
                matrix[1] = matrix[0];
                matrix[0] = temp;
                //row2 = true;
            }
            else if (matrix[2][1] > Math.abs(matrix[2][0]) + Math.abs(matrix[2][2])){
                matrix[1] = matrix[2];
                matrix[2] = temp;
                //row2 = true;
            }
        }

        //check row3
        if (!(matrix[2][2] > Math.abs(matrix[2][0]) + Math.abs(matrix[2][1]))){
            //row3 = false;
            double[] temp = matrix[2];
            if (matrix[0][2] > Math.abs(matrix[0][0]) + Math.abs(matrix[0][1])){
                matrix[2] = matrix[0];
                matrix[0] = temp;
                //row3 = true;
            }
            else if (matrix[1][2] > Math.abs(matrix[1][0]) + Math.abs(matrix[1][1])){
                matrix[2] = matrix[1];
                matrix[1] = temp;
                //row3 = true;
            }
        }

        return matrix;
    }

    public static double[] gaussSeidel(double[][] matrix, double[] currGuess, double tolerance, int decimalPlaces, int iteration){
        
        double[] nextGuess = Arrays.copyOf(currGuess, currGuess.length);

        nextGuess[0] = (-matrix[0][1]*nextGuess[1] - matrix[0][2]*nextGuess[2] + matrix[0][3]) / matrix[0][0];
        nextGuess[0] = Utils.round(nextGuess[0], decimalPlaces);

        nextGuess[1] = (-matrix[1][0]*nextGuess[0] - matrix[1][2]*nextGuess[2] + matrix[1][3]) / matrix[1][1];
        nextGuess[1] = Utils.round(nextGuess[1], decimalPlaces);

        nextGuess[2] = (-matrix[2][0]*nextGuess[0] - matrix[2][1]*nextGuess[1] + matrix[2][3]) / matrix[2][2];
        nextGuess[2] = Utils.round(nextGuess[2], decimalPlaces);
        
        System.out.println("Iteration #" + iteration + ": " + Arrays.toString(nextGuess));

        if (iteration >= 1000) {
            System.out.println("Jacobi method did not converge.");
            return nextGuess;
        }

        if (Math.abs(nextGuess[0] - currGuess[0]) <= tolerance &&
            Math.abs(nextGuess[1] - currGuess[1]) <= tolerance &&
            Math.abs(nextGuess[2] - currGuess[2]) <= tolerance){
            return nextGuess;
        }
        return gaussSeidel(matrix, nextGuess, tolerance, decimalPlaces, iteration+1);
    }
}
