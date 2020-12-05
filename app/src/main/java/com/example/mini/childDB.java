package com.example.mini;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class childDB {
    private int childID;
    private String childName, placeOfBirth;
    //    private LocalDate birthDate;
    private int childAge, childGender;
//    private ArrayList<VaccineData> childVaccines;

    public childDB() {
    }

    public childDB(int childID, String childName, String placeOfBirth, int childGender) {
        this.childID = childID;
        this.childName = childName;
        this.placeOfBirth = placeOfBirth;
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

//    public LocalDate getBirthDate() {
//        return birthDate;
//    }
//
//    public void setBirthDate(LocalDate birthDate) {
//        this.birthDate = birthDate;
//    }

    public int getChildAge() {
        return childAge;
    }

    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

//    public ArrayList<VaccineData> getChildVaccines() {
//        return childVaccines;
//    }
//
//    public void setChildVaccines(ArrayList<VaccineData> childVaccines) {
//        this.childVaccines = childVaccines;
//    }
}
