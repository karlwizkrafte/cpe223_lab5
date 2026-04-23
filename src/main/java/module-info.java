module cpe223.karlvince.lab5 {
    requires javafx.fxml;

    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens cpe223.karlvince.lab5 to javafx.fxml;
    exports cpe223.karlvince.lab5;
}
