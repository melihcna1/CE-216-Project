package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import com.example.artifactcataloggg.ArtifactRepository;
import com.example.artifactcataloggg.ArtifactSearchService;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Modality;
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
    @FXML
    private ArtifactRepository repository;
    @FXML
    private List<Artifact> allArtifacts = new ArrayList<>();
    @FXML
    private Button editButton;

    private ObservableList<Artifact> artifactData = FXCollections.observableArrayList();
    private ArtifactRepository artifactRepository = new ArtifactRepository();
    private Artifact selectedArtifact = null;
    @FXML
    private TableView<Artifact> artifactTableView;
    @FXML
    private TableColumn<Artifact, String> nameColumn;
    @FXML
    private TableColumn<Artifact, String> categoryColumn;
    @FXML
    private TableColumn<Artifact, String> civilizationColumn;
    @FXML
    private TableColumn<Artifact, String> discoveryLocationColumn;
    @FXML
    private TableColumn<Artifact, String> compositionColumn;
    @FXML
    private TableColumn<Artifact, String> discoveryDateColumn;
    @FXML
    private TableColumn<Artifact, String> currentPlaceColumn;
    @FXML
    private ImageView artifactImageView;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button filterByDateButton;
    @FXML
    private Button clearFiltersButton;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArtifacts();
        setupTableColumns();
        displayArtifacts(allArtifacts);
        refreshTags(); // 🔥 artifacts yüklenince tag listesi de hemen dolacak!
        setupTableViewContextMenu(); // 🛠 Sağ tık menüyü aktif et
        artifactImageView.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 0);" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        searchButton.setOnAction(event -> performSearch());
        searchField.setOnAction(event -> performSearch());  // ✨ Enter tuşuna basınca da aynı arama yapılacak


        artifactTableView.setOnMouseClicked(event -> {
            Artifact selected = artifactTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedArtifact = selected;
                showArtifactImage(selected);

                if (event.getClickCount() == 2) { // ✨ ÇİFT TIK kontrolü
                    openEditScreen(selected);
                }
            }
        });

        filterByDateButton.setOnAction(event -> {
            filterArtifactsByDate();
        });
        clearFiltersButton.setOnAction(event -> {
            clearFilters();
        });
        tagListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTag, newTag) -> {
            if (newTag != null) {
                filterArtifactsByTag(newTag);
            }
        });




    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("artifactName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        civilizationColumn.setCellValueFactory(new PropertyValueFactory<>("civilization"));
        discoveryLocationColumn.setCellValueFactory(new PropertyValueFactory<>("discoveryLocation"));
        compositionColumn.setCellValueFactory(new PropertyValueFactory<>("composition"));
        discoveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("discoveryDate"));
        currentPlaceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPlace"));
    }



    public MainScreenController() {
        this.repository = new ArtifactRepository();
    }

    private void loadArtifacts() {
        allArtifacts.clear();
        allArtifacts.addAll(artifactRepository.getArtifacts()); // JSON'dan oku
    }
    private void showArtifactImage(Artifact artifact) {
        if (artifact.getImagePath() != null && !artifact.getImagePath().isEmpty()) {
            try {
                InputStream imageStream = getClass().getResourceAsStream("/" + artifact.getImagePath());
                if (imageStream != null) {
                    artifactImageView.setImage(new Image(imageStream));
                } else {
                    System.out.println("Image not found: " + artifact.getImagePath());
                    artifactImageView.setImage(null); // Resim bulunamazsa boş bırak
                }
            } catch (Exception e) {
                e.printStackTrace();
                artifactImageView.setImage(null);
            }
        } else {
            artifactImageView.setImage(null);
        }
    }





    private void displayArtifacts(List<Artifact> artifacts) {
        artifactTableView.getItems().setAll(artifacts);
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
        box.setSpacing(8);
        box.setPadding(new Insets(12));
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.5, 0, 2);"
        );

        Label nameLabel = new Label(artifact.getArtifactName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        ImageView imageView = new ImageView();
        String imagePath = artifact.getImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {
            Image img;
            File file = new File(imagePath);

            if (file.exists()) {
                img = new Image(file.toURI().toString(), 100, 100, true, true);
            } else {
                InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);
                if (imageStream != null) {
                    img = new Image(imageStream, 100, 100, true, true);
                } else {
                    InputStream placeholderStream = getClass().getResourceAsStream("/artifactImages/placeholder.png");
                    img = new Image(placeholderStream, 100, 100, true, true);
                }
            }
            imageView.setImage(img);
        } else {
            InputStream placeholderStream = getClass().getResourceAsStream("/artifactImages/placeholder.png");
            imageView.setImage(new Image(placeholderStream, 100, 100, true, true));
        }

        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        box.getChildren().addAll(imageView, nameLabel);

        // 🆕 Sağ tıklama için ContextMenu oluşturuyoruz
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem, deleteItem);

        // Edit seçilince edit ekranı açılıyor
        editItem.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
                Parent root = loader.load();

                EditScreenController controller = loader.getController();
                controller.setEditMode(true, artifact);
                controller.setOnArtifactSaved(() -> {
                    artifactRepository.reloadArtifactsFromFile();
                    allArtifacts = artifactRepository.getArtifacts();
                    displayArtifacts(allArtifacts);
                    refreshTags();
                });

                Stage stage = new Stage();
                stage.setTitle("Edit Artifact");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Delete seçilince artifact siliniyor
        deleteItem.setOnAction(e -> {
            artifactRepository.deleteArtifact(artifact.getArtifactID());
            artifactRepository.reloadArtifactsFromFile();
            allArtifacts = artifactRepository.getArtifacts();
            displayArtifacts(allArtifacts);
            refreshTags();
            System.out.println("Artifact deleted via right-click menu: " + artifact.getArtifactID());
        });

        box.setOnMouseClicked(event -> {
            selectedArtifact = artifact;
            highlightSelectedCard(box);

            if (event.getButton() == MouseButton.SECONDARY) { // SAĞ TIK mı diye bakıyoruz
                contextMenu.show(box, event.getScreenX(), event.getScreenY());
            } else if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) { // SOL ÇİFT TIK
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
                    Parent root = loader.load();

                    EditScreenController controller = loader.getController();
                    controller.setEditMode(true, artifact);
                    controller.setOnArtifactSaved(() -> {
                        artifactRepository.reloadArtifactsFromFile();
                        allArtifacts = artifactRepository.getArtifacts();
                        displayArtifacts(allArtifacts);
                        refreshTags();
                    });

                    Stage stage = new Stage();
                    stage.setTitle("Edit Artifact");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                contextMenu.hide(); // Sol tıkta menüyü kapat
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
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));

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
                refreshTags();
            });


            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
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
                refreshTags();
            });

            Stage stage = new Stage();
            stage.setTitle("Add New Artifact");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteArtifact() {
        if (selectedArtifact == null) {
            showAlert("No artifact selected for deletion.");
            return;
        }

        // 🔥 Onay kutusu göster
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the selected artifact?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                artifactRepository.deleteArtifact(selectedArtifact.getArtifactID());
                artifactRepository.reloadArtifactsFromFile();
                allArtifacts = artifactRepository.getArtifacts();
                displayArtifacts(allArtifacts);
                refreshTags();
                selectedArtifact = null;
                System.out.println("Artifact deleted successfully.");
            }
        });
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
    @FXML
    private void handleAboutMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/UserManual.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Manual");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/HelpLogo.png"))); // 🔥 İKON EKLENDİ
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void filterArtifactsByDate() {
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            int startYear = startDatePicker.getValue().getYear();
            int endYear = endDatePicker.getValue().getYear();

            List<Artifact> filtered = allArtifacts.stream()
                    .filter(a -> {
                        try {
                            String discoveryDate = a.getDiscoveryDate();
                            if (discoveryDate != null && discoveryDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                                String[] parts = discoveryDate.split("-");
                                int year = Integer.parseInt(parts[2]); // yıl kısmını alıyoruz
                                return year >= startYear && year <= endYear;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .toList();

            displayArtifacts(filtered);
        } else {
            showAlert("Please select both start and end dates for filtering.");
        }
    }
    private void clearFilters() {
        // DatePicker alanlarını temizle
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);

        // Tags seçimlerini temizle
        tagListView.getSelectionModel().clearSelection();

        // Artifact listesini yeniden yükle
        displayArtifacts(allArtifacts);
    }
    private void filterArtifactsByTag(String selectedTag) {
        List<Artifact> filtered = allArtifacts.stream()
                .filter(a -> a.getTags() != null && a.getTags().contains(selectedTag))
                .toList();

        displayArtifacts(filtered);
    }
    private void openEditScreen(Artifact artifact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController controller = loader.getController();
            controller.setEditMode(true, artifact);
            controller.setOnArtifactSaved(() -> {
                artifactRepository.reloadArtifactsFromFile();
                allArtifacts = artifactRepository.getArtifacts();
                displayArtifacts(allArtifacts);
                refreshTags();
            });

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void performSearch() {
        String query = searchField.getText().toLowerCase();

        if (query.isEmpty()) {
            displayArtifacts(allArtifacts);
            return;
        }

        List<Artifact> filtered = allArtifacts.stream()
                .filter(a -> {
                    if (a.getArtifactName() != null && a.getArtifactName().toLowerCase().contains(query)) return true;
                    if (a.getCategory() != null && a.getCategory().toLowerCase().contains(query)) return true;
                    if (a.getCivilization() != null && a.getCivilization().toLowerCase().contains(query)) return true;
                    if (a.getDiscoveryLocation() != null && a.getDiscoveryLocation().toLowerCase().contains(query)) return true;
                    if (a.getComposition() != null && a.getComposition().toLowerCase().contains(query)) return true;
                    if (a.getDiscoveryDate() != null && a.getDiscoveryDate().toLowerCase().contains(query)) return true;
                    if (a.getCurrentPlace() != null && a.getCurrentPlace().toLowerCase().contains(query)) return true;
                    if (a.getTags() != null && a.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(query))) return true;
                    return false;
                })
                .toList();

        displayArtifacts(filtered);
    }
    private void setupTableViewContextMenu() {
        artifactTableView.setRowFactory(tv -> {
            TableRow<Artifact> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem = new MenuItem("Delete");
            contextMenu.getItems().addAll(editItem, deleteItem);

            // Edit seçilirse
            editItem.setOnAction(event -> {
                Artifact selectedArtifact = row.getItem();
                if (selectedArtifact != null) {
                    openEditScreen(selectedArtifact);
                }
            });

            // Delete seçilirse
            deleteItem.setOnAction(event -> {
                Artifact selectedArtifact = row.getItem();
                if (selectedArtifact != null) {
                    // 🔥 Önce onay kutusu göster
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Delete Confirmation");
                    confirmation.setHeaderText(null);
                    confirmation.setContentText("Are you sure you want to delete this artifact?");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            artifactRepository.deleteArtifact(selectedArtifact.getArtifactID());
                            artifactRepository.reloadArtifactsFromFile();
                            allArtifacts = artifactRepository.getArtifacts();
                            displayArtifacts(allArtifacts);
                            refreshTags();
                        }
                    });
                }
            });


            // Sadece dolu satırlara menü ekle
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }












}