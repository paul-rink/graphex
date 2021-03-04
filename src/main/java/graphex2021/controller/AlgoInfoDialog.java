package graphex2021.controller;

import graphex2021.model.algo.Algo;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Dialog that shows information to a specific algorithm.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 07.02.2021
 */
public class AlgoInfoDialog extends Dialog<Void> {
    public static final String TITLE = "Algorithmus Information";

    AlgoInfoDialog(Algo algo) {
        setTitle(TITLE);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        setHeight(600);
        setHeaderText(algo.getDisplayName());
        setContentText(algo.getDescription());
    }

}
