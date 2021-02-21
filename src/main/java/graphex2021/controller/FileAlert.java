package graphex2021.controller;

import javafx.scene.control.Alert;

public class FileAlert extends Alert {
        public FileAlert(String s) {
            super(AlertType.ERROR);
            this.setTitle("Datei Fehler");
            this.setHeaderText("Die Datei konnte nicht gelesen werden");
            this.setContentText("Pfad: " + s );
        }
}
