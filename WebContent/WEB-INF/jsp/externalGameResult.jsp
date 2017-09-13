<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<title>externalGameResult</title>

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
<br/>
<tr>
<td>
		<a href="processAdmin.html?return_admin"><img src="img/back.jpg" width="50" height="30"></a>
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
<tr><td class="resultm4dlable" style="width:20%"><img src="img/logo_magnum.gif"/>
</td><td class="resultm4dlable">${currXGR.draws[0].provider.name}</td>
<td><input type="submit" name="External0" value="Use" class="button" style="height:23px;"/></td>
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

<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img src="img/logo_toto.gif"/>
</td><td class="resultm4dlable">${currXGR.draws[1].provider.name}</td>
<td><input type="submit" name="External1" value="Use" class="button" style="height:23px;"/></td>
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
</table></div>
<p/>
</td>

<td style="width: 50%;vertical-align:top" valign="top">
<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr>
<td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img src="img/logo_damacai.gif"/>
</td><td class="resultm4dlable">${currXGR.draws[2].provider.name}</td>
<td><input type="submit" name="External2" value="Use" class="button" style="height:23px;"/></td>
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
</tr>
</table></div>
<p/>

<div class="outerbox"><table class="resultTable"  border="1" align="center"><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0"><tr>
<td class="resultm4dlable" style="width:20%"><img src="img/dragon.jpg"/>
</td><td class="resultm4dlable">${drawResultsForm.init.provider}</td>
<td><input type="submit" name="Dx4" value="Use" class="button" style="height:23px;"/></td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${drawResultsForm.init.date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${drawResultsForm.init.drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:100" class="resultprizelable">1st Prize</td>
<td class="resulttop">
<input name="result.firstPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.firstPlace}" style="height:20px;"/>

</td></tr><tr>
<td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">
<input name="result.secondPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.secondPlace}" style="height:20px;"/>
</td></tr><tr>
<td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">
<input name="result.thirdPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.thirdPlace}" style="height:20px;"/>
</td></tr></table></td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td>
</tr><tr><td class="resultbottom">
<input name="result.specials[0]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[0]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.specials[1]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[1]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.specials[2]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[2]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.specials[3]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[3]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.specials[4]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[4]}" style="height:20px;"/>
</td></tr>
<tr><td class="resultbottom">
<input name="result.specials[5]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[5]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.specials[6]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[6]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.specials[7]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[7]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.specials[8]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[8]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.specials[9]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[9]}" style="height:20px;"/>
</td></tr>
<tr>
</tr>
<tr>
<td colspan="5" class="resultprizelable">Consolation</td>
</tr><tr><td class="resultbottom">
<input name="result.consolations[0]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[0]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.consolations[1]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[1]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.consolations[2]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[2]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.consolations[3]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[3]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.consolations[4]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[4]}" style="height:20px;"/>
</td></tr>
<tr><td class="resultbottom">
<input name="result.consolations[5]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[5]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.consolations[6]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[6]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.consolations[7]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[7]}" style="height:20px;"/>
</td><td class="resultbottom">
<input name="result.consolations[8]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[8]}" style="height:20px;"/>
</td>
<td class="resultbottom">
<input name="result.consolations[9]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[9]}" style="height:20px;"/>
</td></tr>
</table></div>

</td>

</table>
<br/>
<tr><td>${drawResultsForm.message}</td></tr>
</form:form>
</body>
</html>
