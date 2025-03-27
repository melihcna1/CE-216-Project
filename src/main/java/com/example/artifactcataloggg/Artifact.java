package com.example.artifactcataloggg;

import java.util.List;

// Data model representing an artifact entity
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

    // Constructors
    public Artifact() {}

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

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

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
                ", width=" + width +
                ", length=" + length +
                ", height=" + height +
                ", weight=" + weight +
                ", tags=" + tags +
                '}';
    }
}
