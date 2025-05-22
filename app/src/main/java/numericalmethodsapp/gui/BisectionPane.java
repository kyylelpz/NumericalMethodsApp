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
    public BisectionPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Bisection Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

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

        Label aLabel = new Label("Enter x0:");
        aLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField aInput = new TextField();
        MainWindow.styleWebflowInput(aInput);

        Label bLabel = new Label("Enter x1:");
        bLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField bInput = new TextField();
        MainWindow.styleWebflowInput(bInput);

        Button runButton = new Button("Calculate");
        MainWindow.styleCalculateButton(runButton);

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
                
                // Extract and display the summary of iterations and final result in secondary output area
                String[] lines = result.split("\n");
                StringBuilder secondaryOutput = new StringBuilder();
                boolean foundSummary = false;
                boolean foundRoot = false;
                
                for (String line : lines) {
                    if (line.startsWith("Summary of Iterations:")) {
                        foundSummary = true;
                        secondaryOutput.append(line).append("\n");
                    } else if (foundSummary && line.startsWith("Iteration #")) {
                        secondaryOutput.append(line).append("\n");
                    } else if (line.startsWith("The approximate solution is:")) {
                        foundRoot = true;
                        secondaryOutput.append("\n").append(line);
                    }
                }
                
                if (foundSummary && foundRoot) {
                    secondaryOutputArea.setText(secondaryOutput.toString());
                } else {
                    secondaryOutputArea.setText("No valid results found.");
                }
                
                detailsLabel.setVisible(true);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                secondaryOutputArea.setText("");
                ex.printStackTrace();
            }
        });

        // Add nodes to VBox in the correct order
        getChildren().addAll(
            titleLabel,
            outputArea,
            secondaryOutputArea,
            fxLabel, fxInput,
            tolLabel, tolInput,
            aLabel, aInput,
            bLabel, bInput,
            runButton
        );
    }
}
