package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.*;
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
    private ArtifactRepository repository;
    private List<Artifact> allArtifacts = new ArrayList<>();
    @FXML
    private TableView<Artifact> artifactTable;

    @FXML
    private ListView<Artifact> artifactListView;
    @FXML
    private Button editButton;

    private ObservableList<Artifact> artifactData = FXCollections.observableArrayList();
    private ArtifactRepository artifactRepository = new ArtifactRepository();
    private Artifact selectedArtifact = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArtifacts();
        displayArtifacts(allArtifacts);
        refreshTags(); // Tüm tag'leri göster

        searchButton.setOnAction(event -> {
            String query = searchField.getText().toLowerCase();
            List<Artifact> filtered = allArtifacts.stream()
                    .filter(a -> a.getArtifactName().toLowerCase().contains(query))
                    .toList();
            displayArtifacts(filtered);
        });

        // ✅ Tag ListView tıklanınca filtreleme yap
        tagListView.setOnMouseClicked(event -> {
            String selectedTag = tagListView.getSelectionModel().getSelectedItem();
            if (selectedTag != null) {
                List<Artifact> filtered = allArtifacts.stream()
                        .filter(a -> a.getTags() != null && a.getTags().contains(selectedTag))
                        .toList();
                displayArtifacts(filtered);
            }
        });
    }

    public MainScreenController() {
        this.repository = new ArtifactRepository();
    }

    private void loadArtifacts() {
        allArtifacts.clear();
        allArtifacts.addAll(artifactRepository.getArtifacts()); // JSON'dan oku
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
    @FXML
    public void exportToJson(ActionEvent event) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Export Artifacts to JSON");
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        // Set default directory to Desktop
        String userDesktop = System.getProperty("user.home") + "/Desktop";
        fileChooser.setInitialDirectory(new File(userDesktop));

        // Show save dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(allArtifacts, writer);
                System.out.println("Artifacts exported successfully to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error exporting artifacts to JSON.");
            }
        } else {
            System.out.println("Export canceled by user.");
        }
    }
    @FXML
    public void handleImportJson(ActionEvent event) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Import Artifact JSON");
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("Seçilen dosya: " + selectedFile.getAbsolutePath());
            ArtifactRepository.getInstance().importFromJson(selectedFile.getAbsolutePath());
            allArtifacts.clear();
            allArtifacts.addAll(ArtifactRepository.getInstance().getArtifacts());
            displayArtifacts(allArtifacts);
            refreshTags();
        } else {
            System.out.println("Dosya seçimi iptal edildi.");
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
        String imagePath = artifact.getImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {
            Image img;
            File file = new File(imagePath);

            if (file.exists()) {
                // Dosya sisteminden gelen görsel (dış dosya)
                img = new Image(file.toURI().toString(), 100, 100, true, true);
            } else {
                // Proje içinden (resources klasörü) yüklemeye çalış
                InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);
                if (imageStream != null) {
                    img = new Image(imageStream, 100, 100, true, true);
                } else {
                    System.out.println("Görsel bulunamadı, placeholder kullanılacak: " + imagePath);
                    InputStream placeholderStream = getClass().getResourceAsStream("/artifactImages/placeholder.png");
                    img = new Image(placeholderStream, 100, 100, true, true);
                }
            }

            imageView.setImage(img);
        } else {
            // Görsel yolu boşsa placeholder göster
            InputStream placeholderStream = getClass().getResourceAsStream("/artifactImages/placeholder.png");
            imageView.setImage(new Image(placeholderStream, 100, 100, true, true));
        }

        imageView.setFitWidth(100);
        imageView.setFitHeight(100);


        box.getChildren().addAll(imageView, nameLabel);
        box.setOnMouseClicked(event -> {

            selectedArtifact = artifact;
            highlightSelectedCard(box);




            if (event.getClickCount() == 2) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
                    Parent root = loader.load();

                    EditScreenController controller = loader.getController();
                    controller.setEditMode(true, artifact);
                    controller.setOnArtifactSaved(() -> {
                        artifactRepository.reloadArtifactsFromFile();  // JSON'dan güncel listeyi yükle
                        allArtifacts = artifactRepository.getArtifacts();
                        displayArtifacts(allArtifacts);
                    });


                    Stage stage = new Stage();
                    stage.setTitle("Edit Artifact");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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
    private void handleEditMenu(ActionEvent event) {
        if (selectedArtifact == null) {
            System.out.println("No artifact selected.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController controller = loader.getController();
            controller.setEditMode(true, selectedArtifact);
            controller.setOnArtifactSaved(() -> {
                artifactRepository.reloadArtifactsFromFile();  // JSON'dan güncel listeyi yükle
                allArtifacts = artifactRepository.getArtifacts();
                displayArtifacts(allArtifacts);
            });



            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAddMenu(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController controller = loader.getController();
            controller.setEditMode(false, null); // Yeni artifact ekleme modu
            controller.setOnArtifactSaved(() -> {
                artifactRepository.reloadArtifactsFromFile();  // JSON'dan güncel listeyi yükle
                allArtifacts = artifactRepository.getArtifacts();
                displayArtifacts(allArtifacts);
            });


            Stage stage = new Stage();
            stage.setTitle("Add New Artifact");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteArtifact() {
        if (selectedArtifact == null) {
            showAlert("Silinecek artifact seçilmedi.");
            return;
        }

        allArtifacts.remove(selectedArtifact);
        displayArtifacts(allArtifacts);
        artifactRepository.deleteArtifact(selectedArtifact.getArtifactID());

        System.out.println("Artifact silindi: " + selectedArtifact.getArtifactID());
        selectedArtifact = null;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void highlightSelectedCard(VBox selectedCard) {
        for (javafx.scene.Node node : artifactGrid.getChildren()) {
            if (node instanceof VBox) {
                node.setStyle("-fx-border-color: rgb(128,128,128); -fx-border-radius: 5; -fx-background-color: #f4f4f4;");
            }
        }
        selectedCard.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: #e0f0ff;");
    }
    public void refreshTags() {
        this.allArtifacts = artifactRepository.getArtifacts();

        List<String> allTags = new ArrayList<>();
        for (Artifact artifact : allArtifacts) {
            if (artifact.getTags() != null) {
                for (String tag : artifact.getTags()) {
                    tag = tag.trim();
                    if (!tag.isEmpty() && !allTags.contains(tag)) {
                        allTags.add(tag);
                    }
                }
            }
        }

        tagListView.setItems(FXCollections.observableArrayList(allTags));
    }





}