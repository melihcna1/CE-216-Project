package com.example.artifactcataloggg;

import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
public class ArtifactController {


    private ArtifactRepository repository;

    public ArtifactController() {
        this.repository = new ArtifactRepository();
    }

    public void createArtifact(Artifact artifact) {
        repository.addArtifact(artifact);
    }
    public List<Artifact> getAllArtifacts() {
        return repository.getArtifacts();
    }
    public void handleDeleteArtifact(String artifactID) {
        if (artifactID == null || artifactID.isEmpty()) {
            System.out.println("Artifact ID cannot be null or empty!");
            return;
        }

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete this artifact?");
        confirmationAlert.setContentText("Artifact ID: " + artifactID);

        // Wait for user response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Proceed with deletion
                repository.deleteArtifact(artifactID);
                System.out.println("Artifact with ID " + artifactID + " has been deleted.");
            } else {
                // Cancel deletion
                System.out.println("Deletion canceled.");
            }
        });
    }
    public void handleAddArtifact(String id, String name) {
        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("Artifact ID ve Name boş olamaz!");
            return;
        }

        Artifact newArtifact = new Artifact(id, name, "Metal", "Ancient", "Rome",
                "Gold", "200 BC", "Museum", 10, 10, 10, 10, Arrays.asList("Ancient", "Gold"), "image.path");

        createArtifact(newArtifact);

    }

}

