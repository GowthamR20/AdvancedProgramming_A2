package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.IdGeneration;
import model.DataBase;
import model.InvalidOfferException;
import model.JobPost;
import model.WrongPriceException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class JobPostController implements Initializable {
    @FXML
    private static String studentId;
    private Label jobOperationLabel;
    @FXML
    private TextArea jobSummaryTextField;
    @FXML
    private Button jobSaleButton;
    @FXML
    private TextField jobName;
    @FXML
    private TextField jobDescription;
    @FXML
    private TextField jobProposedPrice;
    @FXML
    private Label validationMessage;
    @FXML
    private ImageView jobPhoto;

    private static String jobId;
    private boolean canSave = false;
    private FileChooser fileChooser;
    private File filePath;
    private IdGeneration ig;
    private String pathtoDb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jobOperationLabel.setText("Create Job Post");
    }

    public void setLoginId(String studentId) {
        this.studentId = studentId;
    }

    @FXML
    public void saveNewJob(ActionEvent actionEvent) throws IOException, WrongPriceException {
        if (jobName.getText().equals("") || jobDescription.getText().equals("") ||
                jobProposedPrice.getText().equals("")) {
            validationMessage.setText("All Fields Are Mandatory");
        } else {
            int proposedPrice=0;
            try {
                try {
                    proposedPrice = Integer.parseInt(jobProposedPrice.getText());
                } catch (Exception e) {
                    canSave = false;
                }

                if (proposedPrice > 0) {
                    canSave = true;
                } else {
                    throw new InvalidOfferException("Wrong Offer value");
                }


                if (canSave) {
                    ig = new IdGeneration();
                    ig.checkId(DataBase.checkEventIds("Job"));
                    jobId = ig.generateId("Job");
                    File dest;
                    if (this.filePath != null) {
                        dest = new File("./src/images/" + jobId + ".jpg");
                    } else {
                        dest = new File("./src/images/No_image_available.jpg");
                    }

                    JobPost jp = new JobPost(jobId, jobName.getText(), jobDescription.getText(), "Open", studentId,
                            jobProposedPrice.getText(), "0", dest.toString());
                    if (jp.saveJob()) {
                        if (this.filePath != null) {
                            String path = String.valueOf(this.filePath);
                            File source = new File(path);
                            Files.copy(source.toPath(), dest.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                            validationMessage.setText("Successfully created\n Your Job has been saved with ID: " + jobId);
                        } else {
                            validationMessage.setText("Successfully created\n Your Job has been saved with ID: " + jobId);
                        }
                        resetEntries(actionEvent);
                    }
                } else {
                    throw new InvalidOfferException("Wrong Input Type");
                }
            } catch (InvalidOfferException e) {
            }
        }
    }

    @FXML
    public void resetEntries(ActionEvent actionEvent) {
        jobName.setText("");
        jobDescription.setText("");
        jobProposedPrice.setText("");
        jobPhoto.setImage(null);
    }

    @FXML
    public void chooseImageJob(ActionEvent actionEvent) {
        this.filePath = null;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        if (this.filePath != null) {
            try {
                BufferedImage bi = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bi, null);
                jobPhoto.setImage(image);
            } catch (IOException e) {
            }
        } else {
            this.filePath = null;
        }
    }
}

