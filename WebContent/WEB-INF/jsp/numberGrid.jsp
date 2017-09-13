<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<head>
<title>numberGrid</title>

<style>
th{
text-align:center;
}

body{
font: 20px Arial, sans-serif;
}

</style>
</head>

<body>

<form:form method="post" action="NEEDS TO BE SORTED.html" modelAttribute="numberGridForm">


<h2>Numbers</h2>

<table>
    <tbody>
        <c:forEach var="row" items="${numberGridForm.numberGrid.numbers}" varStatus="status">
	<tr>
	    <c:forEach var="column" items="${row}" varStatus="status1">

		<td><a href="number_bet.html?method=setNumber&number=${column.number}">${column.number}</td>
   <!--             <td>${column.number}</td> -->
	    </c:forEach>
	</tr>
        </c:forEach>
     </tbody>
</table>

</form:form>

<body>
