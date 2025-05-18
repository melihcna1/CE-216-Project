package com.example.artifactcataloggg.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controller for the User Manual screen
 */
public class UserManualController {
    
    @FXML
    private TextArea manualTextArea;
    
    /**
     * Initialize the controller
     */
    @FXML
    private void initialize() {
        populateUserManual();
    }
    
    /**
     * Populate the text area with user manual content
     */
    private void populateUserManual() {
        StringBuilder manual = new StringBuilder();
        
        manual.append("ARTIFACT CATALOG - USER MANUAL\n\n");
        
        manual.append("1. NAVIGATION\n");
        manual.append("   - The main screen displays all artifacts in a table view\n");
        manual.append("   - Use the search field to find artifacts by name, category, etc.\n");
        manual.append("   - Click on any artifact to view its details and image\n\n");
        
        manual.append("2. ADDING ARTIFACTS\n");
        manual.append("   - Click 'Edit' -> 'Add' in the menu bar\n");
        manual.append("   - Fill in the required fields (ID and Name are mandatory)\n");
        manual.append("   - Click 'Add Image' to attach an image to the artifact\n");
        manual.append("   - Click 'Save' to add the artifact to the catalog\n\n");
        
        manual.append("3. EDITING ARTIFACTS\n");
        manual.append("   - Select an artifact from the table\n");
        manual.append("   - Click 'Edit' -> 'Edit' in the menu bar\n");
        manual.append("   - Modify the fields as needed\n");
        manual.append("   - Click 'Save' to update the artifact\n\n");
        
        manual.append("4. DELETING ARTIFACTS\n");
        manual.append("   - Select an artifact from the table\n");
        manual.append("   - Click 'Edit' -> 'Delete' in the menu bar\n");
        manual.append("   - Confirm the deletion when prompted\n\n");
        
        manual.append("5. FILTERING\n");
        manual.append("   - Use the tag list on the right to filter artifacts by tags\n");
        manual.append("   - Use ctrl+left click or shift+left click while selecting tags to select multiple tagsn");
        manual.append("   - Use the date pickers to filter by discovery date range\n");
        manual.append("   - Click 'Clear Filters' to reset all filters\n\n");
        
        manual.append("6. IMPORTING/EXPORTING\n");
        manual.append("   - Click 'File' -> 'Import JSON' to import artifacts from a JSON file\n");
        manual.append("   - Click 'File' -> 'Export JSON' to export the catalog to a JSON file\n\n");
        
        manual.append("7. TIPS\n");
        manual.append("   - You can add multiple tags to an artifact (comma separated)\n");
        manual.append("   - The search function checks all fields, not just the visible ones\n");
        manual.append("   - Changes are automatically saved to disk\n");
        
        manualTextArea.setText(manual.toString());
    }
}
