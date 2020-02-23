/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rent.system;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import entities.ContactDetails;
import entities.LandLord;
import entities.Register;
import static entities.Register.getPassword;
import static entities.Register.setPassword;
import generics.Generics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import regex.Regex;
import interfaces.DataFiles;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Oshaine
 */
public class RegisterLandLordController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXRadioButton male;
    @FXML
    private JFXRadioButton female;
    @FXML
    private JFXTextField telephone;
    @FXML
    private JFXTextField userrname;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField confirmPassword;
    @FXML
    private JFXDatePicker landlordDOB;

    @FXML
    private JFXTextField landAccountNo;
    @FXML
    private ImageView landlordPhoto;

    public String fPath = " ";
    @FXML
    private ToggleGroup group;

    public void setFilePath(String fPath) {
        this.fPath = fPath;
    }

    public String getFilePath() {
        return this.fPath;
    }

    public String genderOption() {
        String gender = "";
        if (this.male.isSelected()) {
            gender = "Male";
        } else if (this.female.isSelected()) {
            gender = "Female";
        }
        return gender;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void loginButton(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void registerLandlordBtn(ActionEvent event) {
        ArrayList<LandLord> personList = new ArrayList<>();
        Register register = new Register();

        LandLord landlord = new LandLord();
        ContactDetails contact = new ContactDetails();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String fp = getFilePath();

        if ("".equals(firstName.getText()) || "".equals(lastName.getText())
                || "".equals(email.getText()) || "".equals(password.getText()) || "".equals(landAccountNo.getText()) || "".equals(genderOption())
                || "".equals(confirmPassword.getText()) || landlordDOB.getValue() == null) {
            alert.setContentText("Please Fill Out All Fields");
            alert.show();
        } else {

            //landlord information
            landlord.setFirstName(firstName.getText());
            landlord.setLastName(lastName.getText());
            landlord.setGender(lastName.getText());

            landlord.setGender(genderOption());

            boolean found = true;

            Regex regex = new Regex();

            landlord.setAccountNum(landAccountNo.getText());
            landlord.setDOB(landlordDOB.getValue().toString());
            landlord.setUsername(userrname.getText());

            boolean success = true;

            while (success) {
                String confirmation;
                setPassword(password.getText());
                confirmation = confirmPassword.getText();
                if (getPassword().equals(confirmation)) {
                    landlord.setPassword(confirmation);
                    success = false;
                } else {
                    alert.setTitle("Password Error");
                    alert.setContentText("Password Did Not Match Try Again!");
                    alert.show();
                }
            }
            landlord.setEmailAddress(email.getText());

            contact.setTelephoneNo(telephone.getText());

            if (regex.regexEmailValidator(landlord.getEmailAddress()) == false) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Email Format! (example@gmail.com)");
                alert.show();
            } else if (regex.regexTeleValidator(contact.getTelephoneNo()) == false) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Telephone Format! (123-456-7890)");
                alert.show();
            } else {

                landlord.setContactDetails(contact);
                landlord.setImageLocation(fp);
                register.setLandLord(landlord);
                Generics genp = new Generics<>();
                
                if (genp.getObject(DataFiles.LANDLORD) == null) {//check if file is empty
                    personList = new ArrayList<>();//CREATE NEW INSTANCE    
                } else {
                    personList = (ArrayList<LandLord>) genp.getObject(DataFiles.LANDLORD);//read file
                }

                personList.add(landlord);//add new entry to file       

                if (genp.saveobject(personList, DataFiles.LANDLORD)) {
                    alert.setTitle("Landlord Registration");
                    alert.setContentText("Landlord Regisistration Successfull");
                    alert.show();
                }
            }

        }
    }

    @FXML
    private void uploadLandlordPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exeFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.SVG");
        fileChooser.getExtensionFilters().add(exeFilter);
        File file = fileChooser.showOpenDialog(null);

        //get selcted image
        BufferedImage img = null;

        try {
            img = ImageIO.read(file.getAbsoluteFile());

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Image");
            alert.setContentText("Landlord Did Not Upload Any Image");
            alert.show();
        }
        WritableImage i = SwingFXUtils.toFXImage(img, null);
        landlordPhoto.setImage(i);

        String imagePath = fileLocation(file.getAbsolutePath());
        setFilePath(imagePath);
        File writeImage = new File(imagePath);
        String extention = " ";

        int x = imagePath.lastIndexOf('.');
        if (x >= 0) {
            extention = imagePath.substring(x + 1);
        }
        if (!writeImage.exists()) {
            writeImage.getAbsoluteFile().getParentFile().mkdir();
            try {
                writeImage.createNewFile();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Image");
                alert.setContentText("Landlord Photo selected");
                alert.show();
            }

            try {
                ImageIO.write(img, extention, writeImage);
            } catch (IOException ex) {
            }
        }

    }

    public String fileLocation(String path) {
        File file = new File(DataFiles.TENANT);

        String fileP = file.getAbsolutePath();
        String newFile = fileP.replace(DataFiles.TENANT, "\\");
        String extension = " ";

        int x = path.lastIndexOf('.');
        if (x >= 0) {
            extension = path.substring(x + 1);
        }
        return newFile + "images\\" + this.landAccountNo.getText() + "." + extension;
    }

}
