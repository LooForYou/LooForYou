package com.looforyou.looforyou;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * A list of global constants used for API calls and other misc function calls
 *
 * @author mingtau li
 */

public class Constants {
    public static final String ROOT = "http://ec2-54-183-105-234.us-west-1.compute.amazonaws.com:9000/";
    public static final String API_ROOT = ROOT + "api/";

    /* bathrooms */
    public static final String GET_BATHROOMS = API_ROOT + "Bathrooms/";
    public static final String GET_USERS = API_ROOT + "Accounts/";
    public static final String BATHROOM_COUNT = GET_BATHROOMS + "count/";
    public static final String UPDATE_BATHROOM = GET_BATHROOMS + "update?where=";
    public static final String LOGIN = GET_USERS + "login";
    public static final String LOGOUT = GET_USERS + "logout";
    public static final String REVIEWS_LIST = "/submittedReviews/";
    public static final String GET_REVIEWS = API_ROOT + "Reviews/";
    public static final String UPVOTE = "/increment-likes/";
    public static final String DOWNVOTE = "/decrement-likes/";
    public static final String TOKEN_QUERY = "?access_token=";
    public static final String ALL_BOOKMARKS = "/bookmarks";
    public static final String BOOKMARKS = "/bookmarks/";
    public static final String BOOKMARKS_REL = "/bookmarks/rel/";
    public static final int ONE_YEAR = 60 * 60 * 24 * 365;
    public static final String UPDATE_REVIEW = GET_REVIEWS+"update";
    public static final String UPLOAD_IMAGE = "/upload-image";

    /* sorting options */
    public static final String SORT_BY_DISTANCE = "distance";



}
