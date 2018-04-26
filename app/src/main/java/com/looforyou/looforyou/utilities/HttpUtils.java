package com.looforyou.looforyou.utilities;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by ibreaker on 4/19/2018.
 */

public class HttpUtils {
    /* move this to a utility class later*/
    public static URL encodeQuery(URL query) {
        URL urlStr = query;
        URL url = null;
        url = urlStr;
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
