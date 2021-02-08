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
        System.out.println("---------------------loader----------------------------");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphex2021/GraphexMainWindow.fxml"));
        System.out.println("--------------------root-----------------------------");
        Parent root = loader.load();
        primaryStage.setTitle("GraphEx");
        System.out.println("------------------stage-------------------------------");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println("---------------------controller----------------------------");
        Controller graphController = loader.getController();
        System.out.println("------------------------init-------------------------");
        graphController.init();
    }
}
