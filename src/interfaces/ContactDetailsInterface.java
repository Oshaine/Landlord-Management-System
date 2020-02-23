/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Oshaine
 */
public interface ContactDetailsInterface {

  

    String getTelephoneNo();

    String getStreetLine1();

    String getStreetLine2();

    String getCity();

    String getParish();

    String getCountry();

    String getApartmentNumber();

    String getPoBox();

   

    void setTelephoneNo(String telephoneNo);

    void setStreetLine1(String streetLine1);

    void setStreetLine2(String streetLine2);

    void setCity(String city);

    void setParish(String parish);

    void setCountry(String country);

    void setApartmentNumber(String apartmentNumber);

    void setPoBox(String poBox);

}
