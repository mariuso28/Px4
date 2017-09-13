<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>rollup</title>
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


<form:form method="post" action="processPlayer.html" modelAttribute="rollupForm">
<h2 style="background-color:${rollupForm.role.color}">${rollupForm.role.desc}&nbsp; ${rollupForm.contact} - Account Summary</h2>

<tr>
<td>
<a href="processAccount.html?method=cancelRollup"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</br>
</br>
<c:choose>
<c:when test="${rollupForm.role.rank < 5}">
	<tr>
	<td>
		<font color="green" size="3">
		Total Outstanding Invoices Owed:
		<fmt:formatNumber value="${rollupForm.outstandingInvoicesTotal}" pattern="#0.00"
		/>
       		by ${rollupForm.parentRole} - ${rollupForm.parentContact}
	</td>
	</tr>


</br>

   	<tr height="400px" valign="top" align="left">
       	<td>
       	<div style="height:400px; overflow:auto">
       	<table border="0" style="width:100%;" align="left">
	    <colgroup>
		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 12%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
    	    </colgroup>
	    <tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Invoice</td>
		<td>Issue Date</td>
		<td>Due Date</td>
		<td align="right">Amount</td>
		<td align="right">Commission</td>
		<td align="right">Due Amount</td>
		<td align="right">		<a href="processAccount.html?invoiceIssuerPDF" target="_blank">View PDF</td>
		<c:choose>
		<c:when test="${(rollupForm.parentEmail!=null) && (fn:length(rollupForm.parentEmail)>0)}">		<td align="right">			<a href="processAccount.html?invoiceEmailIssuerPDF">Email PDF
		</td>
		</c:when>
		</c:choose>		<td></td>
		</tr>
	    <c:forEach items="${rollupForm.outstandingInvoices}" var="invoice" varStatus="status">
	    <tr>
		<c:set var="cnt" value="${status.index}" />
		<td>#${invoice.id}</td>
		<td><fmt:formatDate value="${invoice.date}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatDate value="${invoice.dueDate}" pattern="dd-MMM-yy"/></td>
		<td align="right"><fmt:formatNumber value="${invoice.amount}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${invoice.commission}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${invoice.dueAmount}" pattern="#0.00" /></td>
	    </tr>
	    </c:forEach>
	</table>
<tr><td><font color="red" size="3">${rollupForm.message}</td></tr>
<tr><td><font color="blue" size="3">${rollupForm.info}</td></tr>

</c:when>
</c:choose>



<div style="height:30px;">
<tr>Account Summary
</tr>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Code</td>
		<td>Type</td>
		<td align="right">Paid In</td>
		<td align="right">Pay Out</td>
		<td align="right">Deposits</td>
		<td align="right">Withdrawls</td>
		<td align="right">Balance</td>
		<td align="right">Owed</td>
		<td align="right">Owing</td>
		<td align="right">Net Owed</td>
	</tr>
	<tr>
		<td>${rollupForm.rollup.code}</td>
		<td>${rollupForm.rollup.role}</td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.paidIn}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.paidOut}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.deposit}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.withdrawl}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.total}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.owed}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.owing}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.totalOwed}" pattern="#0.00" /></td>
		<td align="right"><a href="processAccount.html?method=accDetails&code=${rollupForm.rollup.code}">
			Details</a></td>
		</tr>
</table>
</div>
</br>
<div style="height:300px; overflow:auto; float:left">
<tr>Member Account Summary</tr>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">

	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Code</td>
		<td>Type</td>
		<td align="right">Paid In</td>
		<td align="right">Pay Out</td>
		<td align="right">Deposits</td>
		<td align="right">Withdrawls</td>
		<td align="right">Balance</td>
		<td align="right">Owed</td>
		<td align="right">Owing</td>
		<td align="right">Net Owed</td>
	</tr>
	<c:forEach items="${rollupForm.memberRollups}" var="rollup" varStatus="status">
	<tr>
		<c:choose>
		<c:when test="${rollup.role == 'Player'}">
			<td>${rollup.code}</td>
		</c:when>
		<c:otherwise>
			<td><a href="processAccount.html?method=subRollup&code=${rollup.code}">${rollup.code}</a></td>
		</c:otherwise>
		</c:choose>
		<td>${rollup.role}</td>
		<td align="right"><fmt:formatNumber value="${rollup.paidIn}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.paidOut}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.deposit}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.withdrawl}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.total}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.owed}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.owing}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${rollup.totalOwed}" pattern="#0.00" /></td>
		<c:choose>
		<c:when test="${rollup.role != 'Player'}">
			<td align="right"><a href="processAccount.html?method=subRollup&code=${rollup.code}">
			Details</a></td>
		</c:when>
		</c:choose>
	</tr>
	</c:forEach>
</table>
</div>

</br>
</br>

</form:form>
</body>
</html>
