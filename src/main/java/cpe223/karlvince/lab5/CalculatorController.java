package cpe223.karlvince.lab5;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CalculatorController {

    @FXML private GridPane buttonContainer;
    @FXML private VBox root;
    @FXML private TextField tField;

    public int themeCycle = 1;
    public int themeMax = 2;

    // Button
    private static final String[][] BUTTONS = {
        {"C", "DEL", "%", "÷"},
        {"7", "8", "9", "×"},
        {"4", "5", "6", "−"},
        {"1", "2", "3", "+"},
        {"±", "0", ".", "="},
    };

    @FXML
    public void initialize() {
        System.out.println("\n[Calculator Controller] Initializing\n");

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

        // Keyboard & Theme
        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            String keyInput = e.getText();
            KeyCode keyCode = e.getCode();

            if (keyInput.matches("[0-9+\\-*/%]") || keyInput.equals(".")) {
                String label = keyInput.replace("*", "×").replace("/", "÷").replace("-", "−");
                handleInteraction(label);
                e.consume();
            } else if (e.isControlDown() && e.getCode().isLetterKey()) {

                if (e.getCode() == KeyCode.T) {

                    Scene rootScene = tField.getScene();
                    
                    if (themeCycle >= themeMax) {
                        themeCycle = 1;
                    } else {
                        themeCycle++;
                    }
                    
                    System.out.println("[Calculator Controller] Theme cycle: " + themeCycle);
                    ThemeHandler.setTheme(rootScene, themeCycle);
                    e.consume();
                }
                
            } else {
                switch (keyCode) {
                    case ENTER -> {
                        handleInteraction("=");
                        e.consume();
                    }
                    case BACK_SPACE -> {
                        handleInteraction("DEL");
                        e.consume();
                    }
                    case ESCAPE -> {
                        handleInteraction("C");
                        e.consume();
                    }
                    default -> {}
                }
            }

        });

    }

    private void modifyBtn(Button btn, int row, int col, String label) {

        String styleNameID = String.format("btn_%d_%d", col, row);

        System.out.println(String.format("[Calculator Controller] Assigned Style for %s: %s", label, styleNameID));

        btn.getStyleClass().add(styleNameID);
        btn.getStyleClass().add("calculatorbtn");
        btn.prefWidthProperty().bind(buttonContainer.widthProperty().multiply(1.0 / BUTTONS[0].length));
        btn.prefHeightProperty().bind(buttonContainer.heightProperty().multiply(0.2));
        btn.setOnAction(e -> {
            handleInteraction(label);
        });

        double margin = 1.5;
        GridPane.setMargin(btn, new Insets(margin,margin,margin,margin));
    }

    private void handleInteraction(String label) {
        String current = tField.getText();
        switch (label) {
            case "="    -> {
                String result = CalculatorCore.parseOperands(tField);
                tField.setText(result);
            }
            case "C"    -> { tField.setText("0"); }
            case "."    -> tField.appendText(label);
            case "+", "−", "×", "÷" -> {

                if (current.matches(".*\\d+[+−×÷]\\d+.*")) {
                    current = CalculatorCore.parseOperands(tField);
                }

                if (current.matches(".*[+−×÷]$")) {
                    tField.setText(current.substring(0, current.length() - 1) + label);
                } else {
                    tField.setText(current + label);
                }

            }

            case "DEL"  -> {
                if (current.length() > 1) {
                    tField.setText(current.substring(0, current.length() - 1));
                } else {
                    tField.setText("0");
                }
            }

            case "%"    -> {
                tField.setText(Double.toString(KVStandardMath.percentage(Double.parseDouble(current))));
            }

            case "±"    -> {
                tField.setText(Double.toString(KVStandardMath.negate(Double.parseDouble(current))));
            }

            default     -> {
                if (label.matches("[0-9]")) {

                    if (tField.getText().equals("0")) {
                        tField.clear();
                    }
                    tField.appendText(label);
                }
            }
        }

        System.out.println(String.format("[Calculator Controller] Event '%s' triggered", label));



    }
}
