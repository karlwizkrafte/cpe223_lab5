package kaviyeslabs.java.graphics;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Internal engine that performs the actual JNA / DWM calls.
 * <p>
 * This class is <b>package-private</b> — consumers should use the
 * {@link ZhanaFX} instead.
 * </p>
 *
 * @author Karl Vince Reyes — Kaviyes Labs
 * @version 0.0.1a
 */
final class DwmBackdropEngine {

    // DWM attribute constants
    static final int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;
    static final int DWMWA_BORDER_COLOR = 34;
    static final int DWMWA_CAPTION_COLOR = 35;
    static final int DWMWA_SYSTEMBACKDROP_TYPE = 38;

    /** Special sentinel: tells DWM to use the default / system colour. */
    static final int DWMWA_COLOR_DEFAULT = 0xFFFFFFFF;
    /** Special sentinel: disables the border / caption entirely (none). */
    static final int DWMWA_COLOR_NONE = 0xFFFFFFFE;

    private DwmBackdropEngine() {
        /* static-only */ }

    // JNA binding to dwmapi.dll
    interface Dwmapi extends StdCallLibrary {
        Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

        int DwmSetWindowAttribute(
                WinDef.HWND hwnd,
                int dwAttribute,
                WinDef.DWORDByReference pvAttribute,
                int cbAttribute);
    }

    // Backdrop

    /**
     * Apply the backdrop effect to the given stage.
     */
    static void applyBackdrop(Stage stage, BackdropType type, Theme theme) {
        if (!isWindows()) {
            log("Skipped — not Windows");
            return;
        }

        try {
            WinDef.HWND hwnd = findHwnd(stage);
            if (hwnd == null) {
                log("Could not find HWND for \"" + stage.getTitle() + "\"");
                return;
            }

            // Apply dark/light mode
            boolean dark = theme.isDark();
            setDwmInt(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, dark ? 1 : 0);

            // Apply backdrop type
            setDwmInt(hwnd, DWMWA_SYSTEMBACKDROP_TYPE, type.getNativeValue());
            log("Applied " + type.name() + " (" + (dark ? "dark" : "light") + ") to \"" + stage.getTitle() + "\"");

        } catch (Exception e) {
            log("Failed: " + e.getMessage());
        }
    }

    // Title-bar / border colour

    /**
     * Set the title-bar (caption) colour.
     *
     * @param stage the shown stage
     * @param color a JavaFX Color; {@code null} to reset to default
     */
    static void setCaptionColor(Stage stage, Color color) {
        setColorAttribute(stage, DWMWA_CAPTION_COLOR, color);
    }

    /**
     * Set the window border colour.
     *
     * @param stage the shown stage
     * @param color a JavaFX Color; {@code null} to reset to default
     */
    static void setBorderColor(Stage stage, Color color) {
        setColorAttribute(stage, DWMWA_BORDER_COLOR, color);
    }

    private static void setColorAttribute(Stage stage, int attribute, Color color) {
        if (!isWindows()) {
            log("Skipped — not Windows");
            return;
        }

        try {
            WinDef.HWND hwnd = findHwnd(stage);
            if (hwnd == null) {
                log("Could not find HWND");
                return;
            }

            int colorRef = (color == null)
                    ? DWMWA_COLOR_DEFAULT
                    : toColorRef(color);

            setDwmInt(hwnd, attribute, colorRef);

            String label = (attribute == DWMWA_CAPTION_COLOR) ? "caption" : "border";
            if (color == null) {
                log("Reset " + label + " colour to default");
            } else {
                log("Set " + label + " colour to " + color);
            }

        } catch (Exception e) {
            log("Failed to set colour: " + e.getMessage());
        }
    }

    // Helpers

    /**
     * Convert a JavaFX {@link Color} to a Win32 {@code COLORREF} (0x00BBGGRR).
     */
    static int toColorRef(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return (b << 16) | (g << 8) | r;
    }

    private static void setDwmInt(WinDef.HWND hwnd, int attribute, int value) {
        var ref = new WinDef.DWORDByReference(new WinDef.DWORD(value));
        Dwmapi.INSTANCE.DwmSetWindowAttribute(hwnd, attribute, ref, WinDef.DWORD.SIZE);
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    // HWND lookup

    /**
     * Locate the native HWND for a JavaFX Stage using three strategies:
     * <ol>
     * <li>GetForegroundWindow (reliable immediately after
     * {@code stage.show()})</li>
     * <li>EnumWindows matching current PID + window title</li>
     * <li>FindWindow with a temporary title probe</li>
     * </ol>
     */
    static WinDef.HWND findHwnd(Stage stage) {
        // Level 1 — foreground window
        WinDef.HWND foreground = User32.INSTANCE.GetForegroundWindow();
        if (foreground != null && matchesStage(foreground, stage)) {
            log("Found HWND via GetForegroundWindow");
            return foreground;
        }

        // Level 2 — enumerate by PID + title
        final int currentPid = Kernel32.INSTANCE.GetCurrentProcessId();
        final String stageTitle = stage.getTitle();
        final WinDef.HWND[] result = { null };

        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hwnd, Pointer data) {
                var pidRef = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hwnd, pidRef);
                if (pidRef.getValue() != currentPid)
                    return true;
                if (!User32.INSTANCE.IsWindowVisible(hwnd))
                    return true;

                char[] buf = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buf, buf.length);
                String title = Native.toString(buf);
                if (stageTitle != null && stageTitle.equals(title)) {
                    result[0] = hwnd;
                    return false;
                }
                return true;
            }
        }, null);

        if (result[0] != null) {
            log("Found HWND via EnumWindows");
            return result[0];
        }

        // Level 3 — title probe
        String original = stage.getTitle();
        String probe = "ZhanaFX_probe_" + System.nanoTime();
        stage.setTitle(probe);

        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, probe);
        stage.setTitle(original);

        if (hwnd != null) {
            log("Found HWND via FindWindow probe");
        }
        return hwnd;
    }

    private static boolean matchesStage(WinDef.HWND hwnd, Stage stage) {
        if (stage.getTitle() == null)
            return false;
        char[] buf = new char[512];
        User32.INSTANCE.GetWindowText(hwnd, buf, buf.length);
        return stage.getTitle().equals(Native.toString(buf));
    }

    static void log(String msg) {
        System.out.println("[ZhanaFX] " + msg);
    }
}
