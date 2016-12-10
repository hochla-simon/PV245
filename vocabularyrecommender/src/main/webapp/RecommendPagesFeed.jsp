<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.Keywords"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.TfIdf"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.FbDataDownloader"%>
<%@page import="cz.muni.fi.pv245.vocabularyrecommender.data.Dictionary"%>
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
<h1>Words based on your FB liked pages</h1>

<%
    ServletContext ctx = getServletContext();    
    //String jsonStr = TfIdf.getTfidf(3, "events.json", ctx.getResource("/WEB-INF/tfidf.rb").getFile() );
    //takto to funguje mne ( Jiri :-) )
    String jsonStr = TfIdf.getTfidf(3, "pagesfeed.json", ctx.getRealPath("/WEB-INF/tfidf.rb") );
//    String jsonStr = Keywords.getKeywords(3, "events.json", ctx.getResource("/WEB-INF/keywords.rb").getFile() );
    HashMap<String, HashMap<String, HashMap<String, String>>> result = new HashMap();
    result = Dictionary.getFinalVocabularyFromString(jsonStr, 2);    
%>
<div class="row">
<%    
    Map<Integer, HashMap<String, HashMap<String, String>>>  mapOfWords = new HashMap<Integer, HashMap<String, HashMap<String, String>>>();
    Integer i = 0;
    for (Map.Entry<String, HashMap<String, HashMap<String, String>>> entry : result.entrySet()) {        
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
                <h3 class="panel-title"><%= entry.getKey() %> </h3>
            </div>
            <div class="panel-body">
                <ul class="list-group">
                    <%
                        HashMap<String, HashMap<String, String>> wordSets = entry.getValue();
                        mapOfWords.put(i, wordSets);
                        for (Map.Entry<String, HashMap<String, String>> wordSetEntry : wordSets.entrySet()) {
                        
                    %>

                    <li class="list-group-item"> <%= wordSetEntry.getKey() %> </li>

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
        i++;
    }
    request.getSession().setAttribute("wordsMap", mapOfWords);
%>
</div>
<%@include file="includes/footer.jsp" %>
