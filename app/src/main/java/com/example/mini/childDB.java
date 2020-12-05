package com.example.mini;

import java.util.ArrayList;
import java.util.Date;

public class childDB {
    private int childID;
    private String childName, placeOfBirth;
    private Date birthDate;
    private short childAge;
    private char childGender;
    private ArrayList<VaccineData> childVaccines;

    public childDB(int childID, String childName, String placeOfBirth, Date birthDate, char childGender) {
        this.childID = childID;
        this.childName = childName;
        this.placeOfBirth = placeOfBirth;
        this.birthDate = birthDate;
        this.childGender = childGender;
    }

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public short getChildAge() {
        return childAge;
    }

    public void setChildAge(short childAge) {
        this.childAge = childAge;
    }

    public char getChildGender() {
        return childGender;
    }

    public void setChildGender(char childGender) {
        this.childGender = childGender;
    }

    public ArrayList<VaccineData> getChildVaccines() {
        return childVaccines;
    }

    public void setChildVaccines(ArrayList<VaccineData> childVaccines) {
        this.childVaccines = childVaccines;
    }
}
