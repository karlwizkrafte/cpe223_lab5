package kaviyeslabs.java.utility;

import javafx.scene.Scene;
import javafx.geometry.Rectangle2D;

/**
 * Kaviyes | Labs - JavaFX Toolbox for faster development
 * 
 * @author Karl Vince Reyes
 * @version 0.0.1b (2026-04-26)
 */
public class Toolbox {

    // File
    public static String respath(String path) {

        // null check
        var url = Toolbox.class.getResource(path);
        if (url == null)
            throw new IllegalArgumentException("Resource not found: " + path);

        return url.toExternalForm();
    }

    public static class Screen {
        private static final Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        public static final double WIDTH = bounds.getWidth();
        public static final double HEIGHT = bounds.getHeight();

        /**
         * Returns a percentage of the screen width.
         * 
         * @param percent Percentage value (0-100)
         * @return Calculated width
         */
        public static double getWidth(double percent) {
            return WIDTH * (percent / 100.0);
        }

        /**
         * Returns a percentage of the screen height.
         * 
         * @param percent Percentage value (0-100)
         * @return Calculated height
         */
        public static double getHeight(double percent) {
            return HEIGHT * (percent / 100.0);
        }

        // Scaling
        public static double scale(double percent) {
            return Math.min(WIDTH, HEIGHT) * (percent / 100.0);
        }

        public static double sceneContextScaling(Scene scene, double percent) {
            return scene.getWidth() * (percent / 100.0);
        }
    }

}