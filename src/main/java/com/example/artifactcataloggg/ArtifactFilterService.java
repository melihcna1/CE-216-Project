package com.example.artifactcataloggg;

import java.util.ArrayList;
import java.util.List;

// Responsible for filtering Artifact data based on selected tags
public class ArtifactFilterService {
    private List<Artifact> artifacts;

    public ArtifactFilterService(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    // Filters artifacts that contain all selected tags
    public List<Artifact> filterByTags(List<String> selectedTags) {
        return artifacts.stream()
                .filter(artifact -> artifact.getTags().containsAll(selectedTags))
                .toList();
    }

    // Filters artifacts that contain at least one of the selected tags
    public List<Artifact> filterByAnyTag(List<String> selectedTags) {
        return artifacts.stream()
                .filter(artifact -> artifact.getTags().stream().anyMatch(selectedTags::contains))
                .toList();
    }
}

