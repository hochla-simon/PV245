<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.Keywords"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.TfIdf"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.web.FBConnection"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.web.RecommendEvents"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Vocabulary by FB events</title>
</head>
<body>
<h1>Recommended sets of Words</h1>

<%
    ServletContext ctx = getServletContext();
    String jsonStr = TfIdf.getTfidf(3, "events.json", ctx.getResource("/WEB-INF/tfidf.rb").getFile() );
//    String jsonStr = Keywords.getKeywords(3, "events.json", ctx.getResource("/WEB-INF/keywords.rb").getPath() );
%>

<%
    JSONArray jsonarray = new JSONArray(jsonStr);
    Map<Integer, List<String>>  mapOfWords = new HashMap<Integer, List<String>>();
    for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
%>
<div class="event">
    <h3><%= jsonobject.getString("event_name") %> </h3>
    <div>
        <ul>
            <%
                JSONArray words = jsonobject.getJSONArray("words");
                ArrayList<String> wordsList = new ArrayList<String>();
                if (words != null) {
                    for (int k=0;k<words.length();k++){
                        wordsList.add(words.get(k).toString());
                    }
                }
                mapOfWords.put(i, wordsList);

                for (int j = 0; j < words.length(); j++) {
            %>

            <li> <%= words.getString(j) %> </li>

            <%
                }
            %>
        </ul>
    </div>
    <div>
        <form action="Practising?item=<%=i%>" method="POST">
            <button type="submit" href="Practising">Learn!</button>
        </form>
    </div>
</div>
<%
        request.getSession().setAttribute("wordsMap", mapOfWords);
    }
%>
</body>
</html>
