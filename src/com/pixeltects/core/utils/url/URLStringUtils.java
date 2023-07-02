package com.pixeltects.core.utils.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLStringUtils {

    public static String getUrlAsString(String url) {
        try {
            URL urlObj = new URL(url);
            URLConnection con = urlObj.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Cookie", "myCookie=test123");
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(String.valueOf(inputLine) + newLine);
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
