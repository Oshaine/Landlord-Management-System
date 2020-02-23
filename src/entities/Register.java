/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import generics.Generics;
import interfaces.PersonInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import superclass.Person;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import interfaces.DataFiles;

/**
 *
 * @author Oshaine
 */
public class Register extends Person implements PersonInterface, Serializable{

    private Tenant tenant;
    private LandLord landLord;
    private ContactDetails contactDetails;
    private static String username;
    private static String password;
    Scanner input = new Scanner(System.in).useDelimiter("\n");



    
    /*This Method when invocked reads from the landlord file and tenant file
    then checks if the current username login in with matches a username in the 
    landlord file and if it maches then it uses that username as the key to the
    HashMap<k,V> and uses the tenant being registred at that moment as the value
    to link them*/

    public void saveHashMap() {
        Generics genp = new Generics();
        Map<String, List<Tenant>> personList = new HashMap<String, List<Tenant>>();
        ArrayList<LandLord> landList = (ArrayList<LandLord>) genp.getObject(DataFiles.LANDLORD);//reads file

        ArrayList<Tenant> tenantList = (ArrayList<Tenant>) genp.getObject(DataFiles.TENANT);
       
        if (genp.getObject(DataFiles.LANDLORDTENANT) == null) {//check if file is empty
            personList = new HashMap<String, List<Tenant>>();//CREATE NEW INSTANCE 
        } else {

            personList = (Map<String, List<Tenant>>) genp.getObject(DataFiles.LANDLORDTENANT);//read file

        }
        
        for (Iterator<LandLord> it = landList.iterator(); it.hasNext();) {
            LandLord l = it.next();
            if(getUsername().equals(l.getUsername())){//check if the username matches the username in the file
                personList.put(l.getUsername(), tenantList);
            }
        }

        if (genp.saveobject(personList, DataFiles.LANDLORDTENANT)) {//write new entry to file
            System.out.println("Landlord Tenant Registered");
        }

    }

    /**
     * @return the tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * @param tenant the tenant to set
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * @return the contactDetails
     */
    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    /**
     * @param contactDetails the contactDetails to set
     */
    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    /**
     * @return the LandLord
     */
    public LandLord getLandLord() {
        return landLord;
    }

    /**
     * @param landLord the LandLord to set
     */
    public void setLandLord(LandLord landLord) {
        this.landLord = landLord;
    }

    /**
     * @return the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * @param aUsername the username to set
     */
    public static void setUsername(String aUsername) {
        username = aUsername;
    }

    /**
     * @return the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * @param aPassword the password to set
     */
    public static void setPassword(String aPassword) {
        password = aPassword;
    }

}
