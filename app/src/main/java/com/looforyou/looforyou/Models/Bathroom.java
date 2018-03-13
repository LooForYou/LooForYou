package com.looforyou.looforyou.Models;

import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ibreaker on 3/9/2018.
 */

public class Bathroom implements Serializable {
    private String name, address;
    private Coordinates latLng;
    private double rating;
    private Date startTime, endTime, maintenanceStart, maintenanceEnd;
    private ArrayList<String> maintenanceDays, amenities, descriptions;
    private boolean bookmarked;
    private ArrayList<String> reviews;
    private Drawable image;

    public Bathroom(){
        this.name = "Unknown";
        latLng = new Coordinates(0,0);
        this.rating = 0.0;
        this.startTime = new Date();
        this.endTime = new Date();
        this.maintenanceStart = new Date();
        this.maintenanceEnd = new Date();
        this.maintenanceDays = new ArrayList<String>();
        this.bookmarked = false;
        this.amenities = new ArrayList<String>();
        this.descriptions = new ArrayList<String>();
        reviews = new ArrayList<String>();
        image = null;
        this.address = "Unknown location";
    }

    public Bathroom(String name, Coordinates latLng, double rating, Date startTime, Date endTime, Date maintenanceStart, Date maintenanceEnd, ArrayList<String> maintenanceDays, boolean bookmarked, ArrayList<String> amenities, ArrayList<String> descriptions, String address){
        this.name = name;
        this.latLng = latLng;
        this.rating = rating;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maintenanceStart = maintenanceStart;
        this.maintenanceEnd = maintenanceEnd;
        this.maintenanceDays = maintenanceDays;
        this.bookmarked = bookmarked;
        this.amenities = amenities;
        this.descriptions = descriptions;
        reviews = new ArrayList<String>();
        image = null;
        this.address = address;

    }



    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Coordinates getLatLng() {
        return latLng;
    }

    public void setLatLng(Coordinates latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getMaintenanceStart() {
        return maintenanceStart;
    }

    public void setMaintenanceStart(Date maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    public Date getMaintenanceEnd() {
        return maintenanceEnd;
    }

    public void setMaintenanceEnd(Date maintenanceEnd) {
        this.maintenanceEnd = maintenanceEnd;
    }

    public ArrayList<String> getMaintenanceDays() {
        return maintenanceDays;
    }

    public void setMaintenanceDays(ArrayList<String> maintenanceDays) {
        this.maintenanceDays = maintenanceDays;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Bathroom name: "+ name + "\ncoord: " + latLng.latitude+", "+ latLng.longitude+"\nrating: "+rating+"\nhours: "+startTime+" - "+endTime+"\nmaintenance: "+
                maintenanceStart+" - "+maintenanceEnd+", "+maintenanceDays+"\nbookmarked: "+String.valueOf(bookmarked)+"\namenities: "+amenities.toString()+"\ndescriptions: "+
                descriptions.toString()+"\nimage: "+String.valueOf(image);

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
