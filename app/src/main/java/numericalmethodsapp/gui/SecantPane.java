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
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");

        // Output area first
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        // Labels and inputs
        Label fxLabel = new Label("Enter f(x):");
        fxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField fxInput = new TextField();
        MainWindow.styleWebflowInput(fxInput);

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField tolInput = new TextField();
        MainWindow.styleWebflowInput(tolInput);

        Label x0Label = new Label("Enter x0:");
        x0Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField x0Input = new TextField();
        MainWindow.styleWebflowInput(x0Input);

        Label x1Label = new Label("Enter x1:");
        x1Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField x1Input = new TextField();
        MainWindow.styleWebflowInput(x1Input);

        Button runButton = new Button("Calculate");
        runButton.setStyle(
            "-fx-background-color: " + MainWindow.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(79, 70, 229, 0.3), 10, 0, 0, 0);"
        );

        // Add hover effects
        runButton.setOnMouseEntered(e -> runButton.setStyle(
            "-fx-background-color: #6366F1;" +  // Lighter purple on hover
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(99, 102, 241, 0.4), 15, 0, 0, 0);"
        ));

        runButton.setOnMouseExited(e -> runButton.setStyle(
            "-fx-background-color: " + MainWindow.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(79, 70, 229, 0.3), 10, 0, 0, 0);"
        ));

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
