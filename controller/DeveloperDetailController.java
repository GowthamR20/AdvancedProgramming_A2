package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeveloperDetailController {
    @FXML
    public void quitDD(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
        stage.close();

    }
}
