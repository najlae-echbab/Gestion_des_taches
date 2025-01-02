package com.backend.task_managing.View;

import com.backend.task_managing.Model.task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddTaskController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> priorityBox;

    @FXML
    private ComboBox<String> categoryBox;

    @FXML
    private DatePicker dueDatePicker;

    private TaskController parentController;

    public void setParentController(TaskController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        priorityBox.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        categoryBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
    }

    @FXML
    public void addTask() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String priority = priorityBox.getValue();
        String category = categoryBox.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title == null || title.isEmpty() ||
                description == null || description.isEmpty() ||
                priority == null || category == null || dueDate == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        task newTask = new task(title, description, priority, category, "À faire", dueDate);

        // Ajouter la tâche à la TableView dans le contrôleur parent
        parentController.addTaskToTable(newTask);

        // Fermer la fenêtre
        closeWindow();
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
