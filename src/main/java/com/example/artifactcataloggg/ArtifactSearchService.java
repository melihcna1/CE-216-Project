package com.example.artifactcataloggg;

import java.util.ArrayList;
import java.util.List;

// Responsible for searching through Artifact data based on query terms
public class ArtifactSearchService {
    private List<Artifact> artifacts;

    public ArtifactSearchService(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    // Searches artifacts by name, category, civilization, or discovery location using case-insensitive partial matching
    public List<Artifact> searchArtifacts(String query) {
        query = query.toLowerCase();
        List<Artifact> results = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.getArtifactName().toLowerCase().contains(query) ||
                    artifact.getCategory().toLowerCase().contains(query) ||
                    artifact.getCivilization().toLowerCase().contains(query) ||
                    artifact.getDiscoveryLocation().toLowerCase().contains(query)) {
                results.add(artifact);
            }
        }
        return results;
    }
}
