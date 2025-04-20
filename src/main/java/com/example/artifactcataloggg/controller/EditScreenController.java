package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.UUID;

public class EditScreenController {

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
    @FXML private javafx.scene.image.ImageView artifactImageView;
    private String selectedImagePath = null;
    private boolean isEditMode = false;
    private Artifact artifact;

    public void setEditMode(boolean isEdit, Artifact artifact) {
        this.isEditMode = isEdit;
        this.artifact = artifact;

        if (isEdit && artifact != null) {
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
            tagsArea.setText(String.join(", ", artifact.getTags()));
        }
    }

    @FXML
    private void onSaveClick(ActionEvent event) {
        if (isEditMode && artifact != null) {
            updateArtifactFromFields(artifact);
            ArtifactRepository.getInstance().updateArtifact(artifact);
        } else {
            Artifact newArtifact = new Artifact();
            newArtifact.setArtifactID(UUID.randomUUID().toString());
            updateArtifactFromFields(newArtifact);
            ArtifactRepository.getInstance().addArtifact(newArtifact);
        }

        closeWindow(event);
    }

    private void updateArtifactFromFields(Artifact a) {
        a.setArtifactName(artifactNameField.getText());
        a.setCategory(categoryField.getText());
        a.setCivilization(civilizationField.getText());
        a.setDiscoveryLocation(discoveryLocationField.getText());
        a.setComposition(compositionField.getText());
        a.setDiscoveryDate(discoveryDateField.getText());
        a.setCurrentPlace(currentPlaceField.getText());
        a.setWidth(Double.parseDouble(widthField.getText()));
        a.setLength(Double.parseDouble(lengthField.getText()));
        a.setHeight(Double.parseDouble(heightField.getText()));
        a.setWeight(Double.parseDouble(weightField.getText()));
        a.setTags(Arrays.asList(tagsArea.getText().split(",\s*")));
        if (selectedImagePath != null) {
            a.setImagePath(selectedImagePath);
        }

    }
    @FXML
    private void onAddImageClick(ActionEvent event) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Artifact Image");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        java.io.File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            artifactImageView.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
        }
    }



    @FXML
    private void onCancelClick(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
