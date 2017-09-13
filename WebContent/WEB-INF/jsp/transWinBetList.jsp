<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
    <title>transWinBetList</title>
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
<tr>Bets for Player ${currAccountUser.email} - ${currAccountUser.contact} Transaction: #${winBetListForm.trans.id}
		  Amount: <fmt:formatNumber value="${winBetListForm.trans.amount}" pattern="#0.00" />
		  Draw Date: <fmt:formatDate value="${winBetListForm.trans.date}"
			pattern="dd-MMM-yyyy"/>

		</tr>
</br>
</br>
<tr>
<td>
  <a href="processAccount.html?cancelBetDetails"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
<td><a href="processAccount.html?transactionPDF&transactionId=${winBetListForm.trans.id}" >
    <img src="../../img/pdf.png" width="50" height="30"></a>
</td>
</td>
<td><a href="processAccount.html?transactionPDFEmail&transactionId=${winBetListForm.trans.id}" >
    <img src="../../img/pdfemail.jpeg" width="50" height="30"></a>
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
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td>Game</td>
	<td>Placed</td>
  <td>Play Date</td>
	<td>Total Stake</td>
	<td>Total Win</td>
	</tr>
</table>
<c:set  var="dwin" value="${winBetListForm.displayMetaBetWin}" />
<table>
	<table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
    <col width="120"/>
      <col width="120"/>
      <col width="120"/>
      <col width="100"/>
      <col width="80"/>
	<tr style="font-family:verdana; color:black; background-color:white">
		<td>${dwin.metaName}</td>
		<td><fmt:formatDate value="${dwin.placed}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatDate value="${dwin.metaBet.playGame.playDate}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatNumber value="${dwin.totalStake}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${dwin.totalWin}" pattern="#0.00" /></td>
	</tr>
	</table>


  <div style="float:left; width:100%; font-size: 15px;">
    <table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
      <col width="100"/>
    <col width="100"/>
      <col width="100"/>
      <col width="120"/>
      <col width="120"/>
      <col width="120"/>
      <col width="120"/>
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
  <td>Bet Type</td>
	<td>Number</td>
  <td>Stake</td>
  <td>Provider 1</td>
  <td>Provider 2</td>
  <td>Provider 3</td>
  <td>Provider 4</td>
	</tr>
</table>
</div>
<div style="float:left; width:100%; font-size: 15px;">

    <table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
      <col width="100"/>
    <col width="100"/>
      <col width="100"/>
      <col width="120"/>
      <col width="120"/>
      <col width="120"/>
      <col width="120"/>
	<c:forEach items="${dwin.metaBet.bets}" var="bet" varStatus="status1">
		<tr>
		<td>${bet.game.gtype}</td>
    <td>${bet.choice}</td>
		<td><fmt:formatNumber value="${bet.stake}" pattern="#0.00" /></td>
  	<c:forEach items="${bet.providers}" var="prov" >
        <td>${prov.name}</td>
    </c:forEach>
    <c:if test="${fn:length(bet.providers)<4}">
        <td></td>
    </c:if>
    <c:if test="${fn:length(bet.providers)<3}">
        <td></td>
    </c:if>
    <c:if test="${fn:length(bet.providers)<2}">
        <td></td>
    </c:if>
		</tr>
	</c:forEach>
	</table>
	</div>

  <c:if test="${fn:length(dwin.expandedWins)>0}">
    <div style="float:left; width:100%; font-size: 15px;">
    </br>
      WINS
          <table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
            <col width="100"/>
          <col width="120"/>
            <col width="100"/>
            <col width="100"/>
            <col width="100"/>
            <col width="120"/>
            <col width="120"/>
    <tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Bet Type</td>
		<td>Provider</td>
		<td>Stake</td>
		<td>Choice</td>
		<td>Drawn</td>
		<td>Place</td>
		<td>Win</td
    </tr>
  </table>
  </div>
  <div style="float:left; width:100%; font-size: 15px;">

      <table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
        <col width="100"/>
      <col width="120"/>
        <col width="100"/>
        <col width="100"/>
        <col width="100"/>
        <col width="120"/>
        <col width="120"/>
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
  </table>
  </div>
    </c:if>

</form:form>
</body>
</html>
