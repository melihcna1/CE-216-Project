module com.example.artifactcataloggg {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.logging;

    opens com.example.artifactcataloggg to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg;
    
    opens com.example.artifactcataloggg.controller to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg.controller;
    
    opens com.example.artifactcataloggg.model to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg.model;
    
    opens com.example.artifactcataloggg.repository to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg.repository;
    
    opens com.example.artifactcataloggg.service to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg.service;
}