/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.gui;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import numericalmethodsapp.methods.NewtonRaphson;

/**
 *
 * @author lopez
 */
public class NewtonRaphsonWindow extends Stage {
    @SuppressWarnings("CallToPrintStackTrace")
    public NewtonRaphsonWindow() {
        setTitle("Newton-Raphson Method");

        TextField fxInput = new TextField();
        fxInput.setPromptText("Enter f(x)");

        TextField tolInput = new TextField();
        tolInput.setPromptText("Tolerance (e.g., 0.001)");

        TextField guessInput = new TextField();
        guessInput.setPromptText("Initial guess");

        Button runButton = new Button("Run");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        runButton.setOnAction(e -> {
            String fx = fxInput.getText().trim();
            String tolStr = tolInput.getText().trim();
            String guessStr = guessInput.getText().trim();

            // Validate f(x)
            String symjaExpr = numericalmethodsapp.utils.Utils.convertExprToSymjaCompatible(fx);
            if (!numericalmethodsapp.utils.Utils.isValidSymjaExpression(symjaExpr)) {
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

            // Validate initial guess
            double guess;
            try {
                guess = Double.parseDouble(guessStr);
            } catch (NumberFormatException ex) {
                outputArea.setText("Initial guess must be a valid number.");
                return;
            }

            // Derivative validation
            try {
                ExprEvaluator util = new ExprEvaluator();
                IExpr derivative = util.evaluate("D(" + symjaExpr + ", x)");
                String derivativeStr = numericalmethodsapp.utils.Utils.convertExprToExp4jCompatible(derivative.toString());

                String result = NewtonRaphson.solve(symjaExpr, derivativeStr, tol, guess);
                outputArea.setText(result);
            } catch (Exception ex) {
                outputArea.setText("Error during solving: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, fxInput, tolInput, guessInput, runButton, outputArea);
        layout.setPrefSize(400, 300);

        setScene(new Scene(layout));
    }
}
