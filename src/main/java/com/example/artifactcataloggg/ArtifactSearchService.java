package com.example.artifactcataloggg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Service responsible for searching through Artifact data based on query terms
public class ArtifactSearchService {
    private List<Artifact> artifacts;

    public ArtifactSearchService(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    // Searches artifacts based on various fields with ranking priority
    public List<Artifact> searchArtifacts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(artifacts);
        }

        String lowerQuery = query.toLowerCase();

        return artifacts.stream()
                .map(artifact -> new SearchResult(artifact, getMatchScore(artifact, lowerQuery)))
                .filter(result -> result.score > 0) // Keep only relevant results
                .sorted((a, b) -> Double.compare(b.score, a.score)) // Sort by relevance
                .map(result -> result.artifact) // Extract artifacts
                .collect(Collectors.toList());
    }

    // Calculates a relevance score based on the fields matched
    private double getMatchScore(Artifact artifact, String query) {
        double score = 0.0;

        if (artifact.getArtifactName().toLowerCase().contains(query)) score += 10;
        if (artifact.getCategory().toLowerCase().contains(query)) score += 8;
        if (artifact.getCivilization().toLowerCase().contains(query)) score += 6;
        if (artifact.getDiscoveryLocation().toLowerCase().contains(query)) score += 5;
        if (artifact.getComposition().toLowerCase().contains(query)) score += 4;
        if (artifact.getCurrentPlace().toLowerCase().contains(query)) score += 3;
        if (artifact.getTags() != null && artifact.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(query))) {
            score += 2;
        }

        return score;
    }

    // Helper class for sorting search results
    private static class SearchResult {
        Artifact artifact;
        double score;

        SearchResult(Artifact artifact, double score) {
            this.artifact = artifact;
            this.score = score;
        }
    }
}
