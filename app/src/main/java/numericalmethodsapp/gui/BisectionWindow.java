/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import numericalmethodsapp.methods.Bisection;
import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class BisectionWindow extends Stage {

    @SuppressWarnings("CallToPrintStackTrace")
    public BisectionWindow() {
        setTitle("Bisection Method");

        TextField fxInput = new TextField();
        fxInput.setPromptText("Enter f(x)");

        TextField tolInput = new TextField();
        tolInput.setPromptText("Tolerance (e.g., 0.001)");

        TextField aInput = new TextField();
        aInput.setPromptText("Initial guess a");

        TextField bInput = new TextField();
        bInput.setPromptText("Initial guess b");

        Button runButton = new Button("Run");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String aStr = aInput.getText().trim();
            String bStr = bInput.getText().trim();

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

            // Optional: Check f(a)*f(b) < 0 before running
            try {
                int decimalPlaces = Utils.getDecimalPlacesFromTolerance(tol);
                String exp4jExpr = Utils.convertExprToExp4jCompatible(symjaExpr);
                double fa = Utils.evaluateFunction(exp4jExpr, aVal, decimalPlaces);
                double fb = Utils.evaluateFunction(exp4jExpr, bVal, decimalPlaces);
                if (fa * fb >= 0) {
                    outputArea.setText("f(a) and f(b) must have opposite signs. Please enter valid initial guesses.");
                    return;
                }
            } catch (Exception ex) {
                outputArea.setText("Error evaluating function at initial guesses: " + ex.getMessage());
                return;
            }

            // Run solver
            try {
                String result = Bisection.solve(fx, aVal, bVal, tol);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("An error occurred during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, fxInput, tolInput, aInput, bInput, runButton, outputArea);
        layout.setPrefSize(450, 350);

        setScene(new Scene(layout));
    }
}
