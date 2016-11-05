package cz.muni.fi.pv245.vocabularyrecommender.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader;

/**
 * Created by simon on 28. 10. 2016.
 */
@WebListener
public class StartListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        System.out.println("aplikace inicializovana");
        ServletContext servletContext = ev.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        System.out.println("aplikace konci");
    }
}