package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class InvalidOfferException extends Exception {
    public InvalidOfferException(String msg){
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }



}
