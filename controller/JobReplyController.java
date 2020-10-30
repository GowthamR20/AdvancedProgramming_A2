package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.*;

public class JobReplyController {
    @FXML
    private TextField offerPrice;
    @FXML
    private Label jobMessage;
    private JobPost jobPost;
    private String studentId;
    private double offer;
    private boolean canSave = false;
    DataBase db = new DataBase();


    public void submitJobReply(ActionEvent actionEvent) throws InvalidOfferException {
        canSave = false;
        if (jobPost.getStatus().equalsIgnoreCase("Open")) {

            try {
                try {
                    offer = Double.parseDouble(offerPrice.getText());
                    if (offer < 0) {
                        throw new InvalidOfferException("Enter valid price");
                    }
                    canSave = true;
                } catch (Exception e) {
                    canSave = false;
                }


                if (canSave) {

                    if (offer > Double.parseDouble(jobPost.getProposedPrice())) {
                        throw new InvalidOfferException("Your Offer is not Accepted.Sorry");
                    } else if (offer < Double.parseDouble(jobPost.getProposedPrice())) {
                        if (Double.parseDouble(jobPost.getProposedPrice()) == 0) {
                            jobPost.setLowestprice(String.valueOf(offer));
                        } else if (offer < Double.parseDouble(jobPost.getProposedPrice())) {
                            jobPost.setLowestprice(String.valueOf(offer));
                        }
                        String q = "Update jobDetails set lowestprice =" + "'" + offer + "' where postid = " + "'" + jobPost.getId() + "'";
                        db.updatePost(q);
                        ReplyPost r = new ReplyPost(jobPost.getId(), studentId, String.valueOf(offer));
                        r.addJobReply(jobPost);
                        alert("Your Offer is Accepted.");
//                    jobMessage.setText("Your Offer is Accepted.");
                    }
                } else {
                    throw new InvalidOfferException("Wrong Input ");
                }

            } catch (InvalidOfferException ie) {
            }
        }
    }

    public void setJobPost(JobPost p, String sid) {
        this.jobPost = p;
        this.studentId = sid;
    }

    public void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}

