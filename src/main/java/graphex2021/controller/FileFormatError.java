package graphex2021.controller;

import graphex2021.model.WrongFileFormatException;
import javafx.scene.control.Alert;

/**
 * Alert that will be displayed when something with the JSON that should contain the graph was wrong
 * and the format was not correct. So no new graph is loaded when this alert is displayed.
 *
 * @author Paul Rink
 * @version 1.0
 */
public class FileFormatError extends Alert {
    public FileFormatError(WrongFileFormatException formatException) {
        super(AlertType.ERROR);
        this.setHeaderText( "Das Format des geladenen JSON war falsch");
        this.setTitle("Falsches Dateiformat");
        this.setContentText(formatException.getMessage());
    }
}
