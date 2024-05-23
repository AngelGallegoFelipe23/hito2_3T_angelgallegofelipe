package com.empresa.hito_angelgallegofelipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController helloController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 700);
        stage.setTitle("Fruteria Gallego");
        stage.setScene(scene);
        stage.show();

        // Obtener el controlador
        helloController = fxmlLoader.getController();
    }

    @Override
    public void stop() throws Exception {
        // Cerrar conexión con MongoDB
        if (helloController != null) {
            helloController.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
