package cpe223.karlvince.lab5;

import javafx.scene.Scene;
import kaviyeslabs.java.utility.Toolbox;

public class ThemeHandler {
    
    public static void setTheme(Scene scene, int num) {

        scene.getStylesheets().clear();
        scene.getStylesheets().add(Toolbox.respath("/cpe223/karlvince/lab5/themes/" + num  +".css"));
        
    }

}
