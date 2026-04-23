package cpe223.karlvince.lab5.util;

import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

/**
 *  Kaviyes | Labs - JavaFX Toolbox for faster development
 * 
 * @author Karl Vince Reyes
 * @version 0.0.1a1 (2026-04-17)
 */
public class Toolbox {

    // File
    public static String respath(String path) {

        // null check
        var url = Toolbox.class.getResource(path);
        if (url == null) throw new IllegalArgumentException("Resource not found: " + path);

        return url.toExternalForm();
    }


    public static class Dynamic {

        // Screen
        private static final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        private static final double WIDTH = screenBounds.getWidth();
        private static final double HEIGHT = screenBounds.getHeight();

        /**
         * @param percentage 80
         * @return 0.8
         */
        public static double screenX(double percentage) {
            return WIDTH * (percentage * .01);
        }

        /**
         * @param percentage 80
         * @return 0.8
         */
        public static double screenY(double percentage) {
            return HEIGHT * (percentage * .01);
        }

        // Scaling
        public static double scale(double percentage) { 
            return Math.min(WIDTH, HEIGHT) * (percentage * .01);
        }
        
        public static double sceneContextScaling(Scene scene, double percentage) {
            return scene.getWidth() * (percentage * .01);
        }
    }

}