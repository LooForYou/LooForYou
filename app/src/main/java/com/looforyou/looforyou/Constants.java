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
    public static final String BATHROOM_COUNT = GET_BATHROOMS + "count/";
    public static final String UPDATE_BATHROOM = GET_BATHROOMS + "update?where=";


    /* move this to a utility class later*/
    public static URL encodeQuery(String query) {
        String urlStr = query;
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URI uri = null;
        try {
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String decodeQuery(String query){
        String url = null;
        try {
            url = URLDecoder.decode(query,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
