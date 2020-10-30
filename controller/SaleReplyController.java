package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.*;

public class SaleReplyController {
    @FXML
    private Label saleMessage;
    @FXML
    private TextField offerPrice;
    private static SalePost salePost;
    private boolean canSave = false;
    private DataBase db = new DataBase();
    private String studentId;
    private double offer;
    private ReplyPost r;

    @FXML
    public void submitSaleReply(ActionEvent actionEvent) throws InvalidOfferException {
        canSave = false;

        try {
            offer = Double.parseDouble(offerPrice.getText());
            if(!(offer > 0)) {
                throw new InvalidOfferException("Enter valid price");
            }
            canSave = true;

            if (canSave) {
                if (offer < Double.parseDouble(salePost.getSaleMinimumRaise())) {
                    canSave = false;
//                    Alert("Your Offer is not Accepted");
                    throw new InvalidOfferException("Your Offer is not Accepted");
//            saleMessage.setText("Your Offer is not Accepted");
                } else if (offer >= Double.parseDouble(salePost.getSaleAskingPrice())) {
                    canSave = true;
                    salePost.setHighestPrice(String.valueOf(offer));
                    salePost.setStatus("Closed");
                    Alert("Congrats! Item is sold to you!");
//            saleMessage.setText("Congrats! Item is sold to you!");
                } else if (offer >= Double.parseDouble(salePost.getHighestPrice()) + Double.parseDouble(salePost.getSaleMinimumRaise())) {
                    canSave = true;
                    salePost.setHighestPrice(String.valueOf(offer));
                    Alert("\"You offer is registered but not sold.The owner will get back to you\"");
//            saleMessage.setText("You offer is registered but not sold.The owner will get back to you");
                } else if (offer < Double.parseDouble(salePost.getHighestPrice()) + Double.parseDouble(salePost.getSaleMinimumRaise())) {
                    canSave=false;
                    throw new InvalidOfferException("Your Offer is not Accepted");
                }
                if (canSave) {
                    String q = "Update saledetails set status = " + "'" + salePost.getStatus() + "'" + "," +
                            "highestprice =" + "'" + salePost.getHighestPrice() + "' where postid = " + "'" + salePost.getId() + "'";
                    db.updatePost(q);
                    r = new ReplyPost(salePost.getId(), studentId, String.valueOf(offer));
                    r.addSaleReply(salePost);
                }
            }
        } catch (InvalidOfferException ie) {
        } catch (Exception e) {
            Alert("Enter an integer");
        }
    }

    public void Alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }


    public void setSalePost(SalePost p, String sid) {
        this.salePost = p;
        this.studentId = sid;
    }
}
