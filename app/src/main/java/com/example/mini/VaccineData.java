package com.example.mini;

public class VaccineData {
    private String vaccineName;
    private short vaccineWeek;
    private boolean isVaccincated;

    public VaccineData(String vaccineName, short vaccineWeek) {
        this.vaccineName = vaccineName;
        this.vaccineWeek = vaccineWeek;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public short getVaccineWeek() {
        return vaccineWeek;
    }

    public void setVaccineWeek(short vaccineWeek) {
        this.vaccineWeek = vaccineWeek;
    }

    public boolean isVaccincated() {
        return isVaccincated;
    }

    public void setVaccincated(boolean vaccincated) {
        isVaccincated = vaccincated;
    }
}