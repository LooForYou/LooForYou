package com.looforyou.looforyou.adapters;

import android.graphics.drawable.Drawable;

import com.looforyou.looforyou.R;

/**
 * Created by ibreaker on 4/9/2018.
 */

public class ReviewsListItem {
    private String reviewer;
    private String content;
    private String profilePicture;
    private int points;
    private int rating;

    public ReviewsListItem(String reviewer, String content, String profilePicture, int points, int rating) {
        this.reviewer = reviewer;
        this.content = content;
        this.profilePicture = profilePicture;
        this.points = points;
        this.rating = rating;
    }

    public String getprofilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
