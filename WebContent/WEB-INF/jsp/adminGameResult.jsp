<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<title>adminGameResult</title>

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

<form:form method="post" action="processAdmin.html" modelAttribute="drawResultsForm">
<h2>Game Results</h2>

<input name="result.provider" type="hidden" value="${drawResultsForm.init.provider}"/>
<input name="result.drawNo" type="hidden" value="${drawResultsForm.init.drawNo}"/>
<input name="result.date" type="hidden" value="${drawResultsForm.init.date}"/>

<div class="outerbox"><table class="resultTable" align="center"><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0"><tr>
<td class="resultm4dlable" style="width:20%"><img src="img/4dx.gif"/>
</td><td class="resultm4dlable">DRAW RESULTS</td></tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${drawResultsForm.init.date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${drawResultsForm.init.drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td style="width:45%" class="resultprizelable">1st Prize</td>
<td class="resulttop">
<input name="result.firstPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.firstPlace}"/>

</td></tr><tr>
<td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">
<input name="result.secondPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.secondPlace}"/>
</td></tr><tr>
<td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">
<input name="result.thirdPlace"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.thirdPlace}"/>
</td></tr></table></td></tr><tr>
<td colspan="5" class="resultprizelable">Special</td>
</tr><tr><td class="resultbottom">
<input name="result.specials[0]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[0]}"/>
</td>
<td class="resultbottom">
<input name="result.specials[1]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[1]}"/>
</td><td class="resultbottom">
<input name="result.specials[2]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[2]}"/>
</td>
<td class="resultbottom">
<input name="result.specials[3]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[3]}"/>
</td><td class="resultbottom">
<input name="result.specials[4]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[4]}"/>
</td></tr>
<tr><td class="resultbottom">
<input name="result.specials[5]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[5]}"/>
</td><td class="resultbottom">
<input name="result.specials[6]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[6]}"/>
</td>
<td class="resultbottom">
<input name="result.specials[7]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[7]}"/>
</td><td class="resultbottom">
<input name="result.specials[8]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[8]}"/>
</td>
<td class="resultbottom">
<input name="result.specials[9]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.specials[9]}"/>
</td></tr>
<tr>
</tr>
<tr>
<td colspan="5" class="resultprizelable">Consolation</td>
</tr><tr><td class="resultbottom">
<input name="result.consolations[0]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[0]}"/>
</td>
<td class="resultbottom">
<input name="result.consolations[1]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[1]}"/>
</td><td class="resultbottom">
<input name="result.consolations[2]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[2]}"/>
</td>
<td class="resultbottom">
<input name="result.consolations[3]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[3]}"/>
</td><td class="resultbottom">
<input name="result.consolations[4]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[4]}"/>
</td></tr>
<tr><td class="resultbottom">
<input name="result.consolations[5]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[5]}"/>
</td><td class="resultbottom">
<input name="result.consolations[6]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[6]}"/>
</td>
<td class="resultbottom">
<input name="result.consolations[7]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[7]}"/>
</td><td class="resultbottom">
<input name="result.consolations[8]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[8]}"/>
</td>
<td class="resultbottom">
<input name="result.consolations[9]"  size="4" maxlength="4" type="text" value="${drawResultsForm.init.consolations[9]}"/>
</td></tr>cancelGameResultsInvalidDraw
</table></div>
<br/>
<tr><td>${drawResultsForm.message}</td></tr>
<br/>
<input type="submit" name="storeGameResults" value="Submit Results" class="button" style="height:23px;"/>
<input type="submit" name="cancelGameResults" value="Cancel" class="button" style="height:23px;"/>
</tr>
</form:form>
</body>
</html>
