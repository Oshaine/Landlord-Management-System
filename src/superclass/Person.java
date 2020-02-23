/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superclass;

import interfaces.PersonInterface;
import java.io.Serializable;

/**
 *
 * @author Oshaine
 */
public class Person implements PersonInterface, Serializable {

    private String firstName;
    private String lastName;
    private String gender;
    private String accountNum;
    private String imageLocation;
    private String DOB;
    private String emailAddress;
  
    private static final long serialVersionUID = 3458489886428771425L;

    /**
     * @return the firstName
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    @Override
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the accountNum
     */
    @Override
    public String getAccountNum() {
        return accountNum;
    }

    /**
     * @param accountNum the accountNum to set
     */
    @Override
    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    /**
     * @return the imageLocation
     */
    @Override
    public String getImageLocation() {
        return imageLocation;
    }

    /**
     * @param imageLocation the imageLocation to set
     */
    @Override
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    /**
     * @return the DOB
     */
    @Override
    public String getDOB() {
        return DOB;
    }

    /**
     * @param DOB the DOB to set
     */
    @Override
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    /**
     * @return the emailAddress
     */
    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }



}
