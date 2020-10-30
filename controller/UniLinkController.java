package controller;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import org.hsqldb.lib.HsqlByteArrayOutputStream;

import java.io.*;
import java.net.URL;
import java.util.*;

public class UniLinkController implements Initializable, Serializable {
    private static final long serialVersionUID = 1L;

    @FXML
    private Label message;
    @FXML
    private MenuItem exportPost;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ChoiceBox<String> typeFilter;
    @FXML
    private ChoiceBox<String> statusFilter;
    @FXML
    private ChoiceBox<String> creatorFilter;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private MenuItem quitSystem;
    @FXML
    private BorderPane uniLink;
    @FXML
    private ListView postList;
    private DataBase db = new DataBase();
    private EventPost ep = new EventPost();
    private SalePost sp;
    private File filepath;

    public String getStudentId() {
        return studentId;
    }

    public EventPost getEp() {
        return ep;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    private static String studentId;
    private String filterType = "All Post";
    private String filterStatus = "All Post";
    private String filterCreator = "All Post";

    private static ArrayList<Post> posts = new ArrayList<>();
    private static ArrayList<ReplyPost> replies = new ArrayList<>();

    private String filterQuery = "";
    private String filterQuerye = "";
    private String filterQuerys = "";
    private String filterQueryj = "";
    private String filterT = "";
    private String filterC = "";
    private String filterS = "";
    //    private ObservableList<String> post;
    private String postDetailsSplit[];
    private String postIDSplit[];
    private String imagepath[];
    private String creator[];
    private static Post p;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        posts.clear();
        welcomeLabel.setText("Welcome " + getStudentId() + "!");

        typeFilter.getItems().add("All Post");
        typeFilter.getItems().add("Event Post");
        typeFilter.getItems().add("Sale Post");
        typeFilter.getItems().add("Job Post");
        typeFilter.setValue("All Post");
        typeFilter.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            filterType = newVal;
        });

        statusFilter.getItems().add("All Post");
        statusFilter.getItems().add("Open Post");
        statusFilter.getItems().add("Closed Post");
        statusFilter.setValue("All Post");
        statusFilter.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            filterStatus = newVal;
        });

        creatorFilter.getItems().add("All Post");
        creatorFilter.getItems().add("My Post");
        creatorFilter.setValue("All Post");
        creatorFilter.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            filterCreator = newVal;
        });


        postList.setCellFactory(param -> new Cell());


    }

    public void setLoginId(String loginId) {
        setStudentId(loginId);
    }

    @FXML
    public void developerDetail(ActionEvent actionEvent) throws IOException {
        Stage mainStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/DeveloperDetail_View.fxml"));
        mainStage.setTitle("Developer Detail");
        mainStage.setScene(new Scene(root));
        mainStage.showAndWait();
    }

    @FXML
    public void importPostToFile(ActionEvent actionEvent) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        this.filepath = fileChooser.showOpenDialog(stage);
        if (this.filepath != null) {
            try {
                File f = new File(String.valueOf(this.filepath));
                if (f.exists()) {
                    ArrayList<Post> temp = null;
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filepath));
                    temp = (ArrayList<Post>) in.readObject();
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i) instanceof EventPost) {
                            ((EventPost) temp.get(i)).saveEvent();
                        }
                        if (temp.get(i) instanceof SalePost) {
                            ((SalePost) temp.get(i)).saveSale();
                        }
                        if (temp.get(i) instanceof JobPost) {
                            ((JobPost) temp.get(i)).saveJob();
                        }
