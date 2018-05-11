package com.looforyou.looforyou.adapters;

import com.looforyou.looforyou.Models.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This is the recycler view list item for reviews
 *
 * @author mingtau li
 */

public class ReviewsListItem {
    /* Review object for data display */
    private Review review;
    /* Reviewer name for data display */
    private String reviewer;
    /* id for reviewer */
    private String reviewerId;
    /* review content */
    private String content;
    /* reviewer profile picture */
    private String profilePicture;
    /* review points */
    private int points;
    /* review rating */
    private int rating;
    /* review timestamp created */
    private Date dateCreated;
    /* review timestamp edited*/
    private Date dateEdited;


    /**
     * Constructor for review list item
     *
     * @param review         Review object
     * @param reviewerId     id of reviewer
     * @param reviewer       Reviewer object
     * @param review         content
     * @param profilePicture reviewerprofile picture
     * @param profilePicture points review points
     * @param rating         review rating
     * @param created        time posted
     * @param edited         time edited
     */
    public ReviewsListItem(Review review, String reviewerId, String reviewer, String content, String profilePicture, int points, int rating, Date created, Date edited) {
        this.reviewer = reviewer;
        this.reviewerId = reviewerId;
        this.content = content;
        setProfilePicture(profilePicture);
        this.profilePicture = getprofilePicture();
        this.points = points;
        this.rating = rating;
        this.review = review;
        this.dateCreated = created;
        this.dateEdited = edited;
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

    /**
     * getter for rating
     * @return int review rating
     * */
    public int getRating() {
        return rating;
    }

    /**
     * setter for rating
     * @param rating review rating
     * */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * getter for date created
     * @return Date time of review creation
     * */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * setter for date created
     * @param dateCreated time of review creation
     * */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /** getter for date edited
     * @return Date time of review edit */
    public Date getDateEdited() {
        return dateEdited;
    }

    /**
     * setter for date edited
     * @param dateEdited time of review edit
     * */
    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    /**
     * calculates days elapsed since lastEdited timestamp
     * @return int number of days ago since last review edit
     * */
    public int getDaysAgo() {
        int daysAgo = 0;
        long diff = 0;
        Date t = Calendar.getInstance().getTime();
            diff = dateEdited.getTime() - t.getTime();
            daysAgo = Math.round(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        return daysAgo;
    }

    /**
     * getter for reviewer id
     * @return String reviewer's account id
     * */
    public String getReviewerId() {
        return reviewerId;
    }

    /**
     * setter for reviewer id
     * @param reviewerId reviewer's account id
     * */
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    /**
     * getter for review id
     * @return String unique id of review
     * */
    public String getReviewId() {
        return review.getId();
    }
}
