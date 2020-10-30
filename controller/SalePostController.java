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
import model.SalePost;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class SalePostController implements Initializable {
    @FXML
    private TextArea saleSummaryTextField;
    @FXML
    private Button saveSaleButton;
    @FXML
    private TextField saleName;
    @FXML
    private TextField saleDescription;
    @FXML
    private TextField saleAskingPrice;
    @FXML
    private TextField saleMinimumRaise;
    @FXML
    private Label validationMessage;
    @FXML
    private ImageView salePhoto;
    @FXML
    private Label saleOperationLabel;

    private String saleId;
    private static String studentId;
    boolean canSave = false;
    private FileChooser fileChooser;
    private File filePath;
    private IdGeneration ig;
    private StringBuffer message = new StringBuffer();
    private String pathtoDb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saleOperationLabel.setText("Create Sale Post");

    }

    @FXML
    public void saveNewSale(ActionEvent actionEvent) throws IOException, InvalidOfferException {
        if (saleName.getText().equals("") || saleDescription.getText().equals("") ||
                saleAskingPrice.getText().equals("") || saleMinimumRaise.getText().equals("")) {
            validationMessage.setText("All Fields Are Mandatory");
        } else {
            try {
                ig = new IdGeneration();
                ig.checkId(DataBase.checkEventIds("Sale"));
                saleId = ig.generateId("Sale");
                File dest;
                if (this.filePath != null) {
                    dest = new File("./src/images/" + saleId + ".jpg");
                } else {
                    dest = new File("./src/images/No_image_available.jpg");
                }
                SalePost sp = new SalePost(saleId, saleName.getText(), saleDescription.getText(), "Open", studentId,
                        saleAskingPrice.getText(), saleMinimumRaise.getText(), "0", dest.toString());
                if (!(sp.validateAskingPrice()) || !(sp.validateMinRaise())) {
//                validationMessage.setText("Enter valid Price");
                    throw new InvalidOfferException("Wrong Price Value");
                } else {
                    if (sp.saveSale()) {
                        if (this.filePath != null) {
                            String path = String.valueOf(this.filePath);
                            File source = new File(path);
                            Files.copy(source.toPath(), dest.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                            validationMessage.setText("Successfully created\n Your sale has been saved with ID: " + saleId);
                            resetEntries(actionEvent);
                        } else {
                            validationMessage.setText("Successfully created\n Your sale has been saved with ID: " + saleId);
                            resetEntries(actionEvent);
                        }
                    }
                }
            } catch (InvalidOfferException ie) {
            }
        }
    }

    @FXML
    public void chooseImageSale(ActionEvent actionEvent) {
        this.filePath = null;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        if (this.filePath != null) {
            try {
                BufferedImage bi = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bi, null);
                salePhoto.setImage(image);
            } catch (IOException e) {
            }
        } else {
            this.filePath = null;
        }
    }

    public void resetEntries(ActionEvent actionEvent) {
        saleName.setText("");
        saleDescription.setText("");
        saleAskingPrice.setText("");
        saleMinimumRaise.setText("");
        salePhoto.setImage(null);

    }

    public void setLoginId(String studentId) {
        this.studentId = studentId;
    }


}
