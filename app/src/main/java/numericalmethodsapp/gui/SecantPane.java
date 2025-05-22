package numericalmethodsapp.gui;

import java.util.Iterator;
import java.util.Set;

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
    public SecantPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        // Title label
        Label titleLabel = new Label("Secant Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
                "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        // Labels and inputs
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

        Label x0Label = new Label("Enter x0:");
        x0Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField x0Input = new TextField();
        MainWindow.styleWebflowInput(x0Input);

        Label x1Label = new Label("Enter x1:");
        x1Label.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        TextField x1Input = new TextField();
        MainWindow.styleWebflowInput(x1Input);

        Button runButton = new Button("Calculate");
        MainWindow.styleCalculateButton(runButton);

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

            //extract variables
            Set<Character> vars = Utils.extractVariables(fx);

            if (vars.size() > 1){
                sb.append("Expression: ").append(fx).append("\n");
                sb.append("Variables: ").append(vars).append("\n\n");
                sb.append("Multiple variables extracted. Re-enter another expression with only 1 variable.\n");
                outputArea.setText(sb.toString());
                return;
            }
            else if (vars.isEmpty()){
                sb.append("No variables extracted. Re-enter another expression.\n");
                outputArea.setText(sb.toString());
                return;
            }

            Iterator<Character> iterator = vars.iterator();
            Character var = iterator.next();
            var = Character.toLowerCase(var);

            // Validate tolerance
            double tolerance;
            try {
                tolerance = Double.parseDouble(tolStr);
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
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                secondaryOutputArea.setText("");
                return;
            }

            // Validate x0
            double x0;
            try {
                x0 = Double.parseDouble(x0Str);
                Utils.evaluateFunction(exp4jExpr, x0, Utils.getDecimalPlacesFromTolerance(tolerance), var);
            } catch (NumberFormatException ex) {
                outputArea.setText("x0 must be a valid number.");
                secondaryOutputArea.setText("");
                return;
            } catch (Exception ex) {
                outputArea.setText("x0 caused evaluation error: " + ex.getMessage());
                secondaryOutputArea.setText("");
                return;
            }

            // Validate x1
            double x1;
            try {
                x1 = Double.parseDouble(x1Str);
                Utils.evaluateFunction(exp4jExpr, x1, Utils.getDecimalPlacesFromTolerance(tolerance), var);
            } catch (NumberFormatException ex) {
                outputArea.setText("x1 must be a valid number.");
                secondaryOutputArea.setText("");
                return;
            } catch (Exception ex) {
                outputArea.setText("x1 caused evaluation error: " + ex.getMessage());
                secondaryOutputArea.setText("");
                return;
            }

            // Solve
            try {
                String result = Secant.solve(exp4jExpr, tolerance, x0, x1, sb, var);
                outputArea.setText(result);
                
                // Extract and display the summary of iterations and final result in secondary output area
                StringBuilder secondaryOutput = new StringBuilder();
                String[] lines = result.split("\n");
                boolean foundSummary = false;
                
                 for (String line : lines) {
            if (line.trim().startsWith("Summary of Iterations:")) {
                foundSummary = true;
                secondaryOutput.append(line).append("\n");
            } else if (foundSummary && line.trim().startsWith("The approximate solution is:")) {
                secondaryOutput.append("\n").append(line);
                break; // Done collecting
            } else if (foundSummary) {
                secondaryOutput.append(line).append("\n");
            }
        }
                
                secondaryOutputArea.setText(secondaryOutput.toString());
                detailsLabel.setVisible(true);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
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
            x0Label, x0Input,
            x1Label, x1Input,
            runButton
        );
    }
}
