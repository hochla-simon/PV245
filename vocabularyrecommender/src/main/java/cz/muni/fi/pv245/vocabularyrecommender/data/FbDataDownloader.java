/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
 
/**
 *
 * @author Daniel
 */
public class FbDataDownloader {
    
    public static void main(String[] args) throws IOException, MalformedURLException {
        String MY_ACCESS_TOKEN = "EAACEdEose0cBAGJmOGmDRZAQAq1ZBNqGaIGHM3z0ZBa6d46p2foMzBxZCSZBx0GrE45wxBZAJIo2US" +
                "sczTyc1kyvgXkvmh0aA8yhY2FGBX3bpLEJMpZCn1d3z6oCmH3dMkY27K7aTZAyD1pWEkAHSnsdaAIoKoL7MmS8T6rA9b4jwwZDZD";
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
     
    private static void saveToJson(JSONArray col, String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        Writer out = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName), "UTF-8"));
        try {
            out.write(col.toJSONString());
        } finally {
            out.close();
        }        
    }

    private static JSONArray getEvents(FacebookClient facebookClient) {
        JsonObject eventsConnection = facebookClient.fetchObject("me/events", JsonObject.class);
        JSONArray array = new JSONArray();
        JSONObject obj;
        String name = "";
        String description = "";
        for (int i=0; i<eventsConnection.getJsonArray("data").length(); i++) {
            if (eventsConnection.getJsonArray("data").getJsonObject(i).has("name")) {
                name = eventsConnection.getJsonArray("data").getJsonObject(i).getString("name");
            }
            if (eventsConnection.getJsonArray("data").getJsonObject(i).has("description")) {
                description = eventsConnection.getJsonArray("data").getJsonObject(i).getString("description");
            }
            obj = new JSONObject();
            obj.put(name, description);
            array.add(obj);
        }
        return array;
    }

    private static JSONArray getLikedPagesFeed(FacebookClient facebookClient) {
        JsonObject likesConnection = facebookClient.fetchObject("me/likes", JsonObject.class);
        JSONArray array = new JSONArray();
        JSONObject obj;
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
            obj = new JSONObject();
            obj.put(pageName, feed);
            array.add(obj);
        }
        return array;
    }

    private static JSONArray getMyFeed(FacebookClient facebookClient) {
        JSONArray array = new JSONArray();
        String feed = new String();
        JsonObject pageConnection = facebookClient.fetchObject("me/feed", JsonObject.class, Parameter.with("message", "utf8"));
        for (int j=0; j<pageConnection.getJsonArray("data").length(); j++) {
            if (pageConnection.getJsonArray("data").getJsonObject(j).has("message")) {
                feed += pageConnection.getJsonArray("data").getJsonObject(j).getString("message") + "\n";
            }
        }
        array.add(feed);
        return array;
    }
}
