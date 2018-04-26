package com.looforyou.looforyou;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by ibreaker on 3/23/2018.
 */

public class Constants {
    public static final String ROOT = "http://ec2-54-183-105-234.us-west-1.compute.amazonaws.com:9000/";
    public static final String API_ROOT = ROOT + "api/";

    /* bathrooms */
    public static final String GET_BATHROOMS = API_ROOT + "Bathrooms/";
    public static final String GET_USERS = API_ROOT + "Users/";
    public static final String BATHROOM_COUNT = GET_BATHROOMS + "count/";
    public static final String UPDATE_BATHROOM = GET_BATHROOMS + "update?where=";
    public static final String LOGIN = GET_USERS + "login";
    public static final String REVIEWS_LIST = "/submittedReviews/";
    public static final String GET_REVIEWS = API_ROOT + "Reviews/";

    /* sorting options */
    public static final String SORT_BY_DISTANCE = "distance";



}
