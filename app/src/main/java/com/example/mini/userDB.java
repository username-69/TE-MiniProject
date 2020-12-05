package com.example.mini;

import java.util.ArrayList;

public class userDB {
    private String userEmail;
    private ArrayList<childDB> userChildren;

    public userDB(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ArrayList<childDB> getUserChildren() {
        return userChildren;
    }

    public void setUserChildren(ArrayList<childDB> userChildren) {
        this.userChildren = userChildren;
    }
}
