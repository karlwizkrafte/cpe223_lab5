package cpe223.karlvince.lab5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import kaviyeslabs.java.utility.Toolbox;

public class App extends Application {

    private static Scene scene;
    
    public static double SCX = Toolbox.Screen.getWidth(25);
    public static double SCY = Toolbox.Screen.getHeight(60);

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("calculator_main"), SCX, SCY);

        scene.getStylesheets().add(Toolbox.respath("/cpe223/karlvince/lab5/themes/standard_theme.css"));

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}