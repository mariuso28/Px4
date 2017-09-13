<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
<title>adminGame</title>

<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}

</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" media="all" href="../../css/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="../..scripts/jsDatePick.min.1.3.js"></script>

<script type="text/javascript">

	var validated = true;

	window.onload = function(){
		new JsDatePick({
			useMode:2,
			target:"playDate",
			dateFormat:"%d-%M-%Y"
			/*selectedDate:{
				day:5,
				month:9,
				year:2006
			},
			yearsRange:[1978,2020],
			limitToToday:false,
			cellColorScheme:"beige",
			dateFormat:"%m-%d-%Y",
			imgPath:"img/",
			weekStartDay:1*/
		});
	};

	 function IsValidDate(myDate) {
                var filter = /^([012]?\d|3[01])-([Jj][Aa][Nn]|[Ff][Ee][bB]|[Mm][Aa][Rr]|[Aa][Pp][Rr]|[Mm][Aa][Yy]|[Jj][Uu][Nn]|[Jj][u]l|[aA][Uu][gG]|[Ss][eE][pP]|[oO][Cc]|[Nn][oO][Vv]|[Dd][Ee][Cc])-(19|20)\d\d$/
                                return filter.test(myDate);
            }
            function test() {

		oFormObject = document.forms['myForm'];

   		var field = "command.playDate";
   		oformElement = oFormObject.elements[field];

		var date= oformElement.value;

                var txtTest = document.getElementById(date);
                var isValid = IsValidDate(date);
                if (isValid) {
                     // alert('Correct format');
			;
                }
                else {
                    alert('Date : ' + date + ' incorrect format, click input box to select correct one');
                }
                return validated=isValid
            };

	    function validate(theForm)
	    {
		return validated;
	    };


</script>


</head>
<body>



<form:form method="post" id="myForm" action="edit_Game.html" modelAttribute="adminGamePlayDateForm" onsubmit="return validate(this)">


<h2>Game : ${currMetaGame.name} - ${currMetaGame.description}</h2>


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
		<td align="center" width="400">${game.gtype}</td>

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


	</br>

<!--
	<table border="0" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td width="40%">Play Date</td>
		<td width="40%">Played At</td>
		<td width="20%">Win Line</td>
	</tr>
	</tbody>

	<tbody align="right" style="font-family:verdana; color:purple">
	<c:forEach items="${currMetaGame.playGames}" var="gameplay" varStatus="status">
      	<tr>
		<td align="center" width="40%"><fmt:formatDate value="${gameplay.playDate}" pattern="dd-MMM-yyyy"/></td>

		<c:choose>
		<c:when test="${gameplay.played == true}">
			<td align="center" width="40%"><fmt:formatDate value="${gameplay.playedAt}" pattern="dd-MMM-yyyy"/></td>

		</c:when>
		</c:choose>
	</tr>
	</c:forEach>
	</tbody>

	</table>
-->


	<table align="left">

	<tr color = "red"><td>${adminGamePlayDateForm.message}</td></tr>

	</br>
	<tr>
		<td><input type="submit" name="cancel" value="Back"/><td>
	</tr>
	</table>

</form:form>
</body>
</html>
