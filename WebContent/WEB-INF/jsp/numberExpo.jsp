<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>numberExpo</title>
<style>
th{
text-align:center;
}
body{
font: 16px Arial, sans-serif;
}

div#info{
    text-align:left;
    background-color:white;
    position:absolute;
    left:500px;
    padding:10px;

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

     	function test() {

		if (!testNumber('command.newAccount.creditLimit','Credit Limit',true))
		    validated=false;
		else
		if (!testNumber('command.deposit','Opening Deposit',true))
		    validated=false;
		else
		   validated=true;
       }

	    function validate(theForm)
	    {
		// alert('VALIDATED ' + validated);
		return validated;
	    }


</script>

</head>


<body>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

$(document).ready(function(){
    $("div#info").hide();

    $("#songlist td").mouseover(function(){
        $(this).css("background-color","#ccc");
        $("#info",this).show();
    }).mouseout(function(){
        $(this).css("background-color","#eee");
        $("#info",this).hide();
    });

});

</script>

<form:form method="post" id="myForm" action="processNumberExpo.html" modelAttribute="expoForm" onsubmit="return validate(this)">

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Number Exposures for Game : ${currActiveGame} 
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></h2>

<table>
<tr height="20">
<td>
	<a href="../agnt/processAgent.html?cancelNumberExpo"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</tr>
<tr height="20">
	<td>&nbsp</td>
</tr>
</table>

<c:choose>
<c:when test="${fn:length(expoForm.hedgableNumbers)>0}">
<table>
<tr><td>Numbers Over Exposed</td></tr>
</table>
<table border="0" style="width:100%;" align="left">
	    <colgroup>
		<col span="1" style="width: 10%;">
       		<col span="1" style="width: 15%;">
       		<col span="1" style="width: 15%;">
		<col span="1" style="width: 15%;">

		<col span="1" style="width: 15%;">
		<col span="1" style="width: 10%;">
	</colgroup>

    <tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Number</td>
		<td>Total Bet</td>
		<td>Bet Limit</td>
		<td>Total Win</td>
		<td>Win Limit</td>
	</tr>

	<c:forEach items="${expoForm.hedgableNumbers}" var="hn" varStatus="status">
	<tr>
		<c:choose>
		<c:when test="${hn.betExpo >=0}">
			<c:choose>
			<c:when test="${hn.tbet > (hn.betExpo  + hn.hedgedStake)}">
				<td style="color:red">${hn.number}</td>
			</c:when>
			<c:otherwise>
				<td style="color:green">${hn.number}</td>
			</c:otherwise>
			</c:choose>
		</c:when>
		</c:choose>

		<td><fmt:formatNumber value="${hn.tbet}" pattern="#0.00" /></td>
		<td>
		<c:choose>
		<c:when test="${hn.betExpo >=0}">
			<fmt:formatNumber value="${hn.betExpo}" pattern="#0.00" />
		</c:when>
		<c:otherwise>
			Unlimited
		</c:otherwise>
		</c:choose>
		</td>
		<td><fmt:formatNumber value="${hn.expo}" pattern="#0.00" /></td>
		<td>
		<c:choose>
		<c:when test="${hn.winExpo >=0}">
			<fmt:formatNumber value="${hn.winExpo}" pattern="#0.00" />
		</c:when>
		<c:otherwise>
			Unlimited
		</c:otherwise>
		</c:choose>
		</td>
		<td>
		<c:choose>
		<c:when test="${hn.blocked==0}">
			<td style="font-size:18px;"><a href="processNumberExpo.html?blockExpo&number=${hn.number}">Block</a></td>
		</c:when>
		<c:otherwise>
			<td style="font-size:18px;"><a href="processNumberExpo.html?unblockExpo&number=${hn.number}">Unblock</a></td>
		</c:otherwise>
		</c:choose>
		</td>
	</tr>
	</c:forEach>
	<tr height="20">
	<td>&nbsp</td>
</tr>
</table>
</c:when>
</c:choose>

<table>

<tr><td>Sorted By :  ${expoForm.sortedBy}</td></tr>
<c:choose>
		<c:when test="${currPage0>1}">
			<td><a href="processNumberExpo.html?currNumberExpoPrev0"><img src="../../img/monthBackward_normal.gif"/></a></td>
		</c:when>
		</c:choose>
		<c:choose>
		<c:when test="${currPage0<currMetaBetExpoPg.lastPage}">
			<td><a href="processNumberExpo.html?currNumberExpoNext0"><img src="../../img/monthForward_normal.gif"/></a></td>
		</c:when>
</c:choose>
</br>
</table>

