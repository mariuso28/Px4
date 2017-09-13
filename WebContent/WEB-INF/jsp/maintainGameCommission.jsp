<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>maintainGameCommission</title>
<style>
th{
text-align:left;
}

body{
font: 18px Arial, sans-serif;
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
		// alert("Testing : " + field);
   		oformElement = oFormObject.elements[field];
		var value = oformElement.value;
		// alert("Value is : " + value);
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

	function testComms(len)
	{
		// alert('testComms ' + len);
		validated = true;
		for (var index=0;index<len;index++)
		{
		var field = "command.commissions[" + index + "].betCommission"
		// alert('testComms field ' + field );
		if (!testNumber(field,'Bet Commission',true))
		{
			validated = false;
			return;
		}

		var field = "command.commissions[" + index + "].winCommission"
		if (!testNumber(field,'Win Commission',true))
		{
			validated = false;
			return;
		}
		}
	}

	 function validate(theForm)
	    {
		// alert('VALIDATED ' + validated);
		return validated;
	    }

function checkForInt(evt)
{
var charCode = ( evt.which ) ? evt.which : event.keyCode;
return ( (charCode >= 48 && charCode <= 57) || (charCode==46) );
}

</script>


</head>
<body>

<form:form method="post" id="myForm" action="processAccount.html"
		modelAttribute="gameCommissionForm" onsubmit="return validate(this)">
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;  ${currUser.contact}</h2>
<h2>Modify Game Commissions for : ${currAccountUser.role.desc}&nbsp;-&nbsp;${currAccountUser.contact}</h2>
<td>
<a href="processAccount.html?method=cancelUpdateGameCommissions"><img src="../../../img/back.jpg" width="50" height="30"></a>
</td>
</br>
</br>
	<table border="0" cellpadding="3" cellspacing="0" rowpadding="3" width="500">
	<tbody align="left" style="font-family:verdana; color:Blue;">
	<tr>
		<td>${gameCommissionForm.gameActivator.metaGame.name}</td>
		<td>${gameCommissionForm.gameActivator.metaGame.description}</td>

      	</tr>
	</tbody>
	</table>
	</br>
	<c:set var="mActivator" value="${gameCommissionForm.memberActivator}"/>

	<table border="1" cellpadding="3" cellspacing="0" width="900">
	<tbody align="center" style="font-family:verdana; color:purple; background-color:LightCyan">
	<tr>
		<td>Bet Type</td>
		<td>Stake</td>
		<td>Prize</td>

		<c:choose>
		<c:when test="${currAccountUser.role.rank > 0 && mActivator!=null}">
			<td>Min Bet Comm</td>
		</c:when>
		</c:choose>
		<td>Bet Comm</td>
		<c:choose>
		<c:when test="${currAccountUser.role.rank < 5}">
			<td>Max Bet Comm</td>
		</c:when>
		</c:choose>

		<c:choose>
		<c:when test="${currAccountUser.role.rank > 0}">
			<c:choose>
			<c:when test="${currAccountUser.role.rank < 5}">
				<td>Min Win Comm</td>
			</c:when>
			</c:choose>
			<td>Win Comm</td>
			<c:choose>
			<c:when test="${currAccountUser.role.rank>1 && mActivator!=null}">
				<td>Max Win Comm</td>
			</c:when>
			</c:choose>
		</c:when>
		</c:choose>

	</tr>
	</tbody>
	<tbody align="right" style="font-family:verdana; color:purple">
	<c:set var="commNum" value="${fn:length(gameCommissionForm.gameActivator.gameCommissions)}" />
	<c:forEach items="${gameCommissionForm.gameActivator.gameCommissions}" var="comm" varStatus="status">
	<tr>
		<c:set var="cnt" value="${status.index}" />
		<c:set var="pComm" value="${gameCommissionForm.parentActivator.gameCommissions[cnt]}"/>
		<c:set var="mComm" value="${gameCommissionForm.memberActivator.gameCommissions[cnt]}"/>
		<!-- <td>${mComm==null}</td>
 -->
		<td align="center" width="25%">${comm.game.gtype}</td>

		<td><fmt:formatNumber value="${comm.game.stake}" pattern="#0.00" /></td>

		<td align="right">

		<select name="payOutPath" >
			<c:forEach items="${comm.game.payOuts}" var="payOut" varStatus="status1">
    				<option value="XX"}>
						${payOut.type}
						<fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
				</option>
  			</c:forEach>
		</select>

		<input type="hidden" name="command.commissions[${status.index}].gameId" value="${comm.gameId}" />

		<c:choose>
		<c:when test="${currAccountUser.role.rank > 0  && mActivator!=null}">
			<td>
			<fmt:formatNumber value="${mComm.betCommission}" pattern="#0.0000" />
			</td>
		</c:when>
		</c:choose>
		<td align="right">

		<input type="text"  maxlength="6" size="8px" name="command.commissions[${status.index}].betCommission"
			onkeypress="return checkForInt(event)"
			value="<fmt:formatNumber value="${comm.betCommission}" pattern="#0.0000"/>"
		 />

		<c:choose>
		<c:when test="${currAccountUser.role.rank < 5}">
			<td>
			<fmt:formatNumber value="${pComm.betCommission}" pattern="#0.0000" />
			</td>
		</c:when>
		</c:choose>

		<c:choose>
		<c:when test="${currAccountUser.role.rank > 0}">

			<c:choose>
			<c:when test="${currAccountUser.role.rank < 5}">
			<td>
				<fmt:formatNumber value="${pComm.winCommission}" pattern="#0.0000" />
			</td>
			</c:when>
			</c:choose>

			<td align="right">
			<input type="text" maxlength="6" size="8px" name="command.commissions[${status.index}].winCommission"
			onkeypress="return checkForInt(event)"
			value="<fmt:formatNumber value="${comm.winCommission}" pattern="#0.0000"/>"
		 	/>

			<c:choose>
			<c:when test="${currAccountUser.role.rank>1 && mActivator!=null}">
			<td>
				<fmt:formatNumber value="${mComm.winCommission}" pattern="#0.0000" />
			</td>
			</c:when>
			</c:choose>

		</c:when>
		</c:choose>


	</tr>
	</c:forEach>
	</tbody>
	</table>

	<table border="0" cellpadding="3" cellspacing="0" rowpadding="3" width="700">
	<tbody align="left" style="font-family:verdana; color:Blue;">
	<tr>
		<c:choose>
		<c:when test="${currAccountUser.role.rank > 0}">
		<td align="right">
		<a href="processAccount.html?method=resetBetCommissions"
		onclick="return confirm('!!THESE AND ALL DOWNSTREAM BET COMMISSIONS WILL BE RESET TO ZERO. ARE YOU SURE?')">Reset Bet Commissions</a>
		</td>
		<td align="right">
		<a href="processAccount.html?method=resetWinCommissions"
		onclick="return confirm('!!THESE AND ALL DOWNSTREAM WIN COMMISSIONS WILL BE RESET TO ZERO. ARE YOU SURE?')">Reset Win Commissions</a>
		</td>

      		</c:when>
		<c:otherwise>
		<td align="right">
		<a href="processAccount.html?method=resetBetCommissions"
		onclick="return confirm('!!THESE COMMISSIONS WILL BE RESET TO ZERO. ARE YOU SURE?')">Reset Bet Commissions</a>
		</td>
		</c:otherwise>
		</c:choose>

		<td align="right"><input type="submit" name="modifyGameCommission" value="Update"
					onclick="testComms(${commNum})" style="height:23px;"/></td>

      	</tr>
	</tbody>
	</table>

<br/>
<tr><td><font color="red" size="3">${gameCommissionForm.message}</font></td></tr>
<br/>

</form:form>
</body>
</html>
