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
import numericalmethodsapp.methods.Secant;
import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class SecantWindow extends Stage {
    @SuppressWarnings("CallToPrintStackTrace")
    public SecantWindow() {
        setTitle("Secant Method");

        TextField fxInput = new TextField();
        fxInput.setPromptText("Enter f(x)");

        TextField tolInput = new TextField();
        tolInput.setPromptText("Tolerance (e.g., 0.001)");

        TextField x0Input = new TextField();
        x0Input.setPromptText("Enter x0");

        TextField x1Input = new TextField();
        x1Input.setPromptText("Enter x1");

        Button runButton = new Button("Run");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String x0Str = x0Input.getText().trim();
            String x1Str = x1Input.getText().trim();

            // Validate f(x)
            String symjaExpr = Utils.convertExprToSymjaCompatible(fx);
            if (!Utils.isValidSymjaExpression(symjaExpr)) {
                outputArea.setText("Invalid f(x) expression syntax. Please check parentheses and functions.");
                return;
            }

            // Validate tolerance
            double tolerance;
            try {
                tolerance = Double.parseDouble(tolStr);
                if (tolerance <= 0) {
                    outputArea.setText("Tolerance must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Tolerance must be a valid decimal number.");
                return;
            }

            // Validate x0
            double x0;
            try {
                x0 = Double.parseDouble(x0Str);
                Math.abs(Utils.evaluateFunction(symjaExpr, x0, Utils.getDecimalPlacesFromTolerance(tolerance)));
            } catch (NumberFormatException ex) {
                outputArea.setText("x0 must be a valid number.");
                return;
            } catch (Exception ex) {
                outputArea.setText("x0 caused evaluation error: " + ex.getMessage());
                return;
            }

            // Validate x1
            double x1;
            try {
                x1 = Double.parseDouble(x1Str);
                Math.abs(Utils.evaluateFunction(symjaExpr, x1, Utils.getDecimalPlacesFromTolerance(tolerance)));
            } catch (NumberFormatException ex) {
                outputArea.setText("x1 must be a valid number.");
                return;
            } catch (Exception ex) {
                outputArea.setText("x1 caused evaluation error: " + ex.getMessage());
                return;
            }

            // Solve
            try {
                String result = Secant.solve(symjaExpr, tolerance, x0, x1);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, fxInput, tolInput, x0Input, x1Input, runButton, outputArea);
        layout.setPrefSize(400, 350);

        setScene(new Scene(layout));
    }
}
