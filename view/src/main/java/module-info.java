module view {
    requires be.hogent.model;
    requires javafx.controls;
    requires javafx.fxml;
    exports be.hogent.view;
    opens be.hogent.view to javafx.graphics, javafx.fxml;
}