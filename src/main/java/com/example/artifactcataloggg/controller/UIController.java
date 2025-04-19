package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
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
import java.util.stream.Collectors;

public class UIController {

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
    private ArtifactRepository artifactRepository = new ArtifactRepository();
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

            // Boş alan kontrolü
            if (artifactID.isEmpty() || name.isEmpty() || category.isEmpty() || widthField.getText().isEmpty()) {
                showAlert("Lütfen gerekli alanları doldurunuz.");
                return;
            }

            // Aynı ID ile daha önce artifact eklenmiş mi?
            if (artifactRepository.existsById(artifactID)) {
                showAlert("Bu ID'ye sahip bir artifact zaten mevcut.");
                return;
            }

            double width = tryParseDouble(widthField.getText(), "Genişlik");
            double length = tryParseDouble(lengthField.getText(), "Uzunluk");
            double height = tryParseDouble(heightField.getText(), "Yükseklik");
            double weight = tryParseDouble(weightField.getText(), "Ağırlık");

            List<String> tags = Arrays.stream(tagsField.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            Artifact newArtifact = new Artifact(artifactID, name, category, civilization,
                    discoveryLocation, composition, discoveryDate,
                    currentPlace, width, length, height, weight, tags, selectedImagePath);

            artifactRepository.addArtifact(newArtifact);
            artifactData.add(newArtifact);
            artifactListView.setItems(artifactData);

            System.out.println("Artifact başarıyla eklendi: " + artifactID);
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Sayısal alanlarda geçersiz giriş: " + e.getMessage());
        }
    }

    private double tryParseDouble(String text, String fieldName) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " değeri geçersiz: " + text);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
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
            selectedImagePath = selectedFile.toURI().toString();
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
        weightField.clear();
        tagsField.clear();
        artifactImageView.setImage(null);
        selectedImagePath = null;
    }


    @FXML
    private void editArtifact() {
        selectedArtifact = artifactListView.getSelectionModel().getSelectedItem();

        if (selectedArtifact == null) {
            showAlert("Düzenlenecek artifact seçilmedi!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/view/EditScreen.fxml"));
            Parent root = loader.load();

            UIController editController = loader.getController();
            editController.setArtifactData(selectedArtifact);

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.setScene(new Scene(root));
            this.editStage = stage;

            editStage.showAndWait();

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
        weightField.setText(String.valueOf(artifact.getWeight()));
        tagsField.setText(String.join(",", artifact.getTags()));

        if (artifact.getImagePath() != null) {
            artifactImageView.setImage(new Image(artifact.getImagePath()));
            selectedImagePath = artifact.getImagePath(); // edit sırasında null olmasın
        }
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
            selectedArtifact.setWidth(tryParseDouble(widthField.getText(), "Genişlik"));
            selectedArtifact.setLength(tryParseDouble(lengthField.getText(), "Uzunluk"));
            selectedArtifact.setHeight(tryParseDouble(heightField.getText(), "Yükseklik"));
            selectedArtifact.setWeight(tryParseDouble(weightField.getText(), "Ağırlık"));

            List<String> tags = Arrays.stream(tagsField.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            selectedArtifact.setTags(tags);

            // Eğer yeni resim seçildiyse güncelle
            if (selectedImagePath != null) {
                selectedArtifact.setImagePath(selectedImagePath);
            }

            artifactListView.refresh(); // UI'da değişikliği göster
            if (editStage != null) {
                editStage.close();
            }
        }
    }
}