//                        if (temp.get(i) instanceof ReplyPost) {
//                            System.out.println("Reply");
//                        }
                    }
                    in.close();
                }
            } catch (IOException | ClassNotFoundException e) {
            }
        }
    }


    @FXML
    public void exportPostToFile(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) menuBar.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String chosenpath = selectedDirectory.getAbsolutePath().replaceAll("\\\\", "/");
            FileOutputStream file = new FileOutputStream(chosenpath + "/export_data.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            getEventPosts();
            out.writeObject(posts);
            System.out.println(posts+"!!!!!!!!!!!!!!!!!!!!!!");
            getSalePosts();
            out.writeObject(posts);
            System.out.println(posts+"!!!!!!!!!!!!!!!!!!!!!!");
            getJobPosts();
            out.writeObject(posts);
            System.out.println(posts+"!!!!!!!!!!!!!!!!!!!!!!");
            replies = db.getReplyDetails("Select * from replydetails");
            out.writeObject(replies);
            postList.getItems().clear();
            out.close();
            file.close();
        }
    }

    @FXML
    public void eventPost(ActionEvent actionEvent) throws IOException {
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/EventPost_View.fxml"));
        Parent eventRoot = eventLoader.load();
        EventPostController eventPostController = eventLoader.getController();
        eventPostController.setLoginId(getStudentId());

        Parent root = FXMLLoader.load(getClass().getResource("/view/EventPost_View.fxml"));
        Stage eventPostWindow = new Stage();
        eventPostWindow.initModality(Modality.APPLICATION_MODAL);
        eventPostWindow.setTitle("Event Post");
        eventPostWindow.setScene(new Scene(root));
        eventPostWindow.showAndWait();
    }


    @FXML
    public void salePost(ActionEvent actionEvent) throws IOException {
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/SalePost_View.fxml"));
        Parent eventRoot = eventLoader.load();
        SalePostController salePostController = eventLoader.getController();
        salePostController.setLoginId(getStudentId());

        Parent root = FXMLLoader.load(getClass().getResource("/view/SalePost_View.fxml"));
        Stage salePostWindow = new Stage();
        salePostWindow.initModality(Modality.APPLICATION_MODAL);
        salePostWindow.setTitle("Sale Post");
        salePostWindow.setScene(new Scene(root));
        salePostWindow.showAndWait();
    }

    @FXML
    public void jobPost(ActionEvent actionEvent) throws IOException {
        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/JobPost_View.fxml"));
        Parent eventRoot = eventLoader.load();
        JobPostController jobPostController = eventLoader.getController();
        jobPostController.setLoginId(getStudentId());

        Parent root = FXMLLoader.load(getClass().getResource("/view/JobPost_View.fxml"));
        Stage salePostWindow = new Stage();
        salePostWindow.initModality(Modality.APPLICATION_MODAL);
        salePostWindow.setTitle("Job Post");
        salePostWindow.setScene(new Scene(root));
        salePostWindow.showAndWait();
    }

    @FXML
    public void logoutUniLink(ActionEvent actionEvent) throws IOException {
        Stage s = (Stage) logoutButton.getScene().getWindow();
        s.close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login_View.fxml"));
        s.setTitle("Login Page");
        s.setScene(new Scene(root));
        s.show();
    }

    public void quitUniLink(ActionEvent actionEvent) {
        Stage stage = (Stage) uniLink.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void filterSearchPost(ActionEvent actionEvent) throws IOException {
        posts.clear();
        postList.getItems().removeAll();
        filterQuery = "";
        filterS = "";
        filterC = "";

        getQuery();
        if (filterType.equalsIgnoreCase("Event Post")) {
            getEventPosts();
        } else if (filterType.equalsIgnoreCase("Sale Post")) {
            getSalePosts();
        } else if (filterType.equalsIgnoreCase("Job Post")) {
            getJobPosts();
        } else {
            getEventPosts();
            getSalePosts();
            getJobPosts();
        }
    }

    private void getJobPosts() {
        filterT = "JOBDETAILS";
        filterQuery = "Select * from JOBDETAILS " + filterC + filterS;
        int i = 0;
        posts = db.getJobRecords(filterQuery);
        while (i < posts.size()) {
            if (posts.get(i) instanceof JobPost) {
                StringBuilder sb = new StringBuilder();
                sb.append(posts.get(i).getDetails()).append("\n").append(posts.get(i).getPostTypeDetails());
                postList.getItems().add(sb);
            }
            i++;

        }
    }

    private void getSalePosts() {
        filterT = "SALEDETAILS";
        filterQuery = "Select * from SALEDETAILS " + filterC + filterS;
        int i = 0;
        posts = db.getSaleRecords(filterQuery);
        while (i < posts.size()) {
            if (posts.get(i) instanceof SalePost) {
                StringBuilder sb = new StringBuilder();
                sb.append(posts.get(i).getDetails()).append("\n").append(posts.get(i).getPostTypeDetails());
                postList.getItems().add(sb);
            }
            i++;
        }
    }

    private void getEventPosts() {
        filterT = "EVENTDETAILS";

        filterQuery = "Select * from EVENTDETAILS " + filterC + filterS;
        int i = 0;
        posts = db.getEventRecords(filterQuery);
        while (i < posts.size()) {
            if (posts.get(i) instanceof EventPost) {
                StringBuilder sb = new StringBuilder();
                sb.append(posts.get(i).getDetails()).append("\n").append(posts.get(i).getPostTypeDetails());
                int finalI = i;
                postList.getItems().add(sb);
            }
            i++;
        }
    }

    private void getQuery() {

        postList.getItems().clear();
        if (filterCreator.equalsIgnoreCase("My Post")) {
            filterC = " where creatorid =" + "'" + studentId + "'";
        } else {
            filterC = "";
        }
        if (filterStatus.equalsIgnoreCase("open post")) {
            if (filterC.equals("")) {
                filterS = " where status = 'Open'";
            } else {
                filterS = " and status = 'Open'";
            }
        } else if (filterStatus.equalsIgnoreCase("closed post")) {
            if (filterC.equals("")) {
                filterS = " where status = 'Closed'";
            } else {
                filterS = " and status = 'Closed'";
            }
        }
    }

    static class Cell extends ListCell<StringBuilder> {
        ObservableList<String> post;
        HBox hbox = new HBox();
        Button replyPost = new Button("Reply");
        Button moreDetails = new Button("More Details");
        Label label = new Label();
        Pane pane = new Pane();
        ImageView view;

        UniLinkController un = new UniLinkController();

        public Cell() {
            super();
            Image postImage = new Image("/images/No_image_available.jpg");
            view = new ImageView(postImage);
            view.setFitHeight(50);
            view.setFitWidth(50);
            hbox.getChildren().addAll(view, label, pane, replyPost, moreDetails);
            hbox.setHgrow(pane, Priority.ALWAYS);
            hbox.setMargin(replyPost, new Insets(10, 10, 10, 10));
            hbox.setMargin(moreDetails, new Insets(10, 10, 10, 10));
            hbox.setMargin(view, new Insets(10, 10, 10, 10));


            replyPost.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(javafx.event.Event event) {
                    for (int i = 0; i < un.posts.size(); i++) {
                        if (label.getText().contains(un.posts.get(i).getDetails())) {
                            un.p = un.posts.get(i);
                        }
                    }
                    if (un.p.getCreator_id().equals(un.getStudentId())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "You Cannot reply to your own post", ButtonType.OK);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {
                            alert.close();
                        }
                    } else {
                        if (p instanceof EventPost) {
                            try {
                                un.getEp().handleReply(p, un.getStudentId());
                            } catch (ClosedPostException e) {
//                                System.out.println("Closed Exception");
                            }
                        } else if (p instanceof SalePost) {
                            if (p.getStatus().equalsIgnoreCase("Open")) {
                                Stage mainStage = new Stage();
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SaleReply_View.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                SaleReplyController saleReplyController = loader.getController();
                                saleReplyController.setSalePost((SalePost) p, studentId);
                                mainStage.initModality(Modality.APPLICATION_MODAL);
                                mainStage.setScene(new Scene(root));
                                mainStage.showAndWait();
                            } else {
                                try {
                                    throw new ClosedPostException("Post is closed");
                                } catch (ClosedPostException e) {
                                }
                            }
                        } else if (p instanceof JobPost) {
                            if (p.getStatus().equalsIgnoreCase("Open")) {
                                Stage mainStage = new Stage();
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/JobReply_View.fxml"));
                                Parent root = null;
                                try {
                                    root = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                JobReplyController jobReplyController = loader.getController();
                                jobReplyController.setJobPost((JobPost) p, studentId);
                                mainStage.initModality(Modality.APPLICATION_MODAL);
                                mainStage.setTitle("Reply To Job");
                                mainStage.setScene(new Scene(root));
                                mainStage.showAndWait();
                            } else {
                                try {
                                    throw new ClosedPostException("Post is closed");
                                } catch (ClosedPostException e) {
                                }
                            }
                        }
                    }
                }
            });

            moreDetails.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(javafx.event.Event event) {
                    for (int i = 0; i < un.posts.size(); i++) {
                        if (label.getText().contains(un.posts.get(i).getDetails())) {
                            un.p = un.posts.get(i);
                        }
                    }
                    if (!(un.p.getCreator_id().equals(un.getStudentId()))) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "You Cannot perform this operation. Only the creator can", ButtonType.OK);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {
                            alert.close();
                        }
                    } else {
                        FXMLLoader eventLoader = new FXMLLoader(getClass().getResource("/view/PostDetails_view.fxml"));
                        try {
                            Parent eventRoot = eventLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PostDetailsController postDetailsController = eventLoader.getController();
                        postDetailsController.setPost(un.p);
                        Stage stage = (Stage) ((Node) (event).getSource()).getScene().getWindow();
                        stage.close();

                        Stage mainStage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PostDetails_view.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mainStage.setTitle("Detailed View");
                        mainStage.setScene(new Scene(root));
                        mainStage.show();
                    }
                }
            });
        }

        public void loadImageInList(String path) {
            String imagepath = path.replaceAll("\\\\", "/");
            File filePath = new File(imagepath);
            Image image = new Image(filePath.toURI().toString());
            view.setImage(image);
        }

        public void updateItem(StringBuilder name, boolean empty) {

            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);
            if (name != null && !empty) {
                label.setText(String.valueOf(name));
                for (int i = 0; i < un.posts.size(); i++) {
                    if (label.getText().contains(un.posts.get(i).getDetails())) {
                        un.p = un.posts.get(i);
                    }
                }
                if (un.p instanceof EventPost) {
                    String path = un.p.getImagePath();
                    loadImageInList(path);
                    setStyle("-fx-background-color: lightslategrey");
                } else if (un.p instanceof SalePost) {
                    String path = un.p.getImagePath();
                    loadImageInList(path);
                    setStyle("-fx-background-color: whitesmoke");
                } else if (un.p instanceof JobPost) {
                    String path = un.p.getImagePath();
                    loadImageInList(path);
                    setStyle("-fx-background-color:  darkgray");
                }
                setGraphic(hbox);
            }

        }
    }
}