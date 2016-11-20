/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;

/**
 *
 * @author Daniel
 */
public class Dictionary {
    public static void main(String[] args) {
        //List<String> result = getSimilarWords("computer", 10); //TOTO FUNGUJE PARADNE
        //List<String> result = getSimilarWords("run", 10); // TOTO ZAFUNGUJE S PRVYM SYNONYMOM
        List<String> result = getSimilarWords("write", 10); // A TOTO JE TAK TROSKU ODVECI VYSLEDOK :D
        for (String s : result) {
            System.out.println(s);
        }
    }

    private static String getWordDomain(String word) {
        URL url = null;
        // get word domain
        System.out.print("Getting domain for: ");
        System.out.println(word);
        try {
            url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + word);
        } catch (MalformedURLException ex) {
            System.err.println(ex.toString());
        }
        JsonObject obj = processRequest(url);
        String domain = "";
        try {
            domain = obj.get("results").getAsJsonArray().get(0).getAsJsonObject().get("lexicalEntries").getAsJsonArray().get(0).getAsJsonObject().get("entries").getAsJsonArray().get(0).getAsJsonObject().get("senses").getAsJsonArray().getAsJsonArray().get(0).getAsJsonObject().get("domains").getAsJsonArray().get(0).getAsString();
        } catch (Exception e) {
            return domain;
        }
        return domain;
    }
    
    private static List getSynonyms(String word) {
        URL url = null;
        try {
            url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + word + "/synonyms");
        } catch (MalformedURLException ex) {
        }
        JsonObject obj = processRequest(url);
        List<String> result = new ArrayList<>();
        try {
            JsonArray synonyms = obj.get("results").getAsJsonArray().get(0).getAsJsonObject().get("lexicalEntries").getAsJsonArray().get(0).getAsJsonObject().get("entries").getAsJsonArray().get(0).getAsJsonObject().get("senses").getAsJsonArray().get(0).getAsJsonObject().get("synonyms").getAsJsonArray();
            for (int i=0; i<synonyms.size(); i++) {
                result.add(synonyms.get(i).getAsJsonObject().get("text").getAsString());
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }
    
    public static List getSimilarWords(String word, Integer limit) {
        boolean domainFound = false;
        List<String> result;
        String domain = getWordDomain(word);
        if (domain.length() == 0) {
            System.out.println("We do not know the domain, trying synonyms.");
            List<String> synonyms = getSynonyms(word);
            for (String s : synonyms) {
                domain = getWordDomain(s);
                if (domain.length() != 0) {
                    domainFound = true;
                    System.out.println("Domain for synoym found.");
                    break;
                }
            }
        } else {
            domainFound = true;
        }
        // get similar words from this domain
        if (domainFound) {
            result = getWordsFromDomain(domain, limit);
        } else {
            System.out.println("Domain from synonym not found, trying words with the same base.");
            result = getWordsWithSameBase(word, limit);
        }
        return result;
    }
    
    private static List getWordsFromDomain(String domain, Integer limit) {
        URL url = null;
        try {
            url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/wordlist/en/domains%3D" + domain);
        } catch (MalformedURLException ex) {
        }
        List<String> result = new ArrayList<>();
        JsonObject obj = processRequest(url);
        for (int i=0; i<obj.get("results").getAsJsonArray().size(); i++) {
            result.add(obj.get("results").getAsJsonArray().get(i).getAsJsonObject().get("word").getAsString());
        }
        
        List<String> selection = new ArrayList<>();
        if (limit >= result.size()) {
            return result;
        } else {
            int koef = result.size() / limit;
            int max = limit * koef;
            for (int i=0; i<max; i+=koef) {
                selection.add(result.get(i));
            }
        }
        return selection;
    }

    private static List getWordsWithSameBase(String word, Integer limit) {
        URL url = null;
        try {
            url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/search/en?q=" + word + "&prefix=false&limit=" + limit.toString());
        } catch (MalformedURLException ex) {
        }
        List<String> result = new ArrayList<>();
        JsonObject obj = processRequest(url);
        for (int i=0; i<obj.get("results").getAsJsonArray().size(); i++) {
            result.add(obj.get("results").getAsJsonArray().get(i).getAsJsonObject().get("word").getAsString());
        }
        return result;
    }
    
    private static JsonObject processRequest(URL url) {
        final String app_id = "4335cf05";
        final String app_key = "9035da7c9722e552b82062e6a3e95cea";
        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(stringBuilder.toString());
            return root.getAsJsonObject();
        }
        catch (Exception e) {
            return null;
        }
    }
}

