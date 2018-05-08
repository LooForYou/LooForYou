package com.looforyou.looforyou.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

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
 * This is a custom data structure used to store bathroom information from server
 *
 * @author mingtau li
 */
@JsonAdapter(BathroomDeserializer.class)
public class Bathroom implements Serializable, Parcelable {
    private String name, address, imageURL, maintenanceDays;
    private Coordinates coordinates;
    private double rating;
    private Date startTime, endTime, maintenanceStart, maintenanceEnd;
    private ArrayList<String> amenities, descriptions;
    private boolean bookmarked;
    private ArrayList<String> reviews;
    private Drawable image;
    private String id;

    /**
     * public Bathroom constructor
     * @param id unique bathroom id from server*/
    public Bathroom(String id){
        this.id = id;
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

    /**
     * public Bathroom constructor with more parameter options
     * @param id unique bathroom id
     * @param name bathroom name
     * @param coordinates Coordinate object for bathroom
     * @param rating bathroom rating amount
     * @param startTime start of hours of operation
     * @param endTime end of hours of operation
     * @param maintenanceStart start of maintenance
     * @param maintenanceEnd end of maintenance
     * @param maintenanceDays days of maintenance
     * @param bookmarked bookmarked status of bathroom
     * @param amenities bathroom amenities list
     * @param descriptions bathroom descriptions list
     * @param address bathroom address
     * */
    public Bathroom(String id, String name, Coordinates coordinates, double rating, Date startTime, Date endTime, Date maintenanceStart, Date maintenanceEnd, String maintenanceDays, boolean bookmarked, ArrayList<String> amenities, ArrayList<String> descriptions, String address){
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
        this.id = id;
    }

    /**
     * getter for bathroom id
     * @return String bathroom id
     * */
    public String getId() {
        return id;
    }

    /**
     * setter for bathroom id
     * @param id bathroom id
     * */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get LatLng object from bathroom coordinates
     * @return LatLng object with bathroom coordinates
     * */
    public LatLng getLatLng(){
        try {
            return new LatLng(coordinates.latitude,coordinates.longitude);
        }catch(Exception e){

        }
        return null;
    }

    /**
     * getter for reviews
     * @return list of reviews
     * */
    public ArrayList<String> getReviews() {
        return reviews;
    }

    /**
     * setter for reviews
     * @param reviews list of reviews
     * */
    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    /** getter for rating
     * @return double bathroom rating amount
     */
    public double getRating() {
        return rating;
    }

    /**
     * setter for rating
     * @param rating bathroom rating amount
     * */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * getter for coordinates
     * @return Coordinates object containing bathroom coordinates
     * */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * setter for coordinates
     * @param coordinates bathroom coordinates
     * */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * getter for bathroom name
     * @return String bathroom name
     * */
    public String getName() {
        return name;
    }

    /**
     * setter for bathroom name
     * @param name name of bathroom
     * */
    public void setName(String name) {
        this.name = name;
    }

    /** getter for bathroom descriptions
     * @return list of descriptions
     * */
    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    /** setter for bathroom descriptions
     * @param descriptions list of descriptions for bathroom
     * */
    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    /** getter for start time
     * @return Date start hours of operation
     * */
    public Date getStartTime() {
        return startTime;
    }

    /** setter for start time
     * @return Date start hours of operation
     * */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /** getter for end time
     * @return Date end hours of operation
     * */
    public Date getEndTime() {
        return endTime;
    }

    /** setter for end time
     * @return Date end hours of operation
     * */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * getter for maintenance start time
     * @return Date start maintenance time
     * */
    public Date getMaintenanceStart() {
        return maintenanceStart;
    }

    /** setter for maintenance start
     * @return Date maintenance start tiem
     * */
    public void setMaintenanceStart(Date maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    /**
     * getter for maintenance end time
     * @return Date end maintenance time
     * */
    public Date getMaintenanceEnd() {
        return maintenanceEnd;
    }

    /** setter for maintenance end time
     * @return Date maintenance end time
     * */
    public void setMaintenanceEnd(Date maintenanceEnd) {
        this.maintenanceEnd = maintenanceEnd;
    }

    /**
     * getter for maintenance days
     * @return String maintenance days
     * */
    public String getMaintenanceDays() {
        return maintenanceDays;
    }

    /**
     * setter for maintenance days
     * @param maintenanceDays string maintenance days
     * */
    public void setMaintenanceDays(String maintenanceDays) {
        this.maintenanceDays = maintenanceDays;
    }

    /*
    * getter for bathroom amenities
    * @return ArrayList<String> list of amenities
    * */
    public ArrayList<String> getAmenities() {
        return amenities;
    }

    /**
     * setter for amenities
     * @param amenities list of amenities
     * */
    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }

    /**
     * getter for image
     * @return Drawable bathroom image
     * */
    public Drawable getImage(){
        return image;
    }

    /**
     * override tostring to print custom bathroom info
     * @return String bathroom info
     * */
    @Override
    public String toString() {
        return "Bathroom name: "+ name + "\ncoord: " + coordinates.latitude+", "+ coordinates.longitude+"\nrating: "+rating+"\nhours: "+startTime+" - "+endTime+"\nmaintenance: "+
                maintenanceStart+" - "+maintenanceEnd+", "+maintenanceDays+"\nbookmarked: "+String.valueOf(bookmarked)+"\namenities: "+amenities.toString()+"\ndescriptions: "+
                descriptions.toString()+"\nimage: "+String.valueOf(image);
    }

    /**
     * getter for addresss
     * @return String bathroom address
     * */
    public String getAddress() {
        return address;
    }

    /**
     * setter for address
     * @param address bathroom address
     * */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getter for image Url
     * @return String bahroom image URL
     * */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * setter for image URl
     * @param imageURL url of bathroom image
     * */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /** setter for image
     * @param image image of bathroom
     * */
    public void setImage(Drawable image) {
        this.image = image;
    }

    /**
     * instantiate variables with parcel object when reading in Parcelable
     * @param in Parcel object
     * */
    protected Bathroom(Parcel in) {
        name = in.readString();
        address = in.readString();
        imageURL = in.readString();
        maintenanceDays = in.readString();
        rating = in.readDouble();
        amenities = in.createStringArrayList();
        descriptions = in.createStringArrayList();
        bookmarked = in.readByte() != 0;
        reviews = in.createStringArrayList();
        id = in.readString();
    }

    /**
     * new Creator to make Bathroom Parcelable so it can be passed into Bundle arguments
     * */
    public static final Creator<Bathroom> CREATOR = new Creator<Bathroom>() {
        @Override
        public Bathroom createFromParcel(Parcel in) {
            return new Bathroom(in);
        }

        @Override
        public Bathroom[] newArray(int size) {
            return new Bathroom[size];
        }
    };

    /* item description */
    @Override
    public int describeContents() {
        return 0;
    }

    /* allow bathroom to be written to parcel */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(imageURL);
        parcel.writeString(maintenanceDays);
        parcel.writeDouble(rating);
        parcel.writeStringList(amenities);
        parcel.writeStringList(descriptions);
        parcel.writeByte((byte) (bookmarked ? 1 : 0));
        parcel.writeStringList(reviews);
        parcel.writeString(id);
    }
}
