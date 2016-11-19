/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.IOException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.json.JsonObject;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.simple.JSONArray;
 
/**
 *
 * @author Daniel
 */
public class FbDataDownloader {
    
    public static void main(String[] args) throws IOException, MalformedURLException {
        String MY_ACCESS_TOKEN = "EAACEdEose0cBAE1oggAeA0olpuXyznsAV3rYciNIGzrCUov4ok7ZA11veeTOWAQZBZBsjoZAXSdNinFsZCwPyc6SCYGkDc119YZCCA3yayUplwvCrxzPdJIa7kzU6k8khhKbWW8rj8UL0F8Yo4lzkZBmymfmgiFlVZBcEFsrAnQd1AZDZD";
        downloadFbData(MY_ACCESS_TOKEN);
    }  
    
    public static void downloadFbData(String token) throws MalformedURLException, IOException {
        FacebookClient facebookClient;
        try {
            facebookClient = new DefaultFacebookClient(token, Version.VERSION_2_8);
            User user = facebookClient.fetchObject("me", User.class);
            System.out.println("User name: " + user.getName());
        } catch (FacebookOAuthException e) {
            System.out.println("You have probably wrong token!\n" + e.toString());
            return;
        }
        saveToJson(getEvents(facebookClient), "events.json");
        System.out.println("File events.json created");
        saveToJson(getLikedPagesFeed(facebookClient), "pagesfeed.json");
        System.out.println("File pagesfeed.json created");
        saveToJson(getMyFeed(facebookClient), "feed.json");
        System.out.println("File feed.json created");
    }
     
    private static void saveToJson(Collection<String> col, String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        JSONArray obj = new JSONArray();
        for (String d : col) {
            obj.add(d);
            System.out.println(d);
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName), "UTF-8"));
        try {
            out.write(obj.toJSONString());
        } finally {
            out.close();
        }        
    }

    private static Collection<String> getEvents(FacebookClient facebookClient) {
        JsonObject eventsConnection = facebookClient.fetchObject("me/events", JsonObject.class);
        List<String> col = new ArrayList<>();
        for (int i=0; i<eventsConnection.getJsonArray("data").length(); i++) {
            col.add(eventsConnection.getJsonArray("data").getJsonObject(i).getString("name") + "\n" + 
                    eventsConnection.getJsonArray("data").getJsonObject(i).getString("description"));
        }
        return col;
    }

    private static Collection<String> getLikedPagesFeed(FacebookClient facebookClient) {
        JsonObject likesConnection = facebookClient.fetchObject("me/likes", JsonObject.class);
        List<String> col = new ArrayList<>();
        for (int i=0; i<likesConnection.getJsonArray("data").length(); i++) {
            String pageId = likesConnection.getJsonArray("data").getJsonObject(i).getString("id");
            String pageName = likesConnection.getJsonArray("data").getJsonObject(i).getString("name");
            String feed = new String();

            JsonObject pageConnection = facebookClient.fetchObject(pageId + "/feed", JsonObject.class);
            for (int j=0; j<pageConnection.getJsonArray("data").length(); j++) {
                if (pageConnection.getJsonArray("data").getJsonObject(j).has("message")) {
                    feed += pageConnection.getJsonArray("data").getJsonObject(j).getString("message") + "\n";
                }
            }
            col.add(pageName + "\n" + feed);
        }
        return col;
    }

    private static Collection<String> getMyFeed(FacebookClient facebookClient) {
        List<String> col = new ArrayList<>();
        String feed = new String();
        JsonObject pageConnection = facebookClient.fetchObject("me/feed", JsonObject.class, Parameter.with("message", "utf8"));
        for (int j=0; j<pageConnection.getJsonArray("data").length(); j++) {
            if (pageConnection.getJsonArray("data").getJsonObject(j).has("message")) {
                feed += pageConnection.getJsonArray("data").getJsonObject(j).getString("message") + "\n";
            }
        }
        col.add(feed);
        return col;
    }
}
