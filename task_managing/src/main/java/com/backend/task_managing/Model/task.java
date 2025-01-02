package com.backend.task_managing.Model;
import java.time.LocalDate;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class task {
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty priority;
    private final StringProperty category;
    private final StringProperty status;
    private final SimpleObjectProperty<LocalDate> dueDate;

    public task(String title, String description, String priority, String category, String status, LocalDate dueDate) {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.priority = new SimpleStringProperty(priority);
        this.category = new SimpleStringProperty(category);
        this.status = new SimpleStringProperty(status);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
    }

    // Getters et Setters pour JavaFX Properties
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public SimpleObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }



}
