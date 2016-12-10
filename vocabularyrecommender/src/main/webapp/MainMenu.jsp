<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="includes/header.jsp" %>
<div class="jumbotron">
  <h1>Welcome ${details.name}</h1>
  <p>We have downloaded your FB data to get vocabulary speicaly tailored to your needs. You can gat words based on your events, feeds or pages you like.</p>
  <div class="text-center">
    <div class="btn-group btn-group-lg" role="group">
      <a href="RecommendEvents" role="button" class="btn btn-default">Events</a>
      <a href="RecommendFeed" role="button" class="btn btn-default">Feed</a>
      <a href="RecommendPagesFeed" role="button" class="btn btn-default">Pages feed</a>
    </div> 
  </div>
</div>
<%@include file="includes/footer.jsp" %>
