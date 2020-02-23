/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Oshaine
 */
public class Regex {

    public boolean regexEmailValidator(String email) {

        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";//pattern
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);//compile emailRegex into pattern
        Matcher matcher = emailPattern.matcher(email);//match user input
        if (matcher.find()) {//find the next subsequence of the input sequence that matches the pattern.
            System.out.println("");
            return true;
        } else {
            return false;
        }

    }

    public boolean regexTeleValidator(String telephoneNo) {

        String telNoRegex = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";//pattern
        Pattern telPattern = Pattern.compile(telNoRegex, Pattern.CASE_INSENSITIVE);//compile telRegex into pattern
        Matcher matcher = telPattern.matcher(telephoneNo);//match user input
        if (matcher.find()) { //find the next subsequence of the input sequence that matches the pattern.
            System.out.println("");
            return true;
        } else {
            return false;
        }
    }
}
