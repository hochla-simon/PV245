package cz.muni.fi.pv245.vocabularyrecommender.web;

import cz.muni.fi.pv245.vocabularyrecommender.data.Dictionary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/practising"})
public class Practising extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(true);
        String word;
        List<String> words;

        if (req.getParameter("Yes, I knew") != null) {
            words = (List<String>) session.getAttribute("words");
            words.remove(0);

            if (words.size() == 0) {
                req.getRequestDispatcher("/RecommendEvents.jsp").forward(req, res);
            }

            word = words.get(0);
        } else if (req.getParameter("Oops, I was wrong") != null) {
            words = (List<String>) session.getAttribute("words");

            if (words.size() > 1) {
                words.remove(0);
            }

            word = words.get(0);
            words.add(word);
        } else {
            //request came from the recommender page
            Map<Integer, List<String>> wordsMap = (Map<Integer, List<String>>) session.getAttribute("wordsMap");
            String item = req.getParameter("item");

            words = wordsMap.get(Integer.parseInt(item));
            word = words.get(0);
        }

        session.setAttribute("word", word);
        session.setAttribute("words", words);

        String meaning = Dictionary.getDefinitionUsing(word, true);
        req.setAttribute("meaning", meaning);

        req.getRequestDispatcher("/Practising.jsp").forward(req, res);
    }
}
