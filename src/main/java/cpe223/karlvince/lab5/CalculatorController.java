package cpe223.karlvince.lab5;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalculatorController {

    @FXML private GridPane buttonContainer;
    @FXML private VBox root;
    @FXML private TextField tField;

    // Button
    private static final String[][] BUTTONS = {
        {"C", "CE", "%", "÷"},
        {"7", "8", "9", "×"},
        {"4", "5", "6", "-"},
        {"1", "2", "3", "+"},
        {"±", "0", ".", "="},
    };

    @FXML
    public void initialize() {

        root.getStyleClass().add("root");
        buttonContainer.getStyleClass().add("container");
        tField.getStyleClass().add("tf");
        
        buttonContainer.setHgap(0);
        buttonContainer.setVgap(0);
        for (int row = 0; row < BUTTONS.length; row++) {
            for (int col = 0; col < BUTTONS[row].length; col++) {
                Button btn = new Button(BUTTONS[row][col]);
                styleBtn(btn, row, col);
                buttonContainer.add(btn, col, row);     
            }
        }

    }

    private void styleBtn(Button btn, int row, int col) {
        String styleNameID = String.format("btn_%d_%d", col, row); System.out.println(styleNameID);
        btn.getStyleClass().add(styleNameID);
        btn.getStyleClass().add("calculatorbtn");
        btn.prefWidthProperty().bind(buttonContainer.widthProperty().multiply(1.0 / BUTTONS[0].length));
        btn.prefHeightProperty().bind(buttonContainer.heightProperty().multiply(0.2));
        double margin = 1.5;
        GridPane.setMargin(btn, new Insets(margin,margin,margin,margin));
    }
}
