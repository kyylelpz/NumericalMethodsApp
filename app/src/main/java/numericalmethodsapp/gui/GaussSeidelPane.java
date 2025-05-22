package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.GaussSeidel;

public class GaussSeidelPane extends VBox {
    public GaussSeidelPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Gauss-Seidel Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Label numEqLabel = new Label("Number of Equations:");
        numEqLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Spinner<Integer> numEqSpinner = new Spinner<>(2, 3, 2);
        MainWindow.styleWebflowSpinner(numEqSpinner);

        TextField eq1Input = new TextField();
        MainWindow.styleWebflowInput(eq1Input);
        TextField eq2Input = new TextField();
        MainWindow.styleWebflowInput(eq2Input);
        TextField eq3Input = new TextField();
        MainWindow.styleWebflowInput(eq3Input);

        Label eq1Label = new Label("Equation 1:");
        Label eq2Label = new Label("Equation 2:");
        Label eq3Label = new Label("Equation 3:");

        for (Label label : new Label[]{eq1Label, eq2Label, eq3Label}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
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
        MainWindow.styleWebflowInput(toleranceInput);
        TextField maxIterInput = new TextField("20");
        MainWindow.styleWebflowInput(maxIterInput);

        Label toleranceLabel = new Label("Tolerance:");
        Label maxIterLabel = new Label("Max Iterations:");

        for (Label label : new Label[]{toleranceLabel, maxIterLabel}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
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
            String result = GaussSeidel.solve(equations, sb, tolerance, maxIterations);
            outputArea.setText(result);
            
            // Extract and display the final result in secondary output area
            String[] lines = result.split("\n");
            for (int i = lines.length - 1; i >= 0; i--) {
                if (lines[i].startsWith("Final Approximated Solution:")) {
                    secondaryOutputArea.setText(lines[i]);
                    break;
                }
            }
            
            detailsLabel.setVisible(true);
        });

        getChildren().addAll(
                titleLabel,
                outputArea,
                secondaryOutputArea,
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
