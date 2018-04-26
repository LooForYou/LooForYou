package com.looforyou.looforyou.Models;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

/**
 * Created by ibreaker on 4/19/2018.
 */

public class Reviewer {
    private String firstName;
    private String lastName;
    private String userName;
    private String imageUrl;
    private String email;
    private String id;

    public Reviewer(String firstName, String lastName, String userName, String imageUrl, String email, String id) {
        this.firstName = WordUtils.capitalize(firstName);
        this.lastName = WordUtils.capitalize(lastName);
        if(userName.equals("") || userName == null){
            this.userName = firstName + " " + lastName;
        }else {
            this.userName = WordUtils.capitalize(userName);
        }
        if(imageUrl.equals("") || imageUrl == null){
            this.imageUrl = "http://pronksiapartments.ee/wp-content/uploads/2015/10/placeholder-face-big.png";
        }else {
            this.imageUrl = imageUrl;
        }
        this.email = email;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = WordUtils.capitalize(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = WordUtils.capitalize(lastName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = WordUtils.capitalize(userName);
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return userName;
    }

}
