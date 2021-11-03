module edu.rit.vexu.pathcreator {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.rit.vexu.pathcreator to javafx.fxml;
    exports edu.rit.vexu.pathcreator;
}