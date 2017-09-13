<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>playerFavourites</title>

<style>
th{
text-align:center;
}

body{
font: 20px Arial, sans-serif;
}

</style>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Set Dates</title>

<link rel="stylesheet" type="text/css" media="all" href="css/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="scripts/jsDatePick.min.1.3.js"></script>

<script type="text/javascript">
	window.onload = function(){
		new JsDatePick({
			useMode:2,
			target:"alertDate",
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


</script>
</head>

<body>

<!--

<script type="text/javascript">

function doValidate()
{
	alert("VALIDATE");
	oFormObject = document.forms['myForm'];

  	var field = "command.favourite.alertDate";


   	oformElement = oFormObject.elements[field];

	if (!oformElement.value || 0 == oformElement.value.length)
	{
	    alert("EMPTY:" + oformElement.value + "<<");
	    oformElement.value = "07-NOV-2013";

	}

	return false;
}

</script>

-->


<form:form method="post" id="myForm" action="processPlayer.html" modelAttribute="favouritesForm">
<h2>Hi : ${currUser.contact} - Edit Favourites:</h2>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
<br/>
</table>
<br/>
<br/>
<td>Favourites:</td>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<th>Choice</th>
	<th>Comment</th>
	<th>Alert Date</th>
	<th>Repeat Year</th>
	<c:forEach items="${currUser.favourites}" var="favourite" varStatus="status">
      	<tr>
		<td>${favourite.choice}</td>
		<td>${favourite.comment}</td>

		<td><fmt:formatDate value="${favourite.alertDate}" pattern="dd-MMM-yyyy"/></a></td>
		<td>${favourite.repeatYear}</td>
		<td><a href="processPlayer.html?cancelFavourite&index=${status.index}">Remove</a></td>
	</tr>
	</c:forEach>
	<br/>
	<br/>
	<tr>
		<td>Number/Date etc.<input type="text" size="12" name="command.favourite.choice" /></td>
		<td>Why favourite?<input type="text" size="26" name="command.favourite.comment" /></td>
		<td><input type="text" size="12" id="alertDate" name="command.favourite.alertDate" /></td>
		<td><input type="checkbox" size="8" name="command.favourite.repeatYear"/></td>
		<td><input type="submit" name="addFavourite" value="Add" /><td>
	</tr>
</table>
</form:form>
<td><a href="processPlayer.html?cancel">Back To Profile</a></td>
</body>
</html>
