package cpe223.karlvince.lab5;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        

        // TextField Modifier
        tField.setEditable(false);
        tField.setFocusTraversable(false);
        
        tField.setPrefHeight(100);
        tField.setMinHeight(100);
        tField.setMaxHeight(100);

        tField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 16) {
                return change;
            }
            return null;
        }));

        tField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 13) {
                tField.setStyle("-fx-font-size: 30px;");
            } else if (newVal.length() > 9) {
                tField.setStyle("-fx-font-size: 38px;");
            } else {
                tField.setStyle("-fx-font-size: 50px;");
            }
        });

        buttonContainer.setHgap(0);
        buttonContainer.setVgap(0);
        for (int row = 0; row < BUTTONS.length; row++) {
            for (int col = 0; col < BUTTONS[row].length; col++) {
                Button btn = new Button(BUTTONS[row][col]);
                modifyBtn(btn, row, col, BUTTONS[row][col]);
                buttonContainer.add(btn, col, row);     
            }
        }
    }

    private void modifyBtn(Button btn, int row, int col, String label) {

        String styleNameID = String.format("[Calculator Controller] Assigned Style for %s: btn_%d_%d", label, col, row); System.out.println(styleNameID);

        btn.getStyleClass().add(styleNameID);
        btn.getStyleClass().add("calculatorbtn");
        btn.prefWidthProperty().bind(buttonContainer.widthProperty().multiply(1.0 / BUTTONS[0].length));
        btn.prefHeightProperty().bind(buttonContainer.heightProperty().multiply(0.2));
        btn.setOnAction(e -> {
            handButtonClick(label);
        });

        double margin = 1.5;
        GridPane.setMargin(btn, new Insets(margin,margin,margin,margin));
    }

    private void handButtonClick(String label) {
        switch (label) {

            case "C"    -> tField.clear();
            case "."    -> tField.appendText(label);
            case "+"    -> tField.appendText(label);
            case "-"    -> tField.appendText(label); 
            case "×"    -> tField.appendText(label);
            case "÷"    -> tField.appendText(label);

            default     -> {
                if (label.matches("[0-9]")) {
                    tField.appendText(label);
                }
            }
        }
    }
}
