package cz.muni.fi.pv245.vocabularyrecommender.web;

import cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainMenu extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String code = "";

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        code = req.getParameter("code");
        if (code == null || code.equals("")) {
            throw new RuntimeException(
                    "ERROR: Didn't get code parameter in callback.");
        }
        FBConnection fbConnection = new FBConnection();
        String accessToken = fbConnection.getAccessToken(code);

        String accessTokenSplit = accessToken.split("=")[1].split("&")[0];

        FbDataDownloader.downloadFbData(accessTokenSplit);
//        FBGraph fbGraph = new FBGraph(accessToken);
//        String graph = fbGraph.getFBGraph();
//        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
//        ServletOutputStream out = res.getOutputStream();
//        out.println("<h1>Facebook Login using Java</h1>");
//        out.println("<h2>Application Main Menu</h2>");
//        out.println("<div>Welcome "+fbProfileData.get("name"));
    }

}