package com.example.artifactcataloggg.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserManualController {

    @FXML
    private TextArea manualTextArea;

    @FXML
    public void initialize() {
        manualTextArea.setText("Artifact Catalog – User Manual\n" +
                "Welcome to Artifact Catalog, a straightforward desktop program for maintaining digital catalogs of historic artifacts. Throughout this manual, we will guide you through the major features of the program and how you may get the best out of it.\n" +
                "\n" +
                "Main Screen Overview\n" +
                "You will display, search, and organize your artifacts on the main screen.\n" +
                "\n" +
                "Top Menu\n" +
                "File\n" +
                "\n" +
                "Import JSON – Import a list of artifacts from a.json file on your computer. \n" +
                "\n" +
                "Export JSON – Export the current list of artifacts to a.json file for sharing or backup. \n" +
                "\n" +
                "Edit\n" +
                "\n" +
                "Add – Opens the Edit Artifact window with empty fields to add a new artifact. \n" +
                "\n" +
                "Edit – Opens the Edit Artifact window with the fields of the selected artifact completed, for you to edit.\n" +
                "\n" +
                "Delete – Permanently deletes the selected artifact from the list.\n" +
                "\n" +
                "Help\n" +
                "\n" +
                "About – Opens this User Manual for reference.\n" +
                "\n" +
                "Search Bar\n" +
                "Type a search term and click Search to find artifacts with relevance. Searching with a blank box will list all artifacts.\n" +
                "\n" +
                "Search Tip: Results are ordered by relevance based on the position of the search term (see Search Priority below).\n" +
                "\n" +
                "Artifact List (Bottom Left)\n" +
                "Displays all artifacts as cards with image and title. Clicking on an artifact selects it for editing or deletion.\n" +
                "\n" +
                "Tags Panel (Right Side)\n" +
                "Displays all your unique tags from artifacts. Clicking on a tag does not yet filter artifacts; it's just for reference.\n" +
                "\n" +
                "Window Controls\n" +
                "As with most desktop apps, you can:\n" +
                "\n" +
                "Minimize the window.\n" +
                "\n" +
                "Maximize/Restore to toggle between full screen.\n" +
                "\n" +
                "Close the application.\n" +
                "\n" +
                "Edit Artifact Window\n" +
                "This window appears when you:\n" +
                "\n" +
                "Add from the Edit menu (empty fields),\n" +
                "\n" +
                "Click Edit with an artifact selected (fields filled in).\n" +
                "\n" +
                "Editable Fields:\n" +
                "These are the attributes for each artifact:\n" +
                "\n" +
                "Artifact Name: Artifact name. (Text field)\n" +
                "\n" +
                "Category: Category or type (e.g., Metal, Pottery). (Text field)\n" +
                "\n" +
                "Civilization: Time period or culture of origin. (Text field)\n" +
                "\n" +
                "Discovery Location: Where the artifact was found. (Text field)\n" +
                "\n" +
                "Composition: Substance (e.g., Stone, Gold). (Text input)\n" +
                "\n" +
                "Discovery Date: Date it was discovered (e.g., 200 BC). (Text input)\n" +
                "\n" +
                "Current Place: Where it is currently stored. (Text input)\n" +
                "\n" +
                "Width / Length / Height: Physical dimensions in units. (Decimal number input)\n" +
                "\n" +
                "Weight: Weight of the artifact. (Decimal number input)\n" +
                "\n" +
                "Tags: Descriptive keywords (comma-separated). (Text list input)\n" +
                "\n" +
                "Image Path: Automatically filled when image is uploaded (not manually editable). (System-generated)\n" +
                "Buttons\n" +
                "Save – Saves the current or new artifact.\n" +
                "\n" +
                "Cancel – Closes the window and does not save changes.\n" +
                "\n" +
                "Add Image – Opens a file chooser to select an image from your computer. The image will be displayed in the main view.\n" +
                "\n" +
                "You may also click on the X (close button) in the upper-right corner of the window, which is the same as \"Cancel\".\n" +
                "\n" +
                "Searching for Artifacts\n" +
                "You can search for artifacts by any field or attribute, such as name, date, tags, size, or even image file names.\n" +
                "\n" +
                "How it works:\n" +
                "When you type in a term and click Search, the app searches through all artifacts and ranks them by how well they match what you typed. It displays the most relevant ones first.\n" +
                "\n" +
                "Search Priority (Most to Least Relevant):\n" +
                "Artifact ID and Artifact Name\n" +
                "\n" +
                "Category and Civilization\n" +
                "\n" +
                "Discovery Location and Composition\n" +
                "\n" +
                "Discovery Date and Present Location\n" +
                "\n" +
                "Width, Length, Height, Weight\n" +
                "\n" +
                "Tags\n" +
                "\n" +
                "Image File Name\n" +
                "\n" +
                "For the best results, search by name, category, or civilization. Partial searches (for example, typing \"Rome\" finds \"Roman Empire\") are acceptable.\n" +
                "\n" +
                "Quick Hints\n" +
                "Always click Search after typing to run your query.\n" +
                "\n" +
                "Typing nothing will show all artifacts.\n" +
                "\n" +
                "Use Export JSON from time to time to back up your collection.\n" +
                "\n" +
                "Add meaningful tags for better categorizing and searching.");
    }
}
