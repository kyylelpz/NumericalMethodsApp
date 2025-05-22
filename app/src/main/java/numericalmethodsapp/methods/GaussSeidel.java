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

/**
 *
 * @author lopez
 */
public class GaussSeidel {

    public static Double[] gaussSeidel(double[][] matrix, Double[] currGuess, double tolerance, int decimalPlaces, int iteration, ArrayList<Double[]> iterations, int maxIteration){
        int n = currGuess.length;

        if (iteration > maxIteration){
            System.out.println("Max iteration (" + maxIteration + ") count reached. Iteration stopped.");
            return currGuess;
        }

        if (iteration > 1000) {
            System.out.println("Jacobi method did not converge.");
            return currGuess;
        }

        Double[] nextGuess = Arrays.copyOf(currGuess, n);

        for (int i = 0; i < n; i++) {
            double sum = matrix[i][n]; // RHS
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    sum -= matrix[i][j] * nextGuess[j]; // Use updated value immediately
                }
            }
            nextGuess[i] = Utils.round(sum / matrix[i][i], decimalPlaces);
        }

        iterations.add(Arrays.copyOf(nextGuess, n));

        boolean converged = true;
        for (int i = 0; i < n; i++) {
            if (Math.abs(nextGuess[i] - currGuess[i]) > tolerance) {
                converged = false;
                break;
            }
        }

        if (converged) return nextGuess;

        return gaussSeidel(matrix, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, maxIteration);
    }

    public static String solve(String[] equations, StringBuilder sb, double tolerance, int maxIterations) {
    int numEq = equations.length;

    if (numEq < 2 || numEq > 3) {
        sb.append("Error: Number of linear equations must be 2 or 3.\n");
        return sb.toString();
    }

    for (int i = 0; i < numEq; i++) {
        sb.append("Equation #").append(i + 1).append(": ").append(equations[i]).append("\n");
    }
    sb.append("\n");

    double[][] matrix;
    try {
        matrix = Utils.parseEquation(equations);
    } catch (IllegalArgumentException e) {
        sb.append("Error parsing equations: ").append(e.getMessage()).append("\n");
        return sb.toString();
    }

    sb.append("Parsed Augmented Matrix:\n\n");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                sb.append(String.format("%10.4f ", matrix[i][j]));
            }
            sb.append("\n");
        }
        sb.append("\n");

        // Make matrix diagonally dominant
        matrix = numericalmethodsapp.methods.Jacobi.diagonallyDominant(matrix);

        sb.append("Diagonally Dominant Matrix:\n\n");
        for (int i = 0; i < numEq; i++) {
            for (int j = 0; j < numEq + 1; j++) {
                sb.append(String.format("%10.4f ", matrix[i][j]));
            }
            sb.append("\n");
        }
        sb.append("\n");

        Double[] guess = new Double[numEq];
        Arrays.fill(guess, 0.0);
        ArrayList<Double[]> iterations = new ArrayList<>();
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        Double[] solutions = gaussSeidel(matrix, guess, tolerance, decimalPlaces, 1, iterations, maxIterations);

        for (int i = 0; i < iterations.size(); i++) {
            sb.append("Iteration #").append(i + 1).append(": ")
                    .append(Arrays.toString(iterations.get(i))).append("\n");
        }

        sb.append("\nFinal Approximated Solution: ").append(Arrays.toString(solutions)).append("\n");
        return sb.toString();
    }

}
