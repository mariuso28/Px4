<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<title>drawGameResult</title>

<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}

</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
<body>

<form:form method="post" action="edit_Game.html" modelAttribute="drawResultsForm">
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}</h2>

<h2>Draw Results for <fmt:formatDate value="${currXGR.draws[0].date}"/></h2>

<tr>
<td>
		<a href="../adm/processAdmin.html?return_admin"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</tr>

<input name="result.provider" type="hidden" value="${drawResultsForm.init.provider.name}"/>
<input name="result.drawNo" type="hidden" value="${drawResultsForm.init.drawNo}"/>
<input name="result.date" type="hidden" value="${drawResultsForm.init.date}"/>

<table align="center" cellpadding="0" cellspacing="20">
<tr valign="top">
<td style="width: 50%;vertical-align:top" valign="top">
<div class="outerbox"><table class="resultTable" border="1" align="center">
<tr><td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%">
	<img width="70" height="70" src="data:image/png;base64,${currXGR.draws[0].provider.image}" alt="No image" border='0'>
</td><td class="resultm4dlable">${currXGR.draws[0].provider.name}</td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[0].date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${currXGR.draws[0].drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">

<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:100" class="resultprizelable">1st Prize</td>
<td class="resulttop">${currXGR.draws[0].firstPlace}</td>
</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">${currXGR.draws[0].secondPlace}</td></tr>
<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">${currXGR.draws[0].thirdPlace}</td></tr>
</table>

</td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td></tr>
<tr><td class="resultbottom">${currXGR.draws[0].specials[0]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[1]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[2]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[3]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[0].specials[5]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[6]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[7]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[8]}</td>
<td class="resultbottom">${currXGR.draws[0].specials[9]}</td>
</tr><tr>
<td colspan="5" class="resultprizelable">Consolation</td></tr>
<tr><td class="resultbottom">${currXGR.draws[0].consolations[0]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[1]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[2]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[3]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[0].consolations[5]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[6]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[7]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[8]}</td>
<td class="resultbottom">${currXGR.draws[0].consolations[9]}</td>
</tr>
</table></div>
<p/>
<c:if test="${fn:length(currXGR.draws) > 1}">
<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[1].provider.image}" alt="No image" border='0'>
</td><td class="resultm4dlable">${currXGR.draws[1].provider.name}</td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[1].date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${currXGR.draws[1].drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:100" class="resultprizelable">1st Prize</td>
<td class="resulttop">${currXGR.draws[1].firstPlace}</td>
</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">${currXGR.draws[1].secondPlace}</td></tr>
<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">${currXGR.draws[1].thirdPlace}</td></tr>
</table></td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td></tr>
<tr><td class="resultbottom">${currXGR.draws[1].specials[0]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[1]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[2]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[3]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[1].specials[5]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[6]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[7]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[8]}</td>
<td class="resultbottom">${currXGR.draws[1].specials[9]}</td>
</tr><tr>
<td colspan="5" class="resultprizelable">Consolation</td></tr>
<tr><td class="resultbottom">${currXGR.draws[1].consolations[0]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[1]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[2]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[3]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[1].consolations[5]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[6]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[7]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[8]}</td>
<td class="resultbottom">${currXGR.draws[1].consolations[9]}</td>
</tr>
</table>
</div>
</c:if>
<p/>
</td>


<td style="width: 50%;vertical-align:top" valign="top">
<c:if test="${fn:length(currXGR.draws) > 2}">
<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[2].provider.image}" alt="No image" border='0'>
</td><td class="resultm4dlable">${currXGR.draws[2].provider.name}</td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[2].date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${currXGR.draws[2].drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:100" class="resultprizelable">1st Prize</td>
<td class="resulttop">${currXGR.draws[2].firstPlace}</td>
</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">${currXGR.draws[2].secondPlace}</td></tr>
<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">${currXGR.draws[2].thirdPlace}</td></tr>
</table></td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td></tr>
<tr><td class="resultbottom">${currXGR.draws[2].specials[0]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[1]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[2]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[3]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[2].specials[5]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[6]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[7]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[8]}</td>
<td class="resultbottom">${currXGR.draws[2].specials[9]}</td>
</tr><tr>
<td colspan="5" class="resultprizelable">Consolation</td></tr>
<tr><td class="resultbottom">${currXGR.draws[2].consolations[0]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[1]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[2]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[3]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[2].consolations[5]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[6]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[7]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[8]}</td>
<td class="resultbottom">${currXGR.draws[2].consolations[9]}</td>
</table></div>
</c:if>
<p/>

<c:if test="${fn:length(currXGR.draws) > 3}">
<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[3].provider.image}" alt="No image" border='0'>
</td><td class="resultm4dlable">${currXGR.draws[3].provider.name}</td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[3].date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${currXGR.draws[3].drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:100" class="resultprizelable">1st Prize</td>
<td class="resulttop">${currXGR.draws[3].firstPlace}</td>
</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">${currXGR.draws[3].secondPlace}</td></tr>
<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">${currXGR.draws[3].thirdPlace}</td></tr>
</table></td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td></tr>
<tr><td class="resultbottom">${currXGR.draws[3].specials[0]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[1]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[2]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[3]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[3].specials[5]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[6]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[7]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[8]}</td>
<td class="resultbottom">${currXGR.draws[3].specials[9]}</td>
</tr><tr>
<td colspan="5" class="resultprizelable">Consolation</td></tr>
<tr><td class="resultbottom">${currXGR.draws[3].consolations[0]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[1]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[2]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[3]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[3].consolations[5]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[6]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[7]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[8]}</td>
<td class="resultbottom">${currXGR.draws[3].consolations[9]}</td>
</tr>
</table></div>
</c:if>
</td>

<td style="width: 50%;vertical-align:top" valign="top">
	<c:if test="${fn:length(currXGR.draws) > 4}">
	<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
	<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
	<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[4].provider.image}" alt="No image" border='0'>
	</td><td class="resultm4dlable">${currXGR.draws[4].provider.name}</td>
	</tr>
	</table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="5">
	<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[4].date}" pattern="dd-MMM-yyyy" /></td>
	<td class="resultdrawdate">Draw No: ${currXGR.draws[4].drawNo}</td>
	</tr></table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="0">
	<tr><td style="width:100" class="resultprizelable">1st Prize</td>
	<td class="resulttop">${currXGR.draws[4].firstPlace}</td>
	</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
	<td class="resulttop">${currXGR.draws[4].secondPlace}</td></tr>
	<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
	<td class="resulttop">${currXGR.draws[4].thirdPlace}</td></tr>
	</table></td></tr><tr>
	<td colspan="5" class="resultprizelable">Special</td></tr>
	<tr><td class="resultbottom">${currXGR.draws[4].specials[0]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[1]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[2]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[3]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[4].specials[5]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[6]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[7]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[8]}</td>
	<td class="resultbottom">${currXGR.draws[4].specials[9]}</td>
	</tr><tr>
	<td colspan="5" class="resultprizelable">Consolation</td></tr>
	<tr><td class="resultbottom">${currXGR.draws[4].consolations[0]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[1]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[2]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[3]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[4]}</td>
