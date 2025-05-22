package numericalmethodsapp.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.GaussianElimination;

public class GaussianEliminationPane extends VBox {
    @SuppressWarnings("CallToPrintStackTrace")
    public GaussianEliminationPane(TextArea outputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Gaussian Elimination Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        Label numEqLabel = new Label("Number of Equations:");
        numEqLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Spinner<Integer> numEqSpinner = new Spinner<>(2, 3, 2);
        MainWindow.styleWebflowSpinner(numEqSpinner);

        Label eq1Label = new Label("Equation 1:");
        eq1Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField eq1Input = new TextField();
        MainWindow.styleWebflowInput(eq1Input);

        Label eq2Label = new Label("Equation 2:");
        eq2Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField eq2Input = new TextField();
        MainWindow.styleWebflowInput(eq2Input);

        Label eq3Label = new Label("Equation 3:");
        eq3Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";");
        TextField eq3Input = new TextField();
        MainWindow.styleWebflowInput(eq3Input);

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
        MainWindow.styleCalculateButton(runButton);

        runButton.setOnAction(e -> {
            int numEq = numEqSpinner.getValue();
            StringBuilder sb = new StringBuilder();

            String[] equations = numEq == 2 ?
                    new String[]{eq1Input.getText().trim(), eq2Input.getText().trim()} :
                    new String[]{eq1Input.getText().trim(), eq2Input.getText().trim(), eq3Input.getText().trim()};

            for (String eq : equations) {
                if (eq.isEmpty()) {
                    outputArea.setText("Please enter all " + numEq + " equations.");
                    return;
                }
            }

            try {
                String result = GaussianElimination.solve(equations, sb);
                outputArea.setText(result);
                detailsLabel.setVisible(true);
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

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
