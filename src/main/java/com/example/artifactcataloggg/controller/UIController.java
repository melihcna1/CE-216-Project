package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UIController {

    @FXML
    private TextField artifactIDField;
    @FXML
    private TextField artifactNameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField civilizationField;
    @FXML
    private TextField discoveryLocationField;
    @FXML
    private TextField compositionField;
    @FXML
    private TextField discoveryDateField;
    @FXML
    private TextField currentPlaceField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField lengthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightField; // Yeni eklenen alan
    @FXML
    private TextField tagsField;
    @FXML
    private Button okButton;
    @FXML
    private Button selectImageButton; // Resim seçme butonu
    @FXML
    private ImageView artifactImageView; // Seçilen resmi gösterecek alan

    private String selectedImagePath = null; // Seçilen resmin yolu

    private ArtifactRepository artifactRepository = new ArtifactRepository();

    @FXML
    private void handleAddArtifact() {
        try {
            String artifactID = artifactIDField.getText();
            String name = artifactNameField.getText();
            String category = categoryField.getText();
            String civilization = civilizationField.getText();
            String discoveryLocation = discoveryLocationField.getText();
            String composition = compositionField.getText();
            String discoveryDate = discoveryDateField.getText();
            String currentPlace = currentPlaceField.getText();
            double width = Double.parseDouble(widthField.getText());
            double length = Double.parseDouble(lengthField.getText());
            double height = Double.parseDouble(heightField.getText());
            double weight = Double.parseDouble(weightField.getText()); // Yeni eklenen alan
            List<String> tags = Arrays.asList(tagsField.getText().split(","));

            Artifact newArtifact = new Artifact(artifactID, name, category, civilization,
                    discoveryLocation, composition, discoveryDate,
                    currentPlace, width, length, height, weight, tags, selectedImagePath);

            artifactRepository.addArtifact(newArtifact);

            System.out.println("Artifact başarıyla eklendi: " + artifactID);

            // UI temizleme (İsteğe bağlı)
            clearFields();

        } catch (NumberFormatException e) {
            System.out.println("Hata: Sayısal değerler yanlış girildi.");
        }
    }
    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            selectedImagePath = selectedFile.toURI().toString(); // JavaFX için uygun URI formatı
            artifactImageView.setImage(new Image(selectedImagePath));
            System.out.println("Seçilen resim: " + selectedImagePath);
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
        weightField.clear(); // Yeni eklenen alanı temizleme
        tagsField.clear();
        artifactImageView.setImage(null);
        selectedImagePath = null;
    }
}