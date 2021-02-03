package graphex2021;

import graphex2021.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene GRAPH_PROPERTY_SCENE = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //init primary stage: GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphexMainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("GraphEx");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        //init Scene for property window for random graph
        FXMLLoader loaderPropWin = new FXMLLoader(getClass().getResource("PropWin.fxml"));
        GRAPH_PROPERTY_SCENE = new Scene(loaderPropWin.load());

        Controller graphController = loader.getController();
        graphController.init();
    }
}
