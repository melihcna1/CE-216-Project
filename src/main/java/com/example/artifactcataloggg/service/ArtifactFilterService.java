package com.example.artifactcataloggg.service;

import com.example.artifactcataloggg.model.Artifact;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service responsible for filtering artifact data based on various criteria
 */
public class ArtifactFilterService {
    
    /**
     * Filter artifacts that contain all selected tags (AND logic)
     * 
     * @param artifacts The collection of artifacts to filter
     * @param selectedTags The list of tags to filter by (all must match)
     * @return Filtered list of artifacts
     */
    public List<Artifact> filterByAllTags(List<Artifact> artifacts, List<String> selectedTags) {
        if (selectedTags == null || selectedTags.isEmpty()) {
            return new ArrayList<>(artifacts);
        }
        
        return artifacts.stream()
                .filter(artifact -> artifact.getTags().containsAll(selectedTags))
                .collect(Collectors.toList());
    }

    /**
     * Filter artifacts that contain at least one of the selected tags (OR logic)
     * 
     * @param artifacts The collection of artifacts to filter
     * @param selectedTags The list of tags to filter by (any can match)
     * @return Filtered list of artifacts
     */
    public List<Artifact> filterByAnyTag(List<Artifact> artifacts, List<String> selectedTags) {
        if (selectedTags == null || selectedTags.isEmpty()) {
            return new ArrayList<>(artifacts);
        }
        
        return artifacts.stream()
                .filter(artifact -> artifact.getTags().stream()
                        .anyMatch(selectedTags::contains))
                .collect(Collectors.toList());
    }
    
    /**
     * Filter artifacts by date range
     * 
     * @param artifacts The collection of artifacts to filter
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return Filtered list of artifacts
     */
    public List<Artifact> filterByDateRange(List<Artifact> artifacts, String startDate, String endDate) {
        if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
            return new ArrayList<>(artifacts);
        }
        
        Predicate<Artifact> dateFilter = artifact -> {
            String discoveryDate = artifact.getDiscoveryDate();
            
            // Handle the case where no specific date is set
            if (discoveryDate == null || discoveryDate.isEmpty() || "Unknown".equals(discoveryDate)) {
                return false;
            }
            
            // Simple string comparison assumes dates are in comparable format
            boolean afterStart = startDate == null || startDate.isEmpty() || 
                    discoveryDate.compareTo(startDate) >= 0;
            boolean beforeEnd = endDate == null || endDate.isEmpty() || 
                    discoveryDate.compareTo(endDate) <= 0;
            
            return afterStart && beforeEnd;
        };
        
        return artifacts.stream()
                .filter(dateFilter)
                .collect(Collectors.toList());
    }
    
    /**
     * Filter artifacts by civilization
     * 
     * @param artifacts The collection of artifacts to filter
     * @param civilization The civilization to filter by
     * @return Filtered list of artifacts
     */
    public List<Artifact> filterByCivilization(List<Artifact> artifacts, String civilization) {
        if (civilization == null || civilization.isEmpty()) {
            return new ArrayList<>(artifacts);
        }
        
        return artifacts.stream()
                .filter(artifact -> civilization.equalsIgnoreCase(artifact.getCivilization()))
                .collect(Collectors.toList());
    }
    
    /**
     * Filter artifacts by category
     * 
     * @param artifacts The collection of artifacts to filter
     * @param category The category to filter by
     * @return Filtered list of artifacts
     */
    public List<Artifact> filterByCategory(List<Artifact> artifacts, String category) {
        if (category == null || category.isEmpty()) {
            return new ArrayList<>(artifacts);
        }
        
        return artifacts.stream()
                .filter(artifact -> category.equalsIgnoreCase(artifact.getCategory()))
                .collect(Collectors.toList());
    }
    
    /**
     * Apply multiple filters in sequence
     * 
     * @param artifacts The initial collection of artifacts
     * @param filters List of filter predicates to apply
     * @return Filtered list of artifacts
     */
    public List<Artifact> applyFilters(List<Artifact> artifacts, List<Predicate<Artifact>> filters) {
        if (filters == null || filters.isEmpty()) {
            return new ArrayList<>(artifacts);
        }
        
        Predicate<Artifact> compositePredicate = filters.stream()
                .reduce(Predicate::and)
                .orElse(artifact -> true);
        
        return artifacts.stream()
                .filter(compositePredicate)
                .collect(Collectors.toList());
    }
} 