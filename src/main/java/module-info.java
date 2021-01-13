module graphex {
    requires javafx.controls;
    requires javafx.fxml;
    requires SmartGraph;

    opens graphex2021 to javafx.fxml;
    exports graphex2021;
}