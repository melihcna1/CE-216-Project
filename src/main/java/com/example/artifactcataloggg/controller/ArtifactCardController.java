package com.example.artifactcataloggg.controller;

import com.example.artifactcataloggg.Artifact;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class ArtifactCardController {

    @FXML
    private Label artifactNameLabel;

    @FXML
    private ImageView artifactImageView;

    public void setData(Artifact artifact) {
        artifactNameLabel.setText(artifact.getArtifactName());

        String imagePath = "/" + artifact.getImagePath(); // örn: /artifactImages/athena.png
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            artifactImageView.setImage(image);
        } else {
            System.out.println("Görsel bulunamadı: " + imagePath);
        }
    }
}
