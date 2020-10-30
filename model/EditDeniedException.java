package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class EditDeniedException extends Exception {
    public EditDeniedException(String msg) {
        super("Closed");
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}