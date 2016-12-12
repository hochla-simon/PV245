<%@ page import="org.json.JSONArray" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Practise the vocabulary</title>
</head>
<%@include file="includes/header.jsp" %>
<body>
<script language="javascript">
    function showDiv() {
        document.getElementById('welcomeDiv').style.display = "block";
    }
</script>

  <div class="jumbotron">

    <h4>
        <%
        String word = (String)session.getAttribute("word");
        int wordLength = word.length();
        if (wordLength < 4) {
        %>
        <div style="color:green;display:inline;">BEGINNER</div> EXPERIENCED MASTER
        <%
        } else if (wordLength < 8) {
        %>
        BEGINNER <div style="color:green;display:inline;">EXPERIENCED</div>  MASTER
        <%   
        } else {
        %>
        BEGINNER EXPERIENCED <div style="color:green;display:inline;">MASTER</div>
        <%
        }
        %>
    </h4>
        
    <h3>
        <div>What is the meaning of word <b style="font-size:30px   ">${word}</b>?</div>
    </h3>

    <div id="welcomeDiv" style="display:none;background-color:whitesmoke;" class="answer_list">${meaning}</div>
    <input type="button" class="btn btn-default" name="answer" value="Show meaning" onclick="javascript:showDiv();"/>

   <p>&nbsp;</p>

    <form action="${pageContext.request.contextPath}/practising" method="post">
        <input type="submit" name="Yes, I knew" value="Yes, I knew" class="btn btn-success"/>
        <input type="submit" name="Oops, I was wrong" value="Oops, I was wrong" class="btn btn-warning"/>
        <p/>
        <div>
            <input type="submit" name="Finish practising" value="Finish practising and return back to the selection page" class="btn btn-link"/>
        </div>
    </form>
    
  </div>
</body>
</html>
