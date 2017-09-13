<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>adminCommand</title>
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

<form:form method="post" action="add_Game.html" modelAttribute="adminCommandForm">

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Current Games</h2>
<table>
<tr height="20">
<td>
		<a href="../adm/processAdmin.html?cancel_admin"><img src="../../img/back.jpg" width="50" height="30"></a>
	</td>
</tr>
<tr height="20">
	<td>&nbsp</td>
</tr>
</table>

<td><a href="../admCmd/add_Game.html?method=addNewGame">Add New Game</a></td>
<!--
<input type="submit" name="../admCmd/addNewGame" value="Add New Game" class="button" style="height:23px;"/>
-->
<br/>
<br/>

<c:forEach items="${adminCommandForm.gameList}" var="mgame" varStatus="status">
	<c:set var="cnt" value="${status.index}"/>
	<table border="0" cellpadding="3" cellspacing="0" rowpadding="3" width="900">
	<tbody align="left" style="font-family:verdana; color:Blue;">
	<tr>
	<!--	<td><a href="edit_Game.html?method=doThis&name=${mgame.name}">${mgame.name}</a></td> -->


		<td align="left" width="200" style="font-family:verdana; color:black">${mgame.name}</td>
		<td align="left" width="400" style="font-family:verdana; color:black">${mgame.description}</td>
		<c:choose>
		<c:when test="${adminCommandForm.activeGames[cnt]=='false'}">
			<td><a href="../admCmd/add_Game.html?method=activate&name=${mgame.name}">Activate</a></td>
		</c:when>
		<c:otherwise>
			<td><a href="../admCmd/add_Game.html?method=deactivate&name=${mgame.name}"
				onclick="return confirm('!! DEACTIVATE WILL DEACTIVATE GAME\n\n(INCLUDING COMMISSIONS) \n\nFOR ALL COMPANIES AND SUBMEMBERS !! \n\nAre you sure?')">Deactivate</a></td>
		</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<td align="left" width="225">Providers:</td>
		<c:forEach items="${mgame.providers}" var="provider" varStatus="status">
		<td align="left" width="225">
		${provider}
		</td>
		</c:forEach>


      	</tr>
	</tbody>
	</table>
	<table border="1" cellpadding="3" cellspacing="0" width="900">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td width="400">Bet Type</td>
		<td width="130">Stake</td>
		<td width="270">Prize</td>
	</tr>
	</tbody>
		<tbody align="right" style="font-family:verdana; color:purple">
		<c:forEach items="${mgame.games}" var="game" varStatus="status">
		<tr>
		<td align="center" width="400">${game.gtype} -
${game.gtype.desc}</td>

		<td width="130"><fmt:formatNumber value="${game.stake}" pattern="#0.00" /></td>
		<td width="270" align="center">
		<select name="payOutPath" style="font-family:verdana; color:purple">
			<c:forEach items="${game.payOuts}" var="payOut" varStatus="status1">
    				<option value="XX"}>
						${payOut.type}
						<fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
				</option>
  			</c:forEach>
		</select>
		</td>
		</tr>
		</c:forEach>
	</tbody>
</table>

<br/>
</c:forEach>


</form:form>
</body>
</html>
