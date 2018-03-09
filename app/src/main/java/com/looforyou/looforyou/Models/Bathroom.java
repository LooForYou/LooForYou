package com.looforyou.looforyou.Models;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ibreaker on 3/9/2018.
 */

public class Bathroom {
    private String name;
    private Location Location;
    private float rating;
    private Date startTime;
    private Date endTime;
    private Date maintenanceStart;
    private Date maintenanceEnd;
    private ArrayList<String> maintenanceDays;
    private boolean bookmarked;
    private ArrayList<String> amenities;
    private String desc1;
    private String desc2;
    private String desc3;


    public Bathroom(){}
}
