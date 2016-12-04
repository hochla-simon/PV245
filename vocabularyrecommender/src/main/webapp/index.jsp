<%@page import="cz.muni.fi.pv245.vocabularyrecommender.web.FBConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%
    FBConnection fbConnection = new FBConnection();
%>
<%@include file="includes/header.jsp" %>
<div class="jumbotron">
  <h1>Welcome to Vocabulary recommender</h1>
  <p>Customized way to learn vocabulary. You are provided new words based on the content of your FB account.</p>
  <p><a href="<%=fbConnection.getFBAuthUrl()%>"> <img src="./img/facebookloginbutton.png" />
    </a></p>
</div>
<%@include file="includes/footer.jsp" %>