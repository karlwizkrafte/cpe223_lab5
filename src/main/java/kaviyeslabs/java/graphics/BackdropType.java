package kaviyeslabs.java.graphics;

/**
 * System backdrop types supported by the Windows 11 DWM (Desktop Window
 * Manager).
 * <p>
 * Each variant maps directly to the native {@code DWM_SYSTEMBACKDROP_TYPE}
 * values
 * introduced in Windows 11 Build 22621 (22H2).
 * </p>
 *
 * <table>
 * <caption>Backdrop visual comparison</caption>
 * <tr>
 * <th>Type</th>
 * <th>Look &amp; Feel</th>
 * <th>Performance</th>
 * </tr>
 * <tr>
 * <td>{@link #NONE}</td>
 * <td>Solid colour, no effect</td>
 * <td>—</td>
 * </tr>
 * <tr>
 * <td>{@link #MICA}</td>
 * <td>Subtle wallpaper tint</td>
 * <td>Excellent</td>
 * </tr>
 * <tr>
 * <td>{@link #ACRYLIC}</td>
 * <td>Heavy blur + noise</td>
 * <td>Moderate</td>
 * </tr>
 * <tr>
 * <td>{@link #TABBED}</td>
 * <td>Mica-Alt, more translucent</td>
 * <td>Excellent</td>
 * </tr>
 * </table>
 *
 * @author Karl Vince Reyes — Kaviyes Labs
 * @since 0.0.1a
 */
public enum BackdropType {

    /** No system backdrop — the window uses its default solid background. */
    NONE(0),

    /**
     * <b>Mica</b> — Pulls a subtle, desaturated tint from the desktop wallpaper.
     * Very performant; recommended for primary application windows.
     */
    MICA(2),

    /**
     * <b>Acrylic</b> — In-app blur with a noise texture overlay.
     * More visually intense than Mica; best for transient surfaces (flyouts,
     * dialogs).
     */
    ACRYLIC(3),

    /**
     * <b>Tabbed</b> (Mica-Alt) — A more translucent variant of Mica.
     * Used by Windows 11 File Explorer's tabbed UI.
     */
    TABBED(4);

    /** The native {@code DWM_SYSTEMBACKDROP_TYPE} integer value. */
    private final int nativeValue;

    BackdropType(int nativeValue) {
        this.nativeValue = nativeValue;
    }

    /**
     * Returns the native DWM integer constant for this backdrop type.
     *
     * @return native value passed to {@code DwmSetWindowAttribute}
     */
    public int getNativeValue() {
        return nativeValue;
    }
}
