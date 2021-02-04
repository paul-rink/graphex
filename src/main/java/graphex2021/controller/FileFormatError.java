package graphex2021.controller;

import graphex2021.model.WrongFileFormatException;
import javafx.scene.control.Alert;

public class FileFormatError extends Alert {
    public FileFormatError(WrongFileFormatException formatException) {
        super(AlertType.ERROR);
        this.setHeaderText( "Das Format des geladenen JSON war falsch");
        this.setTitle("Falsches Dateiformat");
        this.setContentText(formatException.getMessage());
    }
}
