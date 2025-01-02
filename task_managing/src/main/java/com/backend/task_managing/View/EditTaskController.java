package com.backend.task_managing.View;

import com.backend.task_managing.Model.task;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditTaskController {

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

    private task currentTask;
    private TaskController parentController;

    public void setParentController(TaskController parentController) {
        this.parentController = parentController;
    }

    public void setTask(task task) {
        this.currentTask = task;
        // Remplir les champs avec les valeurs actuelles de la tâche
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        priorityBox.setValue(task.getPriority());
        categoryBox.setValue(task.getCategory());
        dueDatePicker.setValue(task.getDueDate());
    }

    @FXML
    public void initialize() {
        priorityBox.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        categoryBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
    }

    @FXML
    public void saveTask() {
        // Mettre à jour les valeurs de la tâche
        currentTask.setTitle(titleField.getText());
        currentTask.setDescription(descriptionField.getText());
        currentTask.setPriority(priorityBox.getValue());
        currentTask.setCategory(categoryBox.getValue());
        currentTask.setDueDate(dueDatePicker.getValue());

        // Rafraîchir la TableView dans le contrôleur principal
        parentController.refreshTable();
        parentController.updateStatistics();


        // Fermer la fenêtre
        closeWindow();
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
