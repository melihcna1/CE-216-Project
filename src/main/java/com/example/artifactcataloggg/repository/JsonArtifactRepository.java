package com.example.artifactcataloggg.repository;

import com.example.artifactcataloggg.model.Artifact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of artifact repository using JSON file for persistence
 */
public class JsonArtifactRepository implements IArtifactRepository {
    private static final Logger LOGGER = Logger.getLogger(JsonArtifactRepository.class.getName());
    private static final String DEFAULT_FILE_PATH = "artifacts.json";
    
    private final String filePath;
    private List<Artifact> artifacts;
    private final Gson gson;
    
    // Singleton instance
    private static JsonArtifactRepository instance;
    
    /**
     * Constructor with default file path
     */
    public JsonArtifactRepository() {
        this(DEFAULT_FILE_PATH);
    }
    
    /**
     * Constructor with custom file path
     */
    public JsonArtifactRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.artifacts = loadArtifacts();
    }
    
    /**
     * Get singleton instance
     */
    public static JsonArtifactRepository getInstance() {
        if (instance == null) {
            instance = new JsonArtifactRepository();
        }
        return instance;
    }
    
    @Override
    public List<Artifact> getAllArtifacts() {
        return new ArrayList<>(artifacts);
    }
    
    @Override
    public boolean addArtifact(Artifact artifact) {
        if (existsById(artifact.getArtifactID())) {
            LOGGER.log(Level.WARNING, "An artifact with ID {0} already exists", artifact.getArtifactID());
            return false;
        }
        
        artifacts.add(artifact);
        LOGGER.log(Level.INFO, "Added new artifact: {0} - {1}", 
                new Object[]{artifact.getArtifactID(), artifact.getArtifactName()});
        saveArtifacts();
        return true;
    }
    
    @Override
    public boolean updateArtifact(String artifactId, Artifact updatedArtifact) {
        for (int i = 0; i < artifacts.size(); i++) {
            if (artifacts.get(i).getArtifactID().equals(artifactId)) {
                artifacts.set(i, updatedArtifact);
                saveArtifacts();
                LOGGER.log(Level.INFO, "Updated artifact: {0}", artifactId);
                return true;
            }
        }
        LOGGER.log(Level.WARNING, "Artifact not found for update: {0}", artifactId);
        return false;
    }
    
    @Override
    public boolean updateArtifact(Artifact artifact) {
        return updateArtifact(artifact.getArtifactID(), artifact);
    }
    
    @Override
    public boolean deleteArtifact(String artifactId) {
        int initialSize = artifacts.size();
        artifacts.removeIf(a -> a.getArtifactID().equals(artifactId));
        
        if (artifacts.size() < initialSize) {
            saveArtifacts();
            LOGGER.log(Level.INFO, "Deleted artifact: {0}", artifactId);
            return true;
        }
        
        LOGGER.log(Level.WARNING, "Artifact not found for deletion: {0}", artifactId);
        return false;
    }
    
    @Override
    public void saveArtifacts() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(artifacts, writer);
            LOGGER.log(Level.INFO, "Artifacts saved to {0}", filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save artifacts", e);
        }
    }
    
    @Override
    public List<Artifact> loadArtifacts() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            LOGGER.log(Level.WARNING, "JSON file not found or empty: {0}. Returning empty list.", filePath);
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type artifactListType = new TypeToken<List<Artifact>>() {}.getType();
            List<Artifact> loadedArtifacts = gson.fromJson(reader, artifactListType);
            LOGGER.log(Level.INFO, "Loaded {0} artifacts from {1}", 
                    new Object[]{loadedArtifacts.size(), filePath});
            return loadedArtifacts != null ? loadedArtifacts : new ArrayList<>();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load artifacts", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean importFromJson(String importFilePath) {
        File file = new File(importFilePath);

        if (!file.exists() || file.length() == 0) {
            LOGGER.log(Level.WARNING, "Import file not found or empty: {0}", importFilePath);
            return false;
        }

        try (Reader reader = new FileReader(file)) {
            Type artifactListType = new TypeToken<List<Artifact>>() {}.getType();
            List<Artifact> importedArtifacts = gson.fromJson(reader, artifactListType);
            
            if (importedArtifacts == null || importedArtifacts.isEmpty()) {
                LOGGER.log(Level.WARNING, "No artifacts found in import file");
                return false;
            }

            int importedCount = 0;
            for (Artifact artifact : importedArtifacts) {
                if (!existsById(artifact.getArtifactID())) {
                    artifacts.add(artifact);
                    importedCount++;
                } else {
                    LOGGER.log(Level.INFO, "Skipped existing artifact: {0}", artifact.getArtifactID());
                }
            }

            if (importedCount > 0) {
                saveArtifacts();
                LOGGER.log(Level.INFO, "Imported {0} artifacts from {1}", 
                        new Object[]{importedCount, importFilePath});
                return true;
            }
            
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to import artifacts", e);
            return false;
        }
    }
    
    @Override
    public boolean existsById(String id) {
        return artifacts.stream()
                .anyMatch(artifact -> artifact.getArtifactID().equals(id));
    }
    
    @Override
    public void reloadArtifactsFromFile() {
        this.artifacts = loadArtifacts();
        LOGGER.log(Level.INFO, "Reloaded artifacts from file");
    }
} 