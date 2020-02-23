/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rent.system;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import entities.LandLord;
import entities.Payments;
import entities.Register;
import static entities.Register.getUsername;
import entities.Tenant;
import generics.Generics;
import interfaces.ChargesInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import interfaces.DataFiles;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * FXML Controller class
 *
 * @author Oshaine
 */
public class MakePaymentController implements Initializable {

    @FXML
    private JFXComboBox<String> combo;

    @FXML
    private JFXTextField cashAmount;
    @FXML
    private JFXTextField accNumber;
    @FXML
    private Label tenantEmail;
    @FXML
    private JFXListView<String> listView;
    @FXML
    private Label landlordInfo;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        combo.setItems(getTenant());//populate the combo list
        MainDashboardController main = new MainDashboardController();
        main.landlordInfo(landlordInfo);
    }

    @FXML
    private void signoutBtn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));//calls the login form
        rootPane.getChildren().setAll(pane);
    }

    /*This method when invoked usese observable list that allows listeners
    to track changes when they occur. This method is used to check the landlord 
    username and if it is the one been active it populates the table*/
    
    public ObservableList<String> getTenant() {
        ObservableList<String> tList = FXCollections.observableArrayList();
        Generics genp = new Generics();
        Map<String, List<Tenant>> tenantList = (Map<String, List<Tenant>>) genp.getObject(DataFiles.LANDLORDTENANT);
        for (String key : tenantList.keySet()) {

            if (getUsername().equals(key)) {//compair the key with the username 
                List<Tenant> hashKey = tenantList.get(key);//gets the key from the file
                for (Tenant t : hashKey) {
                    tList.add(t.getFirstName() + " " + t.getLastName());
                }
            }
        }

        return tList;
    }

    @FXML
    /*This action even makes all tenant payment*/
    private void makePayment(ActionEvent event) throws IOException {

        ObservableList<String> tList = FXCollections.observableArrayList();

        ArrayList<LandLord> landList = new ArrayList<>();
        landList = (ArrayList<LandLord>) new Generics().getObject(DataFiles.LANDLORD);//reads landlord file

        ListIterator it = landList.listIterator();
        while (it.hasNext()) {
            LandLord landlord = (LandLord) it.next();
            if (Register.getUsername().equals(landlord.getUsername())) {//check usermname
                tList.add("LandLord Name: " + landlord.getFirstName() + " " + landlord.getLastName());
                listView.setItems(tList);//add items to the listViw
            }
        }
        ArrayList<Tenant> tenantList = null;
        tenantList = (ArrayList<Tenant>) new Generics().getObject(DataFiles.TENANT);//read tenant file
        ListIterator itr = tenantList.listIterator();
        if ("".equals(combo.getValue()) || "".equals(cashAmount.getText())) {//check if feilds are empty

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Cash/Tenant Entered");
            alert.setContentText("Please select a Tenant and Enter The Cash Amount");
            alert.show();

        } else {
            while (itr.hasNext()) {
                Payments p = new Payments();
                Tenant t = new Tenant();
                Tenant tenant = (Tenant) itr.next();
                if (combo.getValue().equals(tenant.getFirstName() + " " + tenant.getLastName())) {
                    /*check if the selected items are in the tenant file*/
                    double d = Double.parseDouble(cashAmount.getText());
                    p.setCash(d);
                    t.setPayments(p);
                    makePayments(tenant, t.getPayments().getCash());

                    tList.add("Tenant Name: " + tenant.getFirstName() + " " + tenant.getLastName());
                    tList.add("Cash: " + p.moneyFormat(p.getCash()));
                    tList.add("Change: " + p.moneyFormat(tenant.getPayments().getChange()));
                    tList.add("Discount: " + p.moneyFormat(tenant.getPayments().getDiscAmount()));

                    tList.add("Outstanding Amount Due: " + p.moneyFormat(tenant.getPayments().getOutsAmtount()));
                    tList.add("Last Payment Date: " + tenant.getPayments().getLastPayDate());
                    tList.add("Next Payment Date: " + tenant.getPayments().getNextPayDate());
                    tList.add("Next Payment Amount Due: " + p.moneyFormat(tenant.getPayments().getAmountDue()));
                    listView.setItems(tList);

                }

            }
        }

    }

    /*This method calculate the tenant chrages based on the tenant that is passed to it 
    and the cash amount*/
    public boolean makePayments(Tenant tNew, double pay) {
        //search first
        Generics gen = new Generics();

        GregorianCalendar gc = new GregorianCalendar();

        GregorianCalendar now = new GregorianCalendar();

        if (tNew != null) {
            if (gen.checkFileExistence(tNew.getEmailAddress(), DataFiles.TENANT)) {
                //user found, make pyements
                //get current amount due
                double amtDue = tNew.getPayments().getAmountDue();
                //get outstanding amount
                double ouStandAmt = tNew.getPayments().getOutsAmtount();

                double outTemp = 0;

                double changeTemp = 0;

                Tenant nTenant = null;

                Payments p = new Payments();

                gc.setTime(tNew.getPayments().getNextPayDate());

                boolean onTime = false;
                boolean before = false;

                //if the user pays on time
                if (gc.get(GregorianCalendar.DAY_OF_WEEK) == now.get(GregorianCalendar.DAY_OF_WEEK)
                        && gc.get(GregorianCalendar.MONTH) == now.get(GregorianCalendar.MONTH)) {
                    if (gc.get(GregorianCalendar.YEAR) == now.get(GregorianCalendar.YEAR)) {
                        onTime = true;
                    }
                }

                //pay before due date
                if (now.getTime().before(gc.getTime())) {
                    before = true;
                }

                if (onTime) {
                    System.out.println("on time");
                    if (pay >= amtDue) {
                        changeTemp = pay - amtDue;
                        tNew.getPayments().setChange(changeTemp);
                        tNew.getPayments().setOutsAmtount(0);
                    } else if (pay < amtDue) {
                        outTemp = amtDue - pay;
                        tNew.getPayments().setOutsAmtount(outTemp);
                        tNew.getPayments().setChange(0);
                    }
                    tNew.getPayments().setTotalDaysLate(0);
                    tNew.getPayments().setLateFee(0);
                    tNew.getPayments().setLastPayDate(tNew.getPayments().getNextPayDate());
                    nTenant = p.calAmtDue(tNew);
                    nTenant.getPayments().setTotalDaysLate(0);
                    nTenant.getPayments().setLateFee(0);
                    //update
                    gen.updateFile(nTenant, nTenant.getEmailAddress());
                    gen.updateHashMap(Register.getUsername(), nTenant);
                } else if (before) {
                    if (pay >= amtDue) {
                        changeTemp = pay - amtDue;
                        tNew.getPayments().setChange(changeTemp);
                        tNew.getPayments().setOutsAmtount(0);
                        tNew.getPayments().setAmountDue(0);
                    } else if (pay < amtDue) {
                        outTemp = amtDue - pay;
                        tNew.getPayments().setOutsAmtount(outTemp);
                        tNew.getPayments().setChange(0);
                        tNew.getPayments().setAmountDue(ouStandAmt + outTemp);
                    }
                    tNew = p.calAmtDue(tNew);
                    tNew.getPayments().setLastPayDate(now.getTime());
                    gc.add(Calendar.DAY_OF_WEEK, ChargesInterface.BILLING_CYCLE);
                    tNew.getPayments().setNextPayDate(gc.getTime());
                    tNew.getPayments().setTotalDaysLate(0);
                    tNew.getPayments().setLateFee(0);
                    //update
                    gen.updateFile(tNew, tNew.getEmailAddress());
                    gen.updateHashMap(Register.getUsername(), tNew);

                } else if (!before && !onTime) {
                    System.out.println("Late payment");
                    //late payment
                    if (pay >= amtDue) {
                        System.out.println("greater than");
                        changeTemp = pay - amtDue;
                        tNew.getPayments().setChange(changeTemp);
                        tNew.getPayments().setOutsAmtount(0);
                        tNew.getPayments().setAmountDue(0);
                    } else if (pay < amtDue) {
                        System.out.println("less than");
                        outTemp = amtDue - pay;
                        tNew.getPayments().setOutsAmtount(outTemp);
                        tNew.getPayments().setChange(0);
                        tNew.getPayments().setAmountDue(ouStandAmt + outTemp);
                    }

                    tNew.getPayments().setLastPayDate(now.getTime());
                }

            }

        }
        return false;
    }

    @FXML
    private void tenantSelect(ActionEvent event) {
        ObservableList<String> tList = FXCollections.observableArrayList();

        ArrayList<LandLord> landList = new ArrayList<>();
        landList = (ArrayList<LandLord>) new Generics().getObject(DataFiles.LANDLORD);
        ListIterator it = landList.listIterator();
        while (it.hasNext()) {
            LandLord landlord = (LandLord) it.next();
            if (Register.getUsername().equals(landlord.getUsername())) {
                tList.add("LandLord Name: " + landlord.getFirstName() + " " + landlord.getLastName());
                listView.setItems(tList);
            }
        }
        ArrayList<Tenant> tenantList = null;
        tenantList = (ArrayList<Tenant>) new Generics().getObject(DataFiles.TENANT);
        ListIterator itr = tenantList.listIterator();
        Payments p = new Payments();
        while (itr.hasNext()) {
            Tenant tenant = (Tenant) itr.next();
            if (combo.getValue().equals(tenant.getFirstName() + " " + tenant.getLastName())) {

                tList.add("Tenant Name: " + tenant.getFirstName() + " " + tenant.getLastName());
                if (tenant.getPayments().getLastPayDate() != null) {
                    tList.add("Last Payment Date: " + tenant.getPayments().getLastPayDate());

                }

                tList.add("Amount Due: " + p.moneyFormat(tenant.getPayments().getAmountDue()));
                tList.add("Discount: " + p.moneyFormat(tenant.getPayments().getDiscAmount()));
                tenantEmail.setText(tenant.getEmailAddress());
                accNumber.setText(tenant.getAccountNum());
                listView.setItems(tList);

            }

        }
    }

    @FXML
    private void mainDashboardBtn(ActionEvent event) {

    }

    @FXML
    /*This Action event sends the receipt via email to then tenant email*/
    private void emailReciept(ActionEvent event) throws AddressException, MessagingException, IOException {

        ObservableList<String> tList = FXCollections.observableArrayList();
        ArrayList<Tenant> tenantList = null;
        tenantList = (ArrayList<Tenant>) new Generics().getObject(DataFiles.TENANT);
        ListIterator itr = tenantList.listIterator();
        Payments p = new Payments();
        if ("".equals(combo.getValue()) || "".equals(cashAmount.getText())) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Cash/Tenant Entered");
            alert.setContentText("Please select a Tenant and Enter The Cash Amount");
            alert.show();
        } else {
            while (itr.hasNext()) {
                Tenant tenant = (Tenant) itr.next();
                if (combo.getValue().equals(tenant.getFirstName() + " " + tenant.getLastName())) {

                    ArrayList<LandLord> landList = new ArrayList<>();
                    landList = (ArrayList<LandLord>) new Generics().getObject(DataFiles.LANDLORD);
                    ListIterator it = landList.listIterator();
                    while (it.hasNext()) {
                        LandLord landlord = (LandLord) it.next();
                        if (Register.getUsername().equals(landlord.getUsername())) {
                            tList.add("LandLord Name: " + landlord.getFirstName() + " " + landlord.getLastName());
                            listView.setItems(tList);
                        }
                    }
                    tList.add("Tenant Name: " + tenant.getFirstName() + " " + tenant.getLastName());
                    tList.add("Cash: " + p.moneyFormat(p.getCash()));
                    tList.add("Change: " + p.moneyFormat(tenant.getPayments().getChange()));
                    tList.add("Discount: " + p.moneyFormat(tenant.getPayments().getDiscAmount()));
                    tList.add("Outstanding Amount Due: " + p.moneyFormat(tenant.getPayments().getOutsAmtount()));
                    if (tenant.getPayments().getLastPayDate() != null) {
                        tList.add("Last Payment Date: " + tenant.getPayments().getLastPayDate());

                    }
                    tList.add("Next Payment Date: " + tenant.getPayments().getNextPayDate());
                    tList.add("Next Payment Amount Due: " + p.moneyFormat(tenant.getPayments().getAmountDue()));
                    listView.setItems(tList);
                    //create receipt and file name
                    receipt(Register.getUsername(), tenant.getFirstName() + " " + tenant.getLastName() + " " + "Rent Receipt.txt");

                    String host = "smtp.gmail.com";
                    String user = "oshaine429smith@gmail.com";
                    String pass = "oshaine429";
                    String to = tenant.getEmailAddress();

                    String from = "oshaine429smith@gmail.com";
                    String subject = "This is confirmation reciept for your rent payment.";
                    String messageText = "Your payment have been receieved and below is a copy of your receipt:";
                    boolean sessionDebug = false;

                    Properties props = System.getProperties();

                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.required", "true");

                    java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                    Session mailSession = Session.getDefaultInstance(props, null);
                    mailSession.setDebug(sessionDebug);
                    Message msg = new MimeMessage(mailSession);
                    try {
                        msg.setFrom(new InternetAddress(from));
                    } catch (AddressException ex) {
                        Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MessagingException ex) {
                        Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(subject);
                    msg.setSentDate(new Date());
                    msg.setText(messageText);
                    
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    //reads the receipt file name
                    FileDataSource fileDataSource = new FileDataSource(tenant.getFirstName() + " " + tenant.getLastName() + " " + "Rent Receipt.txt") {
                        @Override

                        public String getContentType() {

                            return "application/octet-stream";

                        }

                    };

                    try {
                        attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                    } catch (MessagingException ex) {
                    }

                    try {
                        attachmentPart.setFileName(fileDataSource.getName());
                    } catch (MessagingException ex) {
                    }

                    // Add all parts of the email to Multipart object
                    Multipart multipart = new MimeMultipart();

                    multipart.addBodyPart(attachmentPart);
                    try {
                        multipart.addBodyPart(attachmentPart);
                    } catch (MessagingException ex) {
                    }
                    msg.setContent(multipart);

                    try (Transport transport = mailSession.getTransport("smtp")) {
                        transport.connect(host, user, pass);
                        transport.sendMessage(msg, msg.getAllRecipients());
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Email Sent");
                    alert.setContentText("Reciept has been sent to " + tenant.getFirstName() + " " + tenant.getLastName());
                    alert.show();

                    
                }

            }

        }

    }

    /*This Method accepts a userName and the filename to create a receipt when invoked*/
    public void receipt(String username, String filename) {

        ObservableList<String> tList = FXCollections.observableArrayList();
        ArrayList<LandLord> landList = new ArrayList<>();
        landList = (ArrayList<LandLord>) new Generics().getObject(DataFiles.LANDLORD);
        ListIterator it = landList.listIterator();
        while (it.hasNext()) {
            FileWriter fileWriter = null;

            File f = new File(filename);
            if (!f.exists()) {

                try {
                    f.createNewFile();
                } catch (IOException ex) {
                }

            }
            try {
                fileWriter = new FileWriter(filename);
            } catch (IOException ex) {
            }
            LandLord landlord = (LandLord) it.next();
            if (Register.getUsername().equals(landlord.getUsername())) {
                try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                 
                    printWriter.append("\tRECEIPT INFORMATION" + "\n");
                    printWriter.append("*********************************************************" + "\n\n");

                    printWriter.append("LandLord Name: " + landlord.getFirstName() + " " + landlord.getLastName() + "\n\n");
                    ArrayList<Tenant> tenantList = null;
                    tenantList = (ArrayList<Tenant>) new Generics().getObject(DataFiles.TENANT);
                    ListIterator itr = tenantList.listIterator();
                    while (itr.hasNext()) {
                        Payments p = new Payments();
                        Tenant t = new Tenant();
                        Tenant tenant = (Tenant) itr.next();
                        if (combo.getValue().equals(tenant.getFirstName() + " " + tenant.getLastName())) {
                            double d = Double.parseDouble(cashAmount.getText());
                            p.setCash(d);
                            t.setPayments(p);
                            makePayments(tenant, t.getPayments().getCash());

                            printWriter.append("Tenant Name: " + tenant.getFirstName() + " " + tenant.getLastName() + "\n\n");
                            printWriter.append("Cash: " + p.moneyFormat(p.getCash()) + "\n\n");
                            printWriter.append("Change: " + p.moneyFormat(tenant.getPayments().getChange()) + "\n\n");
                            printWriter.append("Discount: " + p.moneyFormat(tenant.getPayments().getDiscAmount()) + "\n\n");

                            printWriter.append("Outstanding Amount Due: " + p.moneyFormat(tenant.getPayments().getOutsAmtount()) + "\n");
                            printWriter.append("___________________________________________\n\n");
                            printWriter.append("Last Payment Date: " + tenant.getPayments().getLastPayDate() + "\n\n");
                            printWriter.append("Next Payment Date: " + tenant.getPayments().getNextPayDate() + "\n\n");
                            printWriter.append("Next Payment Amount Due: " + p.moneyFormat(tenant.getPayments().getAmountDue()) + "\n\n");
                            printWriter.close();

                        }

                    }
                }
            }
        }
    }
}
