package graphex2021.controller;

import javafx.scene.control.Alert;

/**
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 26.01.2021
 */
public class VertexDoubleClickAlert extends Alert {
    public VertexDoubleClickAlert() {
        super(AlertType.INFORMATION);
        this.setTitle("Information");
        this.setContentText("Du hast einen Knoten mit Doppelklick gewählt.\nWähle eine Kante mit Doppelklick um weiter zu machen.");
    }
}
