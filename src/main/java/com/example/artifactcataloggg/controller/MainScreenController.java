package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.model.Artifact;
import com.example.artifactcataloggg.service.ArtifactService;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainScreenController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MainScreenController.class.getName());

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane artifactGrid;

    @FXML
    private ListView<String> tagListView;
    @FXML
    private List<Artifact> allArtifacts = new ArrayList<>();
    @FXML
    private Button editButton;
    @FXML
    private Button applyTagFilterButton;

    private ObservableList<Artifact> artifactData = FXCollections.observableArrayList();
    private final ArtifactService artifactService = ArtifactService.getInstance();
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

    private List<String> selectedTags = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadArtifacts();
        setupTableColumns();
        displayArtifacts(allArtifacts);
        setupTagListView();
        refreshTags();
        setupTableViewContextMenu();
        artifactImageView.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 0);" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        searchButton.setOnAction(event -> performSearch());
        searchField.setOnAction(event -> performSearch());

        artifactTableView.setOnMouseClicked(event -> {
            Artifact selected = artifactTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedArtifact = selected;
                showArtifactImage(selected);

                if (event.getClickCount() == 2) {
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
        
        if (applyTagFilterButton != null) {
            applyTagFilterButton.setOnAction(event -> applyTagFilters());
        }
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

    private void loadArtifacts() {
        allArtifacts.clear();
        allArtifacts.addAll(artifactService.getAllArtifacts());
    }

    private void showArtifactImage(Artifact artifact) {
        if (artifact.getImagePath() != null && !artifact.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(artifact.getImagePath());
                if (imageFile.exists()) {
                    artifactImageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    // Try to load from resources
                    String resourcePath = artifact.getImagePath().startsWith("/") ? 
                        artifact.getImagePath() : "/" + artifact.getImagePath();
                    InputStream imageStream = getClass().getResourceAsStream(resourcePath);
                    if (imageStream != null) {
                        artifactImageView.setImage(new Image(imageStream));
                    } else {
                        LOGGER.warning("Image not found: " + artifact.getImagePath());
                        artifactImageView.setImage(null);
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error loading image", e);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Artifacts to JSON");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        String userDesktop = System.getProperty("user.home") + "/Desktop";
        fileChooser.setInitialDirectory(new File(userDesktop));

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(allArtifacts, writer);
                LOGGER.info("Artifacts exported successfully to: " + file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Export Successful", 
                        "Artifacts exported successfully to: " + file.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error exporting artifacts", e);
                showAlert(Alert.AlertType.ERROR, "Export Error", "Error exporting artifacts to JSON.");
            }
        }
    }

    @FXML
    public void handleImportJson(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Artifact JSON");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            LOGGER.info("Selected file: " + selectedFile.getAbsolutePath());
            boolean success = artifactService.importFromJson(selectedFile.getAbsolutePath());
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Import Successful", 
                        "Artifacts imported successfully.");
                allArtifacts.clear();
                allArtifacts.addAll(artifactService.getAllArtifacts());
                displayArtifacts(allArtifacts);
                refreshTags();
            } else {
                showAlert(Alert.AlertType.WARNING, "Import Warning", 
                        "No new artifacts were imported or the file was invalid.");
            }
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

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem, deleteItem);

        editItem.setOnAction(e -> openEditScreen(artifact));

        deleteItem.setOnAction(e -> {
            if (confirmDeletion(artifact)) {
                artifactService.deleteArtifact(artifact.getArtifactID());
                loadArtifacts();
                displayArtifacts(allArtifacts);
                refreshTags();
                LOGGER.info("Artifact deleted via context menu: " + artifact.getArtifactID());
            }
        });

        box.setOnMouseClicked(event -> {
            selectedArtifact = artifact;
            highlightSelectedCard(box);

            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(box, event.getScreenX(), event.getScreenY());
            } else if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                openEditScreen(artifact);
            } else {
                contextMenu.hide();
            }
        });

        return box;
    }

    @FXML
    private void handleEditMenu(ActionEvent event) {
        if (selectedArtifact == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No artifact selected for editing.");
            return;
        }

        openEditScreen(selectedArtifact);
    }

    @FXML
    private void handleAddMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController controller = loader.getController();
            controller.initializeNewArtifact();

            Stage stage = new Stage();
            stage.setTitle("Add New Artifact");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Refresh artifacts after dialog closes
            loadArtifacts();
            displayArtifacts(allArtifacts);
            refreshTags();
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening add dialog", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Error opening add dialog: " + e.getMessage());
        }
    }

    @FXML
    private void deleteArtifact() {
        if (selectedArtifact == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No artifact selected for deletion.");
            return;
        }

        if (confirmDeletion(selectedArtifact)) {
            boolean success = artifactService.deleteArtifact(selectedArtifact.getArtifactID());
            
            if (success) {
                LOGGER.info("Artifact deleted: " + selectedArtifact.getArtifactID());
                loadArtifacts();
                displayArtifacts(allArtifacts);
                refreshTags();
                selectedArtifact = null;
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete the artifact.");
            }
        }
    }
    
    /**
     * Show a confirmation dialog for artifact deletion
     */
    private boolean confirmDeletion(Artifact artifact) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText("Are you sure you want to delete this artifact?");
        confirmation.setContentText("Name: " + artifact.getArtifactName() + "\nID: " + artifact.getArtifactID());

        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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

    private void highlightSelectedCard(VBox selectedCard) {
        for (javafx.scene.Node node : artifactGrid.getChildren()) {
            if (node instanceof VBox) {
                node.setStyle("-fx-border-color: rgb(128,128,128); -fx-border-radius: 5; -fx-background-color: #f4f4f4;");
            }
        }
        selectedCard.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: #e0f0ff;");
    }

    public void refreshTags() {
        List<String> allTags = artifactService.getAllTags();
        tagListView.setItems(FXCollections.observableArrayList(allTags));
    }

    @FXML
    private void handleAboutMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/UserManual.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("User Manual");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/HelpLogo.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening user manual", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Error opening user manual: " + e.getMessage());
        }
    }

    private void filterArtifactsByDate() {
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            String startDate = startDatePicker.getValue().toString();
            String endDate = endDatePicker.getValue().toString();
            
            List<Artifact> filtered = artifactService.filterByDateRange(startDate, endDate);
            displayArtifacts(filtered);
        } else {
            showAlert(Alert.AlertType.WARNING, "Incomplete Selection", 
                    "Please select both start and end dates for filtering.");
        }
    }

    private void clearFilters() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        tagListView.getSelectionModel().clearSelection();
        selectedTags.clear();
        loadArtifacts();
        displayArtifacts(allArtifacts);
    }

    /**
     * Apply filters based on all selected tags
     */
    private void applyTagFilters() {
        if (selectedTags.isEmpty()) {
            // If no tags selected, show all artifacts
            displayArtifacts(allArtifacts);
            return;
        }
        
        // Filter artifacts that have all the selected tags
        List<Artifact> filtered = artifactService.filterByAllTags(selectedTags);
        
        // Display the filtered artifacts
        displayArtifacts(filtered);
        
        // Log the applied filter
        LOGGER.info("Applied tag filter with tags: " + String.join(", ", selectedTags));
    }

    private void openEditScreen(Artifact artifact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artifactcataloggg/EditScreen.fxml"));
            Parent root = loader.load();

            EditScreenController controller = loader.getController();
            controller.setArtifact(artifact);

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/artifactImages/Museum.png")));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Refresh artifacts after dialog closes
            loadArtifacts();
            displayArtifacts(allArtifacts);
            refreshTags();
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening edit dialog", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Error opening edit dialog: " + e.getMessage());
        }
    }

    private void performSearch() {
        String input = searchField.getText().trim();

        if (input.isEmpty()) {
            displayArtifacts(allArtifacts);
            return;
        }

        List<Artifact> searchResults = artifactService.searchArtifacts(input);
        displayArtifacts(searchResults);
    }

    private void setupTableViewContextMenu() {
        artifactTableView.setRowFactory(tv -> {
            TableRow<Artifact> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            MenuItem deleteItem = new MenuItem("Delete");
            contextMenu.getItems().addAll(editItem, deleteItem);

            editItem.setOnAction(event -> {
                Artifact selectedArtifact = row.getItem();
                if (selectedArtifact != null) {
                    openEditScreen(selectedArtifact);
                }
            });

            deleteItem.setOnAction(event -> {
                Artifact selectedArtifact = row.getItem();
                if (selectedArtifact != null && confirmDeletion(selectedArtifact)) {
                    artifactService.deleteArtifact(selectedArtifact.getArtifactID());
                    loadArtifacts();
                    displayArtifacts(allArtifacts);
                    refreshTags();
                }
            });

            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }

    /**
     * Set up the tag list view to allow multiple selection and handle selections
     */
    private void setupTagListView() {
        // Set the selection mode to MULTIPLE
        tagListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Add listener for tag selection changes
        tagListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // Get all selected tags
            selectedTags = new ArrayList<>(tagListView.getSelectionModel().getSelectedItems());
            
            // Auto-apply tag filter if a tag was selected or deselected
            if (oldVal != null || newVal != null) {
                applyTagFilters();
            }
        });
    }
}