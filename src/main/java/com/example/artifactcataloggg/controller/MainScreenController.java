package com.example.artifactcataloggg.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainScreenController {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> categoryFilterComboBox;

    @FXML
    private TableView<?> artifactTableView;

    @FXML
    private void initialize() {
        System.out.println("Main Screen Initialized!");
    }
    public void handleExit(ActionEvent event) {
        Platform.exit();
    }

}
