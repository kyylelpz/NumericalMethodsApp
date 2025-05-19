/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;

//import java.util.Scanner;

/**
 *
 * @author lopez
 */
public class Jacobi {
    public static void run(Scanner input){
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

        //Enter tolerance
        double tolerance = 0.001;
        while (true) {
            try {
                System.out.print("Enter tolerance: ");
                tolerance = input.nextDouble();
                if (tolerance <= 0) throw new IllegalArgumentException("Tolerance must be positive.");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                input.nextLine();  // Clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }

        //Get decimal places
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        double[][] matrix;

        try {
            matrix = Utils.parseEquation(equations);

            if (matrix.length != numEq || matrix[0].length != numEq + 1) {
                System.out.println("Error: Invalid matrix size after parsing equations.");
                return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to parse equations. Make sure the format is correct.");
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

        Double[] initialGuess = {0.0,0.0,0.0};
        ArrayList<Double[]> iterations = new ArrayList<>();

        Double[] solutions = jacobi(matrix, initialGuess, tolerance, decimalPlaces, 1, iterations);

        for (int i = 0; i < iterations.size(); i++){
            System.out.println("Iteration #" + (i+1) + ": " + Arrays.toString(iterations.get(i)));
        }
        System.out.println("The approximated solutions are: " + Arrays.toString(solutions));
    }

    public static double[][] diagonallyDominant (double[][] matrix){
        //check row1
        if (!(matrix[0][0] > Math.abs(matrix[0][1]) + Math.abs(matrix[0][2]))){
            double[] temp = matrix[0];
            if (matrix[1][0] > Math.abs(matrix[1][1]) + Math.abs(matrix[1][2])){
                matrix[0] = matrix[1];
                matrix[1] = temp;
            }
            else if (matrix[2][0] > Math.abs(matrix[2][1]) + Math.abs(matrix[2][2])){
                matrix[0] = matrix[2];
                matrix[2] = temp;
            }
        }

        //check row2
        if (!(matrix[1][1] > Math.abs(matrix[1][0]) + Math.abs(matrix[1][2]))){
            double[] temp = matrix[1];
            if (matrix[0][1] > Math.abs(matrix[0][0]) + Math.abs(matrix[0][2])){
                matrix[1] = matrix[0];
                matrix[0] = temp;
            }
            else if (matrix[2][1] > Math.abs(matrix[2][0]) + Math.abs(matrix[2][2])){
                matrix[1] = matrix[2];
                matrix[2] = temp;
            }
        }

        //check row3
        if (!(matrix[2][2] > Math.abs(matrix[2][0]) + Math.abs(matrix[2][1]))){
            double[] temp = matrix[2];
            if (matrix[0][2] > Math.abs(matrix[0][0]) + Math.abs(matrix[0][1])){
                matrix[2] = matrix[0];
                matrix[0] = temp;
            }
            else if (matrix[1][2] > Math.abs(matrix[1][0]) + Math.abs(matrix[1][1])){
                matrix[2] = matrix[1];
                matrix[1] = temp;
            }
        }

        return matrix;
    }

    public static Double[] jacobi(double[][] matrix, Double[] currGuess, double tolerance, int decimalPlaces, int iteration, ArrayList<Double[]> iterations){
        
        Double nextGuess[] = new Double[3];

        nextGuess[0] = (-matrix[0][1]*currGuess[1] - matrix[0][2]*currGuess[2] + matrix[0][3]) / matrix[0][0];
        nextGuess[0] = Utils.round(nextGuess[0], decimalPlaces);

        nextGuess[1] = (-matrix[1][0]*currGuess[0] - matrix[1][2]*currGuess[2] + matrix[1][3]) / matrix[1][1];
        nextGuess[1] = Utils.round(nextGuess[1], decimalPlaces);

        nextGuess[2] = (-matrix[2][0]*currGuess[0] - matrix[2][1]*currGuess[1] + matrix[2][3]) / matrix[2][2];
        nextGuess[2] = Utils.round(nextGuess[2], decimalPlaces);
        
        //System.out.println("Iteration #" + iteration + ": " + Arrays.toString(nextGuess));
        iterations.add(nextGuess);

        if (iteration >= 1000) {
            System.out.println("Jacobi method did not converge.");
            return nextGuess;
        }

        if (Math.abs(nextGuess[0] - currGuess[0]) <= tolerance &&
            Math.abs(nextGuess[1] - currGuess[1]) <= tolerance &&
            Math.abs(nextGuess[2] - currGuess[2]) <= tolerance){
            return nextGuess;
        }
        return jacobi(matrix, nextGuess, tolerance, decimalPlaces, iteration+1, iterations);
    }


}
