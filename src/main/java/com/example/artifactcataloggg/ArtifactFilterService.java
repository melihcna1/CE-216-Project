package com.example.artifactcataloggg;

import java.util.ArrayList;
import java.util.List;

// Responsible for filtering Artifact data based on selected tags
public class ArtifactFilterService {
    private List<Artifact> artifacts;

    public ArtifactFilterService(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    // Filters artifacts by checking if each artifact's tags contain all selected tags
    public List<Artifact> filterByTags(List<String> selectedTags) {
        List<Artifact> filtered = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.getTags() != null && artifact.getTags().containsAll(selectedTags)) {
                filtered.add(artifact);
            }
        }
        return filtered;
    }
}
