<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div>Welcome <c:out value="${details.name}"/></div>
Recommend vocabulary based on:
<br>
<a href="/RecommendEvents"> Events</a>
<br>
<a href="/RecommendFeed"> Feed</a>
<br>
<a href="/RecommendPagesFeed"> Pages feed</a>
</body>
</html>
