package graphex2021.controller;

import graphex2021.model.GXGraph;
import graphex2021.model.GXGraphRandom;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 03.02.2021
 */
public class PropWinController {
    private static final String PATTERN_INTEGER = "[^0-9][+-]?[0-9]{1,9}[^0-9]\n";
    private static GXGraph rndGraph = null;
    private static boolean ready = false;

    private static final String minValueText = "Min : ";
    private static final String maxValueText = "Max : ";

    @FXML
    private TextField numVerticesText;

    @FXML
    private TextField densityText;

    @FXML
    private CheckBox allowIsolates;

    @FXML
    private CheckBox avoidClustering;

    @FXML
    private TextField maxWeightInput;

    @FXML
    private Text numVerticesMinText;

    @FXML
    private Text numVerticesMaxText;

    @FXML
    private Text pMinText;

    @FXML
    private Text pMaxText;

    @FXML
    private Text weightMaxText;

    @FXML
    private Text weightMinText;

    @FXML
    private Text weightNoteText;

    /**
     * Initializes all text of the property window.
     */
    public void initText() {
        numVerticesMinText.setText(minValueText + GXGraphRandom.MIN_NUMBER_VERTICES);
        numVerticesMaxText.setText(maxValueText + GXGraphRandom.MAX_NUMBER_VERTICES);
        pMinText.setText(minValueText + GXGraphRandom.MIN_EDGE_PROBABILITY);
        pMaxText.setText(maxValueText + GXGraphRandom.MAX_EDGE_PROBABILITY);
        weightMinText.setText(minValueText + GXGraphRandom.MIN_EDGE_WEIGHT);
        weightMaxText.setText(maxValueText + GXGraphRandom.MAX_EDGE_WEIGHT);
        weightNoteText.setText("Hinweis: Kantengewichte werden zufällig zwischen " + GXGraphRandom.MIN_EDGE_WEIGHT
                + " und dem gewählten Wert gewählt.");
    }

    /**
     * Parses the user inputs from text fields that can then be used to generate a random graph. It is also checked,
     * that user input does match requirements.
     */
    public void onGenerate() {
        //current window needed for closing later
        Stage stage = (Stage) numVerticesText.getScene().getWindow();

        if (!numVerticesText.getText().matches(PATTERN_INTEGER) || !densityText.getText().matches(PATTERN_INTEGER)
                || !maxWeightInput.getText().matches(PATTERN_INTEGER)) {
            new WrongInputFormatAlert().show();
        } else {
            int numVertices = Integer.parseInt(numVerticesText.getText());
            int maxWeight = Integer.parseInt(maxWeightInput.getText());
            int density = Integer.parseInt(densityText.getText());
            if (numVertices < GXGraphRandom.MIN_NUMBER_VERTICES || numVertices > GXGraphRandom.MAX_NUMBER_VERTICES
                    || maxWeight < GXGraphRandom.MIN_EDGE_WEIGHT || maxWeight > GXGraphRandom.MAX_EDGE_WEIGHT
                    || density < GXGraphRandom.MIN_EDGE_PROBABILITY || density > GXGraphRandom.MAX_EDGE_PROBABILITY) {
                new WrongInputFormatAlert().show();
            } else {
                boolean isolates = allowIsolates.isSelected();
                boolean clustering = avoidClustering.isSelected();
                rndGraph = new GXGraphRandom(numVertices, maxWeight, density, isolates, clustering);
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

    private static class WrongInputFormatAlert extends Alert {
        public WrongInputFormatAlert() {
            super(AlertType.ERROR);
            setTitle("Falsches Eingabeformat");
            setContentText("Überprüfe deine Eingaben.");
        }
    }


}
