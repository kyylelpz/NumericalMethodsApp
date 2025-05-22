package numericalmethodsapp.methods;

import java.util.LinkedList;
import java.util.Queue;

import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class FixedPoint {

    public static Double fixedPoint (String gofx, double currGuess, double tolerance, int decimalPlaces, int iteration, Queue<Double> iterations, StringBuilder sb, Character var){
        if (iteration > 1000) {
            System.out.println("Method did not converge after 1000 iterations.");
            sb.append("Method did not converge after 1000 iterations.\n");
            return null;
        }
        sb.append("Iteration #").append(iteration).append(": \n");

        gofx = gofx.replaceAll("(?<=[0-9])" + var, "*"+var);
        String substituted = gofx.replaceAll("\\b"+var+"\\b", Double.toString(currGuess));
        double nextGuess = Utils.evaluateFunction(gofx, currGuess, decimalPlaces, var);

        sb.append(var).append("(n+1) = ").append(substituted).append(" = ").append(nextGuess).append("\n");

        if (Double.isNaN(nextGuess) || Double.isInfinite(nextGuess) || Math.abs(nextGuess) > 1e10) {
            System.out.println("Divergence detected. Iteration stopped.");
            sb.append("Divergence detected. Iteration stopped.\n");
            return null;
        }

        iterations.offer(nextGuess);

        double check = Math.abs(Utils.round(Math.abs(nextGuess - currGuess), decimalPlaces));

        if (check <= tolerance) {
            sb.append("| ").append(var).append("(n+1) - ").append(var).append("(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is less than or equal to tolerance.\n");
            sb.append("Stopping the iteration...\n\n\n");
            return nextGuess;
        }

        sb.append("| ").append(var).append("(n+1) - ").append(var).append("(n) | = ").append(" | (").append(nextGuess).append(" - ").append(currGuess).append(") | = ").append(check).append(" is greater than tolerance.\n");
        sb.append("Continuing to next iteration...\n\n");

        return fixedPoint(gofx, nextGuess, tolerance, decimalPlaces, iteration + 1, iterations, sb, var);
    }

    public static String solve(String gofx, double tolerance, double iGuess, String dgofxStr, double absDerivative, StringBuilder sb, Character var) {
        gofx = Utils.convertExprToExp4jCompatible(gofx);
        
        sb.append("Test of Convergence:\n\n");
        
        sb.append("Expression = ").append(gofx).append("\n");

        sb.append("Variable: ").append(var).append("\n");
        
        sb.append("g'(").append(var).append(") = ").append(dgofxStr).append("\n");
        
        sb.append(var).append("(n) = ").append(iGuess).append("\n");
        
        if (absDerivative >= 1) {
                sb.append("| g'(").append(iGuess).append(") | = ").append(absDerivative).append(" >= 1: Iterations will NOT likely converge.\n\n");
                sb.append("Enter another initial guess.\n");
                return sb.toString();
            }

        sb.append("| g'(").append(iGuess).append(") | = ").append(absDerivative).append(" < 1: Iterations will likely converge.\n\n");
    
        int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tolerance);

        sb.append("Set tolerance to: ").append(tolerance).append("\n");
        sb.append("Set decimal figures to: ").append(decimalPlaces).append("\n\n");

        sb.append("Start of Fixed-Point Iteration Method:\n\n");

        Queue<Double> iterations = new LinkedList<>();

        iterations.offer(iGuess);

        Double solution = fixedPoint(gofx, iGuess, tolerance, decimalPlaces, 1, iterations, sb, var);

        if (iterations.size() > 1) {
            sb.append("Summary of Iterations:\n\n");

            sb.append(String.format("%-12s%-15s%-15s\n", "Iteration", var + "(n)", var + "(n+1)"));

            String format = String.format("%%-12d%%-15.%df%%-15.%df\n", decimalPlaces, decimalPlaces);

            int i = 0;
            while (iterations.size() > 1) {
                double current = iterations.poll();
                double next = iterations.peek(); // Peek the next without removing
                sb.append(String.format(format, i + 1, current, next));
                i++;
            }

            sb.append("\n");
        }

        if (solution == null) {
            sb.append("Method diverged or stopped due to a mathematical error. No approximate root found.");
        } else {
            sb.append("The approximate root is: ").append(solution);
        }

        return sb.toString();

    }
    
    
}
