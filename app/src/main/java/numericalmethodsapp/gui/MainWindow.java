package numericalmethodsapp.gui;

import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public static final String INPUT_BACKGROUND = "rgba(57, 63, 84, 0.8)";
    public static final String INPUT_TEXT_INACTIVE = "#7881A1";
    public static final String INPUT_TEXT_ACTIVE = "#FFFFFF";
    public static final String GRADIENT_COLORS = "#4F46E5, #6366F1, #818CF8";

    public static TextArea outputArea = new TextArea();
    public static TextArea secondaryOutputArea = new TextArea();
    public VBox outputInputBox = new VBox();
    private Label detailsLabel;
    

    // Current method pane reference so you can clear/replace later
    private VBox currentMethodPane;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Numerical Methods");
        primaryStage.setResizable(false);

        root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to right, #101212 0%, #1E173D 50%, #291452 100%);");

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
            createStyledButton("Cramer's Rule", e -> showCramersRulePane()),
            createStyledButton("Gaussian Elimination", e -> showGaussianEliminationPane()),
            createStyledButton("Jacobi", e -> showJacobiPane()),
            createStyledButton("Gauss-Seidel", e -> showGaussSeidelPane())
        };

        outputArea.setPrefWidth(920);
        outputArea.setPrefHeight(300);
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Courier New", 16));
        styleOutputArea(outputArea);

        secondaryOutputArea.setPrefWidth(920);
        secondaryOutputArea.setPrefHeight(200);
        secondaryOutputArea.setEditable(false);
        secondaryOutputArea.setFont(Font.font("Courier New", 16));
        styleOutputArea(secondaryOutputArea);

        detailsLabel = new Label("Details:");
        detailsLabel.setStyle(
            "-fx-text-fill: " + INPUT_TEXT_ACTIVE + ";" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 0 0 0 20;"
        );
        detailsLabel.setAlignment(Pos.CENTER_LEFT);
        detailsLabel.setMaxWidth(Double.MAX_VALUE);
        detailsLabel.setVisible(false);

        methodSelection.getChildren().addAll(label);
        methodSelection.getChildren().addAll(Arrays.asList(methodButtons));

        // Initially empty outputInputBox children - will be replaced when clicking buttons
        outputInputBox.getChildren().clear();

        showMainBox.getChildren().addAll(methodSelection, outputInputBox);
        root.getChildren().add(showMainBox);
        
        // Automatically load Fixed Point Iteration pane
        showFixedPositionPane();
    }

    //SHOW PANE METHODS

    private void showFixedPositionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        outputArea.setFont(Font.font("Courier New", 16));
        secondaryOutputArea.setFont(Font.font("Courier New", 16));
        currentMethodPane = new FixedPointPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showFalsePositionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new FalsePositionPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showSecantPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new SecantPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showBisectionPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new BisectionPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showNewtonRaphsonPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new NewtonRaphsonPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showCramersRulePane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new CramersRulePane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showGaussianEliminationPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new GaussianEliminationPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showJacobiPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new JacobiPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void showGaussSeidelPane() {
        outputInputBox.getChildren().clear();
        outputArea.clear();
        secondaryOutputArea.clear();
        detailsLabel.setVisible(false);
        currentMethodPane = new GaussSeidelPane(outputArea, secondaryOutputArea, detailsLabel);
        outputInputBox.getChildren().addAll(currentMethodPane, secondaryOutputArea, detailsLabel, outputArea);
        forceStyle();
    }

    private void forceStyle(){
        javafx.application.Platform.runLater(() -> {
        outputArea.applyCss();
        outputArea.layout();});
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
       btn.setStyle(
            "-fx-background-color: #1E1E2A;" +  // Darker background
            "-fx-text-fill: #818CF8;" +         // Lighter primary color for text
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-font-weight: 500;" +           // Medium font weight
            "-fx-padding: 12 16;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #4F46E5;" +      // Using your primary color for border
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, #4F46E5, 10, 0, 0, 0);" +  // Glow effect using primary color
            "-fx-text-effect: dropshadow(gaussian, #818CF8, 5, 0, 0, 0);"  // Text glow effect
        );
        btn.setOnAction(action);
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #252535;" +  // Slightly lighter dark background on hover
            "-fx-text-fill: #A5B4FC;" +         // Even lighter text color on hover
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-font-weight: 600;" +           // Slightly bolder on hover
            "-fx-padding: 12 16;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #6366F1;" +      // Lighter border on hover
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, #6366F1, 15, 0, 0, 0);" +  // Stronger glow on hover
            "-fx-text-effect: dropshadow(gaussian, #A5B4FC, 8, 0, 0, 0);"  // Stronger text glow on hover
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #1E1E2A;" +  // Back to darker background
            "-fx-text-fill: #818CF8;" +         // Back to lighter primary color
            "-fx-font-size: 14;" +
            "-fx-font-family: '" + MAIN_FONT + "';" +
            "-fx-font-weight: 500;" +           // Back to medium weight
            "-fx-padding: 12 16;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #4F46E5;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, #4F46E5, 10, 0, 0, 0);" +
            "-fx-text-effect: dropshadow(gaussian, #818CF8, 5, 0, 0, 0);"  // Back to normal text glow
        ));
        btn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        return btn;
    }


    public static void styleWebflowInput(TextField input) {
        input.setStyle(
            "-fx-background-color: " + INPUT_BACKGROUND + ";" +
            "-fx-text-fill: " + INPUT_TEXT_ACTIVE + ";" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 4 20 4 10;" +
            "-fx-background-radius: 2px;" +
            "-fx-border-radius: 2px;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-border-color: transparent;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);"
        );

        // Create the animated gradient border effect
        input.setOnMouseEntered(e -> {
            input.setStyle(
                input.getStyle() +
                "-fx-border-color: linear-gradient(to right, " + GRADIENT_COLORS + ");" +
                "-fx-border-width: 0 0 2 0;"
            );
        });

        input.setOnMouseExited(e -> {
            input.setStyle(
                input.getStyle().replace(
                    "-fx-border-color: linear-gradient(to right, " + GRADIENT_COLORS + ");",
                    "-fx-border-color: transparent;"
                )
            );
        });
    }

    public static void styleWebflowSpinner(Spinner<?> spinner) {
    // Style the spinner's text field (editor)
    styleWebflowInput(spinner.getEditor());

    spinner.setStyle(
        "-fx-background-color: transparent;" +
        "-fx-padding: 0;"
    );

    // Listen for when the skin is applied
    spinner.skinProperty().addListener((obs, oldSkin, newSkin) -> {
        if (newSkin != null) {
            javafx.application.Platform.runLater(() -> {
                // Style the spinner buttons
                spinner.lookupAll(".increment-arrow-button, .decrement-arrow-button").forEach(node -> {
                    node.setStyle(
                        "-fx-background-color: " + PRIMARY_COLOR + ";" +
                        "-fx-background-radius: 2px;" +
                        "-fx-padding: 0;"
                    );
                    node.setOnMouseEntered(e -> node.setStyle(
                        "-fx-background-color: #6366F1;" +
                        "-fx-background-radius: 2px;" +
                        "-fx-padding: 0;"
                    ));
                    node.setOnMouseExited(e -> node.setStyle(
                        "-fx-background-color: " + PRIMARY_COLOR + ";" +
                        "-fx-background-radius: 2px;" +
                        "-fx-padding: 0;"
                    ));
                });

                spinner.lookupAll(".increment-arrow, .decrement-arrow").forEach(node -> {
                    node.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-shape: null;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 0;" +
                        "-fx-mark-color: " + INPUT_TEXT_ACTIVE + ";"
                    );
                });
            });
        }
    });

    // Add hover effect to the editor
    spinner.getEditor().setOnMouseEntered(e -> {
        spinner.getEditor().setStyle(
            spinner.getEditor().getStyle() +
            "-fx-border-color: linear-gradient(to right, " + GRADIENT_COLORS + ");" +
            "-fx-border-width: 0 0 2 0;"
        );
    });
    spinner.getEditor().setOnMouseExited(e -> {
        spinner.getEditor().setStyle(
            spinner.getEditor().getStyle().replace(
                "-fx-border-color: linear-gradient(to right, " + GRADIENT_COLORS + ");",
                "-fx-border-color: transparent;"
            )
        );
    });
}

    public static void styleCalculateButton(Button button) {
        button.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(79, 70, 229, 0.3), 10, 0, 0, 0);"
        );

        // Add hover effects
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #6366F1;" +  // Lighter purple on hover
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(99, 102, 241, 0.4), 15, 0, 0, 0);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-background-radius: 4px;" +
            "-fx-border-radius: 4px;" +
            "-fx-effect: dropshadow(gaussian, rgba(79, 70, 229, 0.3), 10, 0, 0, 0);"
        ));
    }

    public static void styleOutputArea(TextArea area) {
    area.setStyle(
        //"-fx-control-inner-background: rgba(68, 81, 124, 0.8);" +  // Proper inner background
        "-fx-background-color: transparent;" +                 // Let parent style through
        "-fx-text-fill: " + INPUT_TEXT_ACTIVE + ";" +          // Consistent text color
        "-fx-font-size: 14px;" +
        "-fx-padding: 8 12 8 12;" +
        "-fx-background-insets: 0;" +
        "-fx-border-color: transparent;" +
        "-fx-highlight-fill: #818CF8;" +                       // Selection color
        "-fx-highlight-text-fill: #161517;"                   // Selected text text color
    );

    // Defer scroll pane styling until layout is complete
    javafx.application.Platform.runLater(() -> {
        if (area.lookup(".scroll-pane") != null) {
            area.lookup(".scroll-pane").setStyle("-fx-background-color: transparent;");
        }
        if (area.lookup(".viewport") != null) {
            area.lookup(".viewport").setStyle("-fx-background-color: transparent;");
        }
        if (area.lookup(".content") != null) {
            area.lookup(".content").setStyle("-fx-background-color: transparent;");
        }
    });
}

}
