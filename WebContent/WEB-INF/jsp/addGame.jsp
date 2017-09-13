<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
<title>addGame</title>
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

<script type="text/javascript">

function submitRefreshPrizes(){

    oFormObject = document.forms['myForm'];

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshPrizes';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

</script>

<form:form method="post" id="myForm" action="save_Game.html" modelAttribute="adminAddGameForm">

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Add Game</h2>

<table width="800px">

<tr>
<td align="left" width="130px"><font color="black" size="4">
Name :
</td>
<td>
<input type="text" maxlength="30" name="command.metaGame.name" value="${currMetaGame.name}" /><br/>
</td>
</br>
<td align="left" width="130px"><font color="black" size="4">
Description :
</td>
<td>
<input type="text" maxlength="60" name="command.metaGame.description" value="${currMetaGame.description}" /><br/>
</td>
</td>
</tr>
<tr>
</table>
<td>Available Draw Providers:</td>
</tr>
<tr>
<c:forEach items="${adminAddGameForm.providers}" var="provider" varStatus="status">
<td width="200">
${provider}
</td>

<td>
<c:set var="cnt" value="${status.index}" />
<c:set var="bool" value = "${adminAddGameForm.useProviders[cnt]}" />
<c:choose>

<c:when test="${bool == false}">
	<input type="checkbox" size="8" name="command.useProviders[${status.index}]" value="${provider}" />
</c:when>
<c:otherwise>
<input type="checkbox" size="8" name="command.useProviders[${status.index}]" value="${provider}" checked/>
</c:otherwise>
</c:choose>

</td>

</c:forEach>
</tr>
</br>
<tr><td align="left" width="200px"><font color="red" size="3">${adminAddGameForm.message}</td></tr>

</br>
<tr><td align="left"><font color="blue" size="5">Add Game Bet</td></tr>
</br>
</br>

<table border="1" cellpadding="3" cellspacing="0" width="900">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td width="400">Bet Type</td>
		<td width="130">Stake</td>
		<td width="100">Multi Bet</td>
		<td width="270">Prize</td>
	</tr>
	</tbody>
		<tbody align="right" style="font-family:verdana; color:purple">
		<c:forEach items="${currMetaGame.games}" var="game" varStatus="status">
		<tr>
		<td align="center" width="400">${game.gtype} -
${game.gtype.desc}</td>

		<td width="130"><fmt:formatNumber value="${game.stake}" pattern="#0.00" /></td>

		<td width="270" align="center">
		<select name="payOutPath" >
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

<table border="0" cellpadding="3" cellspacing="0" width="900">
	<tbody align="left" style="font-family:verdana; color:purple">
	<tr>
	<td width="400">
		<form:select path="command.metaGame.games[0].gtype" onChange="submitRefreshPrizes()">
		    <c:forEach items="${gameTypeList}" var="gType">
    				<option value="${gType}" ${gType.index == adminAddGameForm.gameType.index ? 'selected' : ''}>
						${gType} -
${gType.desc}</option>
  		    </c:forEach>
		</form:select>
	</td>
	<td align="center" width="130">
		<form:select path="command.metaGame.games[0].stake" >
			<c:forEach items="${stakeList}" var="stake">
    				<option value="${stake}" ${stake == stake0 ? 'selected' : ''}>
						<fmt:formatNumber value="${stake}" pattern="#0.00" /></option>
  			</c:forEach>
		</form:select>
	</td>


	
	<td align="right" width="270">

<table>
		<c:forEach items="${payOutList}" var="payOut" varStatus="status1">
      		<tr>
		<input type="hidden" name="command.metaGame.games[0].payOuts[${status1.index}].type"
value="${payOut.type}
" />

		<td>${payOut.type}
</td>

		<td>
		<input name="command.metaGame.games[0].payOuts[${status1.index}].payOut"
			type="text" value="<fmt:formatNumber value="${payOut.payOut}
" pattern="#0.00" />"/>

<!--
 onkeyup="this.value=fFormatDecimal(fParseFloat(this.value),0);" onkeypress="if (event.keyCode &lt; 48 || event.keyCode &gt; 57 || event.keyCode == 13) { if (event.keyCode == 13) event.returnValue=true; else event.returnValue = false; }"
-->

		</td>
		</tr>
		</c:forEach>
	</table>
	</td>
	</tr>
	</tbody>
<tr>
<td>
<jsp:include page="_gtypes/${adminAddGameForm.gameType}.htm"/>
</td>


</tr>
</table>

<table width="60%">
<tr>
<td colspan="2">
<input type="submit" name="addGameBet" value="Add Bet" />
</td>
</tr>
</table>
<table width="60%">
<tr>
</tr>
<tr><td colspan="2">&nbsp;</td></tr>
<td colspan="2">
<input type="submit" name="addGame" value="ADD GAME" class="button" style="height:23px;"/>
<td colspan="2">
<input type="submit" name="cancel" value="CANCEL" class="button" style="height:23px;"/>
</table>

</form:form>

</body>
</html>
