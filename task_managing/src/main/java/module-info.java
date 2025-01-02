module com.backend.task_managing {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.backend.task_managing.View to javafx.fxml;
    exports com.backend.task_managing.Main;
    exports com.backend.task_managing.Model;
    exports com.backend.task_managing.View;
}