</tr><tr><td class="resultbottom">${currXGR.draws[4].consolations[5]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[6]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[7]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[8]}</td>
	<td class="resultbottom">${currXGR.draws[4].consolations[9]}</td>
	</tr>
	</table></div>
	</c:if>
	<p/>

		<c:if test="${fn:length(currXGR.draws) > 5}">
		<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
		<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
		<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[5].provider.image}" alt="No image" border='0'>
		</td><td class="resultm4dlable">${currXGR.draws[5].provider.name}</td>
		</tr>
		</table></td></tr><tr><td colspan="5">
		<table class="resultTable2" cellpadding="0" cellspacing="5">
		<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[5].date}" pattern="dd-MMM-yyyy" /></td>
		<td class="resultdrawdate">Draw No: ${currXGR.draws[5].drawNo}</td>
		</tr></table></td></tr><tr><td colspan="5">
		<table class="resultTable2" cellpadding="0" cellspacing="0">
		<tr><td style="width:100" class="resultprizelable">1st Prize</td>
		<td class="resulttop">${currXGR.draws[5].firstPlace}</td>
		</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
		<td class="resulttop">${currXGR.draws[5].secondPlace}</td></tr>
		<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
		<td class="resulttop">${currXGR.draws[5].thirdPlace}</td></tr>
		</table></td></tr><tr>
		<td colspan="5" class="resultprizelable">Special</td></tr>
		<tr><td class="resultbottom">${currXGR.draws[5].specials[0]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[1]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[2]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[3]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[4]}</td>
	</tr><tr><td class="resultbottom">${currXGR.draws[5].specials[5]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[6]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[7]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[8]}</td>
		<td class="resultbottom">${currXGR.draws[5].specials[9]}</td>
		</tr><tr>
		<td colspan="5" class="resultprizelable">Consolation</td></tr>
		<tr><td class="resultbottom">${currXGR.draws[5].consolations[0]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[1]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[2]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[3]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[4]}</td>
		</tr><tr><td class="resultbottom">${currXGR.draws[5].consolations[5]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[6]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[7]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[8]}</td>
		<td class="resultbottom">${currXGR.draws[5].consolations[9]}</td>
		</tr>
		</table></div>
		</c:if>
	</td>
