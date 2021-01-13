module graphex2021 {
    requires javafx.controls;
    requires javafx.fxml;

    opens graphex2021 to javafx.fxml;
    exports graphex2021;
}