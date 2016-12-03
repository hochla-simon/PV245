<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div>Welcome <c:out value="${details.name}"/></div>
<a href="/RecommendEvents"> Recommend vocabulary based on events </a>
<br>
<a href="/RecommendFeed"> Recommend vocabulary based onfeed </a>
<br>
<a href="/RecommendPagesFeed"> Recommend vocabulary based on pages feed </a>
</body>
</html>
