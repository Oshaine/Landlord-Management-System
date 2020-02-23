/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import interfaces.ContactDetailsInterface;
import java.io.Serializable;

/**
 *
 * @author Oshaine
 */
public class ContactDetails implements ContactDetailsInterface, Serializable{

    private String telephoneNo;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String parish;
    private String country;
    private String apartmentNumber;
    private String poBox;

    /**
     * @return the streetLine1
     */
    @Override
    public String getStreetLine1() {
        return streetLine1;
    }

    /**
     * @param streetLine1 the streetLine1 to set
     */
    @Override
    public void setStreetLine1(String streetLine1) {
        this.streetLine1 = streetLine1;
    }

    /**
     * @return the streetLine2
     */
    @Override
    public String getStreetLine2() {
        return streetLine2;
    }

    /**
     * @param streetLine2 the streetLine2 to set
     */
    @Override
    public void setStreetLine2(String streetLine2) {
        this.streetLine2 = streetLine2;
    }

    /**
     * @return the city
     */
    @Override
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    @Override
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the parish
     */
    @Override
    public String getParish() {
        return parish;
    }

    /**
     * @param parish the parish to set
     */
    @Override
    public void setParish(String parish) {
        this.parish = parish;
    }

    /**
     * @return the country
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the apartmentNumber
     */
    @Override
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    /**
     * @param apartmentNumber the apartmentNumber to set
     */
    @Override
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    /**
     * @return the poBox
     */
    @Override
    public String getPoBox() {
        return poBox;
    }

    /**
     * @param poBox the poBox to set
     */
    @Override
    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }



    /**
     * @return the telephoneNo
     */
    @Override
    public String getTelephoneNo() {
        return telephoneNo;
    }

    /**
     * @param telephoneNo the telephoneNo to set
     */
    @Override
    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

}
