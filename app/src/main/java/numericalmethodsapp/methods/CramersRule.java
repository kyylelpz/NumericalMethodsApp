/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;


import java.util.Arrays;
import java.util.Scanner;

import numericalmethodsapp.utils.Utils;

//import org.matheclipse.core.eval.ExprEvaluator;
//import org.matheclipse.core.expression.F;

/**
 *
 * @author lopez
 */
public class CramersRule {
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

        double[][] matrix = Utils.parseEquation(equations);

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
        if (matrix.length == 3){
            answer = cramer3(matrix);
        }

        System.out.println(Arrays.toString(answer));
    }

    public static double[] cramer3(double[][] matrix){
        double d = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                    + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        double dx = matrix[0][3] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][3] * matrix[2][2] - matrix[1][2] * matrix[2][3])
                    + matrix[0][2] * (matrix[1][3] * matrix[2][1] - matrix[1][1] * matrix[2][3]);
        double dy = matrix[0][0] * (matrix[1][3] * matrix[2][2] - matrix[1][2] * matrix[2][3])
                    - matrix[0][3] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                    + matrix[0][2] * (matrix[1][0] * matrix[2][3] - matrix[1][3] * matrix[2][0]);
        double dz = matrix[0][0] * (matrix[1][1] * matrix[2][3] - matrix[1][3] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][0] * matrix[2][3] - matrix[1][3] * matrix[2][0])
                    + matrix[0][3] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);

        double x = dx / d;
        double y = dy / d;
        double z = dz / d;
        
        double[] answer = {x, y, z};

        for (int i = 0; i < answer.length; i++) {
            answer[i] = cleanZero(answer[i]);
        }
        
        return answer;
    }

    public static double cleanZero(double val) {
        if (val == -0.0) return 0.0;
        return val;
    }
}
