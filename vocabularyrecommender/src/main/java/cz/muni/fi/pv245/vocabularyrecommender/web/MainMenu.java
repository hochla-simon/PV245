package cz.muni.fi.pv245.vocabularyrecommender.web;

import cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/mainMenu"})
public class MainMenu extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String code = "";

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        code = req.getParameter("code");
        if (code == null || code.equals("")) {
            throw new RuntimeException(
                    "ERROR: Didn't get code parameter in callback.");
        }
        FBConnection fbConnection = new FBConnection();
        String accessToken = fbConnection.getAccessToken(code);

        //download and save FB data using 'user access token'
        String accessTokenSplit = accessToken.split("=")[1].split("&")[0];
        FbDataDownloader.downloadFbData(accessTokenSplit);

        //get FB profile data
        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);

        //save the data to the reqest
        Map<String, String> m = new HashMap<>();
        m.put("name", fbProfileData.get("name"));
        req.setAttribute("details", m);

        req.getRequestDispatcher("/MainMenu.jsp").forward(req, res);
    }
}