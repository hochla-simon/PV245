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

<%@include file="includes/header.jsp" %>
<h1>Words based on your FB Events</h1>

<%
    ServletContext ctx = getServletContext();    
    //String jsonStr = TfIdf.getTfidf(3, "events.json", ctx.getResource("/WEB-INF/tfidf.rb").getFile() );
    //takto to funguje mne ( Jiri :-) )
    String jsonStr = TfIdf.getTfidf(3, "events.json", ctx.getRealPath("/WEB-INF/tfidf.rb") );
//    String jsonStr = Keywords.getKeywords(3, "events.json", ctx.getResource("/WEB-INF/keywords.rb").getFile() );
%>
<div class="row">
<%
    JSONArray jsonarray = new JSONArray(jsonStr);
    Map<Integer, List<String>>  mapOfWords = new HashMap<Integer, List<String>>();
    for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        if (i % 3 == 0 && i != 0) {
            %>
            </div>            
            <div class="row">
            <%
        }
%>
    <div class="col-md-4">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title"><%= jsonobject.getString("event_name") %> </h3>
            </div>
            <div class="panel-body">
                <ul class="list-group">
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

                    <li class="list-group-item"> <%= words.getString(j) %> </li>

                    <%
                        }
                    %>
                    <li class="list-group-item"> ... etc. </li>
                </ul>
            </div>
            <div class="panel-footer text-center">
                <form action="Practising?item=<%=i%>" method="POST">
                    <button type="submit" class="button">Learn!</button>
                </form>
            </div>
        </div>
    </div>

<%
        request.getSession().setAttribute("wordsMap", mapOfWords);
    }
%>
</div>
<%@include file="includes/footer.jsp" %>
