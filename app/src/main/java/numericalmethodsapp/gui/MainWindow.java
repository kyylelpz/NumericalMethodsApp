package numericalmethodsapp.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Numerical Methods");

        Label label = new Label("Select a Numerical Method:");

        Button btn1 = new Button("1. Fixed-Point Iteration");
        btn1.setOnAction(e -> {new FixedPointWindow().show();});

        Button btn2 = new Button("2. Newton-Raphson");
        //btn2.setOnAction(e -> NewtonRaphson.run(null));

        Button btn3 = new Button("3. Secant");
        //btn3.setOnAction(e -> Secant.run(null));

        Button btn4 = new Button("4. Bisection");
        //btn4.setOnAction(e -> Bisection.run(null));

        Button btn5 = new Button("5. False Position");
        //btn5.setOnAction(e -> FalsePosition.run(null));

        Button btn6 = new Button("6. Matrix");
        //btn6.setOnAction(e -> Matrix.run(null));

        Button btn7 = new Button("7. Cramer's Rule");
        //btn7.setOnAction(e -> CramersRule.run(null));

        Button btn8 = new Button("8. Gaussian Elimination");
        //btn8.setOnAction(e -> GaussianElimination.run(null));

        Button btn9 = new Button("9. Jacobi");
        //btn9.setOnAction(e -> Jacobi.run(null));

        Button btn10 = new Button("10. Gauss-Seidel");
        //btn10.setOnAction(e -> GaussSeidel.run(null));

        VBox root = new VBox(10, label, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 400, 600);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
