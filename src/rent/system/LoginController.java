/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rent.system;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entities.LandLord;
import entities.Register;
import generics.Generics;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import interfaces.DataFiles;

/**
 *
 * @author Oshaine
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField loginUsername;
    @FXML
    private JFXPasswordField loginPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void registerButton(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("RegisterLandLord.fxml"));//calls the register landlord form
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void signInButton(ActionEvent event) throws IOException {

        String dialog = null;
        boolean success = false;
        Alert alert = new Alert(Alert.AlertType.ERROR);

        LandLord land = new LandLord();
        
        //check if feilds are empty
        if ("".equals(loginUsername.getText()) || "".equals(loginPassword.getText())) {
            dialog = "Please Fill Out All Fields";
            alert.setHeaderText("Empty Fields");
            alert.setContentText(dialog);
            alert.show();
        } else {

            Register.setUsername(loginUsername.getText());
            Register.setPassword(loginPassword.getText());
            land = loginConfirmation(Register.getUsername(), Register.getPassword());

            if (land != null) {
                success = true;
                AnchorPane pane = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));//calls the main dashboard form
                rootPane.getChildren().setAll(pane);

            } else {
            dialog = ("User Not Found");
          
            alert.setHeaderText("Incorrect Username or Password");
            alert.setContentText(dialog);
            alert.show();
            }
        }
      

    }

    
    /*This Mrehtod accepts the labdlord username and password as a string
    then reads for the lanlord file that each landlord is stored in when registered
    to verify the username and password they are entereing*/
    public LandLord loginConfirmation(String userN, String pass) {

        Generics genp = new Generics();
        try{
        ArrayList<LandLord> personList = (ArrayList<LandLord>) genp.getObject(DataFiles.LANDLORD);//reads file
        ListIterator<LandLord> itr = personList.listIterator();
        boolean success = false;
        LandLord temp = null;
        while (itr.hasNext()) {
            LandLord land = itr.next();
            temp = null;
            Register r = new Register();
            if (userN.equals(land.getUsername()) && pass.equals(land.getPassword())) {
                if ("male".equals(land.getGender())) {
                    System.out.println("Login sucessfully\n Welcome MR. " + land.getLastName());
                    r.setLandLord(land);
                } else if ("female".equals(land.getGender())) {
                    System.out.println("Login sucessfully\n Welcome Miss. " + land.getLastName());

                } else {
                    System.out.println("Login sucessfully\nWelcome  " + land.getLastName());

                }
                temp = land;
                success = true;
                return temp;
            }
        }
        }catch(Exception e){
            
        }
        return null;
    }

}
