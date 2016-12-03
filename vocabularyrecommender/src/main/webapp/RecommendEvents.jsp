<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.web.FBConnection"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.web.RecommendEvents"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
    String code = request.getParameter("code");
    FBConnection fbconn = new FBConnection();
    String tokenString = fbconn.getAccessToken(code);    
    String[] values = tokenString.split("&");
    String[] token = values[0].split("=");    
    FbDataDownloader.downloadFbData(token[1]);
    
%>
<html>
<head>
    <title>Vocabulary by FB events</title>
</head>
<body>
    <h1>Recommended sets of Words</h1>
    
    <%
    String jsonStr = RecommendEvents.readFile("C:/Users/Jirka/Documents/MUNI/Informatika/Mgr/3. semestr/Recommended Systems/PV245-vocabulary_recommender/vocabularyrecommender/tfidf_output.json");
    %>
    
    <%
    JSONArray jsonarray = new JSONArray(jsonStr);
    for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
    %>
    <div class="event">
        <h3><%= jsonobject.getString("event_name") %> </h3>
        <div>
            <ul>
                <%
                String wordsString = jsonobject.getString("words");
                JSONArray words = new JSONArray(wordsString);
                for (int j = 0; j < words.length(); j++) {
                %>
                
                <li> <%= words.getString(j) %> </li>
                
                <%
                }
                %>
            </ul>
        </div>
            <div>
                <form action="Practising?event=<%= jsonobject.getString("event_name") %>" method="GET">
                <button type="submit" href="Practising">Learn!</button>
                </form>
            </div>
    </div>
    
    <%       
    }
    %>
</body>
</html>
