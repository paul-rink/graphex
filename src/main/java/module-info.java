module graphex2021 {
    requires javafx.controls;
    requires javafx.fxml;
    requires SmartGraph;
    requires org.json;
    requires java.desktop;
    requires org.everit.json.schema;


    opens graphex2021.controller to javafx.fxml;
    exports graphex2021.controller;
    exports graphex2021;
    exports graphex2021.model;
    exports graphex2021.view;

}