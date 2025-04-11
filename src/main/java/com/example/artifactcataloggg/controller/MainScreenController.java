package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane artifactGrid;

    @FXML
    private ListView<String> tagListView;

    private List<Artifact> allArtifacts = new ArrayList<>();
    @FXML
    private TableView<Artifact> artifactTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadArtifacts();


        displayArtifacts(allArtifacts);


        searchButton.setOnAction(event -> {
            String query = searchField.getText().toLowerCase();
            List<Artifact> filtered = allArtifacts.stream()
                    .filter(a -> a.getArtifactName().toLowerCase().contains(query))
                    .toList();
            displayArtifacts(filtered);
        });
    }

    private void loadArtifacts() {

        allArtifacts.add(new Artifact("Ancient Vase", "artifactImages/vase.jpeg"));
        allArtifacts.add(new Artifact("Old Coin", "artifactImages/coin.jpg"));
        allArtifacts.add(new Artifact("Historic Sword", "artifactImages/sword.jpg"));

    }

    private void displayArtifacts(List<Artifact> artifacts) {
        artifactGrid.getChildren().clear();
        artifactGrid.setHgap(10);
        artifactGrid.setVgap(10);

        int column = 0;
        int row = 0;

        for (Artifact artifact : artifacts) {
            VBox card = createArtifactCard(artifact);
            artifactGrid.add(card, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createArtifactCard(Artifact artifact) {
            VBox box = new VBox();
            box.setSpacing(5);
            box.setPadding(new Insets(10));
            box.setStyle("-fx-border-color: rgb(128,128,128); -fx-border-radius: 5; -fx-background-color: #f4f4f4;");

            Label nameLabel = new Label(artifact.getArtifactName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            ImageView imageView = new ImageView();
            String imagePath = "/" + artifact.getImagePath();

            InputStream imageStream = getClass().getResourceAsStream(imagePath);

            if (imageStream != null) {
                imageView.setImage(new Image(imageStream, 100, 100, true, true));
            } else {
                System.out.println("Görsel yüklenemedi: " + imagePath);

                File file = new File("src/main/resources" + imagePath);
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString(), 100, 100, true, true));
                } else {
                    System.out.println("Görsel dosyası gerçekten yok: " + file.getAbsolutePath());

                    // Placeholder kullan
                    InputStream placeholderStream = getClass().getResourceAsStream("/artifactImages/placeholder.png");
                    if (placeholderStream != null) {
                        imageView.setImage(new Image(placeholderStream, 100, 100, true, true));
                    } else {
                        System.out.println("Placeholder da yüklenemedi!");
                    }
                }
            }

            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            box.getChildren().addAll(imageView, nameLabel);
            return box;
        }
    @FXML
    private void onAddButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
        Parent root = fxmlLoader.load();

        EditScreenController controller = fxmlLoader.getController();
        controller.setEditMode(false, null); // Add mode

        Stage stage = new Stage();
        stage.setTitle("Add New Artifact");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) throws IOException {
        Artifact selected = artifactTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
        Parent root = fxmlLoader.load();

        EditScreenController controller = fxmlLoader.getController();
        controller.setEditMode(true, selected); // Edit mode

        Stage stage = new Stage();
        stage.setTitle("Edit Artifact");
        stage.setScene(new Scene(root));
        stage.show();
    }




}