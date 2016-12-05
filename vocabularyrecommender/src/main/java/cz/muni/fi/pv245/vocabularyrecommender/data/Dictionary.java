/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv245.vocabularyrecommender.data;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * The main task of this class is to read the input json file, go through all obtained words and 
 * find for them vocabulary with explanations
 * @author Daniel
 * 
    Example of input json file:
    [{"event_name":"nazov1","language":"sk","words":["rok","rieąenie","ministerstvo"]},
     {"event_name":"nazov2","language":"en","words":["big","mistake","bird"]},
    ]

    Example of output structure:
    {"nazov1": {"rok":          {"slovo1": "vysvetlenie1",
                                 "slovo2": "vysvetlenie2",
                                 "slovo3": "vysvetlenie3"}
                "riesenie":     {"slovo1": "vysvetlenie1",
                                 "slovo2": "vysvetlenie2",
                                 "slovo3": "vysvetlenie3"}
                "ministerstvo": {"slovo1": "vysvetlenie1",
                                 "slovo3": "vysvetlenie3"}
                },
     "nazov2": {"big":          {"slovo1": "vysvetlenie1",
                                 "slovo2": "vysvetlenie2",
                                 "slovo3": "vysvetlenie3"}
                "mistake":      {"slovo1": "vysvetlenie1",
                                 "slovo2": "vysvetlenie2",
                                 "slovo3": "vysvetlenie3"}
                "bird":         {"slovo1": "vysvetlenie1",
                                 "slovo3": "vysvetlenie3"}
                }
    }
*/
public class Dictionary {
    
    static int requestCount = 0;
    
    public static void main(String[] args) throws IOException, ParseException {
        // this is the example how to call getFinalVocabulary an process result
        HashMap<String, HashMap<String, HashMap<String, String>>> result = new HashMap();
        result = getFinalVocabulary("d:\\school\\MUNI\\podzim2016\\recsys\\PV245-vocabulary_recommender\\vocabularyrecommender\\tfidf_output.json", 5);
        System.out.println("======================================================================");
        for (Map.Entry<String, HashMap<String, HashMap<String, String>>> entry : result.entrySet()) {
            System.out.println("Event name: " + entry.getKey());
            HashMap<String, HashMap<String, String>> wordSets = entry.getValue();
            for (Map.Entry<String, HashMap<String, String>> wordSetEntry : wordSets.entrySet()) {
                System.out.println("Source word: " + wordSetEntry.getKey());
                HashMap<String, String> words = wordSetEntry.getValue();
                for (Map.Entry<String, String> wordEntry : words.entrySet()) {
                    System.out.println(wordEntry.getKey() + ": " + wordEntry.getValue());
                }
            }
        }
        //translateText("stretnutie", "sk");
        //List<String> result = getSimilarWords("computer", 10);
        //List<String> result = getSimilarWords("run", 10);
//        HashMap<String, String> map = getWordlistFor("bird", 10);
//        Set set = map.entrySet();
//        Iterator iterator = set.iterator();
//        while(iterator.hasNext()) {
//            Map.Entry mentry = (Map.Entry)iterator.next();
//            System.out.print(mentry.getKey() + ": ");
//            System.out.println(mentry.getValue());
//        }
    }

    public static HashMap getFinalVocabulary(String path, Integer limit) {
        JSONParser parser = new JSONParser();
        HashMap<String, HashMap<String, HashMap<String, String>>> eventsMap = new HashMap();
        try {
            JSONArray events = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            // processing json file
            for (int i=0; i<events.size(); i++) {
                JSONObject event = (JSONObject) events.get(i);
                JSONArray words = (JSONArray) event.get("words");
                String eventName = event.get("event_name").toString();
                String eventLang = event.get("language").toString();
                System.out.println(eventName + " | " + eventLang);
                HashMap<String, HashMap<String, String>> wordsMap = new HashMap();
                for (int j=0; j<words.size(); j++) {
                    String word = words.get(j).toString();
                    if (!eventLang.equals("en")) {
                        word = translateText(word, eventLang);
                    }
                    System.out.println("Getting hashmap for: " + word);
                    HashMap<String, String> newWordsMap = getWordlistFor(word, limit);
                    wordsMap.put(word, newWordsMap);
                }
                eventsMap.put(eventName, wordsMap);
            }

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (ParseException e) {
                e.printStackTrace();
        }   
        return eventsMap;
    }
    
    private static String translateText(String text, String lang) {
        // Instantiates a client
        Translate translate =
            TranslateOptions.builder()
                .apiKey("AIzaSyBrH7yIvjTuRroBowNuKIPk4umHZRAWO_E")
                .build()
                .service();
        // Translates some text into Russian
        Translation translation =
            translate.translate(
                text,
                TranslateOption.sourceLanguage(lang),
                TranslateOption.targetLanguage("en"));
        System.out.printf("Translation: %s -> %s%n", text, translation.translatedText());
        return translation.translatedText();
    }
    
    public static HashMap getWordlistFor(String word, Integer limit) {
        HashMap<String, String> map = new HashMap();
        List<String> result = getSimilarWords(word, limit*2);
        int counter = 0;
        for (String s : result) {
            String def = getDefinition(s);
            if (def.length() > 0) {
                map.put(s, def);
                counter += 1;
            }
            if (counter == limit) {
                break;
            }
        }
        return map;
    }
    
    private static String getDefinition(String word) {
        String result;
        result = getDefinitionUsing(word, true);
        if (result.length() > 0) return result;
        result = getDefinitionUsing(word, false);
        return result;
    }
    
    public static String getDefinitionUsing(String word, boolean ldoce5) {
        String dict;
        if (ldoce5) {
            dict = "ldoce5";
        } else {
            dict = "wordwise";
        }
        URL url = null;
        word = word.replaceAll(" ", "%20");
        try {
            url = new URL("https://api.pearson.com/v2/dictionaries/"+ dict +"/entries?search="+ word);
        } catch (MalformedURLException ex) {
        }
        JsonObject obj = processPearsonRequest(url);
        try {
            if (obj.get("results").getAsJsonArray().size() > 0) {
                return obj.get("results").getAsJsonArray().get(0).getAsJsonObject().get("senses").getAsJsonArray().get(0).getAsJsonObject().get("definition").getAsString();
            }
        } catch (Exception e) {
            System.out.println("Definition for word " + word + " not available.");
        }
        return "";
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
    
    private static List getSimilarWords(String word, Integer limit) {
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
        System.out.println("Getting words for domain: " + domain);
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
        try {
            for (int i=0; i<obj.get("results").getAsJsonArray().size(); i++) {
                result.add(obj.get("results").getAsJsonArray().get(i).getAsJsonObject().get("word").getAsString());
            }
        } catch (Exception e) {
            System.out.println("There are no words with the same base.");
        }
        return result;
    }
    
    private static JsonObject processRequest(URL url) {
        try {
            if (requestCount > 55) {
                System.out.println("Waiting 30s...");
                TimeUnit.SECONDS.sleep(30);
                requestCount = 0;
            }
        } catch (Exception e) {}
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

    private static JsonObject processPearsonRequest(URL url) {
        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");

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
            e.printStackTrace();
            return null;
        }
    }
}

