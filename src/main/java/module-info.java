module cpe223.karlvince.lab5 {
    requires javafx.fxml;

    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    requires com.sun.jna;
    requires com.sun.jna.platform;

    exports kaviyeslabs.java.graphics;

    opens cpe223.karlvince.lab5 to javafx.fxml;
    exports cpe223.karlvince.lab5;
}
