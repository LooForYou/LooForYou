package com.looforyou.looforyou.adapters;

import android.graphics.drawable.Drawable;

import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.R;

/**
 * Created by ibreaker on 4/9/2018.
 */

public class ReviewsListItem {
    private Review review;
    private String reviewer;
    private String content;
    private String profilePicture;
    private int points;
    private int rating;

    public ReviewsListItem(Review review, String reviewer, String content, String profilePicture, int points, int rating) {
        this.reviewer = reviewer;
        this.content = content;
        setProfilePicture(profilePicture);
        this.profilePicture = getprofilePicture();
        this.points = points;
        this.rating = rating;
        this.review = review;
    }

    public String getprofilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String pp) {

        if(pp == null || pp.contains("http://freedomappapk.com/best-whatsapp-dp/")) {
            profilePicture = "http://pronksiapartments.ee/wp-content/uploads/2015/10/placeholder-face-big.png";
        }else {
            profilePicture = pp;
        }
    }

    public void setReview(Review r) {
        review = r;
    }
    public Review getReview() {
        return review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
