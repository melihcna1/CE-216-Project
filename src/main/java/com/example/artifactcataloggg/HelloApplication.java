package com.example.artifactcataloggg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();
        ArtifactController controller = new ArtifactController();

      //  Artifact newArtifact = new Artifact("A002", "Ancient Vase", "Ceramic", "Greek", "Athens", "Clay", "500 BC", "British Museum", 30, 30, 30,30, Arrays.asList("Antique", "Greek", "Handmade"));

       // controller.createArtifact(newArtifact);
      //  controller.deleteArtifact("A004");
        controller.handleAddArtifact("A005", "Ancient Sword");
        System.out.println("Mevcut Artifactler:");
        for (Artifact artifact : controller.getAllArtifacts()) {
            System.out.println(artifact);
        }
    }
}