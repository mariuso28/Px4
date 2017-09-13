<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<html>
<head>
    <title>Dx4 Error</title>
<style>
th{
text-align:left;
}

body{
font: 14px Arial, sans-serif;
}

</style>
</head>
<body>
<h2>Dx4 System Error</h2>
</br>
<c:forEach items="${msgs}" var="msg">
   	<tr><td>${msg}</td></tr>
</c:forEach>


</body>
</html>