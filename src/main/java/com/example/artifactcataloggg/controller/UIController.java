package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class UIController {

    @FXML
    private TextField artifactIdField;
    @FXML
    private TextField tagsField;
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
    private Button okButton;
    @FXML
    private Button selectImageButton; // Resim seçme butonu
    @FXML
    private ImageView artifactImageView; // Seçilen resmi gösterecek alan

    private String selectedImagePath = null; // Seçilen resmin yolu

    private ArtifactRepository artifactRepository = new ArtifactRepository();
    private Stage editStage;
    private Artifact selectedArtifact; // Seçili artifact'i saklamak için

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
    @FXML
    private ListView<Artifact> artifactListView; // Artifact listesini tutan UI bileşeni

    @FXML
    private ObservableList<Artifact> artifactData = FXCollections.observableArrayList(); // Artifact verileri
    @FXML
    private void deleteArtifact() {
        Artifact selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();

        if (selectedArtifact != null) {
            artifactData.remove(selectedArtifact);  // ObservableList'ten kaldır (ListView otomatik güncellenir)
        }
    }
    @FXML
    private void editArtifact() {
        if (selectedArtifact == null) {
            System.out.println("Düzenlenecek artifact seçilmedi!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/view/EditScreen.fxml"));
            Parent root = loader.load();

            // Edit ekranının controller'ını al
            UIController editController = loader.getController();
            editController.setArtifactData(selectedArtifact);

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.setScene(new Scene(root));
            this.editStage = stage;

            editStage.showAndWait(); // Pencereyi aç ve bekle

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
        tagsField.setText(String.join(",", artifact.getTags()));
    }
    @FXML
    private void handleEditSave() {
        if (selectedArtifact != null) {
            selectedArtifact.setArtifactID(artifactIdField.getText());
            selectedArtifact.setArtifactName(artifactNameField.getText());
            selectedArtifact.setCategory(categoryField.getText());
            selectedArtifact.setCivilization(civilizationField.getText());
            selectedArtifact.setDiscoveryLocation(discoveryLocationField.getText());
            selectedArtifact.setComposition(compositionField.getText());
            selectedArtifact.setDiscoveryDate(discoveryDateField.getText());
            selectedArtifact.setCurrentPlace(currentPlaceField.getText());
            selectedArtifact.setWidth(Double.parseDouble(widthField.getText()));
            selectedArtifact.setLength(Double.parseDouble(lengthField.getText()));
            selectedArtifact.setHeight(Double.parseDouble(heightField.getText()));
            selectedArtifact.setTags(Arrays.asList(tagsField.getText().split(",")));
        }

        // Pencereyi kapat
        if (editStage != null) {
            editStage.close();
        }
    }
}