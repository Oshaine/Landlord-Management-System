/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
import java.io.Serializable;
import java.util.Scanner;
import superclass.Person;

/**
 *
 * @author Oshaine
 */
public class Tenant extends Person implements Serializable {

    private Payments payments;
    private ContactDetails contactDetails;
    transient Scanner input = new Scanner(System.in).useDelimiter("\n");


    /**
     * @return the payments
     */
    public Payments getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(Payments payments) {
        this.payments = payments;
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

  

}
