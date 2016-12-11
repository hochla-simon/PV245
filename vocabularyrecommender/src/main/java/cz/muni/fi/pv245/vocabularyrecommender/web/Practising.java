package cz.muni.fi.pv245.vocabularyrecommender.web;

import cz.muni.fi.pv245.vocabularyrecommender.data.Dictionary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.util.Pair;

@WebServlet(urlPatterns = {"/practising"})
public class Practising extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(true);
        Pair<String, String> word;
        TreeMap<String, String> words = null;

        if (req.getParameter("Yes, I knew") != null) {
            words = (TreeMap<String, String>) session.getAttribute("words");
            words.remove(words.firstKey());

            if (words.isEmpty()) {
                req.getRequestDispatcher("/RecommendEvents.jsp").forward(req, res);
            }
           
            String firstKey = words.firstKey();
            word = new Pair(firstKey, words.get(firstKey));
        } else if (req.getParameter("Oops, I was wrong") != null) {
            words = (TreeMap<String, String>) session.getAttribute("words");
            
            if (words.size() > 1) {
                words.remove(words.firstKey());
            }
            String firstKey = words.firstKey();
            word = new Pair(firstKey, words.get(firstKey));
            words.put(word.getKey(), word.getValue());
        } else {
            //request came from the recommender page
            Map<Integer, HashMap<String, HashMap<String, String>>>  wordsMapOfMapsOfMaps =
                    (Map<Integer, HashMap<String, HashMap<String, String>>>) session.getAttribute("wordsMap");
            String item = req.getParameter("item");

            HashMap<String, HashMap<String, String>> wordsMapOfMaps =
                    wordsMapOfMapsOfMaps.get(Integer.parseInt(item));
            
            words = new TreeMap<String, String>();
            for (Map.Entry pair : wordsMapOfMaps.entrySet()) {
                for (Map.Entry pair2 : ((HashMap<String, String>)pair.getValue()).entrySet()) {
                    words.put((String) pair2.getKey(), (String) pair2.getValue());
                }
            }
             
            String firstKey = words.firstKey();
            word = new Pair(firstKey, words.get(firstKey));
        }
           
        session.setAttribute("word", word.getKey());
        session.setAttribute("words", words);

        req.setAttribute("meaning", word.getValue());

        req.getRequestDispatcher("/Practising.jsp").forward(req, res);
    }
}
