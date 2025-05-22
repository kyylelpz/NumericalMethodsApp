package numericalmethodsapp.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatrixPane extends VBox {
    
    public MatrixPane() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        
        Label notAvailableLabel = new Label("                                          Sorry Sir Arai, Not Available :(                             ");
        notAvailableLabel.setFont(Font.font(MainWindow.MAIN_FONT, FontWeight.BOLD, 24));
        notAvailableLabel.setStyle("-fx-text-fill: " + MainWindow.PRIMARY_COLOR + ";");
        
        getChildren().add(notAvailableLabel);
    }
} 