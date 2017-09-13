<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>accountMaintain</title>
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
		if (!testNumber('command.dwAmount','Deposit/Withdrawl Amount',true))
		    validated=false;
		else
		   validated=true;
       }

	    function validate(theForm)
	    {
		// alert('VALIDATED ' + validated);
		return validated;
	    }


// Retrieve last key pressed.  Works in IE and Netscape.
      // Returns the numeric key code for the key pressed.
      function getKey(e)
      {
        if (window.event)
           return window.event.keyCode;
        else if (e)
           return e.which;
        else
           return null;
      }
      function restrictChars(e, obj)
      {
        var CHAR_AFTER_DP = 2;  // number of decimal places
        var validList = "0123456789.";  // allowed characters in field
        var key, keyChar;
        key = getKey(e);
        if (key == null) return true;
        // control keys
        // null, backspace, tab, carriage return, escape
        if ( key==0 || key==8 || key==9 || key==13 || key==27 )
           return true;
        // get character
        keyChar = String.fromCharCode(key);
        // check valid characters
        if (validList.indexOf(keyChar) != -1)
        {
          // check for existing decimal point
          var dp = 0;
          if( (dp = obj.value.indexOf( ".")) > -1)
          {
            if( keyChar == ".")
              return false;  // only one allowed
            else
            {
              // room for more after decimal point?
              if( obj.value.length - dp <= CHAR_AFTER_DP)
                return true;
            }
          }
          else return true;
        }
        // not a valid character
        return false;
      }




</script>

</head>


<body>
<form:form method="post" id="myForm" action="processAccount.html"
		modelAttribute="accountDetailsForm" onsubmit="return validate(this)">


<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Update ${currAccountUser.role.desc} : ${currAccountUser.code} - ${currAccountUser.contact}</h2>

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


<table cellspacing="0" cellpadding="0" border="0"
 bgcolor="white" id="shell" height="200" width="800px">
   <tr height="50" align="left">
     <td valign="top" style="font-family:verdana; color:purple; background-color:${currAccountUser.role.color}">
      <table border="0" cellpadding="3" cellspacing="0"  width="400">
	<colgroup>
       	<col span="1" style="width: 50%;">
       	<col span="1" style="width: 50%;">
    	</colgroup>
	<tr>
		<td>
		<form:select path="command.dwType">
	    		<option value="Deposit">Make Deposit</option>
			<c:choose>
			<c:when test="${currAccountUser.account.balance > 0.0}">
				<option value="Withdrawl">Make Withdrawl</option>
			</c:when>
			</c:choose>
		</form:select>
		</td>
		<td>
		<!--
		<input type="hidden" name="command.dwAmount"
			value="0.0">
		<input type="text" name="command.dwAmount"
			onKeyPress="return restrictChars(event, this)">
		-->

		<input type="text" name="command.dwAmount" id="dw"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"


    	</td>
	    <tr>
		<td><input type="submit" name="saveWithDep" value="Save" onclick="test()" style="height:23px;"/></td>
	    </tr>

	<tr>
		<c:set var="linkColor" value="green" scope="page" />
		<c:choose>
		<c:when test="${currAccountUser.account.balance < '0.00'}">
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

	 <tr>
	<tr>
	<td></td>
	</tr>
	<tr>

		<c:choose>
		<c:when test="${currAccountUser.enabled}">
    <c:if test="${currAccountUser.role.rank == 0}">
  	   <td><input type="submit" name="disableMember"
  			value="Disable" class="button" style="height:23px;"
  			onclick="return confirm('!! WILL DIABLE THIS PLAYER YOU SURE!! WELL??')"/></td>
    </c:if>
    </c:when>
		</c:choose>
	<tr>
	</table>
     	</td>
	<td valign="top" style="font-family:verdana; color:purple; background-color:${currAccountUser.role.color}">
      	<table border="0" cellpadding="3" cellspacing="0"  width="300">
		<colgroup>
       		<col span="1" style="width: 30%;">
       		<col span="1" style="width: 70%;">
    		</colgroup>

		<tr><td>Logon Id:</td><td>${currAccountUser.email}</td></tr>
		<tr><td>Contact:</td><td>${currAccountUser.contact}</td></tr>
		<tr><td>Phone:</td><td>${currAccountUser.phone}</td></tr>
		<tr>
		<td></td>
		</tr>

		<tr>
		<td>Active Games: ${fn:length(accountDetailsForm.activeGames)}</td>
		<td>

		<select name="YYY">
			<c:forEach items="${accountDetailsForm.activeGames}" var="gr">
    				<option value="${gr}"}>${gr}</option>
  			</c:forEach>
		</select>
		</td>

		</tr>
		<tr>

	     	<td><input type="submit" name="modifyGames" value="Update Active Games"/></td>
		</tr>

</table>

	</td>
	</tr>
  <tr>
    <td>Account Activity Rollup for ${currAccountUser.role.shortCode} :</td>
  </tr>
	</table>
  <div style="height:300px;">
  <table align="left" style="margin:0; padding:10px; width:70%; background: #f8f8f8; color:#000;">
  	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
  		<td>Deposited</td>
  		<td>Withdrawn</td>
  		<td>Paid</td>
      <td>Collect</td>
  	</tr>
  	<tr>
      <td align="left"><fmt:formatNumber value="${accountDetailsForm.rollup.deposit}" pattern="#0.00" /></td>
  	  <td align="left"><fmt:formatNumber value="${accountDetailsForm.rollup.withdrawl}" pattern="#0.00" /></td>
      <td align="left"><fmt:formatNumber value="${accountDetailsForm.rollup.pay}" pattern="#0.00" /></td>
      <td align="left"><fmt:formatNumber value="${accountDetailsForm.rollup.collect}" pattern="#0.00" /></td>
  	</tr>
  </table>
  <div style="float:leftwidth:100%;;">
  <table align="left" style="margin:0; width:100%; height:30; color:#000;">
  <tr>
    <td><a href="processAccount.html?method=accDetails&code=${currAccountUser.code}" >
    <link rel="xyz" type="text/css" href="color.css">
      <font color="DarkMagenta" size="3">
                  View Account Activity Details for ${currAccountUser.role.shortCode}
                   </font>
                  </link>
    </a></td>
  </tr>
	<tr><td><font color="red" size="3">${accountDetailsForm.message}</td></tr>
	<tr><td><font color="blue" size="3">${accountDetailsForm.info}</td></tr>
  </table>

</div>




</form:form>

</body>
</html>
