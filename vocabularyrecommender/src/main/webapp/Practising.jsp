<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script language="javascript">
    function showDiv() {
        document.getElementById('welcomeDiv').style.display = "block";
    }
</script>

<div>What is the meaning of word <b>${word}</b>?</div>

<div id="welcomeDiv" style="display:none;" class="answer_list">${meaning}</div>
<input type="button" name="answer" value="Show meaning" onclick="javascript:showDiv();"/>

<form action="${pageContext.request.contextPath}/practising" method="post">
    <input type="submit" name="button1" value="Yes, I knew"/>
    <input type="submit" name="button2" value="Oops, I was wrong"/>
</form>

</body>
</html>