package com.example.artifactcataloggg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing an artifact entity in the catalog
 */
public class Artifact {
    private String artifactID;
    private String artifactName;
    private String category;
    private String civilization;
    private String discoveryLocation;
    private String composition;
    private String discoveryDate;
    private String currentPlace;
    private double width, length, height;
    private double weight;
    private List<String> tags;
    private String imagePath;

    /**
     * Default constructor
     */
    public Artifact() {}

    /**
     * Complete constructor with all fields
     */
    public Artifact(String artifactID, String artifactName, String category, String civilization,
                String discoveryLocation, String composition, String discoveryDate,
                String currentPlace, double width, double length, double height, double weight,
                List<String> tags, String imagePath) {
        this.artifactID = artifactID;
        this.artifactName = artifactName;
        this.category = category;
        this.civilization = civilization;
        this.discoveryLocation = discoveryLocation;
        this.composition = composition;
        this.discoveryDate = discoveryDate;
        this.currentPlace = currentPlace;
        this.width = width;
        this.length = length;
        this.height = height;
        this.weight = weight;
        this.tags = tags;
        this.imagePath = imagePath;
    }
    
    /**
     * Simple constructor with minimal fields, uses default values for others
     */
    public Artifact(String artifactID, String artifactName, String imagePath) {
        this.artifactID = artifactID;
        this.artifactName = artifactName;
        this.category = "Unknown";
        this.civilization = "Unknown";
        this.discoveryLocation = "Unknown";
        this.composition = "Unknown";
        this.discoveryDate = "Unknown";
        this.currentPlace = "Unknown";
        this.width = 0;
        this.length = 0;
        this.height = 0;
        this.weight = 0;
        this.tags = new ArrayList<>();
        this.imagePath = imagePath;
    }

    // Getters and setters
    public String getArtifactID() { return artifactID; }
    public void setArtifactID(String artifactID) { this.artifactID = artifactID; }

    public String getArtifactName() { return artifactName; }
    public void setArtifactName(String artifactName) { this.artifactName = artifactName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCivilization() { return civilization; }
    public void setCivilization(String civilization) { this.civilization = civilization; }

    public String getDiscoveryLocation() { return discoveryLocation; }
    public void setDiscoveryLocation(String discoveryLocation) { this.discoveryLocation = discoveryLocation; }

    public String getComposition() { return composition; }
    public void setComposition(String composition) { this.composition = composition; }

    public String getDiscoveryDate() { return discoveryDate; }
    public void setDiscoveryDate(String discoveryDate) { this.discoveryDate = discoveryDate; }

    public String getCurrentPlace() { return currentPlace; }
    public void setCurrentPlace(String currentPlace) { this.currentPlace = currentPlace; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public List<String> getTags() {
        return tags != null ? tags : new ArrayList<>();
    }

    public void setTags(List<String> tags) { this.tags = tags; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Add a new tag, ensuring it's lowercase and unique
     */
    public void addTag(String tag) {
        if (tags == null) tags = new ArrayList<>();
        if (!tags.contains(tag.toLowerCase())) {
            tags.add(tag.toLowerCase());
        }
    }

    /**
     * Remove a tag
     */
    public void removeTag(String tag) {
        if (tags != null) {
            tags.remove(tag.toLowerCase());
        }
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "artifactID='" + artifactID + '\'' +
                ", artifactName='" + artifactName + '\'' +
                ", category='" + category + '\'' +
                ", civilization='" + civilization + '\'' +
                ", discoveryLocation='" + discoveryLocation + '\'' +
                ", composition='" + composition + '\'' +
                ", discoveryDate='" + discoveryDate + '\'' +
                ", currentPlace='" + currentPlace + '\'' +
                ", dimensions=" + width + "x" + length + "x" + height +
                ", weight=" + weight +
                ", tags=" + tags +
                '}';
    }
} 