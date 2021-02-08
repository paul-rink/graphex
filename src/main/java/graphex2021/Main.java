package graphex2021;

import graphex2021.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //init primary stage: GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("graphex2021/GraphexMainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("GraphEx");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        

        Controller graphController = loader.getController();
        graphController.init();
    }
}
