/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import generics.Generics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;
import interfaces.DataFiles;

/**
 *
 * @author Oshaine
 */
public class Login implements Serializable{

    private LandLord landLord;

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

}
