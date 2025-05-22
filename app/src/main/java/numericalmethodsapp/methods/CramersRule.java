/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;


import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class CramersRule {

    public static double[] cramer3(double[][] matrix, StringBuilder sb){
        sb.append("Determinant |A|\n");
        
        double A = determinant3x3("|A|",
                                    matrix[0][0], matrix[0][1], matrix[0][2],
                                    matrix[1][0], matrix[1][1], matrix[1][2],
                                    matrix[2][0], matrix[2][1], matrix[2][2],
                                    sb);

        sb.append("|A| = ").append(A).append("\n\n");

        if (Math.abs(A) < 1e-9) { // Determinant is zero
            sb.append("Determinant is or approximately zero. System has no unique solution.");
            throw new ArithmeticException();
        }

        sb.append("Solving for x...\n");
        double Ax = determinant3x3("|Ax|",
                                    matrix[0][3], matrix[0][1], matrix[0][2],
                                    matrix[1][3], matrix[1][1], matrix[1][2],
                                    matrix[2][3], matrix[2][1], matrix[2][2],
                                    sb);
        
        sb.append("|Ax| = ").append(Ax).append("\n");

        double x = cleanZero(Ax / A);

        sb.append("x = |Ax| / |A| = ").append(Ax).append(" / ").append(A).append("\n");
        sb.append("x = ").append(x).append("\n\n");

        sb.append("Solving for y...\n");

        double Ay = determinant3x3("|Ay|",
                                    matrix[0][0], matrix[0][3], matrix[0][2],
                                    matrix[1][0], matrix[1][3], matrix[1][2],
                                    matrix[2][0], matrix[2][3], matrix[2][2],
                                    sb);

        sb.append("|Ay| = ").append(Ay).append("\n");

        double y = cleanZero(Ay / A);

        sb.append("y = |Ay| / |A| = ").append(Ay).append(" / ").append(A).append("\n");
        sb.append("y = ").append(y).append("\n\n");

        sb.append("Solving for z...\n");

        double Az = determinant3x3("|Az|",
                                    matrix[0][0], matrix[0][1], matrix[0][3],
                                    matrix[1][0], matrix[1][1], matrix[1][3],
                                    matrix[2][0], matrix[2][1], matrix[2][3],
                                    sb);

        sb.append("|Az| = ").append(Az).append("\n");

        double z = cleanZero(Az / A);

        sb.append("z = |Az| / |A| = ").append(Az).append(" / ").append(A).append("\n");
        sb.append("z = ").append(z).append("\n\n");
        
        return new double[]{x, y, z};
    }

    private static double determinant3x3(String type,
                                        double a, double b, double c,
                                        double d, double e, double f,
                                        double g, double h, double i,
                                        StringBuilder sb) {
        
        sb.append("\n\t").append(a).append("\t").append(b).append("\t").append(c).append("\t").append(a).append("\t").append(b).append("\n");
        sb.append("\t").append(d).append("\t").append(e).append("\t").append(f).append("\t").append(d).append("\t").append(e).append("\n");
        sb.append("\t").append(g).append("\t").append(h).append("\t").append(i).append("\t").append(g).append("\t").append(h).append("\n\n");

        sb.append(type).append(" = (").append(a).append("*").append(e).append("*").append(i).append("\n");
        sb.append("\t+").append(b).append("*").append(f).append("*").append(g).append("\n");
        sb.append("\t+").append(c).append("*").append(d).append("*").append(h).append(")\n");
        sb.append("\t- (").append(g).append("*").append(e).append("*").append(c).append("\n");
        sb.append("\t+").append(h).append("*").append(f).append("*").append(a).append("\n");
        sb.append("\t+").append(i).append("*").append(d).append("*").append(b).append(")\n\n");

        sb.append(type).append(" = ").append(a*e*i+b*f*g+c*d*h).append(" - ").append(g*e*c+h*f*a+i*d*b).append("\n");

        return a * (e * i - f * h)
             - b * (d * i - f * g)
             + c * (d * h - e * g);
    }

    public static double[] cramer2(double[][] matrix, StringBuilder sb) {
        double a1 = matrix[0][0], b1 = matrix[0][1], c1 = matrix[0][2];
        double a2 = matrix[1][0], b2 = matrix[1][1], c2 = matrix[1][2];

        sb.append("Determinant |A|\n\n");
        sb.append("\t").append(a1).append("\t").append(b1).append("\n");
        sb.append("\t").append(a2).append("\t").append(b2).append("\n");

        double A  = a1 * b2 - a2 * b1;
        
        sb.append("\n|A| = ").append(a1).append(" * ").append(b2).append(" - ").append(a2).append(" * ").append(b1).append("\n");
        sb.append("\n|A| = ").append(A).append("\n\n");

        if (Math.abs(A) < 1e-9) { // Determinant is zero
            sb.append("Determinant |A| is equal to or approximately zero. System has no unique solution.\n");
            throw new ArithmeticException();
        }

        sb.append("Solving for x...\n\n");
        sb.append("\t").append(c1).append("\t").append(b1).append("\n");
        sb.append("\t").append(c2).append("\t").append(b2).append("\n\n");

        double Ax = c1 * b2 - c2 * b1;

        sb.append("|Ax| = ").append(c1).append(" * ").append(b2).append(" - ").append(c2).append(" * ").append(b1).append("\n");
        sb.append("|Ax| = ").append(Ax).append("\n");

        double x = Ax / A;
        sb.append("x = |Ax| / |A| = ").append(Ax).append(" / ").append(A).append("\n");
        sb.append("x = ").append(x).append("\n\n");


        sb.append("Solving for y...\n\n");
        sb.append(a1).append("\t").append(c1).append("\n");
        sb.append(a2).append("\t").append(c2).append("\n\n");

        double Ay = a1 * c2 - a2 * c1;

        sb.append("|Ay| = ").append(a1).append(" * ").append(c2).append(" - ").append(a2).append(" * ").append(c1).append("\n");
        sb.append("|Ay| = ").append(Ay).append("\n");

        double y = Ay / A;

        sb.append("y = |Ay| / |A| = ").append(Ay).append(" / ").append(A).append("\n");
        sb.append("y = ").append(y).append("\n\n");

        return new double[] { cleanZero(x), cleanZero(y) };
    }

    public static double cleanZero(double val) {
        return val == -0.0 ? 0.0 : val;
    }

    public static String solve(String[] equations, StringBuilder sb) {
        int numEq = equations.length;


        for (int i = 0; i < numEq; i++) {
            sb.append("Equation #").append(i+1).append(": ").append(equations[i]).append("\n");
        }

        sb.append("\n");

        if (numEq < 2 || numEq > 3) {
            sb.append("Error: Number of linear equations must be 2 or 3.\n");
            return sb.toString();
        }

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
                sb.append(String.format("%10.4f", matrix[i][j])).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Start of Cramer's Rule Method with ").append(numEq).append(" equations:\n\n");

        double[] solution;
        try {
            if (numEq == 2) {
                solution = cramer2(matrix, sb);
            } else { // numEq == 3
                solution = cramer3(matrix, sb);
            }
        } catch (ArithmeticException ex) {
            return sb.toString();
        }

        sb.append("Solution:\n");
        if (numEq == 2) {
            sb.append("[x = ").append(solution[0]).append(", y = ").append(solution[1]).append("]\n");
        } else {
            sb.append("[x = ").append(solution[0]).append(", y = ").append(solution[1]).append(", z = ").append(solution[2]).append("]\n");
        }

        return sb.toString();
    }
}
