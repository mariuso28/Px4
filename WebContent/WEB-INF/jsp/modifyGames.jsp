<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>modifyGames</title>
<style>
th{
text-align:left;
}

body{
font: 18px Arial, sans-serif;
}

</style>
</head>
<body>

<form:form method="post" action="editGame.html" modelAttribute="agentGamesForm">
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Modify Games</h2>
</br>
<h2>Modify Active Games for : ${currAccountUser.role.desc}&nbsp; ${currAccountUser.contact}</h2>
<td>
<a href="processAccount.html?method=cancelModifyGames"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</br>
<h2>Active Games:</h2>
<c:forEach items="${agentGamesForm.gameGroup.gameActivators}" var="mgame" varStatus="status">
	<c:set var="cnt" value="${status.index}" />
	<table border="0" cellpadding="3" cellspacing="0" rowpadding="3" width="500">
	<tbody align="left" style="font-family:verdana; color:Blue;">
	<tr>
		<td>${mgame.metaGame.name}</td>
		<td>${mgame.metaGame.description}</td>

      	</tr>
	</tbody>
	</table>
	</br>
	<table border="1" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td>Bet Type</td>
		<td>Stake</td>
		<td>Prize</td>
	</tr>
	</tbody>
	<tbody align="right" style="font-family:verdana; color:purple">

	<c:forEach items="${mgame.metaGame.games}" var="game" varStatus="status">
	<tr>
		<c:set var="cnt1" value="${status.index}" />
		<td>${game.gtype}</td>

		<td><fmt:formatNumber value="${game.stake}" pattern="#0.00" /></td>

		<td align="right">
		<select name="payOutPath" >
			<c:forEach items="${game.payOuts}" var="payOut" varStatus="status1">
    				<option value="XX"}>
						${payOut.type}
						<fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
				</option>
  			</c:forEach>
		</select>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	</br>
  <c:if test="${currAccountUser.role.rank != 0}">
    <td><a href="processAccount.html?method=deactivateGame&name=${mgame.metaGame.name}"
		    onclick="return confirm('!! DEACTIVATE WILL DEACTIVATE GAME\n\nFOR ALL MEMBERS AND SUBMEMBERS !! \n\nAre you sure?')">Deactivate Game</a></td>
  </c:if>  <c:if test="${currAccountUser.role.rank == 0}">    <td><a href="processAccount.html?method=deactivateGame&name=${mgame.metaGame.name}"        onclick="return confirm('!! DEACTIVATE WILL DEACTIVATE GAME\n\nFOR PLAYER !! \n\nAre you sure?')">Deactivate Game</a></td>  </c:if></c:forEach>

<h2>Available Games:</h2>

<c:forEach items="${agentGamesForm.parentGameGroup.gameActivators}" var="mgame" varStatus="status">
	<table border="0" cellpadding="3" cellspacing="0" rowpadding="3" width="500">
	<tbody align="left" style="font-family:verdana; color:Blue;">
	<tr>
		<td>${mgame.metaGame.name}</td>
		<td>${mgame.metaGame.description}</td>

      	</tr>
	</tbody>
	</table>
	<table border="1" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td width="50%">Bet Type</td>
		<td width="20%">Stake</td>
		<td width="30%">Prize</td>
	</tr>
	</tbody>
	<tbody align="right" style="font-family:verdana; color:purple">
	<c:forEach items="${mgame.metaGame.games}" var="game" varStatus="status">
	<tr>
		<td align="center" width="50%">${game.gtype}</td>

		<td width="20%"><fmt:formatNumber value="${game.stake}" pattern="#0.00" /></td>

		<td width="30%" align="right">
		<select name="payOutPath" >
			<c:forEach items="${game.payOuts}" var="payOut" varStatus="status1">
    				<option value="XX"}>
						${payOut.type}
						<fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
				</option>
  			</c:forEach>
		</select>
	</tr>
	</c:forEach>
	</tbody>
	</table>
<td><a href="processAccount.html?method=useGame&name=${mgame.metaGame.name}">Activate Game</a></td>

</c:forEach>

<br/>
<br/>
</form:form>
</body>
</html>
