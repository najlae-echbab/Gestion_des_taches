package com.backend.task_managing.View;
import com.backend.task_managing.Model.task;
import com.backend.task_managing.View.AddTaskController;
import com.backend.task_managing.View.EditTaskController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;

public class TaskController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> priorityBox;  // fx:id="priorityBox" dans le FXML

    @FXML
    private ComboBox<String> categoryBox;  // fx:id="categoryBox" dans le FXML

    @FXML
    private TableColumn<task, Void> actionColumn;
    @FXML
    private TableColumn<task, Void> editColumn;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private TableView<task> taskTable;

    @FXML
    private TableColumn<task, String> titleColumn;

    @FXML
    private TableColumn<task, String> priorityColumn;

    @FXML
    private TableColumn<task, String> statusColumn;

    @FXML
    private TableColumn<task, LocalDate> dueDateColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterPriorityBox;

    @FXML
    private ComboBox<String> filterCategoryBox;

    @FXML
    private ComboBox<String> filterStatusBox;
    private ObservableList<task> taskList;


    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label completedTasksLabel;
    @FXML
    private Label pendingTasksLabel;
    @FXML
    private PieChart statusPieChart;
    @FXML
    private PieChart priorityPieChart;


    @FXML
    public void initialize() {
        System.out.println("priorityBox : " + priorityBox);
        System.out.println("categoryBox : " + categoryBox);

        taskList = FXCollections.observableArrayList();


        // Configure table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        taskTable.setItems(taskList);
        taskTable.setEditable(true);
        addEditColumn();

        // Initialize ComboBoxes
        priorityBox.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        categoryBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
        // Configurer la colonne Status pour utiliser un ComboBox

        configureStatusColumn();
        filterPriorityBox.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        filterCategoryBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
        filterStatusBox.setItems(FXCollections.observableArrayList("À faire", "En cours", "Terminé"));
        setupSearchAndFilters();



    }

    private ObservableList<task> filteredTaskList = FXCollections.observableArrayList();

    private void setupSearchAndFilters() {
        // Écouteur pour la barre de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTasks());

        // Écouteurs pour les ComboBoxes de filtres
        filterPriorityBox.valueProperty().addListener((observable, oldValue, newValue) -> filterTasks());
        filterCategoryBox.valueProperty().addListener((observable, oldValue, newValue) -> filterTasks());
        filterStatusBox.valueProperty().addListener((observable, oldValue, newValue) -> filterTasks());
    }

    private void filterTasks() {
        String searchText = searchField.getText().toLowerCase();
        String selectedPriority = filterPriorityBox.getValue();
        String selectedCategory = filterCategoryBox.getValue();
        String selectedStatus = filterStatusBox.getValue();

        filteredTaskList.setAll(taskList.filtered(task -> {
            boolean matchesSearch = (task.getTitle().toLowerCase().contains(searchText) ||
                    task.getDescription().toLowerCase().contains(searchText) ||
                    task.getCategory().toLowerCase().contains(searchText));

            boolean matchesPriority = (selectedPriority == null || task.getPriority().equals(selectedPriority));
            boolean matchesCategory = (selectedCategory == null || task.getCategory().equals(selectedCategory));
            boolean matchesStatus = (selectedStatus == null || task.getStatus().equals(selectedStatus));

            return matchesSearch && matchesPriority && matchesCategory && matchesStatus;
        }));

        taskTable.setItems(filteredTaskList);
    }

    @FXML
    public void applyFilters() {
        filterTasks();
    }

    @FXML
    public void resetFilters() {
        searchField.clear();
        filterPriorityBox.setValue(null);
        filterCategoryBox.setValue(null);
        filterStatusBox.setValue(null);
        taskTable.setItems(taskList);
    }


    private void addEditColumn() {
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setOnAction(event -> {
                    task selectedTask = getTableView().getItems().get(getIndex());
                    openEditTaskWindow(selectedTask);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }
    private void openEditTaskWindow(task selectedTask) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/backend/task_managing/View/edit-task-view.fxml"));
            Parent root = loader.load();

            EditTaskController editTaskController = loader.getController();
            editTaskController.setParentController(this);
            editTaskController.setTask(selectedTask);

            Stage stage = new Stage();
            stage.setTitle("Modifier une Tâche");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureStatusColumn() {
        // Définir les options du ComboBox pour la colonne "status"
        ObservableList<String> statusOptions = FXCollections.observableArrayList("À faire", "En cours", "Terminé");

        // Configurer la colonne pour utiliser un ComboBoxTableCell
        statusColumn.setCellFactory(column -> {
            ComboBoxTableCell<task, String> cell = new ComboBoxTableCell<>(statusOptions);
            cell.setComboBoxEditable(false); // Rend le ComboBox non éditable (uniquement sélection)
            return cell;
        });

        // Définir le comportement pour modifier la valeur
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setOnEditCommit(event -> {
            task selectedTask = event.getRowValue();
            selectedTask.setStatus(event.getNewValue()); // Met à jour le statut de la tâche
            taskTable.refresh(); // Rafraîchir la table pour refléter les modifications
            updateStatistics();
        });
    }
    public void refreshTable() {
        taskTable.refresh();
    }


    @FXML
    public void addtask() {
        // Récupérer les valeurs des champs
        String title = titleField.getText();
        String description = descriptionField.getText();
        String priority = priorityBox.getValue();
        String category = categoryBox.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        // Vérifier que tous les champs sont remplis
        if (title == null || title.isEmpty() ||
                description == null || description.isEmpty() ||
                priority == null || category == null || dueDate == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs avant d'ajouter une tâche !");
            return;
        }

        // Créer une nouvelle tâche
        task newTask = new task(title, description, priority, category, "À faire", dueDate);

        // Ajouter la tâche à la liste observable (TableView)
        taskList.add(newTask);

        // Effacer le formulaire après ajout
        clearForm();
        updateStatistics();

        // Afficher le widget de confirmation
        showConfirmationWidget(title);
    }

    private void showConfirmationWidget(String taskTitle) {
        // Créer une nouvelle fenêtre (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Tâche ajoutée");

        // Contenu de la fenêtre
        Label confirmationLabel = new Label("La tâche \"" + taskTitle + "\" a été ajoutée avec succès !");
        confirmationLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        Button closeButton = new Button("OK");
        closeButton.setOnAction(event -> popupStage.close());

        // Mettre les éléments dans un layout
        VBox layout = new VBox(10, confirmationLabel, closeButton);
        layout.setStyle("-fx-padding: 20;");
        layout.setAlignment(Pos.CENTER);

        // Définir la scène
        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);

        // Afficher la fenêtre
        popupStage.show();
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        priorityBox.setValue(null);
        categoryBox.setValue(null);
        dueDatePicker.setValue(null);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void openAddTaskWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/backend/task_managing/View/add-task-view.fxml"));
            Parent root = loader.load();

            AddTaskController addTaskController = loader.getController();
            addTaskController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Tâche");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTaskToTable(task task) {
        taskList.add(task);
    }

    @FXML
    public void deleteTask() {
        // Récupérer la tâche sélectionnée dans la TableView
        task selectedTask = taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            // Demander confirmation avant de supprimer
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette tâche ?");
            confirmationAlert.setContentText("Tâche : " + selectedTask.getTitle());

            // Si l'utilisateur confirme la suppression
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    taskList.remove(selectedTask); // Supprimer la tâche de la liste
                    taskTable.refresh(); // Rafraîchir la TableView
                    updateStatistics();
                    showAlert("Succès", "La tâche a été supprimée avec succès.");
                }
            });
        } else {
            // Afficher un message si aucune tâche n'est sélectionnée
            showAlert("Erreur", "Veuillez sélectionner une tâche à supprimer.");
        }

    }



    @FXML
    public void exportToCSV() {
        // Ouvrir une boîte de dialogue pour sélectionner l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(taskTable.getScene().getWindow());

        if (selectedFile != null) {
            try (FileWriter writer = new FileWriter(selectedFile)) {
                // Écrire l'en-tête du fichier CSV
                writer.append("Titre,Description,Priorité,Catégorie,Statut,Date d'échéance\n");

                // Écrire chaque tâche dans le fichier CSV
                for (task t : taskList) {
                    writer.append(t.getTitle()).append(",")
                            .append(t.getDescription()).append(",")
                            .append(t.getPriority()).append(",")
                            .append(t.getCategory()).append(",")
                            .append(t.getStatus()).append(",")
                            .append(t.getStatus()).append(",")
                            .append(t.getDueDate() != null ? t.getDueDate().toString() : "")
                            .append("\n");
                }

                // Confirmer que l'exportation a été réussie
                showAlert("Succès", "Les tâches ont été exportées avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de l'exportation des tâches.");
            }
        } else {
            showAlert("Information", "Exportation annulée.");
        }
    }
    //staistique
    void updateStatistics() {
        int totalTasks = taskList.size();
        long completedTasks = taskList.stream().filter(t -> t.getStatus().equals("Terminé")).count();
        long pendingTasks = taskList.stream().filter(t -> t.getStatus().equals("À faire")).count();

        totalTasksLabel.setText("Total : " + totalTasks);
        completedTasksLabel.setText("Terminées : " + completedTasks);
        pendingTasksLabel.setText("À faire : " + pendingTasks);

        // Mise à jour des données du graphique des statuts
        ObservableList<PieChart.Data> statusData = FXCollections.observableArrayList(
                new PieChart.Data("À faire", pendingTasks),
                new PieChart.Data("Terminées", completedTasks),
                new PieChart.Data("En cours", taskList.stream().filter(t -> t.getStatus().equals("En cours")).count())
        );
        statusPieChart.setData(statusData);

        // Mise à jour des données du graphique des priorités
        long highPriority = taskList.stream().filter(t -> t.getPriority().equals("Haute")).count();
        long mediumPriority = taskList.stream().filter(t -> t.getPriority().equals("Moyenne")).count();
        long lowPriority = taskList.stream().filter(t -> t.getPriority().equals("Basse")).count();

        ObservableList<PieChart.Data> priorityData = FXCollections.observableArrayList(
                new PieChart.Data("Haute", highPriority),
                new PieChart.Data("Moyenne", mediumPriority),
                new PieChart.Data("Basse", lowPriority)
        );
        priorityPieChart.setData(priorityData);

    }



}
