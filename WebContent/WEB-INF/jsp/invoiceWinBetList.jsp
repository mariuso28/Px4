<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Dx4 invoiceWinBetList</title>
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


<form:form method="post" action="processPlayer.html" modelAttribute="winBetListForm">
<tr>Wins for Player ${winBetListForm.code} - ${currAccountUser.contact} Invoice: #${winBetListForm.invoice.id} 
		  Amount: <fmt:formatNumber value="${winBetListForm.invoice.amount}" pattern="#0.00" />
		 Due Date: <fmt:formatDate value="${winBetListForm.invoice.dueDate}"
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
<table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
	<col width="120"/>
    <col width="120"/>
    <col width="120"/>
    <col width="100"/>
    <col width="80"/>
	<col width="130"/>
	<col width="100"/>
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td>Game</td>
	<td>Placed</td>
    <td>Played On</td>
	<td>Total Stake</td>
	<td>Commission</td>
	<td>Total Stake ExComm</td>
	<td>Total Win</td>
	</tr>
</table>
<c:forEach items="${winBetListForm.displayMetaBetWinList}" var="dwin" varStatus="status">
<table>
	<table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
	<col width="120"/>
    <col width="120"/>
    <col width="120"/>
    <col width="100"/>
    <col width="80"/>
	<col width="130"/>
	<col width="100"/>
	<tr style="font-family:verdana; color:black; background-color:white">

		<td><a href="processAccount.html?method=expandWinBets&id=${dwin.metaId}">${dwin.expanded}${dwin.metaName}</a></td>
		<td><fmt:formatDate value="${dwin.placed}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatDate value="${dwin.playedAt}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatNumber value="${dwin.totalStake}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${dwin.commission}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${dwin.totalStakeExComm}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${dwin.totalWin}" pattern="#0.00" /></td>
	</tr>
	</table>

	<c:choose>
		<c:when test="${dwin.expanded eq '-'}">
		<table style="width:100%" cellspacing="0">
		<tbody style="width:100%;font-family:arial; font-size:15px; color:purple; background-color:LightCyan;">
		<tr style="color:grey; background-color:white;">
		<td>Bet Type</td>
		<td>Provider</td>
		<td>Stake</td>
		<td>Choice</td>
		<td>Drawn</td>
		<td>Place</td>
		<td>Win</td
		</tr>
		<c:forEach items="${dwin.expandedWins}" var="ewin" varStatus="status1">
      		<tr>
			<td>${ewin.game}
</td>
			<td>${ewin.provider}
</td>
			<td><fmt:formatNumber value="${ewin.stake}" pattern="#0.00" /></td>
			<td>${ewin.choice}
</td>
			<td>${ewin.number}
</td>
			<td>${ewin.place}
</td>

			<td><fmt:formatNumber value="${ewin.win}" pattern="#0.00" /></td>
		</tr>
		</c:forEach>
		</tbody>
		</table>
		</c:when>
	</c:choose>
	</table>
	</c:forEach>

</br>

</form:form>
</body>
</html>
