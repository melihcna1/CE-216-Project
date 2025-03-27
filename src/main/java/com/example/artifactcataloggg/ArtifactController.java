package com.example.artifactcataloggg;

import java.util.Arrays;
import java.util.List;

public class ArtifactController {
    private ArtifactRepository repository;

    public ArtifactController() {
        this.repository = new ArtifactRepository();
    }

    public void createArtifact(Artifact artifact) {
        repository.addArtifact(artifact);
    }
    public List<Artifact> getAllArtifacts() {
        return repository.getArtifacts();
    }
    public void deleteArtifact (String artifactID) {
        repository.deleteArtifact(artifactID);
    }
    public void handleAddArtifact(String id, String name) {
        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("Artifact ID ve Name boş olamaz!");
            return;
        }

        Artifact newArtifact = new Artifact(id, name, "Metal", "Ancient", "Rome",
                "Gold", "200 BC", "Museum", 10, 10, 10, 10, Arrays.asList("Ancient", "Gold"), "image.path");

        createArtifact(newArtifact);

    }

}

