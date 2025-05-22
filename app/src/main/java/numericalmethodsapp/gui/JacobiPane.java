package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.Jacobi;

public class JacobiPane extends VBox {
    public JacobiPane(TextArea outputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Jacobi Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        Label numEqLabel = new Label("Number of Equations:");
        numEqLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Spinner<Integer> numEqSpinner = new Spinner<>(2, 3, 2);
        MainWindow.styleWebflowSpinner(numEqSpinner);

        TextField eq1Input = new TextField();
        TextField eq2Input = new TextField();
        TextField eq3Input = new TextField();

        // Apply consistent styling to text fields
        for (TextField field : new TextField[]{eq1Input, eq2Input, eq3Input}) {
            MainWindow.styleWebflowInput(field);
        }

        Label eq1Label = new Label("Equation 1:");
        Label eq2Label = new Label("Equation 2:");
        Label eq3Label = new Label("Equation 3:");

        for (Label label : new Label[]{eq1Label, eq2Label, eq3Label}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                    "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        }

        eq3Label.setVisible(false);
        eq3Input.setVisible(false);
        eq3Input.setManaged(false);

        numEqSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            outputArea.clear();
            boolean showThird = newValue == 3;
            eq3Label.setVisible(showThird);
            eq3Input.setVisible(showThird);
            eq3Input.setManaged(showThird);
        });

        TextField toleranceInput = new TextField("0.001");
        TextField maxIterInput = new TextField("20");

        // Apply consistent styling to parameter text fields
        for (TextField field : new TextField[]{toleranceInput, maxIterInput}) {
            MainWindow.styleWebflowInput(field);
        }

        Label toleranceLabel = new Label("Tolerance:");
        Label maxIterLabel = new Label("Max Iterations:");

        for (Label label : new Label[]{toleranceLabel, maxIterLabel}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                    "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        }

        Button runButton = new Button("Calculate");
        MainWindow.styleCalculateButton(runButton);

        runButton.setOnAction(e -> {
            outputArea.clear();
            int numEq = numEqSpinner.getValue();

            String[] equations = (numEq == 2)
                    ? new String[]{eq1Input.getText().trim(), eq2Input.getText().trim()}
                    : new String[]{eq1Input.getText().trim(), eq2Input.getText().trim(), eq3Input.getText().trim()};

            for (String eq : equations) {
                if (eq.isEmpty()) {
                    outputArea.setText("Please enter all equations.");
                    return;
                }
            }

            double tolerance;
            int maxIterations;
            try {
                tolerance = Double.parseDouble(toleranceInput.getText());
                maxIterations = Integer.parseInt(maxIterInput.getText());
                if (tolerance <= 0 || maxIterations < 2) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid tolerance or iteration count.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            String result = Jacobi.solve(equations, sb, tolerance, maxIterations);
            outputArea.setText(result);
            detailsLabel.setVisible(true);
        });

        getChildren().addAll(
                titleLabel,
                outputArea,
                numEqLabel, numEqSpinner,
                eq1Label, eq1Input,
                eq2Label, eq2Input,
                eq3Label, eq3Input,
                toleranceLabel, toleranceInput,
                maxIterLabel, maxIterInput,
                runButton
        );
    }
}
