package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.model.Artifact;
import com.example.artifactcataloggg.service.ArtifactService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for the artifact edit/add screen
 */
public class EditScreenController {
    private static final Logger LOGGER = Logger.getLogger(EditScreenController.class.getName());
    
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
    @FXML private TextArea tagsArea;
    @FXML private ImageView artifactImageView;
    
    private Artifact artifact;
    private String imagePath;
    private final ArtifactService artifactService;
    private boolean isEditMode = false;
    
    /**
     * Constructor
     */
    public EditScreenController() {
        this.artifactService = ArtifactService.getInstance();
    }
    
    /**
     * Set the artifact to edit
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
        this.isEditMode = true;
        populateFields();
    }
    
    /**
     * Initialize new artifact mode
     */
    public void initializeNewArtifact() {
        this.artifact = new Artifact();
        this.artifact.setArtifactID(generateArtifactId());
        this.isEditMode = false;
        clearFields();
    }
    
    /**
     * Populate the form fields with artifact data
     */
    private void populateFields() {
        if (artifact == null) return;
        
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
        
        // Join tags with commas
        String tagsText = String.join(", ", artifact.getTags());
        tagsArea.setText(tagsText);
        
        // Load image
        this.imagePath = artifact.getImagePath();
        loadImage();
    }
    
    /**
     * Clear all form fields
     */
    private void clearFields() {
        artifactNameField.clear();
        categoryField.clear();
        civilizationField.clear();
        discoveryLocationField.clear();
        compositionField.clear();
        discoveryDateField.clear();
        currentPlaceField.clear();
        widthField.setText("0");
        lengthField.setText("0");
        heightField.setText("0");
        weightField.setText("0");
        tagsArea.clear();
        this.imagePath = null;
        setDefaultImage();
    }
    
    /**
     * Generate a unique ID for new artifacts
     */
    private String generateArtifactId() {
        return "ART-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Handle the save button click
     */
    @FXML
    private void onSaveClick() {
        if (!validateInput()) {
            return;
        }
        
        updateArtifactFromFields();
        
        boolean success;
        if (isEditMode) {
            success = artifactService.updateArtifact(artifact);
        } else {
            success = artifactService.addArtifact(artifact);
        }
        
        if (success) {
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", 
                    isEditMode ? "Failed to update artifact" : "Failed to add artifact");
        }
    }
    
    /**
     * Handle the cancel button click
     */
    @FXML
    private void onCancelClick() {
        closeWindow();
    }
    
    /**
     * Handle the add image button click
     */
    @FXML
    private void onAddImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Artifact Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(artifactImageView.getScene().getWindow());
        if (selectedFile != null) {
            this.imagePath = selectedFile.getAbsolutePath();
            loadImage();
        }
    }
    
    /**
     * Load the artifact image from its path
     */
    private void loadImage() {
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
    
    /**
     * Validate form input
     */
    private boolean validateInput() {
        if (artifactNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Artifact name is required");
            return false;
        }
        
        // Validate numeric fields
        try {
            if (!widthField.getText().trim().isEmpty()) {
                Double.parseDouble(widthField.getText());
            }
            if (!lengthField.getText().trim().isEmpty()) {
                Double.parseDouble(lengthField.getText());
            }
            if (!heightField.getText().trim().isEmpty()) {
                Double.parseDouble(heightField.getText());
            }
            if (!weightField.getText().trim().isEmpty()) {
                Double.parseDouble(weightField.getText());
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Width, Length, Height, and Weight must be valid numbers");
            return false;
        }
        
        return true;
    }
    
    /**
     * Update the artifact object with values from form fields
     */
    private void updateArtifactFromFields() {
        artifact.setArtifactName(artifactNameField.getText().trim());
        artifact.setCategory(categoryField.getText().trim());
        artifact.setCivilization(civilizationField.getText().trim());
        artifact.setDiscoveryLocation(discoveryLocationField.getText().trim());
        artifact.setComposition(compositionField.getText().trim());
        artifact.setDiscoveryDate(discoveryDateField.getText().trim());
        artifact.setCurrentPlace(currentPlaceField.getText().trim());

        // Parse numeric fields with defaults if empty
        artifact.setWidth(widthField.getText().trim().isEmpty() ? 0 :
                Double.parseDouble(widthField.getText().trim()));
        artifact.setLength(lengthField.getText().trim().isEmpty() ? 0 :
                Double.parseDouble(lengthField.getText().trim()));
        artifact.setHeight(heightField.getText().trim().isEmpty() ? 0 :
                Double.parseDouble(heightField.getText().trim()));
        artifact.setWeight(weightField.getText().trim().isEmpty() ? 0 :
                Double.parseDouble(weightField.getText().trim()));

        // Tags işlemi — tüm boşlukları ve boş elemanları temizler
        String rawTags = tagsArea.getText().trim()
                .replaceAll("\\s*,\\s*", ",") // boşluklu virgülleri sadeleştir
                .replaceAll(",{2,}", ",")     // ardışık virgülleri teke indir
                .replaceAll("^,|,$", "");     // baştaki veya sondaki virgülü sil

        List<String> tags = Arrays.stream(rawTags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());
        artifact.setTags(tags);

        // Update image path
        artifact.setImagePath(imagePath);
    }



    /**
     * Close the window
     */
    private void closeWindow() {
        Stage stage = (Stage) artifactNameField.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
