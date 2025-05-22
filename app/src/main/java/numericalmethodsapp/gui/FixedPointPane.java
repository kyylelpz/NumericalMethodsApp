package numericalmethodsapp.gui;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.FixedPoint;
import numericalmethodsapp.utils.Utils;

public class FixedPointPane extends VBox {

    @SuppressWarnings("CallToPrintStackTrace")
    public FixedPointPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Fixed Point Iteration Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Label gxLabel = new Label("Enter g(x):");
        gxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");

        TextField gxInput = new TextField();
        MainWindow.styleWebflowInput(gxInput);

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");

        TextField tolInput = new TextField();
        MainWindow.styleWebflowInput(tolInput);

        Label guessLabel = new Label("Initial guess:");
        guessLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");

        TextField guessInput = new TextField();
        MainWindow.styleWebflowInput(guessInput);

        Button runButton = new Button("Calculate");
        MainWindow.styleCalculateButton(runButton);

        runButton.setOnAction(e -> {
            String gx = gxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            // Validate g(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(gx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid g(x) expression syntax. Please check parentheses and functions.");
                secondaryOutputArea.setText("");
                return;
            }

            // Validate tolerance
            double tol;
            try {
                tol = Double.parseDouble(tolStr);
                if (tol <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    secondaryOutputArea.setText("");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                secondaryOutputArea.setText("");
                return;
            }

            // Validate initial guess
            double guess;
            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                secondaryOutputArea.setText("");
                return;
            }

            try {
                // Calculate derivative for convergence test
                ExprEvaluator util = new ExprEvaluator();
                IExpr derivative = util.evaluate("D(" + symjaExpr + ", x)");
                String dgofxStr = Utils.convertExprToExp4jCompatible(derivative.toString());
                double absDerivative = Math.abs(Utils.evaluateFunction(dgofxStr, guess, Utils.getDecimalPlacesFromTolerance(tol)));

                String result = FixedPoint.solve(gx, tol, guess, dgofxStr, absDerivative, sb);
                outputArea.setText(result);
                
                // Extract and display the final result in secondary output area
                String[] lines = result.split("\n");
                for (int i = lines.length - 1; i >= 0; i--) {
                    if (lines[i].startsWith("The approximate root is:")) {
                        secondaryOutputArea.setText(lines[i]);
                        break;
                    }
                }
                
                detailsLabel.setVisible(true);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
                secondaryOutputArea.setText("");
                ex.printStackTrace();
            }
        });

        getChildren().addAll(
            titleLabel,
            outputArea,
            secondaryOutputArea,
            gxLabel, gxInput,
            tolLabel, tolInput,
            guessLabel, guessInput,
            runButton
        );
    }
}
