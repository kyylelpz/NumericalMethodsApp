package numericalmethodsapp.gui;

import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainWindow extends Application {
    
    private StackPane root;
    private Scene scene;
    public static final String MAIN_FONT = "Poppins";
    public static final String PRIMARY_COLOR = "#4F46E5";
    public static final String SECONDARY_COLOR = "#CCCCCC";
    public static final String BACKGROUND_COLOR = "#161517";

    public static TextArea outputArea = new TextArea();
    public VBox outputInputBox = new VBox();
    

    // Current method pane reference so you can clear/replace later
    private VBox currentMethodPane;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Numerical Methods");
        primaryStage.setResizable(false);

        root = new StackPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        scene = new Scene(root, 1200, 720);

        // Main content container
        HBox mainBox = new HBox(10);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));
        mainBox.setMaxWidth(1200);

        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER_LEFT);
        mainContent.setPadding(new Insets(20));
        mainContent.setMaxWidth(500);
        mainContent.setMaxHeight(720);

        VBox picBox = new VBox(30);
        picBox.setAlignment(Pos.CENTER);
        picBox.setPadding(new Insets(20));
        picBox.setMaxWidth(700);

        // Title with line break
        Label title = new Label("Xynapse");
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");
        title.setFont(Font.font(MAIN_FONT, FontWeight.BOLD, 50));
        title.setAlignment(Pos.CENTER);
        //title.setLineSpacing(0);

        // Subtitle
        Label subtitle = new Label("Precision meets power. Solve numerical method\nequations with confidence.");
        subtitle.setStyle(
            "-fx-text-fill:rgb(213, 218, 228);" +
            "-fx-font-family: " + MAIN_FONT + ";" +
            "-fx-font-size: 16;"
        );
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setMaxWidth(400);

        // Button with proper spacing
        Button continueBtn = new Button("Calculate");
        continueBtn.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: " + SECONDARY_COLOR + ";" +
            "-fx-font-size: 16;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 8;"
        );
        continueBtn.setOnAction(e -> showMain());

        //Image
        Image mainPicImg = new Image(getClass().getResourceAsStream("/img/mainPicTest.png"));
        ImageView mainPicImgV = new ImageView(mainPicImg);

        // Optional: set image size
        mainPicImgV.setFitWidth(600);
        mainPicImgV.setFitHeight(600);

        // Add all elements to main content
        picBox.getChildren().add(mainPicImgV);
        mainContent.getChildren().addAll(title, subtitle, continueBtn);
        mainBox.getChildren().addAll(mainContent, picBox);

        // Use BorderPane for better footer placement
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mainBox);

        root.getChildren().add(borderPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showMain() {
        root.getChildren().clear();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        HBox showMainBox = new HBox(10);
        showMainBox.setAlignment(Pos.CENTER_LEFT);
        showMainBox.setPadding(new Insets(20));
        showMainBox.setMaxWidth(1200);

        VBox methodSelection = new VBox(20);
        methodSelection.setAlignment(Pos.CENTER);
        methodSelection.setPadding(new Insets(20));
        methodSelection.setMinWidth(300);

        outputInputBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        outputInputBox.setAlignment(Pos.CENTER);
        outputInputBox.setPadding(new Insets(20));

        Label label = new Label("Xynapse");
        label.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");
        label.setFont(Font.font(MAIN_FONT, FontWeight.BOLD, 40));

        // Create buttons with handlers that inject corresponding panes
        Button[] methodButtons = {
            createStyledButton("Fixed-Point Iteration", e -> showFixedPositionPane()),
            createStyledButton("Newton-Raphson", e -> showNewtonRaphsonPane()),
            createStyledButton("Secant", e -> showSecantPane() ),
            createStyledButton("Bisection", e -> showBisectionPane()),
            createStyledButton("False Position", e -> showFalsePositionPane()),
            createStyledButton("Matrix", e -> {/* TODO */}),
            createStyledButton("Cramer's Rule", e -> showCramersRulePane()),
            createStyledButton("Gaussian Elimination", e -> {/* TODO */}),
            createStyledButton("Jacobi", e -> {/* TODO */}),
            createStyledButton("Gauss-Seidel", e -> {/* TODO */})
        };

        outputArea.setPrefWidth(920);
        outputArea.setPrefHeight(500);
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Courier New", 16));

        methodSelection.getChildren().addAll(label);
        methodSelection.getChildren().addAll(Arrays.asList(methodButtons));

        // Initially empty outputInputBox children - will be replaced when clicking buttons
        outputInputBox.getChildren().clear();

        showMainBox.getChildren().addAll(methodSelection, outputInputBox);
        root.getChildren().add(showMainBox);
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: #111827;" +
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-padding: 10 10;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        btn.setOnAction(action);
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #D1D5DB;" +  // lighter gray, example hover color
            "-fx-text-fill: #111827;" +
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-padding: 10 10;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 7, 0, 0, 3);"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + SECONDARY_COLOR + ";" +
            "-fx-text-fill: #111827;" +
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-padding: 10 10;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        ));
        btn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        return btn;
    }

    //SHOW PANE METHODS

    private void showFixedPositionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        outputArea.setFont(Font.font("Courier New", 16));
        currentMethodPane = new FixedPointPane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

    private void showFalsePositionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        currentMethodPane = new FalsePositionPane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

    private void showSecantPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        currentMethodPane = new SecantPane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

    private void showBisectionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        currentMethodPane = new BisectionPane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

    private void showNewtonRaphsonPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        currentMethodPane = new NewtonRaphsonPane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

    private void showCramersRulePane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        currentMethodPane = new CramersRulePane(outputArea);
        outputInputBox.getChildren().addAll(currentMethodPane, outputArea);
    }

}