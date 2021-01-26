package graphex2021.controller;

import javafx.scene.control.Alert;

/**
 * TODO JAVADOC
 *
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 26.01.2021
 */
public class VertexDoubleClickAlert extends Alert {
    public VertexDoubleClickAlert() {
        super(AlertType.INFORMATION);
        this.setTitle("Interaction information");
        this.setContentText("You double clicked a vertex. \n Double click an edge to continue.");
    }
}
