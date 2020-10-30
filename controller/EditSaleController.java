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
import model.EventPost;
import model.SalePost;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditSaleController implements Initializable {
    @FXML

    private TextField sTitle;
    @FXML
    private TextField sDesc;
    @FXML
    private TextField sAskingPrice;
    @FXML
    private TextField sMinimumRaise;
    @FXML
    private Label validationMessage;
    private static SalePost sp;

    @FXML
    public void submitEdited(ActionEvent actionEvent) throws IOException {
        boolean canSave = false;
        try {
            double askingPrice = Double.parseDouble(sAskingPrice.getText());
            double minimumRaise = Double.parseDouble(sMinimumRaise.getText());
            if (askingPrice > 0) {
                canSave = true;
            }
            if (minimumRaise > 0) {
                canSave = true;
            }
        } catch (Exception e) {
            canSave = false;
        }
        if (canSave) {

            sp.setTitle(sTitle.getText());
            sp.setDescription(sDesc.getText());
            sp.setSaleAskingPrice(sAskingPrice.getText());
            sp.setSaleMinimumRaise(String.valueOf(sMinimumRaise.getText()));

            FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/PostDetails_View.fxml"));
            Parent eventRoot = eventLoader.load();
            PostDetailsController postDetailsController = new PostDetailsController();
            postDetailsController.setPost(sp);
            Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setSale(SalePost p) {
        this.sp = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sTitle.setText(sp.getTitle());
        sDesc.setText(sp.getDescription());
        sAskingPrice.setText(String.valueOf(sp.getSaleAskingPrice()));
        sMinimumRaise.setText(sp.getSaleMinimumRaise());
    }
}