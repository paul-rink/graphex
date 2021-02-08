package graphex2021.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.Main;
import graphex2021.model.*;
import graphex2021.view.GXTableView;
import graphex2021.view.GraphView;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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
    private static final String PATTERN_FIN_TEXT = "-??[0-9]+";
    private static final String[] IMAGE_FILE_ENDINGS = new String[]{"jpeg", "jpg", "png", "bmp"};
    private static final int MIN_PANE_SIZE = 1000;
    private static final String PATH_TO_TEMPLATES = "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "graphex2021"
            + File.separator + "GraphData" + File.separator + "Templates";


    /**
     * The {@link DisplayModel}, this controller sets the actions for.
     */
    private DisplayModel displayModel;

    private GXTableView gxTable;

    @FXML
    private Menu templates;

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

    @FXML
    private CheckMenuItem verticesMoveable;

    /**
     * Create a new Controller, where the {@link DisplayModel} is newly created
     * by using the standard {@link graphex2021.model.GXGraph}690
     */
    public Controller() {
        System.out.println("---------------------controller construct----------------------------");
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
        initTableView();
        displayModel.notifyObservers();
    }

    /**
     * When program is launched this can be called to notify the model that graphview is about to be initialized.
     */
    public void initGraphView() {
        displayModel.register(graphView);
        graphView.setAutomaticLayout(false);
        this.verticesMoveable.setSelected(graphView.isMoveable());
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

    public void setFreeModeActions() {
        for (Node vertexNode : graphView.getChildren()) {
            if (vertexNode.toString().contains("Circle")) {
                vertexNode.setOnMouseReleased((MouseEvent mouseEvent) -> {
                    this.onDragFinished((SmartGraphVertexNode) vertexNode);
                });
            }
        }
    }

    public void setActions() {
        graphView.setEdgeDoubleClickAction(e -> onSelectEdge((SmartGraphEdge) e));
        graphView.setVertexDoubleClickAction(v -> onSelectVertex((SmartGraphVertex) v));


        for (Node vertexNode : graphView.getChildren()) {
            if (vertexNode.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertexNode;
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        onVertexClicked(vert, mouseEvent.getX(), mouseEvent.getY());
                    }
                });
                vert.setOnMouseEntered(s -> onHoverVertex((SmartGraphVertexNode) s.getSource(), s));
                vert.setOnMouseExited(s -> onHoverVertex((SmartGraphVertexNode) s.getSource(), s));
            }
        }
        if (verticesMoveable.isSelected()) {
            setFreeModeActions();
        }
    }

    private void onDragFinished(SmartGraphVertexNode vertexNode) {
        graphView.setMovedCoordinates(vertexNode);
    }

    public void onHoverVertex(SmartGraphVertexNode vertex, MouseEvent mouseEvent) {
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

    public void onHoverEdge(SmartGraphVertexNode e) {
        e.setStyleClass("testClass");
    }

    public void onLeaveEdge(SmartGraphVertexNode e) {
        e.setStyleClass("vertex");
    }

    /**
     * Is called when an edge is selected.
     *
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
     *
     * @param v is the selected vertex.
     */
    public void onSelectVertex(SmartGraphVertex v) {
        new VertexDoubleClickAlert().show();
    }

    /**()
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
            loadNewGraphView(file);
        }
    }


    /**
     * When loading a new graph view the old graph view needs to be correctly unlinked from the window and listeners.
     * Also the {@link GraphView} needs to be removed from the parent pane it was part of.
     *
     * @param parent parent of the old {@link GraphView} that it should be removed from
     */
    private void remove(Parent parent) {
        //uncoupling the old views from listeners and the observer.
        displayModel.unregister(graphView);
        displayModel.unregister(gxTable);
        graphView.removeListener();
        // The Pane that graphView is part of (In this case boder pane)
        Pane pane  = (Pane) parent;
        // Removing the graphView so that later a graphView with other properties can be added.
        pane.getChildren().remove(graphView);
    }

    /**
     * Will set the passed sizes for the parent and graphView.
     * @param parent the parent pane you want to set the size for
     * @param prefWidth preferred Width
     * @param prefHeight preferred Height
     * @param minWidth minimum Width
     * @param minHeight minimum Height
     */
    private void setSizes(Pane parent, double prefWidth, double prefHeight, double minWidth, double minHeight) {
        parent.setPrefSize(prefWidth, prefHeight);
        parent.setMinSize(minWidth, minHeight);
        graphView.setPrefSize(prefWidth, prefHeight);
        graphView.setMinSize(prefWidth, prefHeight);
    }

    private void setSizes(Pane parent, Background background) {
        if (!background.isEmpty()) {
            if (!background.getImages().isEmpty()) {
                Image backgroundImage = background.getImages().get(0).getImage();
                double width = backgroundImage.getWidth();
                double height = backgroundImage.getHeight();
                setSizes(parent, width, height, MIN_PANE_SIZE, calcMinHeight(width, height));
            }
        } else {
            setSizes(parent, STANDARD_PANE_WIDTH, STANDARD_PANE_HEIGHT
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
    private void initializeUpdatedView(Pane parent) {
        // Layouting the pane ==> all the children get layouted as well ==> graphView gets height and width
        parent.layout();

        // new Tableview
        this.gxTable = new GXTableView();

        //Reinitializing all the views
        init();
        displayModel.notifyObservers();
    }

    /**
     * Creates and adds a new {@link GraphView} to the pane.
     *
     * @param parent parent the {@link GraphView} should be added to.
     */
    private void addToParent(Pane parent) {
        try {
            this.graphView = new GraphView();
        } catch (FileNotFoundException e) {
            Alert fileAlert = new FileAlert(e.getMessage()+ "\n wurde nicht gefunden");
            fileAlert.showAndWait();
            e.printStackTrace();
            return;
        }
        parent.getChildren().add(graphView);
    }

    /**
     * Creates and adds a new {@link GraphView} to the pane.
     *
     * @param parent parent the {@link GraphView} should be added to.
     * @param graphView the {@link GraphView} you want to add
     */
    private void addToParent(Pane parent, GraphView graphView) {
        this.graphView = graphView;
        parent.getChildren().add(graphView);
    }

    private void reset() {
        finish.setText("Start");
        finish.setDisable(false);
    }

    /**
     * Loads a new graphview from the specified file
     * @param file json that the new view should be loaded from.
     */
    private void loadNewGraphView(File file) {
        final Pane parent = (Pane) graphView.getParent();

        DisplayModel newModel = null;
        try {
            newModel = new DisplayModel(file);
        } catch (WrongFileFormatException e) {
            Alert formatError = new FileFormatError(e);
            formatError.showAndWait();
            e.printStackTrace();
            return;
        }
        remove(parent);
        this.displayModel = newModel;

        // Creating a new graphView and adding it to the pane
        addToParent(parent);

        // creating the background if one is in the same folder as the json
        Background background = loadBackground(file);
        parent.setBackground(background);
        // Setting the sizes to either standard if there was no background image or to the size of the background image.
        setSizes(parent, background);

        reset();

        // initialising the window again with the new graph view and updating once to display the graph
        initializeUpdatedView(parent);
    }

    private void loadNewGraphView(GXGraph graph) {
        final Pane parent = (Pane) graphView.getParent();
        remove(parent);
        this.displayModel = new DisplayModel(graph);
        addToParent(parent);
        parent.setBackground(Background.EMPTY);
        setSizes(parent, STANDARD_PANE_WIDTH, STANDARD_PANE_HEIGHT, STANDARD_PANE_MIN_WIDTH, STANDARD_PANE_MIN_HEIGHT);
        reset();
        initializeUpdatedView(parent);
    }

    /**
     * When called the vertices are made moveable
     *
     */
    public void verticesMovable() {
        final Pane parent = (Pane) graphView.getParent();
        Background oldBackground = parent.getBackground();
        remove(parent);
        GraphView movable;
        if (verticesMoveable.isSelected()) {
            try {
                movable = new GraphView(true);
                addToParent(parent, movable);
            } catch (FileNotFoundException e) {
                // Properties not found
                e.printStackTrace();
                movable = null;

            }
            verticesMoveable.setSelected(true);
        } else {
            //TODO make Graph load unmoveable
            verticesMoveable.setSelected(false);
            //Update with better constructor
            addToParent(parent);
        }


        setSizes(parent, oldBackground);
        initializeUpdatedView(parent);
        if (!finish.getText().equals("Start")) {
            setActions();
        }

    }

    /**
     * When this is called, a random Graph will be created an load in the view.
     */
    public void onGenerateRandom() throws IOException {
        Window primaryStage = graphView.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("PropWin.fxml"));
        Scene newScene = new Scene(loader.load());
        Stage propertyWindow = new Stage();
        propertyWindow.setTitle("Random Graph Generator");
        propertyWindow.initOwner(primaryStage);
        propertyWindow.setScene(newScene);
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
    public void onVertexClicked(SmartGraphVertexNode v, double x, double y) {
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
    public void onUndoPressed() {
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
    public void onAlgorithmSelect(Algorithm algo) {
    //TODO do something like the vorlagen where the program scans for available algorithms
    }

    /**
     * Will show some information on the selected algorithm in the view.
     */
    public void onDisplayAlgorithmExplanation() {
        Dialog<Void> dialog = new InfoDialog(AlgorithmName.DIJKSTRA);
        dialog.showAndWait();
    }

    /**
     * Will show some information on interaction options in the view.
     */
    public void onDisplayInteractionHelp() {
        Dialog iaDialog = new InteractionDialog();
        iaDialog.showAndWait();
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
                new Alert(Alert.AlertType.INFORMATION, "Das war das falsche Passwort.").showAndWait();
            }
        }
    }

    private void displayCoordinates() {
        for (Node vertex : graphView.getChildren()) {
            if (vertex.toString().contains("Circle")) {
                SmartGraphVertexNode vert = (SmartGraphVertexNode) vertex;
                vert.setOnMousePressed((MouseEvent mouseEvent) -> {
                    if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                        double x = graphView.calcRelativeX(vert);
                        double y = graphView.calcRelativeY(vert);
                        System.out.println(vert.getUnderlyingVertex().element().toString() + " x = "
                                + x + " , y = " + y + " Style:  " + vertex.getStyleClass());

                    }
                });
            }
        }
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
     * @param width the width of the background image
     * @param height the height of the background image
     * @return the calc minHeight with width set to 1000, while maintaining aspect ratio
     */
    private int calcMinHeight(double width, double height) {
        double ratio = height / width;
        return (int) (MIN_PANE_SIZE * ratio);
    }

    private void loadTemplates() {
        File templateFolder = null;

        templateFolder = new File(PATH_TO_TEMPLATES);
        if (!templateFolder.isDirectory()) {
            Alert fileAlert = new FileAlert(templateFolder.getAbsolutePath()+ "\n An diesem Pfad ist kein Ordner.");
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
                            || templates.getItems().stream().noneMatch(menuItem -> menuItem.getText().equals(finalName))) {
                        templates.getItems().add(item);
                        item.setOnAction(e -> loadNewGraphView(graphTemplate));
                    }
                }
            }
        } catch (IOException e) {
            Alert fileAlert = new FileAlert(templateFolder.getAbsolutePath());
            fileAlert.showAndWait();
            e.printStackTrace();
            return;
        }


    }
}
