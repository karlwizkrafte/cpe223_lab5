package cpe223.karlvince.lab5;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kaviyeslabs.java.graphics.ZhanaFX;
import kaviyeslabs.java.utility.Toolbox;

public class App extends Application {

    private static Scene scene;

    public static double SCX = 325;
    public static double SCY = 480;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("calculator_main"), SCX, SCY);
        scene.getStylesheets().add(Toolbox.respath("/cpe223/karlvince/lab5/themes/1.css"));

        Image icon = new Image(getClass().getResourceAsStream("/cpe223/karlvince/lab5/icons/KVCal-64px.png"));

        stage.setTitle("Calculator");
        stage.setMinWidth(SCX + 10);
        stage.setMinHeight(SCY + 15);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();

        ZhanaFX.on(stage)
                .captionColor(Color.web("#263a2e"))
                .install();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        System.out.println("\033c");
        System.out.println("Kaviyes Labs\n");

        launch();

        System.out.println("[App] Closed calculator");
    }

}