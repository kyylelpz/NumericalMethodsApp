package numericalmethodsapp.gui;

import java.util.Iterator;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.FalsePosition;
import numericalmethodsapp.utils.Utils;

public class FalsePositionPane extends VBox {
    @SuppressWarnings("CallToPrintStackTrace")
    public FalsePositionPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        
        Label titleLabel = new Label("False Position or Regula-Falsi Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        
        Label fxLabel = new Label("Enter f(x):");
        fxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField fxInput = new TextField();
        MainWindow.styleWebflowInput(fxInput);

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField tolInput = new TextField();
        MainWindow.styleWebflowInput(tolInput);

        Label aLabel = new Label("Enter x0:");
        aLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField aInput = new TextField();
        MainWindow.styleWebflowInput(aInput);

        Label bLabel = new Label("Enter x1:");
        bLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField bInput = new TextField();
        MainWindow.styleWebflowInput(bInput);

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                    "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        MainWindow.styleCalculateButton(runButton);

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            fx = fx.toLowerCase();
            String tolStr = tolInput.getText().trim();
            String aStr = aInput.getText().trim();
            String bStr = bInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            // Validate f(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(fx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid f(x) expression syntax. Please check parentheses and functions.");
                secondaryOutputArea.setText("");
                return;
            }

            //extract variables
            Set<Character> vars = Utils.extractVariables(fx);

            if (vars.size() > 1){
                sb.append("Expression: ").append(fx).append("\n");
                sb.append("Variables: ").append(vars).append("\n\n");
                sb.append("Multiple variables extracted. Re-enter another expression with only 1 variable.\n");
                outputArea.setText(sb.toString());
                secondaryOutputArea.setText("");
                return;
            }
            else if (vars.isEmpty()){
                sb.append("No variables extracted. Re-enter another expression.\n");
                outputArea.setText(sb.toString());
                secondaryOutputArea.setText("");
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
                    secondaryOutputArea.setText("");
                    return;
                }
                else if (tol < 0.00001) {
                    outputArea.setText("Tolerance must be at at least 0.00001.");
                    secondaryOutputArea.setText("");
                    return;
                }
                else if (tol > 1){
                    outputArea.setText("Tolerance cannot exceed 1.");
                    secondaryOutputArea.setText("");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                secondaryOutputArea.setText("");
                return;
            }

            // Validate a and b
            double a, b;
            try {
                a = Double.parseDouble(aStr);
                b = Double.parseDouble(bStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Both a and b must be valid numbers.");
                secondaryOutputArea.setText("");
                return;
            }

            fx = Utils.convertExprToExp4jCompatible(fx);

            try {
                String result = FalsePosition.solve(fx, a, b, tol, sb, var);
                outputArea.setText(result);
                
                StringBuilder secondaryOutput = new StringBuilder();
                String[] lines = result.split("\n");
                boolean foundIterations = false;
                
                for (String line : lines) {
                    if (line.startsWith("Iterations:")) {
                        foundIterations = true;
                        continue;
                    }
                    if (foundIterations && line.startsWith("The approximate solution is:")) {
                        secondaryOutput.append("\n").append(line);
                        break;
                    }
                    if (foundIterations && !line.isEmpty()) {
                        secondaryOutput.append(line).append("\n");
                    }
                }
                
                secondaryOutputArea.setText(secondaryOutput.toString());
                detailsLabel.setVisible(true);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                secondaryOutputArea.setText("");
                ex.printStackTrace();
            }
        });

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
