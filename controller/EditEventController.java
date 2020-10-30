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
import model.ValidateDate;
import model.EventPost;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditEventController implements Initializable {

    @FXML
    private TextField eTitle;
    @FXML
    private TextField eDesc;
    @FXML
    private TextField eVenue;
    @FXML
    private TextField eCapacity;
    @FXML
    private TextField eDate;
    @FXML
    private Label validationMessage;
    ValidateDate vd;
    private static EventPost ep;

    @FXML
    public void submitEdited(ActionEvent actionEvent) throws IOException {
        boolean checkDate = false;
        boolean canSave = false;
        if (eTitle.getText().equals("") || eDesc.getText().equals("") ||
                eDate.getText().equals("") || eCapacity.getText().equals("") || eVenue.getText().equals("")) {
            validationMessage.setText("All Fields Are Mandatory");
        } else {
            checkDate = vd.validateDate(eDate.getText());

            int maxCapacity = 0;
            try {
                if (!eCapacity.getText().equals(""))
                    maxCapacity = Integer.parseInt(eCapacity.getText());
                if (maxCapacity > 0) {
                    canSave = true;
                }
            } catch (Exception e) {
                canSave = false;
            }
        }
        if (checkDate && canSave) {
            ep.setTitle(eTitle.getText());
            ep.setDescription(eDesc.getText());
            ep.setDate(eDate.getText());
            ep.setMaxCapacity(Integer.parseInt(eCapacity.getText()));

            ep.setVenue(eVenue.getText());
            FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/PostDetails_View.fxml"));
            Parent eventRoot = eventLoader.load();
            PostDetailsController postDetailsController = new PostDetailsController();
            postDetailsController.setPost(ep);
            Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
            stage.close();
        }
    }


    public void setEvent(EventPost p) {
        this.ep = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eTitle.setText(ep.getTitle());
        eDesc.setText(ep.getDescription());
        eDate.setText(ep.getDate());
        eCapacity.setText(String.valueOf(ep.getMaxCapacity()));
        eVenue.setText(ep.getVenue());
    }
}