<table style="width:920px">
<tr>
<td style="width:50%;vertical-align:top">
	<table border="0" style="width:460px;height:20px" align="left" cellspacing="0">
	    <tr style="font-family:verdana; color:FFF; background-color:B5B5B5">
		<td>${expoForm.headers[0]}
		<c:choose>
		<c:when test="${expoForm.pos[0] > '0'}">
			<a href="processNumberExpo.html?currNumberExposureByChoice&direction=up">
				<img src="../../img/uppoint.gif"/></a>
		</c:when>
		<c:otherwise>
			<a href="processNumberExpo.html?currNumberExposureByChoice&direction=down">
				<img src="../../img/downpoint.gif"/></a>
		</c:otherwise>
		</c:choose>
		</td>
		<td>${expoForm.headers[1]}
		<c:choose>
		<c:when test="${expoForm.pos[1] > 0}">
			<a href="processNumberExpo.html?currNumberExposureByCode&direction=up">
				<img src="../../img/uppoint.gif"/></a>
		</c:when>
		<c:otherwise>
			<a href="processNumberExpo.html?currNumberExposureByCode&direction=down">
				<img src="../../img/downpoint.gif"/></a>
		</c:otherwise>
		</c:choose>
		</td>
		<td align="right">${expoForm.headers[2]}
		<c:choose>
		<c:when test="${expoForm.pos[2] > 0}">
			<a href="processNumberExpo.html?currNumberExposureByStake&direction=up">
			<img src="../../img/uppoint.gif"/></a>
		</c:when>
		<c:otherwise>
			<a href="processNumberExpo.html?currNumberExposureByStake&direction=down">
				<img src="../../img/downpoint.gif"/></a>
		</c:otherwise>
		</c:choose>
		</td>
		</tr>
	</table>
	<div style="left:0px;height:400px;width:460px;overflow:auto;overflow-x:hidden;">
	<table border="0" style="width:450px;" align="left" cellspacing="0">
	    <c:forEach items="${expoForm.metaBetExpos}" var="betExpo" varStatus="status">
		<tr>
			<td>${betExpo.choice}</td>
			<td>${betExpo.code}</td>
			<td align="right"><fmt:formatNumber value="${betExpo.tbet}" pattern="#0.00" />
			<td>&nbsp</td>
		</tr>
		</c:forEach>
	</table>
	</div>
	</td>
	<td style="width:40%;vertical-align:top;hroizontal-align:right">
	<table border="0" style="width:370px;height:20px" align="left" cellspacing="0">
	<tr>
		<c:choose>
		<c:when test="${currPage1>1}">
			<td><a href="processNumberExpo.html?currNumberExpoPrev1"><img src="../../img/monthBackward_normal.gif"/></a></td>
		</c:when>
		</c:choose>
		<c:choose>
		<c:when test="${currPage1<currMetaBetExposForCode.lastPage}">
			<td><a href="processNumberExpo.html?currNumberExpoNext1"><img src="../../img/monthForward_normal.gif"/></a></td>
		</c:when>
		</c:choose>
	</tr>
	<tr style="font-family:verdana; color:FFF; background-color:LIGHTGREEN">
		<td>${expoForm.headers[1]}</td>
		<td>${expoForm.headers[2]}</td>
	</tr>
	</table>
	<div style="height:200px;overflow:auto;overflow-x:hidden;width:370px;">
	<table table id="songlist" border="0" style="width:350px;" align="left" cellspacing="0">
	    <c:forEach items="${expoForm.metaBetExposForCode}" var="betExpo" varStatus="status">
		<tr>
			<td>${betExpo.code}
				<div id="info"  style="font-family:verdana; color:FFF; background-color:LIGHTGREEN">${betExpo.username} - ${betExpo.contact}</div></td>
			<td align="right"><fmt:formatNumber value="${betExpo.tbet}" pattern="#0.00" /></td>
		</tr>
		</c:forEach>
	</table>
	</div>
	<table border="0" style="width:370px;height:20px" align="left" cellspacing="0">
	<tr>
		<c:choose>
		<c:when test="${currPage2>1}">
			<td><a href="processNumberExpo.html?currNumberExpoPrev2"><img src="../../img/monthBackward_normal.gif"/></a></td>
		</c:when>
		</c:choose>
		<c:choose>
		<c:when test="${currPage2<currMetaBetExposForChoice.lastPage}">
			<td><a href="processNumberExpo.html?currNumberExpoNext2"><img src="../../img/monthForward_normal.gif"/></a></td>
		</c:when>
		</c:choose>
	</tr>
	<tr style="font-family:verdana; color:FFF; background-color:LIGHTBLUE">
		<td>${expoForm.headers[0]}</td>
		<td>${expoForm.headers[2]}</td>
	</tr>
	</table>
	<div style="height:200px;overflow:auto;overflow-x:hidden;width:370px;">
	<table border="0" style="width:350px;" align="left" cellspacing="0">
	    <c:forEach items="${expoForm.metaBetExposForChoice}" var="betExpo" varStatus="status">
		<tr>
			<td>${betExpo.choice}</td>
			<td align="right"><fmt:formatNumber value="${betExpo.tbet}" pattern="#0.00" /></td>
		</tr>
		</c:forEach>
	</table>
	</div>
	</td>
</tr>
</table>

</form:form>
</body>
</html>
