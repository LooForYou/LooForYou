package com.looforyou.looforyou.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.looforyou.looforyou.utilities.BathroomDeserializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ibreaker on 3/9/2018.
 */
@JsonAdapter(BathroomDeserializer.class)
public class Bathroom implements Serializable {
    private String name, address, imageURL, maintenanceDays;
    private Coordinates coordinates;
    private double rating;
    private Date startTime, endTime, maintenanceStart, maintenanceEnd;
    private ArrayList<String> amenities, descriptions;
    private boolean bookmarked;
    private ArrayList<String> reviews;
    private Drawable image;


    public Bathroom(){
        this.name = "Unknown";
        coordinates = new Coordinates(0,0);
        this.rating = 0.0;
        this.startTime = new Date();
        this.endTime = new Date();
        this.maintenanceStart = new Date();
        this.maintenanceEnd = new Date();
        this.maintenanceDays = "Unknown";
        this.bookmarked = false;
        this.amenities = new ArrayList<String>();
        this.descriptions = new ArrayList<String>();
        reviews = new ArrayList<String>();
        image = null;
        this.address = "Unknown location";
        imageURL = null;
    }

    public Bathroom(String name, Coordinates coordinates, double rating, Date startTime, Date endTime, Date maintenanceStart, Date maintenanceEnd, String maintenanceDays, boolean bookmarked, ArrayList<String> amenities, ArrayList<String> descriptions, String address){
        this.name = name;
        this.coordinates = coordinates;
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


    public LatLng getLatLng(){
        try {
            return new LatLng(coordinates.latitude,coordinates.longitude);
        }catch(Exception e){

        }
        return null;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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

    public String getMaintenanceDays() {
        return maintenanceDays;
    }

    public void setMaintenanceDays(String maintenanceDays) {
        this.maintenanceDays = maintenanceDays;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    public Drawable getImage(){
        return image;
    }

    @Override
    public String toString() {
        return "Bathroom name: "+ name + "\ncoord: " + coordinates.latitude+", "+ coordinates.longitude+"\nrating: "+rating+"\nhours: "+startTime+" - "+endTime+"\nmaintenance: "+
                maintenanceStart+" - "+maintenanceEnd+", "+maintenanceDays+"\nbookmarked: "+String.valueOf(bookmarked)+"\namenities: "+amenities.toString()+"\ndescriptions: "+
                descriptions.toString()+"\nimage: "+String.valueOf(image);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
