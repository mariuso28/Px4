<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<head>
<title>adminGameSavePlayed</title>

<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}

</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" media="all" href="css/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="scripts/jsDatePick.min.1.3.js"></script>

<script type="text/javascript">
	var validated = true;

	window.onload = function(){
		new JsDatePick({
			useMode:2,
			target:"playDate",
			dateFormat:"%d-%M-%Y"
			/*selectedDate:{				This is an example of what the full configuration offers.
				day:5,						For full documentation about these settings please see the full version of the code.
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
                   //  alert('Correct format');
			;
                }
                else {
                    alert('Date : ' + date + ' incorrect format, click input box to select correct one');
                }
                return validated=isValid
            }

	    function validate(theForm)
	    {
		return validated;
	    }
</script>


</head>
<body>

<form:form method="post" action="edit_Game.html" id="myForm" modelAttribute="adminGamePlayDateForm" onsubmit="return validate(this)">


<h2>Game : ${currMetaGame.name} - ${currMetaGame.description}</h2>


	<table border="1" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td width="50%">Bet Type</td>
		<td width="20%">Stake</td>
	</tr>
	</tbody>
	<tbody align="right" style="font-family:verdana; color:purple">
	<c:forEach items="${currMetaGame.games}" var="game" varStatus="status">
	<tr>
		<td align="center" width="50%">${game.gtype}</td>

		<td width="20%"><fmt:formatNumber value="${game.stake}" pattern="#0.00" /></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	<br/>
	<br/>
	<table border="0" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:MidnightBlue">
		<tr><td width="100%">Save Game Results for: ${currMetaGame.name}</td>
</td>
		<tr><td width="100%">Scheduled Play Date : <fmt:formatDate value="${adminGamePlayDateForm.playGame.playDate}" pattern="dd-MMM-yyyy"/></td>

	</tbody>
	</table>
	<br/>
	<br/>
	<table border="1" cellpadding="3" cellspacing="0" width="500">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	</tr>
	<tr>
		<td width="30%">Play Date</td>



		<td width="50%">Draw</td>
	</tr>
	</tr>
	<c:forEach items="${currXGR.draws}" var="result" varStatus="status">
	<tr>
		<td width="30%"><fmt:formatDate value="${result.date}" pattern="dd-MMM-yyyy"/></td>

		<td width="50%"><a href="edit_Game.html?method=useGame&params=${adminGamePlayDateForm.playGame.id}:${result.provider}">Use ${result.provider}</a></td>
	</tr>
	</c:forEach>
	<tr>
		<td width="30%"><fmt:formatDate value="${adminGamePlayDateForm.playGame.playDate}" pattern="dd-MMM-yyyy"/></td>

		<td width="50%"><a href="edit_Game.html?method=useGame&params=${adminGamePlayDateForm.playGame.id}:Dx4">Use Dx4</a></td>
	</tr>
	</tbody>
	</table>
	</br>
	<tr color = "red"><td>${adminGamePlayDateForm.message}</td></tr>
	</br>
	<input type="submit" name="cancel" value="Cancel"/><td>

</form:form>


</body>
</html>
