package numericalmethodsapp.gui;

import java.util.Iterator;
import java.util.Set;

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
    public FixedPointPane(TextArea outputArea) {

        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Fixed Point Iteration Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        Label gxLabel = new Label("Enter g(x):");
        gxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField gxInput = new TextField();

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField tolInput = new TextField();

        Label guessLabel = new Label("Initial guess:");
        guessLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField guessInput = new TextField();

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        runButton.setOnAction(e -> {
            String gx = gxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            gx = Utils.convertExprToExp4jCompatible(gx);

                        
            //validate expression
            String symjaExpr = Utils.convertExprToSymjaCompatible(gx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid expression syntax. Please check parentheses and functions.");
                return;
            }

            //extract variables
            Set<Character> vars = Utils.extractVariables(gx);

            if (vars.size() > 1){
                sb.append("Expression: ").append(gx).append("\n");
                sb.append("Variables: ").append(vars).append("\n\n");
                sb.append("Multiple variables extracted. Re-enter another expression with only 1 variable.\n");
                outputArea.setText(sb.toString());
                return;
            }
            else if (vars.isEmpty()){
                sb.append("No variables extracted. Re-enter another expression.\n");
                outputArea.setText(sb.toString());
                return;
            }

            Iterator<Character> iterator = vars.iterator();

            Character var = iterator.next();

            //check tolerance
            double tol;
            try {
                tol = Double.parseDouble(tolStr);
                if (tol <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    return;
                }
                else if (tol < 0.00001) {
                    outputArea.setText("Tolerance must be at at least 0.00001.");
                    return;
                }
                else if (tol > 1){
                    outputArea.setText("Tolerance cannot exceed 1.");
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

            gx = Utils.convertExprToExp4jCompatible(gx);

            try {
                ExprEvaluator util = new ExprEvaluator();
                IExpr dgofx = util.evaluate("D(" + symjaExpr + ", " + var + ")");
                dgofxStr = Utils.convertExprToExp4jCompatible(dgofx.toString());
                int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tol);
                guess = Utils.round(guess, decimalPlaces);
                absDerivative = Math.abs(Utils.evaluateFunction(dgofxStr, guess, decimalPlaces, var));
                
            } catch (Exception ex) {
                outputArea.setText("Error evaluating derivative: " + ex.getMessage());
                return;
            }

            try {
                String result = FixedPoint.solve(gx, tol, guess, dgofxStr, absDerivative, sb, var);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Optional: add hover style for runButton
        runButton.setOnMouseEntered(e -> runButton.setStyle("-fx-background-color: #D1D5DB; -fx-text-fill: #111827;"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"));
        runButton.setOnMouseExited(e -> runButton.setStyle("-fx-background-color: " + MainWindow.SECONDARY_COLOR + "; -fx-text-fill: " + MainWindow.BACKGROUND_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"));

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
