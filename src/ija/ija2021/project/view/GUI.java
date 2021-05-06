package ija.ija2021.project.view;

import ija.ija2021.project.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("iny.fxml"));
        Parent root = loader.load();
        Controller c = loader.getController();
        c.init();

        primaryStage.setTitle("Warehouse simulation");
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void launchApp(String[] args) {
        launch(args);
    }

}
