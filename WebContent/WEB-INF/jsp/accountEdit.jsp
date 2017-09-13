<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>accountEdit</title>
<style>
th{
text-align:center;
}
body{
font: 16px Arial, sans-serif;
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
<form:form method="post" id="myForm" action="../../acc/processAccount.html" modelAttribute="accountDetailsForm" onsubmit="return validate(this)">

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Maintain Account for ${currAccountUser.role.desc} ${currAccountUser.code}
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></h2>
<table>
		<tr height="20">
		<td>
			<a href="processAccount.html?cancelAccount"><img src="../../img/back.jpg" width="50" height="30"></a>
		</td>
		</tr>
		<tr height="20">
			<td>&nbsp</td>
		</tr>
	</table>
<table cellspacing="1" cellpadding="0" border="0"
 bgcolor="white" id="shell" height="200" width="650">
   <tr height="50" align="left">
     <td valign="top" style="font-family:verdana; color:purple; background-color:${currAccountUser.role.color}">
      <table border="0" cellpadding="3" cellspacing="0"  width="300">
	<colgroup>
       	<col span="1" style="width: 50%;">
       	<col span="1" style="width: 50%;">
    	</colgroup>
	 <tr>

	<td>Credit Limit:</td>
		<td><input type="text" name="command.newAccount.creditLimit"
		value="<fmt:formatNumber value="${accountDetailsForm.account.creditLimit}"
		pattern="#0.00"/>"/>
		</td>
	</tr>



	<c:choose>
	<c:when test="${currAccountUser.role == 'ROLE_COMP'}">
		<input type="hidden" name="command.newAccount.paymentDays" value="99" />
	</c:when>
	<c:otherwise>
	<tr>
		<td>(Max<fmt:formatNumber value="${accountDetailsForm.availableCredit}"
			pattern="#0.00"
/>)</td>
	</tr>
	<tr>
		<td>Payment Days:</td>
		<td>
		<c:set var="pDay" value="${accountDetailsForm.account.paymentDays}"/>
		<form:select path="command.newAccount.paymentDays" value="${pDay}">
		<c:forEach items="${accountDetailsForm.paymentDays}" var="day" varStatus="status1">
			<c:set var="dDay" value="${status1.index}" />
			<option value="${day}" ${dDay == pDay ? 'selected' : ''}>
				<fmt:formatNumber value="${day}" pattern="#0" />
			</option>
		</c:forEach>
		</form:select>
		</td>
	</tr>
	</c:otherwise>
	</c:choose>
	<tr>
		<c:set var="linkColor" value="green" scope="page" />
		<c:choose>
		<c:when test="${member.account.balance < '0'}">
			<c:set var="linkColor" value="red" scope="page"/>

</c:when>
		</c:choose>
		<td>Curr Balance:</td>
		<td>
		<font color=${linkColor} size="3">
               		<fmt:formatNumber value="${currAccountUser.account.balance}"
							pattern="#0.00" />
              	</font>
		</td>


	</tr>
	</table>
     	</td>
	<c:choose>
	<c:when test="${accountDetailsForm.modify == false}">
	<td valign="top">

 	<table border="0" style="width:300px;" align="left">
            <colgroup>
       		<col span="1" style="width: 50%;">
       		<col span="1" style="width: 50%;">
    	    </colgroup>
	    <tr>
		<td>Make Deposit:</td>
		<td><input type="text" name="command.dwAmount"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"/>
		</td>
		<input type="hidden" name="command.dwType" value="Deposit" />


	    </tr>

	</table>
	</td>
	</c:when>
	</c:choose>
	</tr>
<input type="hidden" name="command.newAccountFlag" value="${accountDetailsForm.newAccountFlag}" />

</table>
<tr><td><font color="red" size="3">${accountDetailsForm.message}</font></td></tr>
<br/>
<table border="0" cellpadding="3" cellspacing="0" width="300">
<tbody align="left" style="font-family:verdana; color:purple; background-color:White">
<tr>
	<td><input type="submit" name="saveAccount" value="Save" onclick="test()" style="height:23px;"/></td>
</tr>
</tbody>
</table>
</form:form>

</body>
</html>
