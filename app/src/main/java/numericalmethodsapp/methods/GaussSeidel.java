/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.ArrayList;
import java.util.Arrays;

import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class GaussSeidel {

    public static Double[] gaussSeidel(double[][] matrix, Double[] currGuess, double tolerance, int decimalPlaces, int iteration, ArrayList<Double[]> iterations, int maxIteration, StringBuilder sb) {
        int n = currGuess.length;

        if (iteration > maxIteration) {
            sb.append("Max iteration (").append(maxIteration).append(") count reached. Iteration stopped.\n");
            return currGuess;
        }

        if (iteration > 1000) {
            sb.append("Gauss-Seidel method did not converge.\n");
            return currGuess;
        }

        sb.append("Iteration #").append(iteration).append(":\n");

        Double[] nextGuess = Arrays.copyOf(currGuess, n);

        for (int i = 0; i < n; i++) {
            double sum = matrix[i][n]; // RHS
            sb.append("x").append(i + 1).append(" = (").append(matrix[i][n]);

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    sum -= matrix[i][j] * nextGuess[j]; // Use updated values
                    sb.append(" - ").append(matrix[i][j]).append(" * x").append(j + 1)
                    .append("(").append(nextGuess[j]).append(")");
                }
            }

            double newVal = Utils.round(sum / matrix[i][i], decimalPlaces);
            sb.append(") / ").append(matrix[i][i]).append(" = ").append(newVal).append("\n");

            nextGuess[i] = newVal;
        }

        iterations.add(Arrays.copyOf(nextGuess, n));

        sb.append("Updated values: ");
        for (int i = 0; i < n; i++) {
            sb.append("x").append(i + 1).append(" = ").append(nextGuess[i]).append(" ");
        }
        sb.append("\n\n");

        boolean converged = true;
        for (int i = 0; i < n; i++) {
            if (Math.abs(nextGuess[i] - currGuess[i]) > tolerance) {
                converged = false;
                break;
            }
        }

        if (converged) {
            sb.append("Convergence achieved within tolerance ").append(tolerance).append("\n");
            return nextGuess;
        }

        return gaussSeidel(matrix, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, maxIteration, sb);
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

        Double[] solutions = gaussSeidel(matrix, guess, tolerance, decimalPlaces, 1, iterations, maxIterations, sb);

        for (int i = 0; i < iterations.size(); i++) {
            sb.append("Iteration #").append(i + 1).append(": ")
                    .append(Arrays.toString(iterations.get(i))).append("\n");
        }

        sb.append("\nFinal Approximated Solution: ").append(Arrays.toString(solutions)).append("\n");
        return sb.toString();
    }

}
