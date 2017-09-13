<!DOCTYPE html>
<html>
    
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<head>
<title>Dx4 Example</title>	

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		


</head>
	

<form:form method="post" action="processAlpha.html" modelAttribute="alphaForm">

<body>
</br>
	</br>
	</br>
	<table border="1" cellpadding="0" cellspacing="0" width="600">
	<tr>
	<c:forEach items="${alphaForm.letters}" var="letter" varStatus="status">
	<td><a href="processLetter.html?letter=${letter}">${letter}</a></td>
	</c:forEach>


	</table>
	</br>
	</br>
	</br>
	<table border="1" cellpadding="0" cellspacing="0" width="200">
	<tr><td>WORD : ${alphaForm.word}</td></tr>
	</table>
</body>


</form:form>

</html>
 