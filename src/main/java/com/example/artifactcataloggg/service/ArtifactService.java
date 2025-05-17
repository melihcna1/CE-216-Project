package com.example.artifactcataloggg.service;

import com.example.artifactcataloggg.model.Artifact;
import com.example.artifactcataloggg.repository.IArtifactRepository;
import com.example.artifactcataloggg.repository.JsonArtifactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Main service for artifact operations that coordinates between repositories and specialized services
 */
public class ArtifactService {
    private static final Logger LOGGER = Logger.getLogger(ArtifactService.class.getName());
    
    private final IArtifactRepository repository;
    private final ArtifactSearchService searchService;
    private final ArtifactFilterService filterService;
    
    // Singleton instance
    private static ArtifactService instance;
    
    /**
     * Constructor with dependency injection
     */
    public ArtifactService(IArtifactRepository repository) {
        this.repository = repository;
        this.searchService = new ArtifactSearchService();
        this.filterService = new ArtifactFilterService();
    }
    
    /**
     * Default constructor that uses the default repository
     */
    public ArtifactService() {
        this(JsonArtifactRepository.getInstance());
    }
    
    /**
     * Get singleton instance
     */
    public static ArtifactService getInstance() {
        if (instance == null) {
            instance = new ArtifactService(JsonArtifactRepository.getInstance());
        }
        return instance;
    }
    
    /**
     * Get all artifacts
     */
    public List<Artifact> getAllArtifacts() {
        return repository.getAllArtifacts();
    }
    
    /**
     * Add a new artifact
     */
    public boolean addArtifact(Artifact artifact) {
        if (artifact == null) {
            LOGGER.log(Level.WARNING, "Cannot add null artifact");
            return false;
        }
        
        if (artifact.getArtifactID() == null || artifact.getArtifactID().isEmpty()) {
            LOGGER.log(Level.WARNING, "Artifact ID cannot be null or empty");
            return false;
        }
        
        if (artifact.getArtifactName() == null || artifact.getArtifactName().isEmpty()) {
            LOGGER.log(Level.WARNING, "Artifact name cannot be null or empty");
            return false;
        }
        
        return repository.addArtifact(artifact);
    }
    
    /**
     * Update an existing artifact
     */
    public boolean updateArtifact(Artifact artifact) {
        if (artifact == null || artifact.getArtifactID() == null || artifact.getArtifactID().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid artifact or artifact ID");
            return false;
        }
        
        return repository.updateArtifact(artifact);
    }
    
    /**
     * Delete an artifact
     */
    public boolean deleteArtifact(String artifactId) {
        if (artifactId == null || artifactId.isEmpty()) {
            LOGGER.log(Level.WARNING, "Artifact ID cannot be null or empty");
            return false;
        }
        
        return repository.deleteArtifact(artifactId);
    }
    
    /**
     * Search for artifacts matching the query
     */
    public List<Artifact> searchArtifacts(List<Artifact> artifacts, String query) {
        if (query == null || query.trim().isEmpty()) {
            return artifacts;
        }

        String[] terms = query.toLowerCase().split(",");

        return artifacts.stream()
                .filter(artifact -> {
                    // Null kontrolü ve küçük harfe çevirme
                    String name = artifact.getArtifactName() != null ? artifact.getArtifactName().toLowerCase() : "";
                    String category = artifact.getCategory() != null ? artifact.getCategory().toLowerCase() : "";
                    String civilization = artifact.getCivilization() != null ? artifact.getCivilization().toLowerCase() : "";
                    String discoveryLocation = artifact.getDiscoveryLocation() != null ? artifact.getDiscoveryLocation().toLowerCase() : "";
                    String composition = artifact.getComposition() != null ? artifact.getComposition().toLowerCase() : "";
                    String discoveryDate = artifact.getDiscoveryDate() != null ? artifact.getDiscoveryDate().toLowerCase() : "";
                    String currentPlace = artifact.getCurrentPlace() != null ? artifact.getCurrentPlace().toLowerCase() : "";
                    // Tags listesi
                    List<String> tags = artifact.getTags() != null ? artifact.getTags().stream()
                            .map(String::toLowerCase)
                            .collect(Collectors.toList()) : new ArrayList<>();

                    for (String term : terms) {
                        String trimmed = term.trim();
                        if (trimmed.isEmpty()) continue;

                        boolean foundInTags = tags.contains(trimmed);
                        boolean foundInName = name.contains(trimmed);
                        boolean foundInCategory = category.contains(trimmed);
                        boolean foundInCivilization = civilization.contains(trimmed);
                        boolean foundInDiscoveryLocation = discoveryLocation.contains(trimmed);
                        boolean foundInComposition = composition.contains(trimmed);
                        boolean foundInDiscoveryDate = discoveryDate.contains(trimmed);
                        boolean foundInCurrentPlace = currentPlace.contains(trimmed);

                        // AND koşulu: her terim en az bir yerde geçmeli
                        if (!(foundInTags || foundInName || foundInCategory || foundInCivilization ||
                                foundInDiscoveryLocation || foundInComposition || foundInDiscoveryDate || foundInCurrentPlace)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }





    /**
     * Filter artifacts by tags (AND logic)
     */
    public List<Artifact> filterByAllTags(List<String> tags) {
        List<Artifact> artifacts = repository.getAllArtifacts();
        return filterService.filterByAllTags(artifacts, tags);
    }
    
    /**
     * Filter artifacts by tags (OR logic)
     */
    public List<Artifact> filterByAnyTag(List<String> tags) {
        List<Artifact> artifacts = repository.getAllArtifacts();
        return filterService.filterByAnyTag(artifacts, tags);
    }
    
    /**
     * Filter artifacts by date range
     */
    public List<Artifact> filterByDateRange(String startDate, String endDate) {
        List<Artifact> artifacts = repository.getAllArtifacts();
        return filterService.filterByDateRange(artifacts, startDate, endDate);
    }
    
    /**
     * Import artifacts from JSON file
     */
    public boolean importFromJson(String filePath) {
        return repository.importFromJson(filePath);
    }
    
    /**
     * Check if an artifact with given ID exists
     */
    public boolean artifactExists(String artifactId) {
        return repository.existsById(artifactId);
    }
    
    /**
     * Find artifact by ID
     */
    public Artifact findById(String artifactId) {
        if (artifactId == null || artifactId.isEmpty()) {
            return null;
        }
        
        return repository.getAllArtifacts().stream()
                .filter(a -> artifactId.equals(a.getArtifactID()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all unique tags from all artifacts
     */
    public List<String> getAllTags() {
        return repository.getAllArtifacts().stream()
                .flatMap(artifact -> artifact.getTags().stream())
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Get all unique categories from all artifacts
     */
    public List<String> getAllCategories() {
        return repository.getAllArtifacts().stream()
                .map(Artifact::getCategory)
                .filter(category -> category != null && !category.isEmpty() && !"Unknown".equals(category))
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Get all unique civilizations from all artifacts
     */
    public List<String> getAllCivilizations() {
        return repository.getAllArtifacts().stream()
                .map(Artifact::getCivilization)
                .filter(civ -> civ != null && !civ.isEmpty() && !"Unknown".equals(civ))
                .distinct()
                .sorted()
                .toList();
    }
} 