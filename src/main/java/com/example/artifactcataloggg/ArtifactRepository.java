package com.example.artifactcataloggg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Responsible for persisting and retrieving Artifact data using JSON
public class ArtifactRepository {
    private static final String FILE_PATH = "artifacts.json";
    private List<Artifact> artifacts;
    private Gson gson;
    private static ArtifactRepository instance;



    public ArtifactRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.artifacts = loadArtifacts();
    }
    public static ArtifactRepository getInstance() {
        if (instance == null) {
            instance = new ArtifactRepository();
        }
        return instance;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void addArtifact(Artifact artifact) {
        if (artifacts.stream().anyMatch(a -> a.getArtifactID().equals(artifact.getArtifactID()))) {
            System.out.println("Bu ID'ye sahip bir artifact zaten var!");
            return;
        }
        artifacts.add(artifact);
        System.out.println("Yeni Artifact eklendi: " + artifact.getArtifactID() + " - " + artifact.getArtifactName());
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
    public void updateArtifact(Artifact artifact) {
        updateArtifact(artifact.getArtifactID(), artifact);
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
        File file = new File("artifacts.json");

        // Eğer dosya yoksa veya içi boşsa, boş bir liste döndür
        if (!file.exists() || file.length() == 0) {
            System.out.println("Uyarı: JSON dosyası bulunamadı veya boş! Boş liste döndürülüyor.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type artifactListType = new TypeToken<List<Artifact>>() {}.getType();
            return new Gson().fromJson(reader, artifactListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void importFromJson(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Dosya bulunamadı veya boş: " + filePath);
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type artifactListType = new TypeToken<List<Artifact>>() {}.getType();
            List<Artifact> importedArtifacts = gson.fromJson(reader, artifactListType);

            for (Artifact artifact : importedArtifacts) {
                if (!existsById(artifact.getArtifactID())) {
                    artifacts.add(artifact);
                } else {
                    System.out.println("Zaten mevcut: " + artifact.getArtifactID());
                }
            }

            saveArtifacts();
            System.out.println("Veriler başarıyla içe aktarıldı: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean existsById(String id) {
        return artifacts.stream()
                .anyMatch(artifact -> artifact.getArtifactID().equals(id));
    }
    public void reloadArtifactsFromFile() {
        this.artifacts = loadArtifacts();
    }
}
