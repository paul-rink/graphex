package graphex2021.controller;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Dialog window that displays interaction possibilities.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 05.02.2021
 */
public class InteractionDialog extends Dialog<Void> {
    InteractionDialog() {
        setTitle("Steuerung");
        setContentText(generateContextText());
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    private String generateContextText() {
        StringBuilder text = new StringBuilder();
        for (Interaction action : Interaction.values()) {
            text.append(action.getAction()).append(" - ").append(action.getText()).append("\n");
        }
        return text.toString();
    }
}
