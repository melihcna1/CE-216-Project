package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.model.Artifact;
import com.example.artifactcataloggg.repository.JsonArtifactRepository;
import com.example.artifactcataloggg.service.ArtifactService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UIController {
    private static final Logger LOGGER = Logger.getLogger(UIController.class.getName());

    @FXML private TextField artifactIdField;
    @FXML private TextField tagsField;
    @FXML private TextField artifactIDField;
    @FXML private TextField artifactNameField;
    @FXML private TextField categoryField;
    @FXML private TextField civilizationField;
    @FXML private TextField discoveryLocationField;
    @FXML private TextField compositionField;
    @FXML private TextField discoveryDateField;
    @FXML private TextField currentPlaceField;
    @FXML private TextField widthField;
    @FXML private TextField lengthField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private Button okButton;
    @FXML private Button selectImageButton;
    @FXML private ImageView artifactImageView;

    private String selectedImagePath = null;
    private final ArtifactService artifactService = ArtifactService.getInstance();
    private Stage editStage;
    private Artifact selectedArtifact;

    @FXML private ListView<Artifact> artifactListView;
    @FXML private ObservableList<Artifact> artifactData = FXCollections.observableArrayList();

    @FXML
    private void handleAddArtifact() {
        try {
            String artifactID = artifactIDField.getText().trim();
            String name = artifactNameField.getText().trim();
            String category = categoryField.getText().trim();
            String civilization = civilizationField.getText().trim();
            String discoveryLocation = discoveryLocationField.getText().trim();
            String composition = compositionField.getText().trim();
            String discoveryDate = discoveryDateField.getText().trim();
            String currentPlace = currentPlaceField.getText().trim();

            // Validate required fields
            if (artifactID.isEmpty() || name.isEmpty() || category.isEmpty() || widthField.getText().isEmpty()) {
                showAlert("Please fill in all required fields.");
                return;
            }

            // Check if artifact with same ID already exists
            if (artifactService.artifactExists(artifactID)) {
                showAlert("An artifact with this ID already exists.");
                return;
            }

            double width = tryParseDouble(widthField.getText(), "Width");
            double length = tryParseDouble(lengthField.getText(), "Length");
            double height = tryParseDouble(heightField.getText(), "Height");
            double weight = tryParseDouble(weightField.getText(), "Weight");

            List<String> tags = Arrays.stream(tagsField.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            Artifact newArtifact = new Artifact(artifactID, name, category, civilization,
                    discoveryLocation, composition, discoveryDate,
                    currentPlace, width, length, height, weight, tags, selectedImagePath);

            artifactService.addArtifact(newArtifact);
            artifactData.add(newArtifact);
            artifactListView.setItems(artifactData);

            LOGGER.info("Artifact added successfully: " + artifactID);
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Invalid numeric input: " + e.getMessage());
            LOGGER.log(Level.WARNING, "Invalid numeric input", e);
        }
    }

    private double tryParseDouble(String text, String fieldName) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " value is invalid: " + text);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            try {
                artifactImageView.setImage(new Image(selectedFile.toURI().toString()));
                LOGGER.info("Selected image: " + selectedImagePath);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to load image", e);
                showAlert("Failed to load image: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        artifactIDField.clear();
        artifactNameField.clear();
        categoryField.clear();
        civilizationField.clear();
        discoveryLocationField.clear();
        compositionField.clear();
        discoveryDateField.clear();
        currentPlaceField.clear();
        widthField.clear();
        lengthField.clear();
        heightField.clear();
        weightField.clear();
        tagsField.clear();
        artifactImageView.setImage(null);
        selectedImagePath = null;
    }


    @FXML
    private void editArtifact() {
        selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();

        if (selectedArtifact == null) {
            showAlert("No artifact selected for editing!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController editController = loader.getController();
            editController.setArtifact(selectedArtifact);

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.setScene(new Scene(root));
            this.editStage = stage;

            editStage.showAndWait();
            
            // Refresh the list after editing
            refreshArtifactList();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening edit dialog", e);
            showAlert("Error opening edit dialog: " + e.getMessage());
        }
    }
    
    /**
     * Refresh the artifact list with current data
     */
    private void refreshArtifactList() {
        artifactData.clear();
        artifactData.addAll(artifactService.getAllArtifacts());
        artifactListView.refresh();
    }

    /**
     * Set the data for the selected artifact during editing
     */
    public void setArtifactData(Artifact artifact) {
        this.selectedArtifact = artifact;

        artifactIdField.setText(artifact.getArtifactID());
        artifactNameField.setText(artifact.getArtifactName());
        categoryField.setText(artifact.getCategory());
        civilizationField.setText(artifact.getCivilization());
        discoveryLocationField.setText(artifact.getDiscoveryLocation());
        compositionField.setText(artifact.getComposition());
        discoveryDateField.setText(artifact.getDiscoveryDate());
        currentPlaceField.setText(artifact.getCurrentPlace());
        widthField.setText(String.valueOf(artifact.getWidth()));
        lengthField.setText(String.valueOf(artifact.getLength()));
        heightField.setText(String.valueOf(artifact.getHeight()));
        weightField.setText(String.valueOf(artifact.getWeight()));
        tagsField.setText(String.join(",", artifact.getTags()));

        if (artifact.getImagePath() != null && !artifact.getImagePath().isEmpty()) {
            loadImage(artifact.getImagePath());
            selectedImagePath = artifact.getImagePath();
        }
    }
    
    /**
     * Load an image from the provided path
     */
    private void loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                artifactImageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                // Try to load from resources if not found as a file
                String resourcePath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
                Image image = new Image(getClass().getResourceAsStream(resourcePath));
                artifactImageView.setImage(image);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load image: " + imagePath, e);
            artifactImageView.setImage(null);
        }
    }

    @FXML
    private void handleEditSave() {
        if (selectedArtifact != null) {
            try {
                selectedArtifact.setArtifactID(artifactIdField.getText());
                selectedArtifact.setArtifactName(artifactNameField.getText());
                selectedArtifact.setCategory(categoryField.getText());
                selectedArtifact.setCivilization(civilizationField.getText());
                selectedArtifact.setDiscoveryLocation(discoveryLocationField.getText());
                selectedArtifact.setComposition(compositionField.getText());
                selectedArtifact.setDiscoveryDate(discoveryDateField.getText());
                selectedArtifact.setCurrentPlace(currentPlaceField.getText());
                selectedArtifact.setWidth(tryParseDouble(widthField.getText(), "Width"));
                selectedArtifact.setLength(tryParseDouble(lengthField.getText(), "Length"));
                selectedArtifact.setHeight(tryParseDouble(heightField.getText(), "Height"));
                selectedArtifact.setWeight(tryParseDouble(weightField.getText(), "Weight"));

                List<String> tags = Arrays.stream(tagsField.getText().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                selectedArtifact.setTags(tags);

                // Update image path if a new image was selected
                if (selectedImagePath != null) {
                    selectedArtifact.setImagePath(selectedImagePath);
                }

                // Save changes
                artifactService.updateArtifact(selectedArtifact);
                
                LOGGER.info("Artifact updated successfully: " + selectedArtifact.getArtifactID());
                
                artifactListView.refresh(); // Refresh UI
                if (editStage != null) {
                    editStage.close();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error saving artifact", e);
                showAlert("Error saving artifact: " + e.getMessage());
            }
        }
    }
}

