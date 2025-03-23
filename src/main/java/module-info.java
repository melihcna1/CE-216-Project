module com.example.artifactcataloggg {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.artifactcataloggg to javafx.fxml, com.google.gson;
    exports com.example.artifactcataloggg;
}