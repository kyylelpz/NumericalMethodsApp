package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.Secant;
import numericalmethodsapp.utils.Utils;

public class SecantPane extends VBox {
    @SuppressWarnings("CallToPrintStackTrace")
    public SecantPane(TextArea outputArea) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Secant Method");
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

        Label x0Label = new Label("Enter x0:");
        x0Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField x0Input = new TextField();

        Label x1Label = new Label("Enter x1:");
        x1Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField x1Input = new TextField();

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + "; -fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String x0Str = x0Input.getText().trim();
            String x1Str = x1Input.getText().trim();
            StringBuilder sb = new StringBuilder();

            // Validate f(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(fx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid f(x) expression syntax. Please check parentheses and functions.");
                return;
            }

            String exp4jExpr = Utils.convertExprToExp4jCompatible(symjaExpr);

            // Validate tolerance
            double tolerance;
            try {
                tolerance = Double.parseDouble(tolStr);
                if (tolerance <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                return;
            }

            // Validate x0
            double x0;
            try {
                x0 = Double.parseDouble(x0Str);
                Math.abs(Utils.evaluateFunction(exp4jExpr, x0, Utils.getDecimalPlacesFromTolerance(tolerance)));
            } catch (NumberFormatException ex) {
                outputArea.setText("x0 must be a valid number.");
                return;
            } catch (Exception ex) {
                outputArea.setText("x0 caused evaluation error: " + ex.getMessage());
                return;
            }

            // Validate x1
            double x1;
            try {
                x1 = Double.parseDouble(x1Str);
                Math.abs(Utils.evaluateFunction(exp4jExpr, x1, Utils.getDecimalPlacesFromTolerance(tolerance)));
            } catch (NumberFormatException ex) {
                outputArea.setText("x1 must be a valid number.");
                return;
            } catch (Exception ex) {
                outputArea.setText("x1 caused evaluation error: " + ex.getMessage());
                return;
            }

            // Solve
            try {
                String result = Secant.solve(exp4jExpr, tolerance, x0, x1, sb);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
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

        getChildren().addAll(
            titleLabel,
            outputArea,
            fxLabel, fxInput,
            tolLabel, tolInput,
            x0Label, x0Input,
            x1Label, x1Input,
            runButton
        );
    }
}
