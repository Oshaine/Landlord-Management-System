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
public interface PersonInterface {
    
     String getFirstName();
    String getLastName();
    String getGender();
   String getAccountNum();
   String getImageLocation();
   String getDOB();
     String getEmailAddress();
 
    
    
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setGender(String gender);
    void setAccountNum(String accountNum);
    void setImageLocation(String imageLocation);
    void setDOB(String DOB);
     void setEmailAddress(String emailAddress);
    
}
