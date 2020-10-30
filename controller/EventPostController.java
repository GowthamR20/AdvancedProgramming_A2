package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.IdGeneration;
import model.DataBase;
import model.EventPost;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EventPostController implements Initializable {

    @FXML
    public VBox createEventBox;

    @FXML
    public Label eventOperationLabel;
    @FXML
    public TextField eventName;
    @FXML
    public TextField eventDescription;
    @FXML
    public TextField eventVenue;
    @FXML
    public TextField eventDate;
    @FXML
    public TextField eventMaxCapacity;

    @FXML
    public Label validationMessage;
    @FXML
    public Button saveEventButton;
    @FXML
    public Label postNameId;
    @FXML
    public ImageView eventPhoto;


    private FileChooser fileChooser;
    private File filePath;
    private static String studentId;
    private static String postId;
    private boolean canSave = false;
    private IdGeneration ig;
    private String pathtoDb = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventOperationLabel.setText("Create Event Post");
    }

    @FXML
    public void resetEntries(ActionEvent actionEvent) {
        eventName.setText("");
        eventDescription.setText("");
        eventVenue.setText("");
        eventDate.setText("");
        eventMaxCapacity.setText("");
        eventPhoto.setImage(null);

    }

    @FXML
    public void validateCapacity() {
        int maxCapacity = 0;
        try {
            if (!eventMaxCapacity.getText().equals(""))
                maxCapacity = Integer.parseInt(eventMaxCapacity.getText());
            if (maxCapacity > 0) {
                canSave = true;
                saveEventButton.setDisable(false);
                validationMessage.setText("");
            }
        } catch (Exception e) {
            validationMessage.setText("Capacity value must be greater than 0");
            canSave = false;
        }
    }

    @FXML
    public void saveNewEvent(ActionEvent actionEvent) throws IOException {
        File dest = null;
        if (eventName.getText().equals("") || eventDescription.getText().equals("") ||
                eventVenue.getText().equals("") || eventMaxCapacity.getText().equals("")) {
            validationMessage.setText("All Fields Are Mandatory");
        } else {
            ig = new IdGeneration();
            ig.checkId(DataBase.checkEventIds("Event"));
            postId = ig.generateId("Event");

            if (filePath != null) {
                dest = new File("./src/images/" + postId + ".jpg");
            } else {
                dest = new File("./src/images/No_image_available.jpg");
            }
            EventPost ep = new EventPost(postId, eventName.getText(), eventDescription.getText(), eventDate.getText(), studentId,
                    "Open", eventVenue.getText(), Integer.parseInt(eventMaxCapacity.getText()), "",0, dest.toString());
            if (!ep.validateDate()) {
                validationMessage.setText("Enter Valid Date.");
            } else {
                if (ep.saveEvent()) {
                    String path = String.valueOf(this.filePath);
                    if (this.filePath != null) {
                        File source = new File(path);
                        Files.copy(source.toPath(), dest.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                        validationMessage.setText("Successfully created\n Your event has been saved with ID: " + postId);
                        resetEntries(actionEvent);
                    } else {
                        validationMessage.setText("Successfully created\n Your event has been saved with ID: " + postId);
                        resetEntries(actionEvent);
                    }
                }
            }
        }
    }

    @FXML
    public void chooseImage(ActionEvent actionEvent) throws IOException {
        this.filePath = null;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        if (this.filePath != null) {
            try {
                BufferedImage bi = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bi, null);
                eventPhoto.setImage(image);
            } catch (IOException e) {
            }
        } else {
            this.filePath = null;
        }
    }

    public void setLoginId(String studentId) {
        this.studentId = studentId;
    }
}