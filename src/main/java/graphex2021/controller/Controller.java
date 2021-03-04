package graphex2021.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.Main;
import graphex2021.model.*;
import graphex2021.view.GXTableView;
import graphex2021.view.GraphView;

import graphex2021.view.ZoomableScrollPane;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;


import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Controller {

    /**
     * Used as the standard pane size in cas no background image is found.
     */
    private static final int STANDARD_PANE_WIDTH = 1280;
    private static final int STANDARD_PANE_HEIGHT = 720;
    /**
     * Used  to set the min pane height in case no backgroundimage is found.
     */
    private static final int STANDARD_PANE_MIN_WIDTH = 1000;
    private static final int STANDARD_PANE_MIN_HEIGHT = 563;
    private static final String UNLOCKPASSWORD = "Algorithmus";
    private static final String PATTERN_FIN_TEXT = "-??[0-9]{1,9}";
    private static final String[] IMAGE_FILE_ENDINGS = new String[]{"jpeg", "jpg", "png", "bmp", "JPEG", "JPG", "PNG"
            , "BMP"};
    private static final int MIN_PANE_SIZE = 1000;
    private static final String PATH_TO_TEMPLATES = "resources" + File.separator + "graphex2021"
            + File.separator + "GraphData" + File.separator + "Templates";


    /**
     * The {@link DisplayModel}, this controller sets the actions for.
     */
    private DisplayModel displayModel;

    private GXTableView gxTable;
    private Stage tableStage;

    private boolean debugMode;

    @FXML
    private Menu templates;

    @FXML
    private Group group;

    @FXML
    private GraphView graphView;

    @FXML
    private ZoomableScrollPane scrollPane;
    @FXML
    private MenuItem check;

    @FXML
    private MenuItem tip;

    @FXML
    private Button finish;

    @FXML
    private TextField finTextField;

    @FXML
    private CheckMenuItem verticesMoveable;

    /**
     * Create a new Controller, where the {@link DisplayModel} is newly created
     * by using the standard {@link graphex2021.model.GXGraph}690
     */
    public Controller() {
        try {
            this.displayModel = new DisplayModel();
        } catch (WrongFileFormatException e) {
            Alert error = new FileAlert(e.getMessage());
            error.showAndWait();
            e.printStackTrace();
            return;
        }

        this.gxTable = new GXTableView();
    }

    public void init() {
        loadTemplates();
        initGraphView();
        initScrollPane();
        initTableView();
        bind();
        displayModel.notifyObservers();
    }

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    private void initGraphView() {
        displayModel.register(graphView);
        graphView.setAutomaticLayout(false);
        this.verticesMoveable.setSelected(graphView.isMoveable());
        graphView.init();
    }

    private void initScrollPane() {
        Node parentPane = graphView.getParent();
        scrollPane.init(parentPane, group, graphView);
    }

    /**
     * Start/finish button has 2 states. In Start state, pressing will enable interactions with the graph. The button
     * then switches in finish state. This will be disabled until the "done" button is pressed with success. Then the
     * finish button will be enabled. Pressing the finish button will (reset the graphview and turn the button state
     * into start state) exiting the program.
     * the
     */
    @FXML
    private void onStartPressed() {
        setActions();
        if (finish.getText().equals("Start")) {
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
    @FXML
    private void onFinishedPressed() {
        Alert alert;
        String finText = finTextField.getText();
        if (!finText.matches(PATTERN_FIN_TEXT)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe!");
            alert.setContentText("Du musst noch die kürzeste Distanz zum Ziel eintragen.");
        } else {
            int finalDist = Integer.parseInt(finTextField.getText());
            if (displayModel.checkFoundShortestPath(finalDist)) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Geschafft!");
                alert.setContentText("Super! Du hast den kürzesten Weg gefunden!");
                finish.setDisable(false);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Noch nicht!");
                alert.setContentText("Du hast noch keinen kürzesten Weg zum Ziel gefunden,\nversuche es noch weiter!");
            }
        }
        alert.show();
    }

    /**
     * Initialize the table where user steps (according to algorithm) are displayed.
     */
    private void initTableView() {
        if (!gxTable.isInitialized()) {
            gxTable.init(displayModel.getAllVertices());
        }
        displayModel.register(gxTable);
    }


    /**
     * Sets the table to be visible on the menuitem being pressed
     */
    @FXML
    private void showTable() {
        //window of primary stage
        Window primaryWindow = finish.getScene().getWindow();
        Stage tableStage = new Stage();
        tableStage.setTitle("Tabelle");
        tableStage.setScene(new Scene(new VBox(gxTable)));
        //table window is bound to main window, so when main window closes, table window should close as well
        tableStage.initOwner(primaryWindow);
        tableStage.show();
    }

    /**
     * Sets the additional actions for a vertex if free mode is activated
     *
     * @param vertexNode the {@link SmartGraphVertexNode} that the free mode actions should be set for
     */
    private void setFreeModeActions(SmartGraphVertexNode vertexNode) {
        vertexNode.setOnMouseReleased((MouseEvent mouseEvent) -> {
            this.onDragFinished(vertexNode);
        });

    }

    /**
     * Sets the actions for all the nodes and edges of the {@link GraphView}.
     * ALl the elements of the view will have actions for double click, single click, drag and so on.
     */
    private void setActions() {
        graphView.setEdgeDoubleClickAction(e -> onSelectEdge(e));
        graphView.setVertexDoubleClickAction(v -> onSelectVertex());


        /*
          Setting the actions for the Vertices
         */
        for (Node vertexNode : graphView.getChildren()) {
            if (vertexNode.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertexNode;
                //Action for clicking primary button
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        onVertexClicked(vert, mouseEvent.getX(), mouseEvent.getY());
                    }
                });
                //Style change when hovering
                vert.setOnMouseEntered(s -> onHoverVertex((SmartGraphVertexNode) s.getSource(), s));
                //Style change when hovering ends
                vert.setOnMouseExited(s -> onHoverVertex((SmartGraphVertexNode) s.getSource(), s));
                if (debugMode) {
                    displayCoordinates(vert);
                }
                if (verticesMoveable.isSelected()) {
                    setFreeModeActions(vert);
                }
            }
        }
    }

    /**
     * Sets coordinates after a vertex was dragged around. Updates the coordinates for the underlying vertex.
     *
     * @param vertexNode the vertex that was dragged.
     */
    private void onDragFinished(SmartGraphVertexNode vertexNode) {
        graphView.setMovedCoordinates(vertexNode);
    }

    /**
     * Changes the style class while the mouse is on a vertex to indicate it being hovered
     *
     * @param vertex     that is being hovered
     * @param mouseEvent whether the vertex was entered or exited
     */
    private void onHoverVertex(SmartGraphVertexNode vertex, MouseEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
            vertex.setStyleClass("hoverVertex");
        } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
            GXVertex vert = (GXVertex) vertex.getUnderlyingVertex();
            if (!vert.getStartOrEnd().equals(GXVertexType.NORMAL)) {
                vertex.setStyleClass(vert.getStartOrEnd().toString());
            } else {
                if (vert.isMarked()) {
                    vertex.setStyleClass("markedVertex");
                } else {
                    vertex.setStyleClass("vertex");
                }
            }
        }
    }

    /**
     * Is called when an edge is selected.
     *
     * @param e is the edge the user selected.
     */
    private void onSelectEdge(SmartGraphEdge e) {
        try {
            displayModel.markEdge((GXEdge) e.getUnderlyingEdge());
            Platform.runLater(() -> {
                setActions();
            });
        } catch (ElementNotInGraphException elementNotInGraphException) {
            new ElementNotInGraphAlert().show();
        } catch (EdgeCompletesACircleException edgeCompletesACircleException) {
            new EdgeCompletesACircleAlert().show();
        }
    }

    /**
     * Is called when the user selects a vertex.
     *
     */
    private void onSelectVertex() {
        new VertexDoubleClickAlert().show();
    }

    /**
     * When the user requests a hint, the next step according to the selected algorithm should be shown.
     */
    @FXML
    private void hintRequest() {
        displayModel.nexStep();
    }

    /**
     * Checks, if the user input corresponds to the steps the selected algorithm would perform.
     */
    @FXML
    private void onCheck() {
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
     * Opens a Filechooser window allowing the selection of a Json file containing the graph data
     */
    @FXML
    private void onLoadGraph() {
        Stage browserStage = new Stage();
        browserStage.setTitle("FileBrowser");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Graph auswaehlen");
        FileChooser.ExtensionFilter jsonFilter
                = new FileChooser.ExtensionFilter("JSON filter (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonFilter);
        File file = fileChooser.showOpenDialog(browserStage);
        if (!(file == null)) {
            loadNewGraphView(file);
        }
    }


    /**
     * When loading a new graph view the old graph view needs to be correctly unlinked from the window and listeners.
     * Also the {@link GraphView} needs to be removed from the parent pane it was part of.
     *
     * @param parent parent of the old {@link GraphView} that it should be removed from
     */
    private void remove(Parent parent, boolean remTable) {
        //uncoupling the old views from listeners and the observer.
        displayModel.unregister(graphView);
        if (remTable) {
            displayModel.unregister(gxTable);
        }
        graphView.removeListener();
        // The Pane that graphView is part of (In this case boder pane)
        Group pane = (Group) parent;
        // Removing the graphView so that later a graphView with other properties can be added.
        pane.getChildren().remove(graphView);
    }

    /**
     * Will set the passed sizes for the parent and graphView.
     *
     * @param prefWidth  preferred Width
     * @param prefHeight preferred Height
     * @param minWidth   minimum Width
     * @param minHeight  minimum Height
     */
    private void setSizes(double prefWidth, double prefHeight, double minWidth, double minHeight) {
        graphView.setMinSize(minWidth, minHeight);
        graphView.setPrefSize(prefWidth, prefHeight);
    }

    private void bind() {
        graphView.prefWidthProperty().bind(graphView.getScene().widthProperty());
        graphView.prefHeightProperty().bind(graphView.getScene().heightProperty());
    }
    /**
     * Will set the size of the graphview pane according to its background.
     *
     * @param background background the size will be calculated from
     */
    private void setSizes(Background background) {
        if (!background.getImages().isEmpty()) {
            Image backgroundImage = background.getImages().get(0).getImage();
            double width = backgroundImage.getWidth();
            double height = backgroundImage.getHeight();
            setSizes(width, height, MIN_PANE_SIZE, calcMinHeight(width, height));
        } else {
            setSizes(STANDARD_PANE_WIDTH, STANDARD_PANE_HEIGHT
                    , STANDARD_PANE_MIN_WIDTH, STANDARD_PANE_MIN_HEIGHT);
        }
    }


    /**
     * Loads a background for a pane. If the file is an image the image will be used as background. If the file
     * isn't an image an empty background will be returned. The size of the background will be the size of the passed
     * image and it will be set to cover.
     *
     * @param file you want the background created from
     * @return the background either from the image or empty
     */
    private Background loadBackground(File file) {

        File imageFile = findBackgroundImage(file);
        BufferedImage image;
        if (imageFile != null) {
            image = checkIfImage(imageFile);
            if (image == null) {
                new Alert(Alert.AlertType.INFORMATION, "Kein Hintergrundbild gefunden").showAndWait();
                return Background.EMPTY;
            } else {
                //Creates ne BackgroundImage if there was an image found.
                Image back = new Image(imageFile.toURI().toString());
                //Creating the new background with all its parameters
                BackgroundSize size = new BackgroundSize(back.getWidth(), back.getHeight()
                        , false, false, false, true);
                BackgroundImage backgroundImage = new BackgroundImage(back, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        size);
                return new Background(backgroundImage);
            }
        }
        return Background.EMPTY;
    }

    /**
     * Adds the {@link GraphView} to the pane and initializes the {@link GXTableView}. Then correctly initializes
     * all the features of the Graphex window
     *
     * @param parent the parent pane that the graphView needs to be added to.
     */
    private void initializeUpdatedView(Group parent, boolean newTable) {
        // Layouting the pane ==> all the children get layouted as well ==> graphView gets height and width
        parent.layout();

        if (newTable) {
            // new Tableview
            this.gxTable = new GXTableView();
        }

        //Reinitializing all the views
        init();

        displayModel.notifyObservers();
    }


    /**
     * Creates and adds a new {@link GraphView} to the pane.
     *
     * @param parent parent the {@link GraphView} should be added to.
     */
    private void addToParent(Group parent) {
        try {
            this.graphView = new GraphView();
        } catch (FileNotFoundException e) {
            Alert fileAlert = new FileAlert(e.getMessage() + "\n wurde nicht gefunden");
            fileAlert.showAndWait();
            e.printStackTrace();
            return;
        }
        parent.getChildren().add(graphView);
    }

    /**
     * Creates and adds a new {@link GraphView} to the pane.
     *
     * @param parent    parent the {@link GraphView} should be added to.
     * @param graphView the {@link GraphView} you want to add
     */
    private void addToParent(Group parent, GraphView graphView) {
        this.graphView = graphView;
        parent.getChildren().add(graphView);
    }

    private void reset() {
        this.debugMode = false;
        finish.setText("Start");
        finish.setDisable(false);
    }

    /**
     * Loads a new graphview from the specified file
     *
     * @param file json that the new view should be loaded from.
     */
    private void loadNewGraphView(File file) {
        //close existing table view window
        if (tableStage != null && tableStage.isShowing()) tableStage.close();
        Group parent = (Group) graphView.getParent();
        //final Pane parent = (Pane) graphView.getParent();

        DisplayModel newModel = null;
        try {
            newModel = new DisplayModel(file);
        } catch (WrongFileFormatException e) {
            Alert formatError = new FileFormatError(e);
            formatError.showAndWait();
            e.printStackTrace();
            return;
        }
        remove(parent, true);
        this.displayModel = newModel;

        // Creating a new graphView and adding it to the pane
        addToParent(parent);

        // creating the background if one is in the same folder as the json
        Background background = loadBackground(file);
        graphView.setBackground(background);
        // Setting the sizes to either standard if there was no background image or to the size of the background image.
        setSizes(background);

        reset();
        // initialising the window again with the new graph view and updating once to display the graph
        initializeUpdatedView(parent, true);
    }

    private void loadNewGraphView(GXGraph graph) {
        Group parent = (Group) graphView.getParent();
        //final Pane parent = (Pane) graphView.getParent();
        remove(parent, true);
        try {
            this.displayModel = new DisplayModel(graph);
        } catch (WrongFileFormatException e) {
            new FileAlert(e.getMessage()).showAndWait();
        }
        addToParent(parent);
        graphView.setBackground(Background.EMPTY);
        setSizes(Background.EMPTY);
        reset();
        initializeUpdatedView(parent, true);
    }

    /**
     * When called the vertices are made moveable
     */
    @FXML
    private void verticesMovable() {
        Group parent = (Group) graphView.getParent();
        Background oldBackground = graphView.getBackground();
        remove(parent, false);
        GraphView movable;
        if (verticesMoveable.isSelected()) {
            try {
                movable = new GraphView(true);
                addToParent(parent, movable);
            } catch (FileNotFoundException e) {
                // Properties not found
                e.printStackTrace();
            }
            verticesMoveable.setSelected(true);
        } else {
            verticesMoveable.setSelected(false);
            //Update with better constructor
            addToParent(parent);
        }

        graphView.setBackground(oldBackground);
        setSizes(oldBackground);
        initializeUpdatedView(parent, false);
        if (!finish.getText().equals("Start")) {
            Platform.runLater(this::setActions);

        }

    }

    /**
     * When this is called, a random Graph will be created an load in the view.
     */
    @FXML
    private void onGenerateRandom() throws IOException {
        Window primaryStage = graphView.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("PropWin.fxml"));
        Scene newScene = new Scene(loader.load());
        Stage propertyWindow = new Stage();
        propertyWindow.setTitle("Random Graph Generator");
        propertyWindow.initOwner(primaryStage);
        propertyWindow.setScene(newScene);
        PropWinController ctr = loader.getController();
        ctr.initText();
        propertyWindow.showAndWait();
        if (PropWinController.lastGenerationSuccessful()) {
            loadNewGraphView(PropWinController.getLastGeneratedGraph());
        }
    }

    /**
     * When a vertex is clicked with single mouse click, shortest path to the vertex is displayed, depending on the
     * selected edges by the user. Additionally a context menu opens, showing the current shortest distance to the
     * clicked vertex.
     *
     * @param v vertex node
     * @param x coordinate of mouse event
     * @param y coordinate of mouse event
     */
    private void onVertexClicked(SmartGraphVertexNode v, double x, double y) {
        GXVertex vertex = (GXVertex) v.getUnderlyingVertex();
        //context menu that displays current distance
        if (vertex.isMarked()) {
            graphView.showVertexDistance(v, x, y);
            displayModel.highlightShortestPathTo(vertex);
        }
    }

    /**
     * This is called, when the user wants to revert a step, i.e. unmark the last selected edge or vertex and
     * everything that comes along with this.
     */
    @FXML
    private void onUndoPressed() {
        try {
            displayModel.undo();
        } catch (ElementNotInGraphException e) {
            Alert a = new ElementNotInGraphAlert();
            a.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Will update the model for the specific algorithm that is selected.
     *
     * @param algo is the algorithm that is selected to be performed at the graph in the view.
     */
    private void onAlgorithmSelect(Algorithm algo) {
        //TODO do something like the vorlagen where the program scans for available algorithms
    }

    /**
     * Will show some information on the selected algorithm in the view.
     */
    @FXML
    private void onDisplayAlgorithmExplanation() {
        Dialog<Void> dialog = new InfoDialog(AlgorithmName.DIJKSTRA);
        dialog.showAndWait();
    }

    /**
     * Will show some information on interaction options in the view.
     */
    @FXML
    private void onDisplayInteractionHelp() {
        Dialog iaDialog = new InteractionDialog();
        iaDialog.showAndWait();
    }

    /**
     * Called on the reset button being pressed
     */
    @FXML
    private void onResetPressed() {
        displayModel.reset();
    }

    /**
     * Will open a texInputDialog to input the password for help and for the debugmode
     *
     */
    @FXML
    private void unlockHints() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Gib das Passwort ein um die"
                + " Hilfefunktionen freizuschalten");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (UNLOCKPASSWORD.equals(result.get())) {
                check.setDisable(false);
                tip.setDisable(false);
            } else if ("Test".equals(result.get())) {
                this.debugMode = true;
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Das war das falsche Passwort.").showAndWait();
            }
        }
    }

    /**
     * Will output a string containing the current relative coordinates as well as the style class of the passed node
     *
     * @param vertexNode the node the coordinates and styl class should be printed of
     */
    private void displayCoordinates(SmartGraphVertexNode vertexNode) {
        vertexNode.setOnMousePressed((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                //Making sure that the position will not be calculated at the same time as it is set by putting
                //this calculation in the Queue for the fx thread
                Platform.runLater(() -> {
                    double x = graphView.calcRelativeX(vertexNode);
                    double y = graphView.calcRelativeY(vertexNode);
                    System.out.println(vertexNode.getUnderlyingVertex().element().toString() + " x = "
                            + x + " , y = " + y + " Style:  " + vertexNode.getStyleClass());
                });
            }
        });

    }

    /**
     * Takes a JSON file for a graph and checks if a background image with the same name is in the folder.
     * Checks for file types like jpg, jpeg, png, bmp
     *
     * @param graph the json file that the graph was saved in
     * @return the {@link File} of the matching background. If there is no {@link File} with this name null is returned.
     */
    private File findBackgroundImage(File graph) {
        String name = graph.getName();
        name = name.substring(0, name.length() - "json".length());
        Path pathToDir = Path.of(graph.getParentFile().getAbsolutePath());
        String[] allowedPictures = new String[IMAGE_FILE_ENDINGS.length];
        for (int i = 0; i < IMAGE_FILE_ENDINGS.length; i++) {
            allowedPictures[i] = name + IMAGE_FILE_ENDINGS[i];
        }
        Path pathToFile = null;
        if (Files.isDirectory(pathToDir)) {
            try {
                DirectoryStream<Path> dirStream = Files.newDirectoryStream(pathToDir);
                for (Path path : dirStream) {
                    pathToFile = path;
                    for (String allowedName : allowedPictures) {
                        if (pathToFile.endsWith(Path.of(allowedName))) {
                            dirStream.close();
                            return new File(String.valueOf(pathToFile));
                        }
                    }
                }
                dirStream.close();
            } catch (IOException e) {
                Alert fileAlert = new FileAlert(pathToDir.toString() + e.getMessage());
                fileAlert.showAndWait();
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Takes a file that could potentially an image and checks if it actually is one.
     *
     * @param imageFile the file that could potentially be an image
     * @return {@code null} if the file wasn't an image. Else the image will be returned as a {@link BufferedImage}
     */
    private BufferedImage checkIfImage(File imageFile) {
        // Check whether it isn't just an image by name but also an image file
        // Try with resources so that the stream is correctly closed if something goes wrong
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            try {
                BufferedImage image = ImageIO.read(inputStream);
                if (image != null) {
                    return image;
                }
            } catch (IOException ioe) {
                Alert fileAlert = new FileAlert(imageFile.getAbsolutePath() + ioe.getMessage());
                fileAlert.showAndWait();
                ioe.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            Alert fileAlert = new FileAlert(imageFile.getAbsolutePath() + e.getMessage());
            fileAlert.showAndWait();
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Calcs a min Height for the pane after an new graph was loaded. Uses the background images' side to side ratio to
     * calculate. As starting point for this it takes minWidth MIN_PANE_SIZE set to 1000.
     *
     * @param width  the width of the background image
     * @param height the height of the background image
     * @return the calc minHeight with width set to 1000, while maintaining aspect ratio
     */
    private int calcMinHeight(double width, double height) {
        double ratio = height / width;
        return (int) (MIN_PANE_SIZE * ratio);
    }

    /**
     * Loads all the templates in the template folder as entries for the "Vorlagen" menu
     *
     */
    private void loadTemplates() {
        File templateFolder = null;

        try {
            templateFolder = new File(new File(getClass().getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile(), PATH_TO_TEMPLATES);
        } catch (URISyntaxException e) {
            new FileFormatError(new WrongFileFormatException(e.getMessage()));
        }
        if (!templateFolder.isDirectory()) {
            Alert fileAlert = new FileAlert(templateFolder.getAbsolutePath() + "\n An diesem Pfad ist kein Ordner.");
            fileAlert.showAndWait();
            return;
        }


        try (DirectoryStream<Path> folderStream = Files.newDirectoryStream(templateFolder.toPath())) {
            for (Path template : folderStream) {
                if (template.toString().endsWith(".json")) {
                    File graphTemplate = new File(String.valueOf(template));
                    String name = graphTemplate.getName();
                    name = name.substring(0, name.length() - ".json".length());
                    name = name.replace("_", " ");
                    MenuItem item = new MenuItem(name);
                    String finalName = name;
                    if (templates.getItems().isEmpty()
                            || templates.getItems().stream().
                            noneMatch(menuItem -> menuItem.getText().equals(finalName))) {
                        templates.getItems().add(item);
                        item.setOnAction(e -> loadNewGraphView(graphTemplate));
                    }
                }
            }
        } catch (IOException e) {
            Alert fileAlert = new FileAlert(templateFolder.getAbsolutePath());
            fileAlert.showAndWait();
            e.printStackTrace();
        }
    }
}
