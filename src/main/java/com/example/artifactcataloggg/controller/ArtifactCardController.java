package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.model.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the ArtifactCard component that displays a preview of an artifact
 */
public class ArtifactCardController {
    private static final Logger LOGGER = Logger.getLogger(ArtifactCardController.class.getName());
    
    @FXML
    private ImageView artifactImageView;
    
    @FXML
    private Label artifactNameLabel;
    
    private Artifact artifact;
    
    /**
     * Set the artifact to display in this card
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
        updateDisplay();
    }
    
    /**
     * Get the artifact displayed in this card
     */
    public Artifact getArtifact() {
        return artifact;
    }
    
    /**
     * Update the display with the current artifact data
     */
    private void updateDisplay() {
        if (artifact == null) {
            artifactNameLabel.setText("No Artifact");
            setDefaultImage();
            return;
        }
        
        artifactNameLabel.setText(artifact.getArtifactName());
        loadArtifactImage();
    }
    
    /**
     * Load the artifact image from its path
     */
    private void loadArtifactImage() {
        String imagePath = artifact.getImagePath();
        
        if (imagePath == null || imagePath.isEmpty()) {
            setDefaultImage();
            return;
        }
        
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                artifactImageView.setImage(image);
            } else {
                // Try to load from resources if not found as a file
                String resourcePath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
                Image image = new Image(getClass().getResourceAsStream(resourcePath));
                artifactImageView.setImage(image);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load image: " + imagePath, e);
            setDefaultImage();
        }
    }
    
    /**
     * Set a default placeholder image
     */
    private void setDefaultImage() {
        try {
            Image placeholderImage = new Image(getClass().getResourceAsStream("/artifactImages/placeholder.png"));
            artifactImageView.setImage(placeholderImage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load placeholder image", e);
        }
    }
}
