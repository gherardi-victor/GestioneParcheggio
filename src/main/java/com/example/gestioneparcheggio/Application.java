package com.example.gestioneparcheggio;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    static boolean caricato = false;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Gestione Parcheggio");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        caricato = true;
    }
    public static void main(String[] args) {
        launch();
    }
}