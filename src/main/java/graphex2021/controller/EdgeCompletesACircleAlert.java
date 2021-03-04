package graphex2021.controller;

import javafx.scene.control.Alert;

/**
 * This alert is displayed when an edge is selected, that is blocked.
 *
 * @author kmarq
 * @version 1.0 26.01.2021
 */
public class EdgeCompletesACircleAlert extends Alert {
    public EdgeCompletesACircleAlert() {
        super(AlertType.INFORMATION);
        setTitle("Information");
        setHeaderText("Diese Kante kann nicht mehr ausgewählt werden.");
        setContentText("");//Hier warst du schonmal. Versuche einen anderen Weg.
    }
}
