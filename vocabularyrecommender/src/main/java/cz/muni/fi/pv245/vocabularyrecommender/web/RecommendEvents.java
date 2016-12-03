package cz.muni.fi.pv245.vocabularyrecommender.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.*;

@WebServlet(urlPatterns = {"/recommendEvents"})
public class RecommendEvents extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    }
    
    public static String readFile(String path) 
        throws IOException 
      {
            InputStream is = new FileInputStream(path);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine(); StringBuilder sb = new StringBuilder();
            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();            
            return fileAsString;
      }

}