package graphex2021.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.model.*;
import graphex2021.view.GXTableView;
import graphex2021.view.GraphView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import javax.tools.Tool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Controller {

    private static final String UNLOCKPASSWORD = "Algorithmus";
    private static final String PATTERN_FIN_TEXT = "[0-9]+";

    /**
     * The {@link DisplayModel}, this controller sets the actions for.
     */
    private DisplayModel displayModel;

    private GXTableView gxTable;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GraphView graphView;

    @FXML
    private MenuItem check;

    @FXML
    private MenuItem tip;

    @FXML
    private Button finish;

    @FXML
    private TextField finTextField;

    /**
     * Create a new Controller, where the {@link DisplayModel} is newly created
     * by using the standard {@link graphex2021.model.GXGraph}
     */
    public Controller() {
        try {
            this.displayModel = new DisplayModel();
        } catch (WrongFileFormatException e) {
            //TODO handle this error by displaying a message box
            e.printStackTrace();
        }

        this.gxTable = new GXTableView();
    }

    public void init() {
        initGraphView();
        initTableView();
        displayModel.notifyObservers();
    }

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    public void initGraphView() {
        displayModel.register(graphView);
        graphView.setAutomaticLayout(false);
        graphView.init();
    }

    /**
     * Start/finish button has 2 states. In Start state, pressing will enable interactions with the graph. The button
     * then switches in finish state. This will be disabled until the "done" button is pressed with success. Then the
     * finish button will be enabled. Pressing the finish button will (reset the graphview and turn the button state
     * into start state) exiting the program.
     * the
     */
    public void onStartPressed() {
        setActions();
        if(finish.getText().equals("Start")) {
            finish.setText("Beenden");
            finish.setDisable(true);
        } else {
            Stage stage = (Stage) finish.getScene().getWindow();
            stage.close();
            finish.setText("Start");
        }

    }

    /**
     * When user presses finish button, it has to be checked, if end vertex was reached and the distance is correct.
     * Then the user will get a feedback.
     */
    public void onFinishedPressed() {
        Alert alert;
        String finText = finTextField.getText();
        if (!finText.matches(PATTERN_FIN_TEXT)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe!");
            alert.setContentText("Du musst noch die kürzeste Distanz zum Ziel im Textfeld eintragen.");
        } else {
            int finalDist = Integer.parseInt(finTextField.getText());
            if (displayModel.checkFinishRequirements(finalDist)) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Geschafft!");
                alert.setContentText("Super! Du hast den kürzesten Weg gefunden!");
                finish.setDisable(false);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Noch nicht!");
                alert.setContentText("Du hast noch keinen kürzesten Weg zum Ziel gefunden, versuche es noch weiter!");
            }
        }
        alert.show();
    }

    /**
     * Initialize the table where user steps (according to algorithm) are displayed.
     */
    public void initTableView() {
        gxTable.init(displayModel.getAllVertices());
        displayModel.register(gxTable);
    }

    public void showTable() {
        Stage tableStage = new Stage();
        tableStage.setTitle("Tabelle");
        tableStage.setScene(new Scene(new VBox(gxTable)));
        tableStage.show();


    }

    public void setActions() {
        graphView.setEdgeDoubleClickAction(e -> onSelectEdge((SmartGraphEdge) e));
        graphView.setVertexDoubleClickAction(v -> onSelectVertex((SmartGraphVertex) v));

        //TODO WIP
        for (Node vertexNode : graphView.getChildren())  {
            if (vertexNode.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertexNode;
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        onVertexClicked(vert);
                    }
                });
            }
        }

        for (Node node : graphView.getChildren()) {
           if (node instanceof SmartGraphVertexNode) {
               SmartGraphVertexNode vert = (SmartGraphVertexNode) node;
               vert.setOnMouseEntered(s-> onHoverEdge((SmartGraphVertexNode) s.getSource()));
               vert.setOnMouseExited(s -> onLeaveEdge((SmartGraphVertexNode) s.getSource()));
           }
        }
    }

    public void onHoverEdge(SmartGraphVertexNode e) {
        e.setStyleClass("testClass");
    }

    public void onLeaveEdge(SmartGraphVertexNode e) {
        e.setStyleClass("vertex");
    }

    /**
     * Is called when an edge is selected.
     * @param e is the edge the user selected.
     */
    public void onSelectEdge(SmartGraphEdge e) {
        try {
            displayModel.markEdge((GXEdge) e.getUnderlyingEdge());
            setActions();
        } catch (ElementNotInGraphException elementNotInGraphException) {
            new ElementNotInGraphAlert().show();
        } catch (EdgeCompletesACircleException edgeCompletesACircleException) {
            new EdgeCompletesACircleAlert().show();
        }
    }

    /**
     * Is called when the user selects a vertex.
     * @param v is the selected vertex.
     */
    public void onSelectVertex(SmartGraphVertex v) {
        new VertexDoubleClickAlert().show();
    }

    /**
     * When the user requests a hint, the next step according to the selected algorithm should be shown.
     */
    public void hintRequest() {
        displayModel.nexStep();
    }

    /**
     * Checks, if the user input corresponds to the steps the selected algorithm would perform.
     */
    public void onCheck() {
        Alert check = new Alert(Alert.AlertType.INFORMATION);
        check.setTitle("Check");
        if (displayModel.checkCorrect()) {
            check.setHeaderText("Bisher ist alles richtig.");
        } else {
            check.setHeaderText("Da hat sich leider ein Fehler eingeschlichen");
        }
        check.showAndWait();
    }



    /**
     * Will give the user the ability to load a new graph via a json file.
     */
    public void onLoadGraph() {
        Stage browserStage = new Stage();
        browserStage.setTitle("FileBrowser");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Graph auswaehlen");
        FileChooser.ExtensionFilter jsonFilter
                 = new FileChooser.ExtensionFilter("JSON filter (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonFilter);
        File file = fileChooser.showOpenDialog(browserStage);
        if (!(file == null)) {
            initNewGraph(file);
        }
    }

    public void onStandardGraph() {
        File file = new File("src/main/resources/graphex2021/GraphData/exampleGraph.json");
        initNewGraph(file);
    }

    public void onGraph1() {
        File file = new File("src/main/resources/graphex2021/GraphData/test-2-node-graph.json");
        initNewGraph(file);
    }

    public void onGraph2() {
        File file = new File("src/main/resources/graphex2021/GraphData/test-3-node-graph.json");
        initNewGraph(file);
    }

    public void onGraph3() {
        File file = new File("src/main/resources/graphex2021/GraphData/test-2-node-graph.json");
        initNewGraph(file);
    }

    public void onGraph4() {
        File file = new File("src/main/resources/graphex2021/GraphData/test-2-node-graph.json");
        initNewGraph(file);
    }

    public void onGraph5() {
        File file = new File("src/main/resources/graphex2021/GraphData/test-2-node-graph.json");
        initNewGraph(file);
    }

    private void initNewGraph(File file) {
        displayModel.unregister(graphView);
        displayModel.unregister(gxTable);
        try {
            this.displayModel = new DisplayModel(file);
        } catch (WrongFileFormatException e) {

            e.printStackTrace();//TODO handle this
        }

        // The Pane that graphView is part of (In this case boder pane)
        Pane parent = (Pane) graphView.getParent();

        // Removing the graphView so that later a graphView with other properties can be added.
        parent.getChildren().remove(graphView);
        finish.setText("Start");
        finish.setDisable(false);

        // TODO check how height is set
        double height = graphView.getHeight();
        double width = graphView.getWidth();

        try {
            // TODO propably needs to be done like this, so that properties can be changed as well.
            this.graphView = new GraphView();

            //TODO Check what needs to happen for this to work correctly
            graphView.setPrefSize(width, height);
            // Adding the new graphView to the pane
            parent.getChildren().add(graphView);
            parent.setPrefSize(1000, 100);
            parent.layout();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // new Tableview
        this.gxTable = new GXTableView();

        displayModel.register(graphView);
        //Reinitializing all the views
        init();
        displayModel.notifyObservers();
    }

    /**
     * When this is called, a random Graph will be created an load in the view.
     */
    public void onGenerateRandom() {

    }

    /**
     *When a vertex is clicked with single mouse click, shortest path to the vertex is displayed, depending on the
     * selected edges by the user.
     * @param v
     */
    public void onVertexClicked(SmartGraphVertexNode v) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        displayModel.highlightShortestPathTo(vertex);
    }

    /**
     * This is called, when the user wants to revert a step, i.e. unmark the last selected edge or vertex and
     * everything that comes along with this.
     */
    public void onUndoPressed() {
        try {
            displayModel.undo();
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will update the model for the specific algorithm that is selected.
     * @param algo is the algorithm that is selected to be performed at the graph in the view.
     */
    public void onAlgorithmSelect(Algorithm algo) {

    }

    /**
     * Will show some information on the selected algorithm in the view.
     */
    public void onDisplayAlgorithmExplanation() {

    }

    /**
     * Will show some information on interaction options in the view.
     */
    public void onDisplayInteractionHelp() {

    }

    /**
     * Called on the reset button being pressed
     */
    public void onResetPressed() {
        displayModel.reset();
    }

    public void unlockHints() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Gib das Passwort ein um die"
                + " Hilfefunktionen freizuschalten");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (UNLOCKPASSWORD.equals(result.get())) {
                check.setDisable(false);
                tip.setDisable(false);
            } else if ("Test".equals(result.get())) {
                displayCoordinates();
            } else {
                new Alert(Alert.AlertType.ERROR, "Das war das falsche Passwort.").showAndWait();
            }
        }
    }

    private void displayCoordinates() {
        for (Node vertex : graphView.getChildren())  {
            if (vertex.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertex;
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                        double x = vert.getPositionCenterX() / graphView.getSceneWidth();
                        double y = vert.getPositionCenterY() / graphView.getSceneHeight();
                        System.out.println(vert.getUnderlyingVertex().element().toString() + " x = " + x + " , y = " + y + " Style:  " + vertex.getStyleClass());

                    }
                });
            }
        }
    }

    private void bindAspectRation() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        double ratio = stage.getWidth() / stage.getHeight();
        stage.minWidthProperty().bind(stage.heightProperty().multiply(ratio));
        stage.minHeightProperty().bind(stage.widthProperty().divide(ratio));

    }
}
