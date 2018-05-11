package com.looforyou.looforyou.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * This is a custom data structure used to store review information from server
 *
 * @author mingtau li
 */

public class Review {
    /* relevant variables for review properties */
    private int rating;
    private String content;
    private Date timeCreated;
    private Date timeUpdated;
    private int likes;
    private String id;
    private String bathroomId;
    private String submittedById;

    private String reviewerFirstName;
    private String reviewerLastName;
    private String reviewerUserName;
    private String reviewerImageUrl;
    private String reviewerEmail;
    private String reviewerId;

    /**
     * public constructor for review
     *
     * @param rating        review star amount
     * @param content       review content
     * @param likes         number of likes for review
     * @param id            unique id of review
     * @param bathroomId    id of bathroom review is for
     * @param submittedById id of user who submitted review
     */
    public Review(int rating, String content, int likes, String id, String bathroomId, String submittedById) {
        this.rating = rating;
        this.content = content;
        this.likes = likes;
        this.id = id;
        this.bathroomId = bathroomId;
        this.submittedById = submittedById;
        this.timeCreated = null;
        this.timeUpdated = null;
    }

    /**
     * getter for rating
     *
     * @return int review rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * setter for rating
     *
     * @param rating number of stars
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * getter for review content
     *
     * @return string review content
     */
    public String getContent() {
        return content;
    }

    /**
     * setter for review content
     *
     * @param content review content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * getter for review creation timestamp
     *
     * @return Date timestamp of creation
     */
    public Date getTimeCreated() {
        return timeCreated;
    }

    /**
     * setter for review creation timestamp
     *
     * @param timeCreated date of review creation
     */
    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * getter for review update timestamp
     *
     * @return Date timestamp of review update
     */
    public Date getTimeUpdated() {
        return timeUpdated;
    }

    /**
     * setter for review update timestamp
     *
     * @param timeUpdated timestamp of review update
     */
    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    /**
     * getter for likes
     *
     * @return int number of likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * setter for likes
     *
     * @param likes number of likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * getter for review id
     *
     * @returm String unique review id
     */
    public String getId() {
        return id;
    }

    /**
     * setter for review id
     *
     * @param id unique review id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * adds reviewer content from to review from list of reviewer models.
     * Matches reviewers to review by review id
     *
     * @param reviewers list of sll reviewers
     */
    public void setReviewerInfo(ArrayList<Reviewer> reviewers) {
        for (Reviewer r : reviewers) {
            if (r.getId().equals(getSubmitedById())) {
                try {
                    /* capture first name of reviewer */
                    setReviewerFirstName(r.getFirstName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    /* capture last name of reviewer */
                    setReviewerLastName(r.getLastName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {

                    /* capture username of reviewer */
                    setReviewerUserName(r.getUserName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    /* capture profile image of reviewer */
                    setReviewerImageUrl(r.getImageUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    /* capture email of reviewer */
                    setReviewerEmail(r.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    /* capture id of reviewer */
                    setReviewerId(r.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * getter for bathroom id
     *
     * @return String id of bathroom reviewed
     */
    public String getBathroomId() {
        return bathroomId;
    }

    /**
     * setter for bathroom id
     *
     * @param bathroomId id of bathroom reviewed
     */
    public void setBathroomId(String bathroomId) {
        this.bathroomId = bathroomId;
    }

    /**
     * getter for reviewer id
     *
     * @return String id of user who made the review
     */
    public String getSubmitedById() {
        return submittedById;
    }

    /**
     * setter for reviewer id
     *
     * @param submittedById String id of user who made the review
     */
    public void setSubmitedById(String submittedById) {
        this.submittedById = submittedById;
    }

    /**
     * getter for reviewer first name
     *
     * @return String first name of user who made the review
     */
    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    /**
     * setter for reviewer first name
     *
     * @param reviewerFirstName first name of user who made the review
     */
    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    /**
     * getter for reviewer last name
     *
     * @return String last name of user who made the review
     */
    public String getReviewerLastName() {
        return reviewerLastName;
    }

    /**
     * setter for reviewer last name
     *
     * @param reviewerLastName last name of user who made the review
     */
    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }

    /**
     * getter for reviewer username
     *
     * @return String username of user who made the review
     */
    public String getReviewerUserName() {
        return reviewerUserName;
    }

    /**
     * setter for reviewer username
     *
     * @param reviewerUserName username of user who made the review
     */
    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    /**
     * getter for reviewer profile picture
     *
     * @return String url of reviewer profile picture
     */
    public String getReviewerImageUrl() {
        return reviewerImageUrl;
    }

    /**
     * setter for reviewer profile picture
     *
     * @param reviewerImageUrl url of reviewer profile picture
     */
    public void setReviewerImageUrl(String reviewerImageUrl) {
        this.reviewerImageUrl = reviewerImageUrl;
    }

    /**
     * getter for reviewer email
     *
     * @return String email of user who made the review
     */
    public String getReviewerEmail() {
        return reviewerEmail;
    }

    /**
     * setter for reviewer email
     *
     * @param reviewerEmail email of user who made the review
     */
    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    /**
     * getter for reviewer id
     *
     * @return String id of user who made the review
     */
    public String getReviewerId() {
        return reviewerId;
    }

    /**
     * setter for reviewer id
     *
     * @param reviewerId id of user who made the review
     */
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
}
