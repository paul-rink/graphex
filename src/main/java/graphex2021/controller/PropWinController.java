package graphex2021.controller;

import graphex2021.model.GXGraph;
import graphex2021.model.GXGraphRandom;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 03.02.2021
 */
public class PropWinController {
    private static final String PATTERN_INTEGER = "[0-9]+";

    @FXML
    private TextField numVerticesText;

    @FXML
    private TextField densityText;

    @FXML
    private TextField maxWeightText;

    @FXML
    private CheckBox allowIsolates;

    @FXML
    private Button generateButton;

    private static GXGraph rndGraph = null;
    private static boolean ready = false;

    public void onGenerate() {
        //current window needed for closing later
        Stage stage = (Stage) numVerticesText.getScene().getWindow();

        if (!numVerticesText.getText().matches(PATTERN_INTEGER) || !densityText.getText().matches(PATTERN_INTEGER)
                || !maxWeightText.getText().matches(PATTERN_INTEGER)) {
            new WrongInputFormatAlert();
        } else {
            int numVertices = Integer.parseInt(numVerticesText.getText());
            int maxWeight = Integer.parseInt(maxWeightText.getText());
            int density = Integer.parseInt(densityText.getText());
            if (numVertices < GXGraphRandom.MIN_NUMBER_VERTICES || numVertices > GXGraphRandom.MAX_NUMBER_VERTICES
                    || maxWeight <GXGraphRandom.MIN_EDGE_WEIGHT || density < GXGraphRandom.MIN_EDGE_PROBABILITY
                    || density > GXGraphRandom.MAX_EDGE_PROBABILITY) {
                new WrongInputFormatAlert();
            } else {
                boolean isolates = allowIsolates.isSelected();
                rndGraph = new GXGraphRandom(numVertices, maxWeight, density, isolates);
                ready = true;
            }
        }
        stage.close();
    }

    /**
     * Returns whether there was a graph successfully generated.
     * @return {@code true} in case there's a {@link GXGraph} that can be used, {@code false} otherwise.
     */
    public static boolean lastGenerationSuccessful() {
        return ready;
    }

    /**
     * Returns a random generated graph. <b>WARNING</b> for correct working, always check first
     * {@link PropWinController#lastGenerationSuccessful()}!
     * @return a random generated {@link GXGraph}
     */
    public static GXGraph getLastGeneratedGraph() {
        ready = false;
        return rndGraph;
    }

    private class WrongInputFormatAlert extends Alert {
        public WrongInputFormatAlert() {
            super(AlertType.ERROR);
            setTitle("Wrong input format");
            setContentText("Check all text fields for correct format.");
        }
    }


}
