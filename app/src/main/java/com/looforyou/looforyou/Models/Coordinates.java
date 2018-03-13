package com.looforyou.looforyou.Models;

import java.io.Serializable;

/**
 * Created by ibreaker on 3/9/2018.
 */

public class Coordinates implements Serializable {
    public double latitude = 0.0;
    public double longitude = 0.0;

    public Coordinates(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCoordinates(){
        return latitude+", "+longitude;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        return (latitude+", "+longitude);
    }
}
