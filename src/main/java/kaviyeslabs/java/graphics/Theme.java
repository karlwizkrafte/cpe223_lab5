package kaviyeslabs.java.graphics;

/**
 * Theme modes for the window title bar and DWM immersive dark-mode attribute.
 *
 * <ul>
 * <li>{@link #LIGHT} — Forces a light title bar regardless of system
 * settings.</li>
 * <li>{@link #DARK} — Forces a dark title bar regardless of system
 * settings.</li>
 * <li>{@link #AUTO} — Reads the current Windows 11 system theme and applies
 * it.</li>
 * </ul>
 *
 * <h3>How AUTO detection works</h3>
 * <p>
 * {@code Theme.AUTO} reads the Windows registry key:<br>
 * {@code HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme}<br>
 * A value of {@code 0} = dark mode, {@code 1} = light mode.
 * </p>
 *
 * @author Karl Vince Reyes — Kaviyes Labs
 * @since 0.0.1a
 */
public enum Theme {

    /** Force light title bar. */
    LIGHT,

    /** Force dark title bar. */
    DARK,

    /**
     * Detect system theme automatically.
     * <p>
     * Uses the Windows registry to determine if the user is in dark or light mode
     * and applies the matching title-bar style.
     * </p>
     */
    AUTO;

    /**
     * Resolve the theme to a concrete dark-mode boolean value.
     * <p>
     * For {@link #LIGHT} returns {@code false}, for {@link #DARK} returns
     * {@code true},
     * and for {@link #AUTO} queries the Windows registry.
     * </p>
     *
     * @return {@code true} if dark mode should be applied
     */
    public boolean isDark() {
        switch (this) {
            case DARK:
                return true;
            case LIGHT:
                return false;
            case AUTO:
                return SystemThemeDetector.isSystemDarkMode();
            default:
                return false;
        }
    }
}
