/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.ArrayList;
import java.util.Arrays;

import numericalmethodsapp.utils.Utils;

//import java.util.Scanner;

/**
 *
 * @author lopez
 */
public class Jacobi {

    public static Double[] jacobi(double[][] matrix, Double[] currGuess, double tolerance,
                              int decimalPlaces, int iteration, ArrayList<Double[]> iterations,
                              int maxIteration, StringBuilder sb) {
        int n = currGuess.length;

        if (iteration > maxIteration){
            sb.append("Max iteration (").append(maxIteration).append(") reached. Iteration stopped.\n");
            return currGuess;
        }

        if (iteration > 1000) {
            sb.append("Jacobi method did not converge within 1000 iterations.\n");
            return currGuess;
        }

        Double[] nextGuess = new Double[n];
        sb.append("Iteration ").append(iteration).append(":\n");

        for (int i = 0; i < n; i++) {
            double sum = matrix[i][n]; // RHS constant
            sb.append("  x").append(i + 1).append(" = (").append(sum);

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    sum -= matrix[i][j] * currGuess[j];
                    sb.append(" - ").append(matrix[i][j])
                    .append("*").append(currGuess[j]);
                }
            }

            double xi = sum / matrix[i][i];
            nextGuess[i] = Utils.round(xi, decimalPlaces);

            sb.append(") / ").append(matrix[i][i])
            .append(" = ").append(nextGuess[i]).append("\n");
        }

        iterations.add(nextGuess);

        boolean converged = true;

        for (int i = 0; i < n; i++) {
            double diff = Math.abs(nextGuess[i] - currGuess[i]);
            sb.append("  |x").append(i + 1).append(" (new - old)| = ")
            .append(String.format("%.6f", diff))
            .append(diff <= tolerance ? " <= " : " > ")
            .append("tolerance (").append(tolerance).append(")\n");
            if (diff > tolerance) {
                converged = false;
            }
        }

        sb.append("\n");

        if (converged) {
            sb.append("Convergence achieved in ").append(iteration).append(" iterations.\n");
            return nextGuess;
        }

        return jacobi(matrix, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, maxIteration, sb);
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

        // Make matrix diagonally dominant if possible
        double[][] diagMatrix = diagonallyDominant(matrix);
        if (diagMatrix != matrix) {
            sb.append("Diagonally Dominant Matrix:\n\n");
            for (int i = 0; i < numEq; i++) {
                for (int j = 0; j < numEq + 1; j++) {
                    sb.append(String.format("%10.4f ", diagMatrix[i][j]));
                }
                sb.append("\n");
            }
            sb.append("\n");
        } else {
            sb.append("Warning: Matrix was NOT rearranged to be diagonally dominant.\nIterations may not converge.\n\n");
        }

        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        Double[] initialGuess = new Double[numEq];
        Arrays.fill(initialGuess, 0.0);

        ArrayList<Double[]> iterations = new ArrayList<>();

        Double[] solutions = jacobi(diagMatrix, initialGuess, tolerance, decimalPlaces, 1, iterations, maxIterations, sb);

        sb.append("Jacobi Iterations:\n");
        for (int i = 0; i < iterations.size(); i++) {
            sb.append("Iteration ").append(i + 1).append(": ")
            .append(Arrays.toString(iterations.get(i))).append("\n");
        }
        sb.append("\nFinal Approximation:\n").append(Arrays.toString(solutions)).append("\n");

        return sb.toString();
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
}
