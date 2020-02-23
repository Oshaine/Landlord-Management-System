/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rent.system;

import entities.LandLord;
import entities.Register;
import static entities.Register.getUsername;
import entities.Tenant;
import generics.Generics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import interfaces.DataFiles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Oshaine
 */
public class MainDashboardController implements Initializable {

    @FXML
    private TableView<Tenant> tenantTable;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView tenantPhoto;
    @FXML
    private ImageView landlordPhoto;
    @FXML
    private Label landlordInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<Tenant, String> fName = new TableColumn<>("First Name");
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Tenant, String> lName = new TableColumn<>("Last Name");
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Tenant, String> gen = new TableColumn<>("Gender");
        gen.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Tenant, String> DOB = new TableColumn<>("Date of Birth");
        DOB.setCellValueFactory(new PropertyValueFactory<>("DOB"));

        TableColumn<Tenant, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        TableColumn<Tenant, String> accountNo = new TableColumn<>("Account Number");
        accountNo.setCellValueFactory(new PropertyValueFactory<>("accountNum"));

        tenantTable.setItems(getTenant());
        tenantTable.getColumns().addAll(fName, lName, gen, DOB, email, accountNo);

        landlordInfo(landlordInfo);
        //loadLandPhoto();
    }

    @FXML
    private void addTenantBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RegisterTenant.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void landlordInfo(Label l) {
        ArrayList<LandLord> landlordList = new ArrayList<>();
        if (new Generics().getObject(DataFiles.LANDLORD) != null) {
            landlordList = (ArrayList<LandLord>) new Generics().getObject(DataFiles.LANDLORD);
            ListIterator itr = landlordList.listIterator();
            while (itr.hasNext()) {
                LandLord landlord = (LandLord) itr.next();
                if (getUsername().equals(landlord.getUsername())) {
                    l.setText(landlord.getFirstName() + " " + landlord.getLastName());
                }
            }
        }

    }

    public ObservableList<Tenant> getTenant() {
        ObservableList<Tenant> landTenant = FXCollections.observableArrayList();
        Map<String, List<Tenant>> tenantList = null;
        if (new Generics().getObject(DataFiles.LANDLORDTENANT) != null) {
            tenantList = (Map<String, List<Tenant>>) new Generics().getObject(DataFiles.LANDLORDTENANT);
            Register r = new Register();
            for (String key : tenantList.keySet()) {

                if (getUsername().equals(key)) {//compair the key with the username 
                    List<Tenant> hashKey = tenantList.get(key);//gets the key from the file
                    for (Tenant t : hashKey) {
                        landTenant.add(t);

                    }
                }
            }
        }

        return landTenant;
    }

    @FXML
    private void signoutBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void makePaymentBtn(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MakePayment.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Tenant Found");
            alert.setContentText("Please Register a Tenant To Make Payment");
            alert.show();
        }
    }

    @FXML
    private void loadTenantPhoto(MouseEvent event) {

        tenantPhoto.imageProperty().set(null);
        Tenant tenant = this.tenantTable.getSelectionModel().getSelectedItem();
        if (tenant != null) {
            File file = new File(tenant.getImageLocation());
            BufferedImage image = null;
            try {
                image = ImageIO.read(file.getAbsoluteFile());
                WritableImage i = SwingFXUtils.toFXImage(image, null);
                tenantPhoto.imageProperty().set(i);
            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("No Image");
                a.setContentText("Tenant Did Not Upload Any Image");
                a.show();
            }

        }
    }

    public void loadLandPhoto() {
        landlordPhoto.imageProperty().set(null);
        LandLord land = new LandLord();
        Generics genp = new Generics();
        land.getImageLocation();
        if (land != null) {
            File file = new File(land.getImageLocation());
            BufferedImage image = null;
            try {
                image = ImageIO.read(file.getAbsoluteFile());
                WritableImage i = SwingFXUtils.toFXImage(image, null);
                landlordPhoto.imageProperty().set(i);
            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("No Image");
                a.setContentText("Tenant Did Not Upload Any Image");
                a.show();
            }

        }

    }

    @FXML
    private void sortTenant(ActionEvent event) {
        Collections.sort(getTenant(), new Comparator<Tenant>() {
            @Override
            public int compare(Tenant t1, Tenant t2) {
                if (t1.getLastName() == null || t2.getLastName() == null) {
                    return 0;
                }
                
                return t1.getLastName().compareTo(t2.getLastName());
                
            }

        });
    }

}
