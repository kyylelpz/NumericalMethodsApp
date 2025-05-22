package numericalmethodsapp.gui;

import java.util.Iterator;
import java.util.Set;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import numericalmethodsapp.methods.FixedPoint;
import numericalmethodsapp.utils.Utils;

public class FixedPointPane extends VBox {

    @SuppressWarnings("CallToPrintStackTrace")
    public FixedPointPane(TextArea outputArea, TextArea secondaryOutputArea, Label detailsLabel) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Fixed Point Iteration Method");
        titleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        Label gxLabel = new Label("Enter g(x):");
        gxLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField gxInput = new TextField();
        MainWindow.styleWebflowInput(gxInput);

        Label tolLabel = new Label("Tolerance (e.g., 0.001):");
        tolLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField tolInput = new TextField();
        MainWindow.styleWebflowInput(tolInput);

        Label guessLabel = new Label("Initial guess:");
        guessLabel.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";"+
            "-fx-font-family: " + MainWindow.MAIN_FONT + ";");

        TextField guessInput = new TextField();
        MainWindow.styleWebflowInput(guessInput);

        Button runButton = new Button("Calculate");
        runButton.setStyle("-fx-text-fill: " + MainWindow.SECONDARY_COLOR + ";" +
                    "-fx-font-family: " + MainWindow.MAIN_FONT + ";");
        MainWindow.styleCalculateButton(runButton);

        runButton.setOnAction(e -> {
            String gx = gxInput.getText().trim();
            gx = gx.toLowerCase();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();
            StringBuilder sb = new StringBuilder();

            gx = Utils.convertExprToExp4jCompatible(gx);

                        
            //validate expression
            String symjaExpr = Utils.convertExprToSymjaCompatible(gx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid expression syntax. Please check parentheses and functions.");
                return;
            }

            //extract variables
            Set<Character> vars = Utils.extractVariables(gx);

            if (vars.size() > 1){
                sb.append("Expression: ").append(gx).append("\n");
                sb.append("Variables: ").append(vars).append("\n\n");
                sb.append("Multiple variables extracted. Re-enter another expression with only 1 variable.\n");
                outputArea.setText(sb.toString());
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

            //check tolerance
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

            // Validate initial guess
            double guess;
            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                secondaryOutputArea.setText("");
                return;
            }

            String dgofxStr;
            double absDerivative;
            gx = Utils.convertExprToExp4jCompatible(gx);

            try {
                // Calculate derivative for convergence test
                ExprEvaluator util = new ExprEvaluator();
                IExpr dgofx = util.evaluate("D(" + symjaExpr + ", " + var + ")");
                dgofxStr = Utils.convertExprToExp4jCompatible(dgofx.toString());
                int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tol);
                guess = Utils.round(guess, decimalPlaces);
                absDerivative = Math.abs(Utils.evaluateFunction(dgofxStr, guess, decimalPlaces, var));
                
            } catch (Exception ex) {
                outputArea.setText("Error evaluating derivative: " + ex.getMessage());
                return;
            }

            try {
                String result = FixedPoint.solve(gx, tol, guess, dgofxStr, absDerivative, sb, var);
                outputArea.setText(result);
                
                String[] lines = result.split("\n");
                StringBuilder secondaryOutput = new StringBuilder();
                boolean foundSummary = false;
                
                for (String line : lines) {
                    if (line.startsWith("Summary of Iterations:")) {
                        foundSummary = true;
                        secondaryOutput.append(line).append("\n");
                    } else if (foundSummary && line.startsWith("Iteration #")) {
                        secondaryOutput.append(line).append("\n");
                    } else if (foundSummary && line.trim().isEmpty()) {
                        secondaryOutput.append("\n");
                    } else if (line.startsWith("The approximate root is:")) {
                        secondaryOutput.append(line);
                        break;
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
            gxLabel, gxInput,
            tolLabel, tolInput,
            guessLabel, guessInput,
            runButton
        );
    }
}
