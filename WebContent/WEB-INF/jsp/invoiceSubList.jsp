<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Dx4 invoiceSubList</title>
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

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;  ${currUser.contact}</h2>


<form:form method="post" action="processPlayer.html" modelAttribute="invoiceListForm">
<tr>Member Invoices for ${invoiceListForm.role.desc} ${invoiceListForm.code} - ${currAccountUser.contact} Invoice: #${invoiceListForm.invoice.id}
		  Amount: <fmt:formatNumber value="${invoiceListForm.invoice.dueAmount}" pattern="#0.00" />
		 Due Date: <fmt:formatDate value="${invoiceListForm.invoice.dueDate}"
			pattern="dd-MMM-yyyy"/>

		</tr>
</br>
</br>
<tr>
<td>
<a href="processAccount.html?cancelInvoiceDetails"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</tr>
</br>
</br>
<div style="height:400px; overflow:auto">
<table border="0" style="width:100%;" align="left">
	    <colgroup>
		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 12%;">
	   </colgroup>
	    <tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Invoice</td>
		<td>Member</td>
		<td>Type</td>
		<td>Issue Date</td>
		<td>Due Date</td>
		<td>Amount</td>
		<td>Commission</td>
		<td>Due Amount</td>
	    </tr>
	   <c:forEach items="${invoiceListForm.displayList}" var="dInvoice" varStatus="status">
      	   <tr>
		<c:set var="invoice" value="${dInvoice.invoice}" />
		<c:set var="iId" value="${invoice.id}" />
		<td><a href="processAccount.html?invoiceDetails&invoiceId=${iId}">#${invoice.id}</td>
		<td>${dInvoice.userCode}</td>
		<td>${dInvoice.role.shortCode}</td>
		<td><fmt:formatDate value="${invoice.date}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatDate value="${invoice.dueDate}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatNumber value="${invoice.amount}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${invoice.commission}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${invoice.dueAmount}" pattern="#0.00" /></td>
	    </tr>
	    </c:forEach>
</table>
</div>
</br>
</br>

</form:form>
</body>
</html>