<td style="width: 50%;vertical-align:top" valign="top">
			<c:if test="${fn:length(currXGR.draws) > 6}">
			<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
			<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
			<tr><td class="resultm4dlable" style="width:20%"><img width="70" height="70" src="data:image/png;base64,${currXGR.draws[6].provider.image}" alt="No image" border='0'>
			</td><td class="resultm4dlable">${currXGR.draws[6].provider.name}</td>
			</tr>
			</table></td></tr><tr><td colspan="5">
			<table class="resultTable2" cellpadding="0" cellspacing="5">
			<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${currXGR.draws[6].date}" pattern="dd-MMM-yyyy" /></td>
			<td class="resultdrawdate">Draw No: ${currXGR.draws[6].drawNo}</td>
			</tr></table></td></tr><tr><td colspan="5">
			<table class="resultTable2" cellpadding="0" cellspacing="0">
			<tr><td style="width:100" class="resultprizelable">1st Prize</td>
			<td class="resulttop">${currXGR.draws[6].firstPlace}</td>
			</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
			<td class="resulttop">${currXGR.draws[6].secondPlace}</td></tr>
			<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
			<td class="resulttop">${currXGR.draws[6].thirdPlace}</td></tr>
			</table></td></tr><tr>
			<td colspan="5" class="resultprizelable">Special</td></tr>
			<tr><td class="resultbottom">${currXGR.draws[6].specials[0]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[1]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[2]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[3]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[4]}</td>
		</tr><tr><td class="resultbottom">${currXGR.draws[6].specials[5]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[6]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[7]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[8]}</td>
			<td class="resultbottom">${currXGR.draws[6].specials[9]}</td>
			</tr><tr>
			<td colspan="5" class="resultprizelable">Consolation</td></tr>
			<tr><td class="resultbottom">${currXGR.draws[6].consolations[0]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[1]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[2]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[3]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[4]}</td>
		</tr><tr><td class="resultbottom">${currXGR.draws[6].consolations[5]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[6]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[7]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[8]}</td>
			<td class="resultbottom">${currXGR.draws[6].consolations[9]}</td>
			</tr>
			</table></div>
			</c:if>
			<p/>
</td>


<div class="outerbox"><table class="resultTable"  border="1" align="center">
<tr>
<td><input type="submit" name="play" value="SUBMIT" class="button" style="height:40px;"
	onclick="return confirm('Will use these results for all outstanding bets for this date. Confirm?')"
	/>
</td>
<td><input type="submit" name="invalidDraw" value="CANCEL INVALID DRAW" class="button" style="height:23px;"
	onclick="return confirm('Intended only for invalid or incomplete draw results.\nThe draw results for this date will need to be updated.\n(Hint: Check all draws complete before updating)\nConfirm Cancel Invalid Draw?')"
	/>
</td>
</tr>
</table></div>
<p/>



<br/>
<tr><td>${drawResultsForm.message}</td></tr>

</form:form>
</body>
</html>
