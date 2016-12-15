package cz.muni.fi.pv245.vocabularyrecommender.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
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

        /**
         * Pair of 'word' and its 'translation' via exaplanatory dictionary that is sent to the user
         */
        Pair<String, String> word = null;

        /**
         * Map of words (keys) and their corresponding translations (values) via explanatory dictionary.
         * The map is sorted in the order respective to the words difficulty (see the Comparator).
         */
        TreeMap<String, String> words = null;

        /**
         * Index to the words map pointing to the word that is sent to the user.
         */
        int index = 0;

        //The user pressed 'Yes, I knew' for the previous word
        if (req.getParameter("Yes, I knew") != null) {
            index = (int) session.getAttribute("index");
            words = (TreeMap<String, String>) session.getAttribute("words");

            //redirection the user back to the recommender page after practising is finished
            if (words.size() <= 1) {
                req.getRequestDispatcher("/RecommendEvents.jsp").forward(req, res);
                return;
            }  

            //removing the previous word (that the user has already mastered)
            String key = (String) words.keySet().toArray()[index];
            words.remove(key);

            //determining the new index
            index = ((words.keySet().size() - 1) + index) / 2;

            //...and the new word
            String newKey = (String)words.keySet().toArray()[index];
            word = new Pair(newKey, words.get(newKey));

        //The user pressed 'Oops, I was wrong' for the previous word
        } else if (req.getParameter("Oops, I was wrong") != null) {
            index = (int) session.getAttribute("index");
            words = (TreeMap<String, String>) session.getAttribute("words");

            //determining the new index
            index = index / 2;

            //...and the new word
            String key = (String)words.keySet().toArray()[index];
            word = new Pair(key, words.get(key));

        //request came from the recommender page
        } else if (req.getParameter("Finish practising") != null) {
            req.getRequestDispatcher("/RecommendEvents.jsp").forward(req, res);
            return;
        } else {
            //datastructure obtained from the recommender page
            Map<Integer, HashMap<String, HashMap<String, String>>>  wordsMapOfMapsOfMaps =
                    (Map<Integer, HashMap<String, HashMap<String, String>>>) session.getAttribute("wordsMap");
            //index pointing to the item selected by the user
            String item = req.getParameter("item");

            //group of words user selected on the recommender page to learn
            HashMap<String, HashMap<String, String>> wordsMapOfMaps =
                    wordsMapOfMapsOfMaps.get(Integer.parseInt(item));

            //comparator ordering the values of map using the words difficulty
            Comparator<String> wordsComparator = new Comparator<String>() {
                @Override public int compare(String o1, String o2) {
                    if (o1.length() > o2.length()) {
                        return 1;
                    } else if (o1.length() < o2.length()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            };
            words = new TreeMap<>(wordsComparator);

            //transfering words and their translations to the new datastructre
            for (Map.Entry pair : wordsMapOfMaps.entrySet()) {
                for (Map.Entry pair2 : ((HashMap<String, String>)pair.getValue()).entrySet()) {
                    words.put((String) pair2.getKey(), (String) pair2.getValue());
                }
            }

            //selection of the first word user is going to learn
            //let's start with the moderate difficulty
            int wordsCount = words.keySet().size();
            index = wordsCount / 2;
            String key = (String) words.keySet().toArray()[index];
            word = new Pair(key, words.get(key));
        }
           
        session.setAttribute("word", word.getKey());
        session.setAttribute("words", words);
        session.setAttribute("index", index);
        
        req.setAttribute("meaning", word.getValue());

        req.getRequestDispatcher("/MainMenu.jsp").forward(req, res);
    }
}
