package com.example.artifactcataloggg.repository;

import com.example.artifactcataloggg.model.Artifact;
import java.util.List;

/**
 * Interface for artifact repository operations
 */
public interface IArtifactRepository {
    
    /**
     * Get all artifacts in the repository
     */
    List<Artifact> getAllArtifacts();
    
    /**
     * Add a new artifact to the repository
     */
    boolean addArtifact(Artifact artifact);
    
    /**
     * Update an existing artifact 
     */
    boolean updateArtifact(String artifactId, Artifact updatedArtifact);
    
    /**
     * Update an existing artifact using its own ID
     */
    boolean updateArtifact(Artifact artifact);
    
    /**
     * Delete an artifact by ID
     */
    boolean deleteArtifact(String artifactId);
    
    /**
     * Save artifacts to persistent storage
     */
    void saveArtifacts();
    
    /**
     * Load artifacts from persistent storage
     */
    List<Artifact> loadArtifacts();
    
    /**
     * Import artifacts from a JSON file
     */
    boolean importFromJson(String filePath);
    
    /**
     * Check if an artifact with the given ID exists
     */
    boolean existsById(String id);
    
    /**
     * Reload artifacts from storage
     */
    void reloadArtifactsFromFile();
} 