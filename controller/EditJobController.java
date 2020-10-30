package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.JobPost;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditJobController implements Initializable {
    private static JobPost jp;

    @FXML
    private TextField jTitle;
    @FXML
    private TextField jDesc;
    @FXML
    private TextField jProposedPrice;
    private Label validationMessage;

    public void setjob(JobPost p) {
        this.jp = p;
    }

    @FXML
    public void submitEdited(ActionEvent actionEvent) throws IOException {
        boolean canSave = false;
        try {
            int proposedPrice = Integer.parseInt(jProposedPrice.getText());
            if (proposedPrice > 0) {
                canSave = true;
            }
        } catch (Exception e) {
            canSave = false;
            validationMessage.setText("Enter a valid amount");

        }

        if (canSave) {
            jp.setTitle(jTitle.getText());
            jp.setDescription(jDesc.getText());
            jp.setProposedPrice(jProposedPrice.getText());
            FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/PostDetails_View.fxml"));
            Parent eventRoot = eventLoader.load();
            PostDetailsController postDetailsController = new PostDetailsController();
            postDetailsController.setPost(jp);
            Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jTitle.setText(jp.getTitle());
        jDesc.setText(jp.getDescription());
        jProposedPrice.setText(jp.getProposedPrice());
    }
}