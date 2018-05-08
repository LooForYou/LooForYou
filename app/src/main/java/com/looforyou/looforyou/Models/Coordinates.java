package com.looforyou.looforyou.Models;

import java.io.Serializable;

/**
 * Small utility class to store and parse coordinates
 *
 * @author mingtau li
 */

public class Coordinates implements Serializable {
    public double latitude = 0.0;
    public double longitude = 0.0;

    /**
     * public constructor for coordinate representation
     *
     * @param latitude  latitude amount
     * @param longitude longitude amount
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * get Coordiantes
     *
     * @return String latitude and longitude concatenated
     */
    public String getCoordinates() {
        return latitude + ", " + longitude;
    }

    /**
     * set Coordinates
     *
     * @param latitude  latitude amount
     * @param longitude longitude amount
     */
    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * displays Coordinate info
     *
     * @return String coordinate info
     */
    @Override
    public String toString() {
        return (latitude + ", " + longitude);
    }
}
