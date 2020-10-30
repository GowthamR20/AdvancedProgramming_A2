package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button login_Button;
    @FXML
    private TextField studentId_TextField;
    @FXML
    private Label message;
    @FXML
    public void eraseErrorMessage(KeyEvent keyEvent) {
        message.setText("");
    }

    @FXML
    public void loginUniLink(ActionEvent actionEvent) throws IOException {
        if (!studentId_TextField.getText().matches("^[s][0-9]+")) {
            message.setText("Login Error! Enter correct student Id");
        } else {
            // Passing login Id to main window
            FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/UniLink_View.fxml"));
            Parent eventRoot = eventLoader.load();
            UniLinkController uniLinkController = eventLoader.getController();
            uniLinkController.setLoginId(studentId_TextField.getText());
            Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
            stage.close();


            // opening main window
            Stage mainStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UniLink_View.fxml"));
            Parent root = loader.load();
            mainStage.setTitle("Uni Link System");
            mainStage.setScene(new Scene(root));
            mainStage.show();



            mainStage.setOnCloseRequest(e -> {
                mainStage.close();
            });
        }
    }
}
