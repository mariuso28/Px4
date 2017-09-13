<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>accDetails</title>
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

<form:form method="post" action="processPlayer.html" modelAttribute="transactionForm">

<h2 style="background-color:${transactionForm.role.color}">${transactionForm.role.desc} ${transactionForm.contact}
	-  Account Details</h2>
<tr></tr>
<tr>
<td>
<a href="processAccount.html?method=cancelAccDetails"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</tr>

<div style="height:300px;">
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Id</td>
		<td>Date</td>
		<td>Type</td>
    <td>User</td>
		<td>C/P</td>
		<td align="left">Amnt</td>
    <td></td>
	</tr>
	<c:forEach items="${transactionForm.txList}" var="tx" varStatus="status">
	<tr>
		<td>#${tx.transX.id}</td>
		<td><fmt:formatDate value="${tx.transX.date}" pattern="dd-MMM-yy HH:mm:ss" /></td>
		<td>${tx.transX.type}</td>
		<td>${tx.userEmail}</td>
    <td>${tx.cpEmail}</td>
		<td align="left"><fmt:formatNumber value="${tx.transX.amount}" pattern="#0.00" /></td>
    <c:if test="${tx.transX.type == 'Pay' || tx.transX.type == 'Collect'}">
      <td><a href="processAccount.html?method=betDetails&transactionId=${tx.transX.id}" >
      <link rel="xyz" type="text/css" href="color.css">
        <font color="DarkMagenta" size="3">
                    Details
                     </font>
                    </link>
      </a></td>
    </c:if>
	</tr>
	</c:forEach>

</table>
</div>

</br>
<tr>
<c:choose>
<c:when test="${transactionForm.currentPage>1}">
<td><a href="processAccount.html?method=accDetailsLast"><img src="../../img/monthBackward_normal.gif"/></a></td>
</c:when>
</c:choose>
<c:choose>
<c:when test="${transactionForm.currentPage<transactionForm.lastPage}">
<td><a href="processAccount.html?method=accDetailsNext"><img src="../../img/monthForward_normal.gif"/></a></td>
</c:when>
</c:choose>
</tr>
</br>

</form:form>
</body>
</html>
