package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.Bisection;
import numericalmethodsapp.utils.Utils;

public class BisectionPane extends VBox {

    @SuppressWarnings("CallToPrintStackTrace")
    public BisectionPane(TextArea outputArea) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Bisection Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        // Output area
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

        Label aLabel = new Label("Enter x0:");
        aLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField aInput = new TextField();

        Label bLabel = new Label("Enter x1:");
        bLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField bInput = new TextField();

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + "; -fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String aStr = aInput.getText().trim();
            String bStr = bInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            // Validate f(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(fx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid f(x) expression syntax. Please check parentheses and functions.");
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

            // Validate initial guesses
            double aVal, bVal;
            try {
                aVal = Double.parseDouble(aStr);
                bVal = Double.parseDouble(bStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guesses must be valid numbers.");
                return;
            }

            // Run solver
            try {
                String result = Bisection.solve(fx, aVal, bVal, tol, sb);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        runButton.setOnMouseEntered(e -> runButton.setStyle(
            "-fx-background-color: #D1D5DB; -fx-text-fill: " + MainWindow.BACKGROUND_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"
        ));

        runButton.setOnMouseExited(e -> runButton.setStyle(
            "-fx-background-color: " + MainWindow.SECONDARY_COLOR + "; -fx-text-fill: " + MainWindow.BACKGROUND_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"
        ));

        // Add all to VBox
        getChildren().addAll(
            titleLabel,
            outputArea,
            fxLabel, fxInput,
            tolLabel, tolInput,
            aLabel, aInput,
            bLabel, bInput,
            runButton
        );
    }
}
