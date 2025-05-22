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
import numericalmethodsapp.methods.NewtonRaphson;
import numericalmethodsapp.utils.Utils;

public class NewtonRaphsonPane extends VBox {

    @SuppressWarnings("CallToPrintStackTrace")
    public NewtonRaphsonPane(TextArea outputArea) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Newton-Raphson Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        // Output area first
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        // Labels and inputs
        Label fxLabel = new Label("Enter f(x):");
        fxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField fxInput = new TextField();

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField tolInput = new TextField();

        Label guessLabel = new Label("Initial guess:");
        guessLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField guessInput = new TextField();

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + "; -fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            // Validate f(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(fx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid f(x) expression syntax. Please check parentheses and functions.");
                return;
            }

            //extract variables
            Set<Character> vars = Utils.extractVariables(fx);

            if (vars.size() > 1){
                sb.append("Expression: ").append(fx).append("\n");
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
            var = Character.toLowerCase(var);

            // Validate tolerance
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

            // Validate initial guess
            double guess;
            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                return;
            }

            // Derivative calculation and validation
            String derivativeStr;
            try {
                ExprEvaluator util = new ExprEvaluator();
                IExpr derivative = util.evaluate("D(" + symjaExpr + ", " + var + ")");
                derivativeStr = Utils.convertExprToExp4jCompatible(derivative.toString());
                String exp4jExpr = Utils.convertExprToExp4jCompatible(symjaExpr);

                String result = NewtonRaphson.solve(exp4jExpr, derivativeStr, tol, guess, sb, var);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        runButton.setOnMouseEntered(e -> runButton.setStyle(
            "-fx-background-color: #D1D5DB;"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"
        ));
        runButton.setOnMouseExited(e -> runButton.setStyle(
            "-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + "; -fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"
        ));

        // Add nodes to VBox in order
        getChildren().addAll(
            titleLabel,
            outputArea,
            fxLabel, fxInput,
            tolLabel, tolInput,
            guessLabel, guessInput,
            runButton
        );
    }
}
