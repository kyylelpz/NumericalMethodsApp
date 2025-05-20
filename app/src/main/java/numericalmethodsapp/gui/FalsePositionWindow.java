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
import numericalmethodsapp.methods.FalsePosition;
import numericalmethodsapp.utils.Utils;

/**
 *
 * @author lopez
 */
public class FalsePositionWindow extends Stage {
    @SuppressWarnings("CallToPrintStackTrace")
    public FalsePositionWindow() {
        setTitle("False Position Method");

        TextField fxInput = new TextField();
        fxInput.setPromptText("Enter f(x)");

        TextField tolInput = new TextField();
        tolInput.setPromptText("Tolerance (e.g., 0.001)");

        TextField aInput = new TextField();
        aInput.setPromptText("Enter a (x0)");

        TextField bInput = new TextField();
        bInput.setPromptText("Enter b (x1)");

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

            // Validate a and b
            double a, b;
            try {
                a = Double.parseDouble(aStr);
                b = Double.parseDouble(bStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Both a and b must be valid numbers.");
                return;
            }

            try {
                String result = FalsePosition.solve(fx, a, b, tol);
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
