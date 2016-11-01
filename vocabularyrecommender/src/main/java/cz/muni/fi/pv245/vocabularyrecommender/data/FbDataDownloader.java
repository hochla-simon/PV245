/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import java.io.IOException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class FbDataDownloader {
    
    public static void main(String[] args) throws IOException {
        String MY_ACCESS_TOKEN = "EAACEdEose0cBAO5urxPG9hrAbegvzhvc3HSzTM4PZAa6dXqL17B0AL2XFUs0ZBqiAqeFl5WexyKoCZBAWY3IkYqwHVZCZAo6cAQZAVwc6vK9TWO8OM9FhLiNLuhZCtZB2ZCcmkyvjOiA7oDiRp0691QY7Hr484MZANY9bKIGesPfVqEwZDZD";
        downloadFbData(MY_ACCESS_TOKEN);
    }  
    
    public static void downloadFbData(String token) {
        FacebookClient facebookClient;
        try {
            facebookClient = new DefaultFacebookClient(token, Version.VERSION_2_8);
            User user = facebookClient.fetchObject("me", User.class);
            System.out.println("User name: " + user.getName());
        } catch (FacebookOAuthException e) {
            System.out.println(e.toString());
            return;
        }
        Collection<String> events = getEvents(facebookClient);
//        for (String event : events) {
//            System.out.println("EVENT:\n" + event);
//        }
        getLikedPagesFeed(facebookClient);
        getMyFeed(facebookClient);
        
    }
     
    public static Collection<String> getEvents(FacebookClient facebookClient) {
        JsonObject eventsConnection = facebookClient.fetchObject("me/events", JsonObject.class);
        List<String> col = new ArrayList<>();
        for (int i=0; i<eventsConnection.getJsonArray("data").length(); i++) {
            col.add(eventsConnection.getJsonArray("data").getJsonObject(i).getString("name") + "\n" + 
                    eventsConnection.getJsonArray("data").getJsonObject(i).getString("description"));
        }
        return col;
    }

    public static Collection<String> getLikedPagesFeed(FacebookClient facebookClient) {
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

    public static String getMyFeed(FacebookClient facebookClient) {
        String feed = new String();
        JsonObject pageConnection = facebookClient.fetchObject("me/feed", JsonObject.class);
        for (int j=0; j<pageConnection.getJsonArray("data").length(); j++) {
            if (pageConnection.getJsonArray("data").getJsonObject(j).has("message")) {
                feed += pageConnection.getJsonArray("data").getJsonObject(j).getString("message") + "\n";
            }
        }
        return feed;
    }
}
