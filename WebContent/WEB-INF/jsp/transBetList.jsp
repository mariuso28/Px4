<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>invoiceBetList</title>
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

<form:form method="post" action="processPlayer.html" modelAttribute="betListForm">
<tr>Bets for Player ${betListForm.code} - ${currAccountUser.contact} Invoice: #${betListForm.invoice.id}
		  Amount: <fmt:formatNumber value="${betListForm.invoice.dueAmount}" pattern="#0.00" />
		 Due Date: <fmt:formatDate value="${betListForm.invoice.dueDate}"
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
<table table align="left" width:100%;>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td style="width:15%;">Game</td>
        <td style="width:10%;">Placed</td>
	<td style="width:10%;">Draw Date</td>
	<td style="width:10%;">Total Stake</td>
	<td style="width:10%;">Commission</td>
	<td style="width:14%;">Total Stake ExComm</td>
	</tr>
</table>
<div>
<c:forEach items="${betListForm.displayMetaBetList}" var="dispBet" varStatus="status">
<tr>
<table align="left" style="margin:0; padding:0px; width:100%; background: #f8f8f8; color:#000;">
      	<tr>
		<td style="width:15%;"><a href="processAccount.html?method=expandBets&id=${dispBet.metaBet.id}">${dispBet.expanded}${dispBet.metaBet.metaGame.name}</td>
		<td style="width:10%;"><fmt:formatDate value="${dispBet.metaBet.placed}" pattern="dd-MMM-yy"/></td>
		<td style="width:10%;"><fmt:formatDate value="${dispBet.metaBet.playGame.playDate}" pattern="dd-MMM-yy"/></td>
		<td style="width:10%;align:right"><fmt:formatNumber value="${dispBet.totalStake}" pattern="#0.00" /></td>
		<td style="width:10%;align:right"><fmt:formatNumber value="${dispBet.commission}" pattern="#0.00" /></td>
		<td style="width:10%;align:right"><fmt:formatNumber value="${dispBet.totalStakeExComm}" pattern="#0.00" /></td>
	</tr>
</table>
	<c:choose>
	<c:when test="${dispBet.expanded eq '-'}">
	<tr>
	<div style="float:left; width:30%; font-size: 15px;">
	<table>
	<thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
	<tr>
	<th>Bet Type</th>
	<th>Stake</th>
	</tr>


	</thead>
	<tbody align="left" style="font-family:arial; font-size:15px; color:purple; background-color:LightCyan;">
	<c:forEach items="${dispBet.metaBet.bets}" var="bet" varStatus="status1">
		<tr>
		<td>${bet.game.gtype}</td>
		<td><fmt:formatNumber value="${bet.stake}" pattern="#0.00" /></td>
		</tr>
	</c:forEach>
	</tbody>
	</table>
	</div>
	<div style="float:left; width:30%; font-size: 15px;">

	<table>
	<thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
	<tr>
	<th>Providers</th>
	</tr>


	</thead>
	<tbody align="left" style="font-family:verdana; font-size:15px; color:purple; background-color:LightCyan">
	<c:forEach items="${dispBet.providers}" var="provider" varStatus="status1">
      		<tr>
		<td width="100%">${provider}</td>
		</tr>
	</c:forEach>
	</tbody>
	</table>
	</div>
	<div style="float:left; width:30%; font-size: 15px;">

	<table>
	<thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
	<tr>
	<th>Numbers</th>
	</tr>


	</thead>
	<tbody align="left" style="font-family:verdana; font-size:15px; color:purple; background-color:LightCyan">
	<c:forEach items="${dispBet.metaBet.choices}" var="choice" varStatus="status1">
      		<c:choose>
		<c:when test="${status1.index % 5 == 0}">
 			<tr><td align="right">${choice}</td>
		</c:when>
		<c:otherwise>
		<td align="left">${choice}</td>
		</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${status1.index % 5 == 4}">
		</tr>
		</c:when>
		</c:choose>
	</c:forEach>
	</tbody>
	</table>
	</div>
	</tr>
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>
</tr>
<div style="float:left; width:100%;"/>
</c:forEach>

</table>
<div style="float:left; width:100%;">
</p>
<table>

</table>
</div>
</form:form>
</body>
</html>
