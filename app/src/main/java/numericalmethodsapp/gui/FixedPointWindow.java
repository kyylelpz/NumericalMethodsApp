package numericalmethodsapp.gui;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import numericalmethodsapp.methods.FixedPoint;

public class FixedPointWindow extends Stage {
    @SuppressWarnings("CallToPrintStackTrace")
    public FixedPointWindow() {
        setTitle("Fixed Point Iteration");

        TextField fxInput = new TextField();
        fxInput.setPromptText("Enter g(x)");

        TextField tolInput = new TextField();
        tolInput.setPromptText("Tolerance (e.g., 0.001)");

        TextField guessInput = new TextField();
        guessInput.setPromptText("Initial guess");

        Button runButton = new Button("Run");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();

            // Validate g(x)
            String symjaExpr = numericalmethodsapp.utils.Utils.convertExprToSymjaCompatible(fx);
            if (!numericalmethodsapp.utils.Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid g(x) expression syntax. Please check parentheses and functions.");
                return;
            }

            // Validate tolerance
            double tol;
            try {
                tol = Double.parseDouble(tolStr);
                if (tol <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                return;
            }

            // Validate initial guess
            double guess;
            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                return;
            }

            // Optional: Check derivative condition |g'(guess)| < 1
            try {
                ExprEvaluator util = new ExprEvaluator();
                IExpr dgofx = util.evaluate("D(" + symjaExpr + ", x)");
                String dgofxStr = numericalmethodsapp.utils.Utils.convertExprToExp4jCompatible(dgofx.toString());
                int decimalPlaces = numericalmethodsapp.utils.Utils.getDecimalPlacesFromTolerance(tol);
                double absDerivative = Math.abs(numericalmethodsapp.utils.Utils.evaluateFunction(dgofxStr, guess, decimalPlaces));

                if (absDerivative >= 1) {
                    outputArea.setText(String.format("g'(%.4f) = %.4f which is >= 1. Try a different initial guess.", guess, absDerivative));
                    return;
                }
            } catch (Exception ex) {
                outputArea.setText("Error evaluating derivative: " + ex.getMessage());
                return;
            }

            // If all validations passed, call solve
            try {
                String result = FixedPoint.solve(fx, tol, guess);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, fxInput, tolInput, guessInput, runButton, outputArea);
        layout.setPrefSize(400, 300);

        setScene(new Scene(layout));
    }
}