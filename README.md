# Artifact Catalog

A JavaFX application for cataloging historical artifacts with detailed information and images.

## Project Structure

The application follows a clean architecture with clear separation of concerns:

```
src/main/java/com/example/artifactcataloggg/
  ├── model/           # Data models
  │   └── Artifact.java
  ├── repository/      # Data access layer
  │   ├── IArtifactRepository.java
  │   └── JsonArtifactRepository.java
  ├── service/         # Business logic
  │   ├── ArtifactService.java
  │   ├── ArtifactSearchService.java
  │   └── ArtifactFilterService.java
  ├── controller/      # UI controllers
  │   ├── ArtifactCardController.java
  │   ├── EditScreenController.java
  │   ├── MainScreenController.java
  │   └── UserManualController.java
  └── ArtifactCatalogApp.java  # Main application class
```

## Key Features

- Add, edit, and delete historical artifacts
- Store detailed information about each artifact
- Associate images with artifacts
- Search and filter artifacts by various criteria
- Tag-based organization
- Import/export data via JSON

## Getting Started

1. Ensure you have Java 17+ and JavaFX installed
2. Clone the repository
3. Run the application using the `ArtifactCatalogApp` main class

## Dependencies

- JavaFX for the UI
- Gson for JSON serialization/deserialization

## Architecture

This application follows a layered architecture:

1. **Model Layer**: Contains the data models such as `Artifact`
2. **Repository Layer**: Handles data persistence using JSON files
3. **Service Layer**: Contains business logic for searching, filtering, and managing artifacts
4. **Controller Layer**: Handles UI interaction and coordinates between views and services
5. **View Layer**: FXML files defining the user interface

## Design Patterns

- **Model-View-Controller (MVC)**: Separates the application into components for data, UI, and control logic
- **Singleton**: Used for service and repository instances
- **Repository Pattern**: Abstracts data access operations
- **Dependency Injection**: Through constructor-based injection of repositories into services
- **Interface Segregation**: Repository interface defines clear contracts

## Future Improvements

- Add support for multiple image storage formats
- Implement a more robust date parsing system
- Add user authentication
- Implement cloud synchronization of artifact data 