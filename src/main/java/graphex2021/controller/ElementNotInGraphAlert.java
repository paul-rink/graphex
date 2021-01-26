package graphex2021.controller;

import javafx.scene.control.Alert;

/**
* TODO JAVADOC
*
*
* @author D. Flohs, K. Marquardt, P. Rink
* @version 1.0 26.01.2021
*/
public class ElementNotInGraphAlert extends Alert {
    public ElementNotInGraphAlert() {
        super(AlertType.ERROR);
        this.setTitle("Critical Error");
        this.setContentText("There happened something very bad to the graph, that shouldn't happen at all! Contact the"
                + "devs and tell them how bad they are!");
    }
}
