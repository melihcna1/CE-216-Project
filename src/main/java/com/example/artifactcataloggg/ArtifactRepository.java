package com.example.artifactcataloggg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Responsible for persisting and retrieving Artifact data using JSON
public class ArtifactRepository {
    private static final String FILE_PATH = "artifacts.json";
    private List<Artifact> artifacts;
    private Gson gson;

    public ArtifactRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.artifacts = loadArtifacts();
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
        saveArtifacts();
    }

    public void updateArtifact(String artifactID, Artifact updatedArtifact) {
        for (int i = 0; i < artifacts.size(); i++) {
            if (artifacts.get(i).getArtifactID().equals(artifactID)) {
                artifacts.set(i, updatedArtifact);
                saveArtifacts();
                return;
            }
        }
    }

    public void deleteArtifact(String artifactID) {
        artifacts.removeIf(a -> a.getArtifactID().equals(artifactID));
        saveArtifacts();
    }

    public void saveArtifacts() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(artifacts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Artifact> loadArtifacts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, new TypeToken<List<Artifact>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
