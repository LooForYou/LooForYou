package com.looforyou.looforyou.Models;

import android.util.Log;

import com.looforyou.looforyou.R;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ibreaker on 4/19/2018.
 */

public class Review {
    private int rating;
    private String content;
    private Date timeCreated;
    private Date timeUpdated;
    private int likes;
    private String id;
    private String bathroomId;
    private String submitedById;

    private String reviewerFirstName;
    private String reviewerLastName;
    private String reviewerUserName;
    private String reviewerImageUrl;
    private String reviewerEmail;
    private String reviewerId;

    public Review(int rating, String content, int likes, String id, String bathroomId, String submitedById) {
        this.rating = rating;
        this.content = content;
        this.likes = likes;
        this.id = id;
        this.bathroomId = bathroomId;
        this.submitedById = submitedById;
        this.timeCreated = null;
        this.timeUpdated = null;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReviewerInfo(ArrayList<Reviewer> reviewers) {
        for(Reviewer r: reviewers){
            Log.v("testresultid reviewerID",r.getId());
            Log.v("testresultid reviewID",getId());
            if(r.getId().equals(getSubmitedById())){
                try {
                    setReviewerFirstName(r.getFirstName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    setReviewerLastName(r.getLastName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    setReviewerUserName(r.getUserName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.v("testreviewer reviewobj",r.getUserName());
                try {
                    setReviewerImageUrl(r.getImageUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    setReviewerEmail(r.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    setReviewerId(r.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBathroomId() {
        return bathroomId;
    }

    public void setBathroomId(String bathroomId) {
        this.bathroomId = bathroomId;
    }

    public String getSubmitedById() {
        return submitedById;
    }

    public void setSubmitedById(String submitedById) {
        this.submitedById = submitedById;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }

    public String getReviewerUserName() {
        return reviewerUserName;
    }

    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    public String getReviewerImageUrl() {
        return reviewerImageUrl;
    }

    public void setReviewerImageUrl(String reviewerImageUrl) {
        this.reviewerImageUrl = reviewerImageUrl;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
}
