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
    public FixedPointPane(TextArea outputArea, Label detailsLabel) {

        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Fixed Point Iteration Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

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

            String symjaExpr = Utils.convertExprToSymjaCompatible(gx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid g(x) expression syntax. Please check parentheses and functions.");
                return;
            }

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

            double guess;

            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                return;
            }
            String dgofxStr;
            double absDerivative;
            try {
                ExprEvaluator util = new ExprEvaluator();
                IExpr dgofx = util.evaluate("D(" + symjaExpr + ", x)");
                dgofxStr = Utils.convertExprToExp4jCompatible(dgofx.toString());
                int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tol);
                guess = Utils.round(guess, decimalPlaces);
                absDerivative = Math.abs(Utils.evaluateFunction(dgofxStr, guess, decimalPlaces));

                if (absDerivative >= 1) {
                    sb.append("Test of Convergence:\n\n");
                    sb.append("g(x) = ").append(gx).append("\n");
                    sb.append("g'(x) = ").append(dgofxStr).append("\n");
        
                    sb.append("x(n) = ").append(guess).append("\n");
                    

                    sb.append("| g'(").append(guess).append(") | = ").append(absDerivative).append(" >= 1: Iterations will NOT likely converge.\n\n");
                    sb.append("Enter another initial guess.\n");
                    outputArea.setText(sb.toString());
                    return;
                }
            } catch (Exception ex) {
                outputArea.setText("Error evaluating derivative: " + ex.getMessage());
                return;
            }

            try {
                String result = FixedPoint.solve(gx, tol, guess, dgofxStr, absDerivative, sb);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
            detailsLabel.setVisible(true);
        });

        getChildren().addAll(
            titleLabel,
            outputArea,
            gxLabel, gxInput,
            tolLabel, tolInput,
            guessLabel, guessInput,
            runButton
        );
    }
}
