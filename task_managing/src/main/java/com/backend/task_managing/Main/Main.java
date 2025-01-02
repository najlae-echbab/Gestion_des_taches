package com.backend.task_managing.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        URL fxmlLocation = getClass().getResource("/com/backend/task_managing/View/hello-view.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("Fichier hello-view.fxml introuvable !");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Gestion de Tâches");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
