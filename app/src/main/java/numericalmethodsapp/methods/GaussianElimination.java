/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

/**
 *
 * @author lopez
 */
public class GaussianElimination {

    public static double[] gaussianElimination(double[][] matrix, StringBuilder sb) {
        int n = matrix.length;
        int cols = matrix[0].length;

        sb.append("Initial Augmented Matrix:\n");
        printMatrix(matrix, sb);
        sb.append("\n");

        // Forward Elimination
        for (int i = 0; i < n; i++) {
            sb.append("Step ").append(i + 1).append(" - Forward Elimination:\n");

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
                sb.append("Swapped row ").append(i + 1).append(" with row ").append(maxRow + 1).append(" (Partial Pivoting)\n");
                printMatrix(matrix, sb);
            }

            // Check for zero pivot
            if (Math.abs(matrix[i][i]) < 1e-12) {
                throw new ArithmeticException("Zero pivot encountered at row " + (i + 1) + ", system may have no unique solution.");
            }

            // Eliminate entries below pivot
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                sb.append(String.format("Eliminating row %d using row %d (factor = %.4f):\n", j + 1, i + 1, factor));
                for (int k = i; k < cols; k++) {
                    double before = matrix[j][k];
                    matrix[j][k] -= factor * matrix[i][k];
                    sb.append(String.format("  M[%d][%d] = %.4f - (%.4f * %.4f) = %.4f\n",
                            j + 1, k + 1, before, factor, matrix[i][k], matrix[j][k]));
                }
                sb.append("\n");
            }

            sb.append("Matrix after Step ").append(i + 1).append(":\n");
            printMatrix(matrix, sb);
            sb.append("\n");
        }

        // Back Substitution
        double[] solution = new double[n];
        sb.append("Back Substitution:\n");

        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            sb.append(String.format("Solving for x%d:\n", i + 1));
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
                sb.append(String.format("  sum += M[%d][%d] * x%d = %.4f * %.6f = %.6f\n",
                        i + 1, j + 1, j + 1, matrix[i][j], solution[j], matrix[i][j] * solution[j]));
            }

            if (Math.abs(matrix[i][i]) < 1e-12) {
                throw new ArithmeticException("Division by zero during back substitution at row " + (i + 1));
            }

            solution[i] = (matrix[i][cols - 1] - sum) / matrix[i][i];
            sb.append(String.format("  x%d = (%.4f - %.6f) / %.4f = %.6f\n\n",
                    i + 1, matrix[i][cols - 1], sum, matrix[i][i], solution[i]));
        }

        sb.append("Solution:\n");
            if (n == 2) {
                sb.append("[x = ").append(solution[0])
                .append(", y = ").append(solution[1])
                .append("]\n");
            } else {
                sb.append("[x = ").append(solution[0])
                .append(", y = ").append(solution[1])
                .append(", z = ").append(solution[2])
                .append("]\n");
        }           

        return solution;
    }

    public static String solve(String[] equations, StringBuilder sb) {
        int numEq = equations.length;

        for (int i = 0; i < numEq; i++) {
            sb.append("Equation #").append(i + 1).append(": ").append(equations[i]).append("\n");
        }

        sb.append("\n");

        if (numEq < 2 || numEq > 3) {
            sb.append("Error: Number of linear equations must be 2 or 3.\n");
            return sb.toString();
        }

        double[][] matrix;
        try {
            matrix = numericalmethodsapp.utils.Utils.parseEquation(equations);
        } catch (IllegalArgumentException e) {
            sb.append("Error parsing equations: ").append(e.getMessage()).append("\n");
            return sb.toString();
        }

        sb.append("Parsed Augmented Matrix:\n\n");
        for (double[] row : matrix) {
            for (double val : row) {
                sb.append(String.format("%10.4f", val)).append(" ");
            }
            sb.append("\n");
        }

        sb.append("Start of Gaussian Elimination Method with ").append(numEq).append(" equations:\n\n");

        double[] result;
        try {
            result = gaussianElimination(matrix, sb);  // assume this modifies matrix in place
        } catch (ArithmeticException ex) {
            sb.append("Error during Gaussian Elimination: ").append(ex.getMessage()).append("\n");
            return sb.toString();
        }

        sb.append("Solution:\n");
        char var = 'x';
        for (double val : result) {
            sb.append(var++).append(" = ").append(String.format("%.6f", val)).append("\n");
        }

        return sb.toString();
    }

    private static void printMatrix(double[][] matrix, StringBuilder sb) {
        for (double[] row : matrix) {
            for (double val : row) {
                sb.append(String.format("%10.4f", val)).append(" ");
            }
            sb.append("\n");
        }
    }
}
