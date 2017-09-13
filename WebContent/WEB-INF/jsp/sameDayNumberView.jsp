<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<head>
<title>sameDayNumberView</title>

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

<form:form method="post" action="processAnalytic.html" modelAttribute="sameDayNumberViewForm">

<fmt:formatDate value="${sameDayNumberViewForm.date}" pattern="dd-MMM" var="d1" />


<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}</h2>

<h2>Yearly Same Day Winning Draws
 for ${d1} - ${fn:length(sameDayNumberViewForm.drawResults)} Matches</h2>


<br/>

<input type="submit" name="cancel_view" value="Back" class="button" style="height:23px;"/>

<c:forEach var="dr" items="${sameDayNumberViewForm.drawResults}" varStatus="status">
<td style="width: 50%;vertical-align:top" valign="top">
<div class="outerbox"><table class="resultTable"  border="1" align="left"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr>
	    <c:choose>
	    <c:when test="${dr.provider eq 'Magnum 4D'}">
		<td class="resultm4dlable" style="width:20%"><img src="img/logo_magnum.gif"/>
	    </c:when>
	    </c:choose>
	    <c:choose>
	    <c:when test="${dr.provider eq 'Da  Ma Cai 1+3D'}">
		<td class="resultm4dlable" style="width:20%"><img src="img/logo_damacai.gif"/>
	    </c:when>
	    </c:choose>
	    <c:choose>
	    <c:when test="${dr.provider eq 'Sports Toto 4D'}">
		<td class="resultm4dlable" style="width:20%"><img src="img/logo_toto.gif"/>
	    </c:when>
	    </c:choose>

	    <td class="resultm4dlable">${dr.provider}</td>
	</tr>
	</table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="5">
	<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${dr.date}" pattern="dd-MMM-yyyy" /></td>
	<td class="resultdrawdate">Draw No: ${dr.drawNo}</td>
	</tr></table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="0">
	<tr><td style="width:100" class="resultprizelable">1st Prize</td>
	<c:choose>
	<c:when test="${sameDayNumberViewForm.external == 'true'}">
		<td class="resulttop">
		<a href ="number_bet.html?method=setNumber&number=${dr.firstPlace}" target="_blank">
		<link rel="xyz" type="text/css" href="color.css">
               			<font color="lime" size="3">
               				${dr.firstPlace}
              			 </font>
               			</link>
               	</a>
		</td>
	    </c:when>
	    <c:otherwise>
		<td class="resulttop">${dr.firstPlace}</td>
	    </c:otherwise>
	    </c:choose>
	</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
	<c:choose>
	<c:when test="${sameDayNumberViewForm.external == 'true'}">
		<td class="resulttop">
		<a href ="number_bet.html?method=setNumber&number=${dr.secondPlace}" target="_blank">
		<link rel="xyz" type="text/css" href="color.css">
               			<font color="magenta" size="3">
               				${dr.secondPlace}
              			 </font>
               			</link>
               	</a>
		</td>
	    </c:when>
	    <c:otherwise>
		<td class="resulttop">${dr.secondPlace}</td>
	    </c:otherwise>
	    </c:choose>
	<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
	<c:choose>
	<c:when test="${sameDayNumberViewForm.external == 'true'}">
		<td class="resulttop">
		<a href ="number_bet.html?method=setNumber&number=${dr.thirdPlace}" target="_blank">
		<link rel="xyz" type="text/css" href="color.css">
               			<font color="yellow" size="3">
               				${dr.thirdPlace}
              			 </font>
               			</link>
               	</a>
		</td>
	    </c:when>
	    <c:otherwise>
		<td class="resulttop">${dr.thirdPlace}</td>
	    </c:otherwise>
	    </c:choose>
	</table></td>
 </table></div>
</c:forEach>

</form:form>

<body>
