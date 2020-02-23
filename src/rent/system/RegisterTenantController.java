/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rent.system;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import entities.ContactDetails;
import entities.Payments;
import entities.Register;
import entities.Tenant;
import generics.Generics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Oshaine
 */
public class RegisterTenantController implements Initializable {

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
    private JFXTextField streetLine1;
    @FXML
    private JFXTextField streetLine2;
    @FXML
    private JFXTextField tenantAccountNo;
    @FXML
    private JFXTextField city;
    @FXML
    private JFXTextField parish;
    @FXML
    private JFXTextField poBox;
    @FXML
    private JFXTextField country;
    @FXML
    private JFXTextField appartmentNo;
    @FXML
    private ImageView tenantPhoto;

    @FXML
    private JFXDatePicker tenantDOB;

    public String fPath = " ";
    @FXML
    private Label landlordInfo;
    @FXML
    private ToggleGroup group;

    public String genderOption() {
        String gender = "";
        if (this.male.isSelected()) {
            gender = "Male";
        } else if (this.female.isSelected()) {
            gender = "Female";
        }
        return gender;
    }

    public void setFilePath(String fPath) {
        this.fPath = fPath;
    }

    public String getFilePath() {
        return this.fPath;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainDashboardController main = new MainDashboardController();
        main.landlordInfo(landlordInfo);

    }

    @FXML
    private void registerTenantBtn(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        Generics genp = new Generics<>();
        ArrayList<Tenant> tenantList = new ArrayList<>();
        Tenant tenant = new Tenant();
        Payments payment = new Payments();
        ContactDetails contact = new ContactDetails();
        Register register = new Register();
        LocalDate date = LocalDate.now();
        String fp = getFilePath();

        if ("".equals(firstName.getText()) || "".equals(lastName.getText())
                || "".equals(email.getText()) || "".equals(telephone.getText()) || "".equals(streetLine1.getText())
                || "".equals(streetLine2.getText()) || "".equals(appartmentNo.getText())
                || "".equals(city.getText()) || "".equals(parish.getText()) || "".equals(poBox.getText())
                || "".equals(country.getText()) || "".equals(tenantAccountNo.getText()) || "".equals(tenantDOB.getValue().toString()) 
                || "".equals(genderOption())) {
            alert.setContentText("Please Fill Out All Fields");
            alert.show();
        }

        //Tenant Informaation
        tenant.setFirstName(firstName.getText());
        tenant.setLastName(lastName.getText());

        tenant.setGender(genderOption());

        Regex regex = new Regex();

        tenant.setDOB(tenantDOB.getValue().toString());

        contact.setStreetLine1(streetLine1.getText());
        contact.setStreetLine2(streetLine2.getText());
        contact.setApartmentNumber(appartmentNo.getText());
        contact.setCity(city.getText());
        contact.setParish(parish.getText());
        contact.setPoBox(poBox.getText());
        contact.setCountry(country.getText());
        tenant.setAccountNum(tenantAccountNo.getText());

        tenant.setEmailAddress(email.getText());
        contact.setTelephoneNo(telephone.getText());
        if (regex.regexEmailValidator(tenant.getEmailAddress()) == false) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Email Format! (example@gmail.com)");
            alert.show();
        } else if (regex.regexTeleValidator(contact.getTelephoneNo()) == false) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Telephone Format! (123-456-7890)");
            alert.show();
        } else {
            //set tenants payment information
            tenant.setPayments(payment);
            tenant.setContactDetails(contact);
            tenant.setImageLocation(fp);
            register.setTenant(payment.calAmtDue(tenant));

            if (genp.getObject(DataFiles.TENANT) == null) {//check if file is empty
                tenantList = new ArrayList<>();//CREATE NEW INSTANCE    
            } else {
                tenantList = (ArrayList<Tenant>) genp.getObject(DataFiles.TENANT);//read file
            }
            tenantList.add(tenant);//add new entry to list 
            if (genp.saveobject(tenantList, DataFiles.TENANT)) {   //write new entry to file    
                register.saveHashMap();
                AnchorPane pane = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
                rootPane.getChildren().setAll(pane);
            }
        }

    }

    @FXML
    private void uploadTenantPhoto(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter exeFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.SVG");
        fileChooser.getExtensionFilters().add(exeFilter);
        File file = fileChooser.showOpenDialog(null);

        //get selcted image
        BufferedImage img = null;

        img = ImageIO.read(file.getAbsoluteFile());
        WritableImage i = SwingFXUtils.toFXImage(img, null);
        tenantPhoto.setImage(i);

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
            writeImage.createNewFile();
            ImageIO.write(img, extention, writeImage);
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
        return newFile + "images\\" + this.firstName.getText() + " " + this.lastName.getText() + "." + extension;
    }

    @FXML
    private void mainDashboardBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void signoutBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
