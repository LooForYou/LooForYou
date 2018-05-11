package com.looforyou.looforyou.Models;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This is a custom data structure used to store reviewer information from server
 *
 * @author mingtau li
 */

public class Reviewer {
    /* relevant variables for reviewer properties */
    private String firstName;
    private String lastName;
    private String userName;
    private String imageUrl;
    private String email;
    private String id;

    /**
     * public constructor for reviewer
     *
     * @param firstName reviewer first name
     * @param lastName  reviewer last name
     * @param userName  reviewer username
     * @param imageUrl  reviewer profile image url
     * @param email     reviewer email
     * @param id        reviewer id
     */
    public Reviewer(String firstName, String lastName, String userName, String imageUrl, String email, String id) {
        /* initialize user's name and forces it to capitalize */
        this.firstName = WordUtils.capitalize(firstName);
        this.lastName = WordUtils.capitalize(lastName);

        /* set username to first and lastname of user if no username is available */
        if (userName.equals("") || userName == null) {
            this.userName = firstName + " " + lastName;
        } else {
            this.userName = WordUtils.capitalize(userName);
        }

        /* set custom placeholder image if no profile picture found */
        if (imageUrl.equals("") || imageUrl == null) {
            this.imageUrl = "http://pronksiapartments.ee/wp-content/uploads/2015/10/placeholder-face-big.png";
        } else {
            this.imageUrl = imageUrl;
        }

        /* initialize user email and id*/
        this.email = email;
        this.id = id;
    }

    /**
     * getter for reviewer first name
     *
     * @return String reviewer's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter for reviewer first name
     *
     * @param firstName reviewer's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = WordUtils.capitalize(firstName);
    }

    /**
     * getter for reviewer last name
     *
     * @return String reviewer's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter for reviewer last name
     *
     * @param lastName reviewer's last name
     */
    public void setLastName(String lastName) {
        this.lastName = WordUtils.capitalize(lastName);
    }

    /**
     * getter for reviewer username
     *
     * @return String reviewer's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * getter for reviewer username
     *
     * @param userName reviewer's username
     */
    public void setUserName(String userName) {
        this.userName = WordUtils.capitalize(userName);
    }

    /**
     * getter for reviewer profile picture
     *
     * @return String reviewer's profile picture
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * setter for reviewer profile picture
     *
     * @param imageUrl reviewer's profile picture
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * getter for reviewer email
     *
     * @return String reviewer's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for reviewer email
     *
     * @return String reviewer's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter for reviewer id
     *
     * @return String reviewer's id
     */
    public String getId() {
        return id;
    }

    /**
     * setter for reviewer id
     *
     * @param id reviewer's id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Override reviewer toString function
     *
     * @return reviewer's username
     */
    @Override
    public String toString() {
        return userName;
    }

}
