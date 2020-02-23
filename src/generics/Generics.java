/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generics;


import entities.Register;
import entities.Tenant;
import interfaces.DataFiles;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javafx.scene.control.Alert;


/**
 *
 * @author Oshaine
 * @param <T>
 */
public class Generics<T> {

    /*This Generic method accepts a generic type parameter passed to generic class "T". 
    It can take any Object type and also a String file name to write the object type to.*/
    public boolean saveobject(T obj, String fileName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ObjectOutputStream out = null;
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(obj);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException ex) {
            alert.setHeaderText("New File");
            alert.setContentText("File has been created");
            alert.show();
        } catch (IOException ex) {
            alert.setHeaderText(" " + ex);
            alert.show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    alert.setHeaderText(" " + ex);
                    alert.show();
                }
            }
        }

        return false;

    }

        /*This Generic method accepts String file name to read the object type from.*/
    public T getObject(String fileName) {

        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {

            fin = new FileInputStream(fileName);
            ois = new ObjectInputStream(fin);
            T obj = (T) ois.readObject();
            fin.close();
            return obj;
        } catch (FileNotFoundException ex) {

        } catch (IOException | ClassNotFoundException ex) {

        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException ex) {
                }
            }
        }
        return null;
    }

    /*This method when invoked accpets a key and file name to valiate if the key
    matches a record in the file name been passed to it*/
    public boolean checkFileExistence(String key, String filename) {
        Object obj = null;
        obj = new Generics<>().getObject(filename);
        boolean found = false;
        if (obj != null) {
            ArrayList<Tenant> Tlist = (ArrayList<Tenant>) obj;
            ListIterator iterate = Tlist.listIterator();
            while (iterate.hasNext()) {
                Tenant temp = (Tenant) iterate.next();
                if (temp.getEmailAddress().equals(key)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    /*This Method when invoked accpets a tenant file and a email
    to verefy then if it is matched it removes that tenant information
    and updates the file with the new object contents passed to i*/
    public void updateFile(Tenant nTenant, String email) {
        ArrayList<Tenant> tenant;//array list
        tenant = new ArrayList();
        File file = null;
        boolean found = false;
        Generics source = new Generics();
        try {
            // create new file
            file = new File(DataFiles.TENANT);
            // tries to create new file in the system
            found = file.createNewFile();//test to see if the file is created

        } catch (Exception e) {
        }
        //bool returns false if the file is created and hence the file is not created again
        if (found == true) {
            //since the newfile is created from GenericClass this is used to
            //create the file if created
            source.saveobject(tenant, DataFiles.TENANT);
        }

        tenant = (ArrayList<Tenant>) source.getObject(DataFiles.TENANT);

        //search
        //iterate
        ListIterator itr = tenant.listIterator();
        while (itr.hasNext()) {
            Tenant t = (Tenant) itr.next();
            if (t.getEmailAddress().equalsIgnoreCase(email)) {

                itr.remove();

            }
        }

        //write new data to file
        nTenant.setFirstName(nTenant.getFirstName());
        nTenant.setLastName(nTenant.getLastName());
        nTenant.setGender(nTenant.getGender());
        nTenant.setAccountNum(nTenant.getAccountNum());
        nTenant.setDOB(nTenant.getDOB());
        nTenant.setImageLocation(nTenant.getImageLocation());

        nTenant.setEmailAddress(nTenant.getEmailAddress());
        nTenant.getContactDetails().setTelephoneNo(nTenant.getContactDetails().getTelephoneNo());
        nTenant.getContactDetails().setStreetLine1(nTenant.getContactDetails().getStreetLine1());
        nTenant.getContactDetails().setApartmentNumber(nTenant.getContactDetails().getApartmentNumber());
        nTenant.getContactDetails().setCity(nTenant.getContactDetails().getCity());
        nTenant.getContactDetails().setParish(nTenant.getContactDetails().getParish());

        nTenant.getPayments().setAmountDue(nTenant.getPayments().getAmountDue());
        nTenant.getPayments().setNextPayDate(nTenant.getPayments().getNextPayDate());
        nTenant.getPayments().setChange(nTenant.getPayments().getChange());
        nTenant.getPayments().setOutsAmtount(nTenant.getPayments().getOutsAmtount());
        nTenant.getPayments().setTotalDaysLate(nTenant.getPayments().getTotalDaysLate());

        tenant.add(nTenant);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (source.saveobject(tenant, DataFiles.TENANT)) {

            alert.setHeaderText("Payment Details Updated");
            alert.setContentText("Tenant Payment Details Have Been Updated");
            alert.show();
        } else {
            alert.setHeaderText("Payment Details Not Updated");
            alert.setContentText("Tenant Payment Details Did Not Updated");
            alert.show();
        }

    }

    
    /*This Method when invoked accpets a username and a tenant
    to verefy then if it is matched it removes that tenant information
    and updates the file with the new object contents passed to i*/
    public void updateHashMap(String username, Tenant nTenant) {
        HashMap<String, List<Tenant>> dict = new HashMap<>();
        ArrayList<Tenant> tList = new ArrayList<>();
        File file = null;
        boolean found = false;
        Generics source = new Generics();
        try {
            // create new file
            file = new File(DataFiles.LANDLORDTENANT);
            //create new file in the system
            found = file.createNewFile();//test to see if the file is created

        } catch (Exception e) {
        }
        //returns false if the file is created and hence the file is not created again
        if (found == true) {
            source.saveobject(dict, DataFiles.LANDLORDTENANT);
        }

        dict = (HashMap<String, List<Tenant>>) source.getObject(DataFiles.LANDLORDTENANT);
        tList = (ArrayList<Tenant>) source.getObject(DataFiles.TENANT);
        //search
        //iterate
        if (new Generics().getObject(DataFiles.LANDLORDTENANT) != null) {
            dict = (HashMap<String, List<Tenant>>) new Generics().getObject(DataFiles.LANDLORDTENANT);

            for (String key : dict.keySet()) {

                if (Register.getUsername().equals(key)) {//compair the key with the username 

                    List<Tenant> hashKey = (List<Tenant>) dict.get(key);//gets the key from the file
                    for (Tenant ten : hashKey) {
                        dict.remove(Register.getUsername(), ten);
                        dict.put(Register.getUsername(), tList);

                    }
                }
            }
        }

        if (source.saveobject(dict, DataFiles.LANDLORDTENANT)) {

            System.out.println("Saved");
        } else {
            System.out.println("NOT Saved");
        }

    }

}
