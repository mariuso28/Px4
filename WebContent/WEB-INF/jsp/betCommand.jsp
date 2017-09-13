<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
    <title>betCommand</title>

<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}

</style>
</head>
<body>

<form:form method="post" action="processPlayer.html" modelAttribute="betCommandForm">

<h2 style="background-color:${currUser.role.color}">Hey : ${currUser.role.desc}&nbsp;  ${currUser.contact}</h2>

    <tr>
	<th>Games:     <fmt:formatDate value="${betCommandForm.startDate}" pattern="dd-MMM-yyyy"/>    -    <fmt:formatDate value="${betCommandForm.endDate}" pattern="dd-MMM-yyyy"/></th>
    </tr>
    <br/><br/>

<table align="left" style="margin:0; padding:10px; width:800; background: #f8f8f8; color:#000;">

      <c:forEach items="${betCommandForm.currentGames}" var="mgame" varStatus="status">
      <tr>
<!--	<td><a href="create_Bet.html?method=createBets&name=${mgame.name}">Bet ${mgame.name}</a></td> -->
	<td>${mgame.description}</td>
	<td>Next Playing : <fmt:formatDate value="${mgame.playGames[0].playDate}" pattern="dd-MMM-yyyy"/></td>
      </tr>
	<tr>
	<th>Bet Types</th>
        <th>Stake</th>
	<th>Prize</th>
     	</tr>
	<c:forEach items="${mgame.games}" var="game" varStatus="status">
      	<tr>
		<td>${game.gtype.desc}</td>

		<td><fmt:formatNumber value="${game.stake}" pattern="#0" /></td>
		<td align="left">
		<c:choose>
		<c:when test="${fn:length(game.payOuts) == 1}">
			<c:set var="pp" value="${game.payOuts[0]}"/>
			<fmt:formatNumber value="${pp.payOut}" pattern="#0.0" />
		</c:when>
		<c:otherwise>
		<select name="payOutPath" >
			<c:forEach items="${game.payOuts}" var="payOut" varStatus="status1">
    				<option value="XX"}>
						${payOut.type}
						<fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
				</option>
  			</c:forEach>
		</select>
		</c:otherwise>
		</c:choose>
</td>

		</tr>
	</c:forEach>
      </c:forEach>
</table>
<input type="submit" name="cancel_player" value="Back" class="button" style="height:23px;"/>
<c:choose>
<c:when test="${empty betCommandForm.displayMetaBetList}">
<tr></tr>
</c:when>
<c:otherwise>
<br/>
<br/>
<tr>Current Bets:</tr>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<col width="120">
      	<col width="50">
      	<col width="50">
      	<col width="50">
      	<col width="50">
	<tr>
	<th>Game</th>
        <th>Placed</th>
	<th>Type</th>
	<th>Choice</th>
	<th>Stake</th>
	</tr>
	<c:forEach items="${betCommandForm.displayMetaBetList}" var="bet" varStatus="status">
      	<tr>
		<td>${bet.name} - ${bet.description}</td>
		<td><fmt:formatDate value="${bet.placed}" pattern="dd-MMM-yyyy"/></td>
		<td>${bet.gtype}</td>

		<td>${bet.choices}</td>

		<td><fmt:formatNumber value="${bet.stake}" pattern="#0.00" /></td>

	</tr>
	</c:forEach>
</table>
</c:otherwise>
</c:choose>
</form:form>
</body>
</html>
