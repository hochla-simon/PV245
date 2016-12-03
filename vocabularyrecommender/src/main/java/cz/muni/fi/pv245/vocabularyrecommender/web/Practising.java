package cz.muni.fi.pv245.vocabularyrecommender.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/practising"})
public class Practising extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setAttribute("word", "dog");
        req.setAttribute("meaning", "a common animal with four legs, fur, and a tail. Dogs are kept as pets or trained to guard places");

        req.getRequestDispatcher("/Practising.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //aby fungovala čestina z formuláře
        req.setCharacterEncoding("utf-8");

        if (req.getParameter("button1") != null) {
            System.out.println("Button 1 pressed.");
        } else if (req.getParameter("button2") != null) {
            System.out.println("Button 2 pressed.");
        } else {
            // ???
        }
    }
}
