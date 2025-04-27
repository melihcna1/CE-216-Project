package com.example.artifactcataloggg.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class UserManualController {

    @FXML
    private TextArea manualTextArea;

    @FXML
    public void initialize() {
        manualTextArea.setText(
                "Artifact Catalog – User Manual\n\n" +
                        "Welcome to Artifact Catalog!\n" +
                        "This is a user-friendly desktop application for managing digital catalogs of historical artifacts.\n\n" +
                        "Main Features:\n" +
                        "- Add new artifacts with detailed information\n" +
                        "- Edit or update existing artifacts\n" +
                        "- Delete artifacts (with confirmation dialogs)\n" +
                        "- View artifact images and enlarge them\n" +
                        "- Search artifacts by name, category, civilization, discovery date, tags, and more\n" +
                        "- Filter artifacts by tags\n" +
                        "- Filter artifacts by discovery date range\n" +
                        "- Import and export artifacts as JSON files\n" +
                        "- Right-click on artifacts to quickly Edit or Delete\n" +
                        "- Automatically updates the tag list when adding or editing artifacts\n" +
                        "- Beautifully displayed artifact images with borders and shadows\n\n" +

                        "Main Screen Overview:\n" +
                        "Top Menu:\n" +
                        "- File\n" +
                        "  - Import JSON: Load artifacts from a JSON file.\n" +
                        "  - Export JSON: Save the current list to a JSON file.\n" +
                        "- Edit\n" +
                        "  - Add: Open the Add Artifact window.\n" +
                        "  - Edit: Open the Edit Artifact window for the selected artifact.\n" +
                        "  - Delete: Permanently delete the selected artifact (with confirmation).\n" +
                        "- Help\n" +
                        "  - About: Opens this User Manual.\n\n" +

                        "Search and Filter:\n" +
                        "- Use the Search Bar to find artifacts based on any attribute.\n" +
                        "- Clicking on tags filters artifacts based on selected tag.\n" +
                        "- Filter artifacts by a discovery date range.\n\n" +

                        "Artifact Table:\n" +
                        "- Displays artifacts with their details.\n" +
                        "- Single-click to select and view an artifact's image.\n" +
                        "- Double-click to open the edit window.\n" +
                        "- Right-click to quickly Edit or Delete an artifact.\n\n" +

                        "Artifact Image:\n" +
                        "- Displayed with a border and shadow.\n" +
                        "- Centered neatly in the right panel.\n\n" +

                        "Edit/Add Artifact Window:\n" +
                        "- Fill in artifact details like name, category, civilization, discovery location, etc.\n" +
                        "- Upload an image for the artifact.\n" +
                        "- Save to update or add a new entry.\n\n" +

                        "Quick Tips:\n" +
                        "- Export your artifacts regularly for backup.\n" +
                        "- Add meaningful tags to improve searchability.\n" +
                        "- Use Delete option carefully – requires confirmation.\n\n" +

                        "Developed using Java, JavaFX, and GSON library."
        );
    }
}
