package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        eq1Label.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        Label eq2Label = new Label("Equation 2:");
        eq2Label.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        Label eq3Label = new Label("Equation 3:");
        eq3Label.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        for (Label label : new Label[]{eq1Label, eq2Label, eq3Label}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        }

        eq3Label.setVisible(false);
        eq3Input.setVisible(false);
        eq3Input.setManaged(false);

        numEqSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            outputArea.clear();
            secondaryOutputArea.clear();
            boolean showThird = newValue == 3;
            eq3Label.setVisible(showThird);
            eq3Input.setVisible(showThird);
            eq3Input.setManaged(showThird);
        });

        TextField toleranceInput = new TextField();
        MainWindow.styleWebflowInput(toleranceInput);
        TextField maxIterInput = new TextField();
        MainWindow.styleWebflowInput(maxIterInput);

        Label toleranceLabel = new Label("Tolerance:");
        toleranceLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        Label maxIterLabel = new Label("Max Iterations:");
        maxIterLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        for (Label label : new Label[]{toleranceLabel, maxIterLabel}) {
            label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        }

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                    "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
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
                    secondaryOutputArea.setText("");
                    return;
                }
            }

            double tolerance;
            try {
                tolerance = Double.parseDouble(toleranceInput.getText());
                if (tolerance <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    secondaryOutputArea.setText("");
                    return;
                }
                else if (tolerance < 0.00001) {
                    outputArea.setText("Tolerance must be at at least 0.00001.");
                    secondaryOutputArea.setText("");
                    return;
                }
                else if (tolerance > 1){
                    outputArea.setText("Tolerance cannot exceed 1.");
                    secondaryOutputArea.setText("");
                    return;
                }
            }
            catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                secondaryOutputArea.setText("");
                return;
            }

            int maxIterations;
            try {
                maxIterations = Integer.parseInt(maxIterInput.getText());
                if (maxIterations < 2) {
                    outputArea.setText("Max Iterations should be at least 2.");
                    secondaryOutputArea.setText("");
                    return;
                }
                if (maxIterations >1000){
                    outputArea.setText("Max Iterations should not exceed 1000.");
                    secondaryOutputArea.setText("");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid tolerance or iteration count.");
                secondaryOutputArea.setText("");
                return;
            }

            StringBuilder sb = new StringBuilder();
            String result = GaussSeidel.solve(equations, sb, tolerance, maxIterations);
            outputArea.setText(result);
            
        
           
            String resultLower = result.toLowerCase();
            if (resultLower.contains("error") || resultLower.contains("no unique solution")) {
                secondaryOutputArea.setText("");
                return;
            }
            
            
            String[] lines = result.split("\n");
            StringBuilder secondaryOutput = new StringBuilder();
            boolean foundIterations = false;
            
            for (String line : lines) {
                if (line.startsWith("Iteration #") && line.contains("[")) {
                    foundIterations = true;
                    secondaryOutput.append(line).append("\n");
                } else if (line.startsWith("Final Approximation:")) {
                    if (foundIterations) {
                        secondaryOutput.append("\n");
                    }
                    secondaryOutput.append(line).append("\n");
                    int nextLineIndex = java.util.Arrays.asList(lines).indexOf(line) + 1;
                    if (nextLineIndex < lines.length) {
                        secondaryOutput.append(lines[nextLineIndex]);
                    }
                    break;
                }
            }
            
            secondaryOutputArea.setText(secondaryOutput.toString());
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
