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

        int maxIteration;

        while (true) {
            try {
                System.out.print("Enter max iteration (min: 2): ");
                maxIteration = input.nextInt();
                if (maxIteration < 2) throw new IllegalArgumentException("Max iteration count must be at least 2.");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
                input.nextLine();  // Clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }

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

        Double[] initialGuess = new Double[numEq];
        Arrays.fill(initialGuess, 0.0);

        ArrayList<Double[]> iterations = new ArrayList<>();

        Double[] solutions = jacobi(matrix, initialGuess, tolerance, decimalPlaces, 1, iterations, maxIteration);

        for (int i = 0; i < iterations.size(); i++){
            System.out.println("Iteration #" + (i+1) + ": " + Arrays.toString(iterations.get(i)));
        }
        System.out.println("The approximated solutions are: " + Arrays.toString(solutions));
    }

    public static double[][] diagonallyDominant (double[][] matrix){
        int n = matrix.length;
        boolean[] used = new boolean[n];
        double[][] newMatrix = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < n; j++) {
                if (used[j]) continue;
                double diag = Math.abs(matrix[j][i]);
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    if (k != i) sum += Math.abs(matrix[j][k]);
                }
                if (diag >= sum) {
                    newMatrix[i] = matrix[j];
                    used[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Warning: Could not make matrix diagonally dominant. Results may not converge.");
                return matrix;
            }
        }

        return newMatrix;
    }

    public static Double[] jacobi(double[][] matrix, Double[] currGuess, double tolerance, int decimalPlaces, int iteration, ArrayList<Double[]> iterations, int maxIteration){
        int n = currGuess.length;
        
        if (iteration > maxIteration){
            System.out.println("Max iteration (" + maxIteration + ") count reached. Iteration stopped.");
            return currGuess;
        }

        Double nextGuess[] = new Double[n];

        for (int i = 0; i < n; i++) {
            double sum = matrix[i][n]; // RHS value
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sum -= matrix[i][j] * currGuess[j];
                    }
                }
            nextGuess[i] = Utils.round(sum / matrix[i][i], decimalPlaces);
        }
        
        iterations.add(nextGuess);

        if (iteration >= 1000) {
            System.out.println("Jacobi method did not converge.");
            return nextGuess;
        }

        boolean converged = true;

        for (int i = 0; i < n; i++) {
        if (Math.abs(nextGuess[i] - currGuess[i]) > tolerance) {
            converged = false;
            break;
            }
        }

        if (converged) return nextGuess;

        return jacobi(matrix, nextGuess, tolerance, decimalPlaces, iteration+1, iterations, maxIteration);
    }


}
