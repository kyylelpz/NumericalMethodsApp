package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.CramersRule;

public class CramersRulePane extends VBox {
    @SuppressWarnings("CallToPrintStackTrace")
    public CramersRulePane(TextArea outputArea) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Cramer's Rule Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        Label numEqLabel = new Label("Number of Equations:");
        numEqLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Spinner<Integer> numEqSpinner = new Spinner<>(2, 3, 2);
        numEqSpinner.setStyle("-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Label eq1Label = new Label("Equation 1:");
        eq1Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField eq1Input = new TextField();

        Label eq2Label = new Label("Equation 2:");
        eq2Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField eq2Input = new TextField();

        Label eq3Label = new Label("Equation 3:");
        eq3Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField eq3Input = new TextField();


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

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.BACKGROUND_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        runButton.setOnAction(e -> {
            int numEq = numEqSpinner.getValue();
            StringBuilder sb = new StringBuilder();

            String[] equations;
            if (numEq == 2) {
                equations = new String[]{eq1Input.getText().trim(), eq2Input.getText().trim()};
            } else {
                equations = new String[]{eq1Input.getText().trim(), eq2Input.getText().trim(), eq3Input.getText().trim()};
            }

            // Basic validation: Check empty
            for (int i = 0; i < numEq; i++) {
                if (equations[i].isEmpty()) {
                    outputArea.setText("Please enter all " + numEq + " equations.");
                    return;
                }
            }

            try {
                String result = CramersRule.solve(equations, sb);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        runButton.setOnMouseEntered(e -> runButton.setStyle("-fx-background-color: #D1D5DB;"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"));
        runButton.setOnMouseExited(e -> runButton.setStyle("-fx-background-color: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";"));

        getChildren().addAll(
            titleLabel,
            outputArea,
            numEqLabel, numEqSpinner,
            eq1Label, eq1Input,
            eq2Label, eq2Input,
            eq3Label, eq3Input,
            runButton
        );
    }
}
