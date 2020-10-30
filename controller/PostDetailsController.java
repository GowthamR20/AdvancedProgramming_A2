package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PostDetailsController implements Initializable {
    @FXML
    private Label postDetailTitle;
    private static String postId;
    @FXML
    private TextArea postDetail;
    @FXML
    private BorderPane moreDetails;
    @FXML
    private ImageView postImage;
    @FXML
    private TableView<ReplyPost> replyTable;
    private  TableColumn pId = new TableColumn("Post-Id");
    private  TableColumn prId = new TableColumn("Replier");
    private static TableColumn pVal;

    private static String imagePath;
    private static String tableName;

    private FileChooser fileChooser;
    private static File filePath;
    private static Post p;
    private static ArrayList<ReplyPost> rpList = new ArrayList<>();
    private ArrayList<ReplyPost> replyList = new ArrayList<>();
    static ObservableList<ReplyPost> rDetails = null;
    private DataBase db;


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postid) {
        this.postId = postid;
    }

    public void setPost(Post p) {
        this.p = p;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (p != null) {
            loadPost();
            if (p instanceof EventPost) {
                tableName = "eventDetails";
                pVal = new TableColumn("Value");
            } else if (p instanceof SalePost) {
                tableName = "saledetails";
                pVal = new TableColumn("Offer Price");
            } else {
                tableName = "jobdetails";
                pVal = new TableColumn("Offer Price");
            }
            replyTable.getColumns().addAll(pId, prId, pVal);
            loadReplies();

        }
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) (actionEvent).getSource()).getScene().getWindow();
        stage.close();
        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UniLink_View.fxml"));
        Parent root = loader.load();
        mainStage.setTitle("Uni Link System");
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }


    public void loadPost() {

        postDetailTitle.setText("More Details for post: " + p.getId());
        setPostId(p.getId());
        setImagePath(p.getImagePath());
        StringBuffer s = new StringBuffer();
        s.append(p.getDetails()).append(p.getPostTypeDetails());
        if (p instanceof SalePost) {
            s.append("Asking Price: ").append(((SalePost) p).getSaleAskingPrice()).append("\nHighest Price:").append(((SalePost) p).getHighestPrice()).append("\nMinimum Raise: ").append(((SalePost) p).getSaleMinimumRaise());
        }
        String sd = String.valueOf(s);
        postDetail.setText(sd);
        loadImage();
    }

    public void loadImage() {
        String path = getImagePath();
        String imagepath = path.replaceAll("\\\\", "/");
        filePath = new File(imagepath);
        Image image = new Image(filePath.toURI().toString());
        postImage.setImage(image);
    }

    public void loadReplies() {
        replyList = null;
        String q = "";
        if (p instanceof SalePost) {
            q = "Select * from REPLYDETAILS where postid=" + "'" + p.getId() + "'" + " order by value desc";
        } else if (p instanceof EventPost) {
            q = "Select * from REPLYDETAILS where postid=" + "'" + p.getId() + "'";
        } else if (p instanceof JobPost) {
            q = "Select * from REPLYDETAILS where postid=" + "'" + p.getId() + "'";
        }
        DataBase db = new DataBase();
        replyList = db.getReplyDetails(q);
        rDetails = FXCollections.observableArrayList();

        for (int i = 0; i < replyList.size(); i++) {
            if (replyList.get(i).getPostid().equals(p.getId())) {
                rDetails.add(replyList.get(i));
            }
        }

        pId.setCellValueFactory(new PropertyValueFactory<ReplyPost, String>("postid"));
        pId.setMinWidth(100);
        prId.setCellValueFactory(new PropertyValueFactory<ReplyPost, String>("attendees"));
        pId.setMinWidth(100);
        pVal.setCellValueFactory(new PropertyValueFactory<ReplyPost, String>("value"));
        pId.setMinWidth(100);
        replyTable.setItems(rDetails);
    }

    @FXML
    public void closePost(ActionEvent actionEvent) throws IOException {
        String query = "UPDATE " + tableName +
                " SET status = 'Closed'" +
                " WHERE postid =" + "'" + getPostId() + "'";
        DataBase.updatePost(query);
        backToMain(actionEvent);
    }

    @FXML
    public void deletePost(ActionEvent actionEvent) throws IOException {
        boolean delete = false;
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete the post?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            delete = true;
            alert.close();
        }
        if (delete) {
            String query = "DELETE FROM " + tableName +
                    " WHERE postid =" + "'" + getPostId() + "'";
            String deleteReply = "DELETE FROM REPLYDETAILS WHERE postid =" + "'" + getPostId() + "'";
            db.deletePost(query);
            db.deletePost(deleteReply);

            if (!(p.getImagePath().contains("No_image_available"))) {
                File f = new File(p.getImagePath());
                f.delete();
            }

            backToMain(actionEvent);
        }
    }

    public void reUploadImage(ActionEvent actionEvent) throws EditDeniedException {
        this.filePath = null;
        try {
            if (rDetails.size() > 0) {
                throw new EditDeniedException("Students have replied. You cannot Edit the post.");
//                Alert alert = new Alert(Alert.AlertType.WARNING, "Students have replied. You cannot Edit the post.", ButtonType.OK);
//                alert.showAndWait();
//                if (alert.getResult() == ButtonType.OK) {
//                    alert.close();
//                }
            } else {

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image");
                this.filePath = fileChooser.showOpenDialog(stage);
                if (this.filePath != null) {
                    try {
                        BufferedImage bi = ImageIO.read(filePath);
                        Image image = SwingFXUtils.toFXImage(bi, null);
                        postImage.setImage(image);
                    } catch (IOException e) {
                    }
                } else {
                        filePath = new File(getImagePath());
                    BufferedImage bi = ImageIO.read(filePath);
                    Image image = SwingFXUtils.toFXImage(bi, null);
                    postImage.setImage(image);

                }
            }
        } catch (EditDeniedException | IOException e) {
        }
    }

    public void saveEditedPost(ActionEvent actionEvent) throws IOException {
        File dest;

        if (filePath != null) {
            dest = new File("./src/images/" + postId + ".jpg");
//            dest = filePath;
        } else {
            dest = new File("./src/images/No_image_available.jpg");
        }
        String path = String.valueOf(this.filePath);
        if (this.filePath != null) {
            File source = new File(path);
            Files.copy(source.toPath(), dest.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        String query = "";
        if (tableName.equalsIgnoreCase("eventdetails")) {
            query = "UPDATE " + tableName +
                    " SET postimage =" + "'" + dest + "'" + ", postname =" + "'" + p.getTitle() + "'" + ", postdescription=" + "'" + p.getDescription() + "'" +
                    ", eventvenue=" + "'" + ((EventPost) p).getVenue() + "'" + ", eventmaxlimit=" + "'" + ((EventPost) p).getMaxCapacity() + "'" +
                    ", eventdate=" + "'" + ((EventPost) p).getDate() + "'" +
                    " WHERE postid =" + "'" + getPostId() + "'";
        } else if (tableName.equalsIgnoreCase("saledetails")) {
            query = "UPDATE " + tableName +
                    " SET postname =" + "'" + p.getTitle() + "'" + ", postdescription =" + "'" + p.getDescription() + "'" +
                    ", askingprice =" + "'" + ((SalePost) p).getSaleAskingPrice() + "'" +
                    ", minimumprice =" + "'" + ((SalePost) p).getSaleMinimumRaise() + "'" + ", postimage =" + "'" + dest + "'"
                    + " WHERE postid =" + "'" + getPostId() + "'";
        } else if (tableName.equalsIgnoreCase("jobdetails")) {
            query = "UPDATE " + tableName +
                    " SET postimage =" + "'" + dest + "'" + ", postname =" + "'" + p.getTitle() + "'" + ", postdescription =" + "'" + p.getDescription() + "'" +
                    ", proposedprice =" + "'" + ((JobPost) p).getProposedPrice() + "'" +
                    " WHERE postid =" + "'" + getPostId() + "'";
        }
        DataBase.updatePost(query);
        backToMain(actionEvent);
    }

    @FXML
    public void editPost(ActionEvent actionEvent) throws IOException, EditDeniedException {
        try {
            if (rDetails.size() > 0) {
                throw new EditDeniedException("Students have replied. You cannot Edit the post.");
//                Alert alert = new Alert(Alert.AlertType.WARNING, "Students have replied. You cannot Edit the post.", ButtonType.OK);
//                alert.showAndWait();
//                if (alert.getResult() == ButtonType.OK) {
//                    alert.close();
//                }
            } else {
                if (p instanceof EventPost) {
                    editEvent();
                } else if (p instanceof SalePost) {
                    editSave();
                } else {
                    editJob();
                }
            }
        } catch (EditDeniedException e) {
        }
    }

    private void editJob() throws IOException {
        Stage s = new Stage();
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/EditJob_View.fxml"));
        EditJobController editJobController = new EditJobController();
        editJobController.setjob((JobPost) p);
        Parent root = eventLoader.load();
        s.setTitle("Edit Job View");
        s.setScene(new Scene(root));
        s.showAndWait();
        loadPost();
    }

    private void editSave() throws IOException {
        Stage s = new Stage();
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/EditSale_View.fxml"));
        EditSaleController editSaleController = new EditSaleController();
        editSaleController.setSale((SalePost) p);
        Parent root = eventLoader.load();
        s.setTitle("Edit Sale View");
        s.setScene(new Scene(root));
        s.showAndWait();
        loadPost();
    }


    public void editEvent() throws IOException {

        Stage s = new Stage();
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/EditEvent_View.fxml"));
        EditEventController editEventController = new EditEventController();
        editEventController.setEvent((EventPost) p);
        Parent eventRoot = eventLoader.load();
        s.setTitle("Edit Event View");
        s.setScene(new Scene(eventRoot));
        s.showAndWait();
        loadPost();
    }
}


