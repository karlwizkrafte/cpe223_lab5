package kaviyeslabs.java.graphics;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * <b>ZhanaFX</b> — One-call API for Windows 11 Fluent Design effects on JavaFX.
 *
 * <h2>Quick Start</h2>
 * 
 * <pre>{@code
 * // After stage.show():
 * ZhanaFX.apply(stage, BackdropType.MICA);
 * }</pre>
 *
 * <h2>Theme Control</h2>
 * 
 * <pre>{@code
 * ZhanaFX.apply(stage, BackdropType.MICA, Theme.AUTO); // follow system
 * ZhanaFX.apply(stage, BackdropType.MICA, Theme.DARK); // force dark
 * ZhanaFX.apply(stage, BackdropType.MICA, Theme.LIGHT); // force light
 * }</pre>
 *
 * <h2>Title-bar &amp; Border Colours</h2>
 * 
 * <pre>{@code
 * ZhanaFX.setCaptionColor(stage, Color.web("#1a1a2e"));
 * ZhanaFX.setBorderColor(stage, Color.web("#e94560"));
 * }</pre>
 *
 * <h2>Builder Pattern</h2>
 * 
 * <pre>{@code
 * ZhanaFX.on(stage)
 *         .backdrop(BackdropType.ACRYLIC)
 *         .theme(Theme.AUTO)
 *         .captionColor(Color.web("#1a1a2e"))
 *         .borderColor(Color.web("#e94560"))
 *         .install();
 * }</pre>
 *
 * <h2>Requirements</h2>
 * <ul>
 * <li>Windows 11 22H2+ (Build 22621)</li>
 * <li>JNA 5.x on the module / classpath</li>
 * <li>{@code stage.show()} must be called <b>before</b> applying effects</li>
 * <li>For best results use {@link javafx.stage.StageStyle#UNIFIED} and set
 * {@code scene.setFill(Color.TRANSPARENT)}</li>
 * </ul>
 *
 * @author Karl Vince Reyes — Kaviyes Labs
 * @version 0.0.1a
 */
public final class ZhanaFX {

    /** Library version string. */
    public static final String VERSION = "0.0.1a";

    private ZhanaFX() {
        /* utility class */ }

    /**
     * Apply a backdrop effect with automatic system-theme detection.
     * <p>
     * Uses {@link Theme#AUTO} to match the user's Windows dark/light setting.
     * </p>
     *
     * @param stage the already-shown JavaFX stage
     * @param type  desired backdrop type
     */
    public static void apply(Stage stage, BackdropType type) {
        apply(stage, type, Theme.AUTO);
    }

    /**
     * Apply a backdrop effect with explicit theme control.
     *
     * @param stage the already-shown JavaFX stage
     * @param type  desired backdrop type
     * @param theme {@link Theme#LIGHT}, {@link Theme#DARK}, or {@link Theme#AUTO}
     */
    public static void apply(Stage stage, BackdropType type, Theme theme) {
        runOnFx(() -> DwmBackdropEngine.applyBackdrop(stage, type, theme));
    }

    /**
     * Set the title-bar (caption) background colour.
     *
     * @param stage the already-shown JavaFX stage
     * @param color desired colour; pass {@code null} to reset to system default
     */
    public static void setCaptionColor(Stage stage, Color color) {
        runOnFx(() -> DwmBackdropEngine.setCaptionColor(stage, color));
    }

    /**
     * Set the window border colour.
     *
     * @param stage the already-shown JavaFX stage
     * @param color desired colour; pass {@code null} to reset to system default
     */
    public static void setBorderColor(Stage stage, Color color) {
        runOnFx(() -> DwmBackdropEngine.setBorderColor(stage, color));
    }

    /**
     * Query the Windows registry to check whether the system is in dark mode.
     *
     * @return {@code true} if the user's Windows apps theme is set to dark
     */
    public static boolean isSystemDarkMode() {
        return SystemThemeDetector.isSystemDarkMode();
    }

    /**
     * Query the Windows registry to check whether the system is in light mode.
     *
     * @return {@code true} if the user's Windows apps theme is set to light
     */
    public static boolean isSystemLightMode() {
        return SystemThemeDetector.isSystemLightMode();
    }

    /**
     * Start a builder-style configuration for the given stage.
     *
     * @param stage the already-shown JavaFX stage
     * @return a new {@link Builder} instance
     */
    public static Builder on(Stage stage) {
        return new Builder(stage);
    }

    /**
     * Fluent builder for configuring ZhanaFX effects.
     *
     * <pre>{@code
     * ZhanaFX.on(stage)
     *         .backdrop(BackdropType.MICA)
     *         .theme(Theme.DARK)
     *         .captionColor(Color.web("#1a1a2e"))
     *         .borderColor(Color.web("#e94560"))
     *         .install();
     * }</pre>
     */
    public static final class Builder {
        private final Stage stage;
        private BackdropType type = BackdropType.MICA;
        private Theme theme = Theme.AUTO;
        private Color caption = null; // null = don't change
        private Color border = null;
        private boolean setCaption = false;
        private boolean setBorder = false;

        private Builder(Stage stage) {
            this.stage = stage;
        }

        /** Set the backdrop type (default: {@link BackdropType#MICA}). */
        public Builder backdrop(BackdropType type) {
            this.type = type;
            return this;
        }

        /** Set the theme mode (default: {@link Theme#AUTO}). */
        public Builder theme(Theme theme) {
            this.theme = theme;
            return this;
        }

        /**
         * Set the title-bar colour.
         *
         * @param color desired colour; {@code null} to reset to system default
         */
        public Builder captionColor(Color color) {
            this.caption = color;
            this.setCaption = true;
            return this;
        }

        /**
         * Set the window border colour.
         *
         * @param color desired colour; {@code null} to reset to system default
         */
        public Builder borderColor(Color color) {
            this.border = color;
            this.setBorder = true;
            return this;
        }

        /**
         * Apply all configured effects. Dispatched to the FX thread if necessary.
         */
        public void install() {
            runOnFx(() -> {
                DwmBackdropEngine.applyBackdrop(stage, type, theme);
                if (setCaption)
                    DwmBackdropEngine.setCaptionColor(stage, caption);
                if (setBorder)
                    DwmBackdropEngine.setBorderColor(stage, border);
            });
        }
    }

    /**
     * Check whether the current platform likely supports DWM backdrop effects.
     *
     * @return {@code true} on Windows; {@code false} otherwise
     */
    public static boolean isSupported() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    // Internal Helper
    private static void runOnFx(Runnable work) {
        if (Platform.isFxApplicationThread()) {
            work.run();
        } else {
            Platform.runLater(work);
        }
    }
}
