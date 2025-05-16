package com.example.artifactcataloggg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class for the Artifact Catalog application
 */
public class ArtifactCatalogApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(ArtifactCatalogApp.class.getName());
    private static final String MAIN_SCREEN_FXML = "/com/example/artifactcataloggg/MainScreen.fxml";
    private static final String APPLICATION_ICON = "/artifactImages/Museum.png";
    
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_SCREEN_FXML));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage.setTitle("Artifact Catalog");
            
            // Set application icon
            stage.getIcons().add(new Image(getClass().getResourceAsStream(APPLICATION_ICON)));
            
            stage.setScene(scene);
            stage.show();
            
            LOGGER.log(Level.INFO, "Application started successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 