package kaviyeslabs.java.graphics;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * Detects the current Windows 11 system theme (dark or light) by reading
 * the registry.
 * <p>
 * Registry key queried:<br>
 * {@code HKCU\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme}
 * </p>
 * <ul>
 * <li>{@code 0} → Dark mode</li>
 * <li>{@code 1} → Light mode</li>
 * </ul>
 *
 * This class is <b>package-private</b> — consumers should use
 * {@link Theme#isDark()} or {@link ZhanaFX#isSystemDarkMode()} instead.
 *
 * @author Karl Vince Reyes — Kaviyes Labs
 * @since 0.0.1a
 */
final class SystemThemeDetector {

    private static final String PERSONALIZE_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize";
    private static final String APPS_USE_LIGHT_THEME = "AppsUseLightTheme";

    private SystemThemeDetector() {
        /* static-only */ }

    /**
     * Returns {@code true} if the system is currently in dark mode.
     *
     * @return {@code true} for dark mode, {@code false} for light mode
     */
    static boolean isSystemDarkMode() {
        try {
            int value = Advapi32Util.registryGetIntValue(
                    WinReg.HKEY_CURRENT_USER,
                    PERSONALIZE_KEY,
                    APPS_USE_LIGHT_THEME);
            // 0 = dark, 1 = light
            return value == 0;
        } catch (Exception e) {
            log("Could not read system theme, defaulting to dark: " + e.getMessage());
            return true; // safe default — dark mode is more common with backdrop effects
        }
    }

    /**
     * Returns {@code true} if the system is currently in light mode.
     *
     * @return {@code true} for light mode, {@code false} for dark mode
     */
    static boolean isSystemLightMode() {
        return !isSystemDarkMode();
    }

    private static void log(String msg) {
        System.out.println("[ZhanaFX] " + msg);
    }
}
