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

//    public int getDefaultPicture() {
//        return R.drawable.ic_profile;
//    }
    public ReviewsListItem(String reviewer, String content, String profilePicture) {
        this.reviewer = reviewer;
        this.content = content;
        this.profilePicture = profilePicture;
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
}
