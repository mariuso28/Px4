<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>


<head>
<title>numberViewExposure</title>


<style type="text/css">
h1{
page-break-before: always;
}

h3{
font: 20px Arial, sans-serif;
}

body{
font: 20px Arial, sans-serif;
}


</style>

<script type="text/javascript">

var validated = true;

function isNumber(s , checkFloat, checkNegative) {
     var found = false;
     var i;
     var dCheck = false;
     for (i = 0; i < s.length; i++)
     {
         var c = s.charAt(i);
         if((c == "-") && (i == 0) && (s.length > 0)) {
           if(checkNegative == false) {
             found = true
           }
         }
         else {
           if( ((c == ".") && (checkFloat == true) && (dCheck == false)))
           {
     	     dCheck = true
           }
           else if (((c < "0") || (c > "9")))
           {
               found = true
           }
       }
     }
     if( s.length == 0)
     {
         found = true
     }

     return found==false;
     }

     function testNumber(testField,fieldName,doubleAllowed) {

		oFormObject = document.forms['myForm'];
   		var field = testField;
   		oformElement = oFormObject.elements[field];
		var value = oformElement.value;
		var isValid = isNumber(value,doubleAllowed,false);
                if (!isValid)
		{
                    alert(fieldName + ' has incorrect format');
		    if (doubleAllowed)
			oformElement.value = '0.00';
		    else
			oformElement.value = '0';
		    return false;
                }
		else
		   return true;
	 }

     	function testBet() {
		validated=true;
		// alert('IN TEST ' + validated);
		if (!testNumber('command.betExpo','Default Bet Exposure',true))
		    validated=false;
       }

	function testWin() {
		validated=true;
		// alert('IN TEST ' + validated);
		if (!testNumber('command.winExpo','Default Win Exposure',true))
		    validated=false;
       }

	function setValidated()
	    {
		// alert('setValidated ' + validated);
		validated=true;
		// alert('setValidated1 ' + validated);
	    }

	    function validate(theForm)
	    {
		// alert('VALIDATED ' + validated);
		return validated;
	    }

</script>


</head>

<body>

<form:form method="post" action="processAnalytic.html" modelAttribute="numberViewExposureForm">


<h2>Exposure For : ${numberViewExposureForm.number}</h2>
<h3>Total Outstanding Bet: ${numberViewExposureForm.numberExpo.tbet} Total Outstanding Win: ${numberViewExposureForm.numberExpo.expo}</h3>

<td>
 <input type="image" name="cancel_viewExposure" width="50" height="30" src="../../img/back.jpg"
			  onMouseOut="this.src='../../img/back.jpg'" onMouseOver="this.src='../../img/cancel.jpg'">
</td>
<c:choose>
<c:when test="${numberViewExposureForm.threeDigits != null}">
<table style="width:30%;">
<tr>
<td style="width:10%;">
   <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
	<div class='m4d-num-list'>
	<img width="100" height="100" src='../../img/${numberViewExposureForm.threeDigits}.png' border='0'></div></span>
</td>
<tr>
<td>
	${numberViewExposureForm.numberDesc}
</td>
<tr>
	<c:choose>
		<c:when test="${externalReturnAddress eq 'processHedge.html?hedgeManage'}" >
		<td><a href="../hdgBet/processHedge.html?method=hedgeNumber&number=${numberViewExposureForm.number}">
			Hedge/Bet Number</a></td>
		</c:when>
	</c:choose>
<tr>
</c:when>
</c:choose>

<input type="hidden" name="command.number" value="${numberViewExposureForm.number}"/>
<table cellspacing="1" cellpadding="0" border="0"
 bgcolor="white" id="shell" height="100" width="1000">

   <td bgcolor="lightcyan" width="600px">
   <table border="0" cellpadding="3" cellspacing="0">
	<tr>
	<td>Maximum Total Bet:</td>
	<c:choose>
	<c:when test="${numberViewExposureForm.expo.betExpo < 0}">
	   	<td>
Unlimited</td>
	</c:when>
	<c:otherwise>
		<td>
		<fmt:formatNumber value="${numberViewExposureForm.expo.betExpo}"
			pattern="#0.00"/>
		</td>
	</c:otherwise>
	</c:choose>
	<td>
	    <input type="text" name="command.betExpo"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"
	</td>
	<td><input type="submit" name="modifyBetExpo" value="Modify"
					onclick="testBet()" style="height:23px;"/></td>
	<td><input type="submit" name="modifyUnLimitedBetExpo" value="Unlimited"
					style="height:23px;"/></td>

	<td><input type="submit" name="resetExpoToDefault" value="Reset to Default"
					style="height:23px;"/></td>

	</tr>
	<tr>
	<td>Maximum Total Win:</td>
	<c:choose>
	<c:when test="${numberViewExposureForm.expo.winExpo < 0}">
	   	<td>
Unlimited</td>
	</c:when>
	<c:otherwise>
		<td>
		<fmt:formatNumber value="${numberViewExposureForm.expo.winExpo}"
				pattern="#0.00"/>
		</td>
	</c:otherwise>
	</c:choose>
	<td>
	    <input type="text" name="command.winExpo"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"
	</td>
	<td><input type="submit" name="modifyWinExpo" value="Modify"
					onclick="testWin()" style="height:23px;"/></td>
	<td><input type="submit" name="modifyUnLimitedWinExpo" value="Unlimited"
					style="height:23px;"/></td>
	<c:choose>
	<c:when test="${numberViewExposureForm.expo.blocked <= 0}">
	   	<td><input type="submit" name="blockExpo" value="Block For Bet"
					style="height:23px;"/></td>
	</c:when>
	<c:otherwise>
		<td><input type="submit" name="unblockExpo" value="Unblock For Bet"
					style="height:23px;"/></td>
	</c:otherwise>
	</c:choose>

    </tr>

    </table>
    </td>

</table>


</form:form>

<body>
