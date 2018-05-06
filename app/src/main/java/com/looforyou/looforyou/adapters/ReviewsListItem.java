package com.looforyou.looforyou.adapters;

import com.looforyou.looforyou.Models.Review;

/**
 * This is the recycler view list item for reviews
 *
 * @author: mingtau li
 */

public class ReviewsListItem {
    /* Review object for data display */
    private Review review;
    /* Reviewer object for data display */
    private String reviewer;
    /* review content */
    private String content;
    /* reviewer profile picture */
    private String profilePicture;
    /* review points */
    private int points;
    /* review rating */
    private int rating;

    /**
     * Constructor for revoew list item
     *
     * @param review         Review object
     * @param reviewer       Reviewer object
     * @param review         content
     * @param profilePicture reviewerprofile picture
     * @param profilePicture points review points
     * @param rating         review rating
     */
    public ReviewsListItem(Review review, String reviewer, String content, String profilePicture, int points, int rating) {
        this.reviewer = reviewer;
        this.content = content;
        setProfilePicture(profilePicture);
        this.profilePicture = getprofilePicture();
        this.points = points;
        this.rating = rating;
        this.review = review;
    }

    /* getter for profile picture
     * @return String image url for reviewer profile picture
     * */
    public String getprofilePicture() {
        return profilePicture;
    }

    /**
     * setter for profile picture
     *
     * @param pp url image of profile image
     */
    public void setProfilePicture(String pp) {
        if (pp == null || pp.isEmpty() || pp.contains("http://freedomappapk.com/best-whatsapp-dp/")) {
            profilePicture = "http://pronksiapartments.ee/wp-content/uploads/2015/10/placeholder-face-big.png";
        } else {
            profilePicture = pp;
        }
    }

    /**
     * setter for review
     *
     * @param r review object
     */
    public void setReview(Review r) {
        review = r;
    }

    /** getter for review
     * @return Review object
     * */
    public Review getReview() {
        return review;
    }

    /** getter for reviewer
     * @return Reviewer object
     * */
    public String getReviewer() {
        return reviewer;
    }

    /** getter for content
     * @return String review content
     * */
    public String getContent() {
        return content;
    }

    /**
     * setter for content
     * @param content review content
     * */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * getter for points
     * @return review points
     * */
    public int getPoints() {
        return points;
    }


    /**
     * setter for points
     * @param points review points
     * */
    public void setPoints(int points) {
        this.points = points;
    }

    /** getter for rating
     * @return int review rating
     * */
    public int getRating() {
        return rating;
    }

    /** setter for rating
     * @param rating review rating
     * */
    public void setRating(int rating) {
        this.rating = rating;
    }
}
