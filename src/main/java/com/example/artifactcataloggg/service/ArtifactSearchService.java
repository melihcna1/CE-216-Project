package com.example.artifactcataloggg.service;

import com.example.artifactcataloggg.model.Artifact;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for searching through artifact data based on query terms
 */
public class ArtifactSearchService {
    
    /**
     * Search for artifacts matching the query
     * 
     * @param artifacts The collection of artifacts to search through
     * @param query The search query string
     * @return List of matching artifacts sorted by relevance
     */
    public List<Artifact> searchArtifacts(List<Artifact> artifacts, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(artifacts);
        }

        String lowerQuery = query.toLowerCase();

        return artifacts.stream()
                .map(artifact -> new SearchResult(artifact, calculateRelevanceScore(artifact, lowerQuery)))
                .filter(result -> result.score > 0) // Keep only relevant results
                .sorted((a, b) -> Double.compare(b.score, a.score)) // Sort by relevance
                .map(result -> result.artifact) // Extract artifacts
                .collect(Collectors.toList());
    }

    /**
     * Calculate a relevance score for an artifact based on how well it matches the query
     * Higher weights are given to more important fields
     */
    private double calculateRelevanceScore(Artifact artifact, String query) {
        double score = 0.0;

        // Primary fields - highest relevance
        if (artifact.getArtifactID().toLowerCase().contains(query)) score += 10;
        if (artifact.getArtifactName().toLowerCase().contains(query)) score += 10;
        
        // Secondary fields - high relevance
        if (artifact.getCategory().toLowerCase().contains(query)) score += 8;
        if (artifact.getCivilization().toLowerCase().contains(query)) score += 8;
        
        // Medium fields - medium relevance
        if (artifact.getDiscoveryLocation().toLowerCase().contains(query)) score += 6;
        if (artifact.getComposition().toLowerCase().contains(query)) score += 6;
        if (artifact.getDiscoveryDate().toLowerCase().contains(query)) score += 5;
        if (artifact.getCurrentPlace().toLowerCase().contains(query)) score += 5;
        
        // Measurement fields - lower relevance
        if (Double.toString(artifact.getWidth()).contains(query)) score += 4;
        if (Double.toString(artifact.getLength()).contains(query)) score += 4;
        if (Double.toString(artifact.getHeight()).contains(query)) score += 4;
        if (Double.toString(artifact.getWeight()).contains(query)) score += 4;
        
        // Tags - medium-low relevance
        if (artifact.getTags() != null && artifact.getTags().stream()
                .anyMatch(tag -> tag.toLowerCase().contains(query))) {
            score += 3;
        }
        
        // Image path - lowest relevance
        if (artifact.getImagePath() != null && artifact.getImagePath().toLowerCase().contains(query)) {
            score += 2;
        }

        return score;
    }

    /**
     * Helper class for storing and sorting search results
     */
    private static class SearchResult {
        final Artifact artifact;
        final double score;

        SearchResult(Artifact artifact, double score) {
            this.artifact = artifact;
            this.score = score;
        }
    }
} 