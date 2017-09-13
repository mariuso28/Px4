<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>

<head>
    <title>maintainExposures</title>
<style>
th{
text-align:center;
<style type="text/css">
   a {
      text-decoration:none;
   }


}

body{
font: 24px Arial, sans-serif;
}
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

var validated = true;
var num4D = "";
var digitSize = '4D';

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

		function submitSelectBetExpo(){

			oFormObject = document.forms['myForm'];
			var myin = document.createElement('input');
			myin.type='hidden';
			myin.name='selectBetExpo';
			myin.value='MaHa';
			oFormObject.appendChild(myin);
			oFormObject.submit();
		}

		function submitSelectWinExpo(){

			oFormObject = document.forms['myForm'];
			var myin = document.createElement('input');
			myin.type='hidden';
			myin.name='selectWinExpo';
			myin.value='MaHa';
			oFormObject.appendChild(myin);
			oFormObject.submit();
		}

function addDigitFunc(index){

	/*
    var elem = document.getElementById('myForm').elements;
        for(var i = 0; i < elem.length; i++)
        {
	    alert(elem[i].type + " : " + elem[i].type + " : " + elem[i].value);
        }

   */

    num4D = num4D + index;

	if ((num4D.length == 3 && digitSize == '3D') || (num4D.length == 4 && digitSize == '4D'))
	{
		oFormObject = document.forms['myForm'];
		var field = 'command.useNumber';
		validated=true;
	    oformElement = oFormObject.elements[field];
		oformElement.value = num4D;

		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='keyNumberExpo';
		myin.value='MaHa';
		oFormObject.appendChild(myin);

		oFormObject.submit();
	}

	if (digitSize == '4D')
	{
    if (num4D.length == 1)
		document.getElementById("etd1").innerHTML=index;
    else
    if (num4D.length == 2)
		document.getElementById("etd2").innerHTML=index;
    else
    if (num4D.length == 3)
		document.getElementById("etd3").innerHTML=index;
    else
    if (num4D.length == 4 && digitSize == '4D')
		document.getElementById("etd4").innerHTML=index;
	}
    else
	{
	if (num4D.length == 1)
		document.getElementById("etd2").innerHTML=index;
    else
    if (num4D.length == 2)
		document.getElementById("etd3").innerHTML=index;
    else
    if (num4D.length == 3)
		document.getElementById("etd4").innerHTML=index;
	}

	validated=false;
}

function clearDigitFunc()
{
	num4D = '';
	document.getElementById("etd1").innerHTML='&nbsp';
    document.getElementById("etd2").innerHTML='&nbsp';
    document.getElementById("etd3").innerHTML='&nbsp';
    document.getElementById("etd4").innerHTML='&nbsp';
}

function submitRefreshDigits4()
{
	digitSize = '4D';
//	document.getElementById("number4D").innerHTML=digitSize;
    clearDigitFunc();
}

function submitRefreshDigits3()
{
	digitSize = '3D';
//	document.getElementById("number4D").innerHTML=digitSize;
    clearDigitFunc();
}

</script>


</head>


<body>



<form:form method="post" id="myForm" action="processAnalytic.html" modelAttribute="numberExpoForm"
		onsubmit="return validate(this)">
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}</h2>

<h2>Maintain Number Exposures</h2>

<tr>
<td>
<td>
<a href="${numberExpoForm.returnAdress}"><img src="../../img/back.jpg" width="50" height="30"></a>
</td>
</tr>

<input type="hidden" name="command.useNumber" value="" />

<c:choose>
	<c:when test="${numberGridExpo.gtype == 4}" >
		<script type="text/javascript">
			submitRefreshDigits4();
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			submitRefreshDigits3();
		</script>
	</c:otherwise>
</c:choose>

<table cellspacing="1" cellpadding="0" border="0"
 bgcolor="white" id="shell" height="100" width="1000">
    <tr>
   <td bgcolor="lightcyan" width=700px">
   <table border="0" cellpadding="3" cellspacing="0">
	<tr>
	<td>Default Max Total Bet:</td>
	<c:choose>
	<c:when test="${numberExpoForm.defaultExpo.betExpo < 0}">
	   	<td>
Unlimited</td>
	</c:when>
	<c:otherwise>
		<td>
${numberExpoForm.defaultExpo.betExpo}</td>
	</c:otherwise>
	</c:choose>
	<td>
	    <input type="text" name="command.betExpo"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"
	</td>
	<td><input type="submit" name="modifyBetExposure" value="Modify"
					onclick="testBet()" style="height:23px;"/></td>
	<td><input type="submit" name="modifyUnLimitedBetExposure" value="Unlimited"
					style="height:23px;"/></td>
	<td>

    <form:select path="command.betNumber" onChange="submitSelectBetExpo()">
	<option value="bn">Bet Exposures</option>
	<c:forEach items="${numberExpoForm.betExpos}" var="nr">
		<c:choose>
		<c:when test="${nr.betExpo>=0}" >
    			<option value="${nr.number}">${nr.number} - <fmt:formatNumber value="${nr.betExpo}" pattern="#0.00" /></option>
  		</c:when>
		</c:choose>
	</c:forEach>
	</form:select>
    	</td>
	</tr>
	<tr>
	<td>Default Max Total Win:</td>
	<c:choose>
	<c:when test="${numberExpoForm.defaultExpo.winExpo < 0}">
	   	<td>
Unlimited</td>
	</c:when>
	<c:otherwise>
		<td>
${numberExpoForm.defaultExpo.winExpo}</td>
	</c:otherwise>
	</c:choose>
	<td>
	    <input type="text" name="command.winExpo"
			value="<fmt:formatNumber value="${0.00}" pattern="#0.00" />"
	</td>
	<td><input type="submit" name="modifyWinExposure" value="Modify"
					onclick="testWin()" style="height:23px;"/></td>
	<td><input type="submit" name="modifyUnLimitedWinExposure" value="Unlimited"
					style="height:23px;"/></td>
	<td>
	<form:select path="command.winNumber" onChange="submitSelectWinExpo()">
	<option value="wn">Win Exposures</option>
	<c:forEach items="${numberExpoForm.winExpos}" var="nr">
		<c:choose>
		<c:when test="${nr.winExpo>=0}" >
    			<option value="${nr.number}">${nr.number} - <fmt:formatNumber value="${nr.winExpo}" pattern="#0.00" /></option>
  		</c:when>
		</c:choose>
	</c:forEach>
	</form:select>
    	</td>
	</tr>

    </table>
    </td>

    <td bgcolor="lightcyan" width="300px">

	<table border="0" cellpadding="3" cellspacing="0">
	<tbody align="left" style="font-family:verdana; color:purple; background-color:white">
	<tr>
	<td>
	<table id="songlist" border="1">
		<tr style="height:36px;font-size:24px;" >
		<td style="width:23px;"><div id="etd1">&nbsp</div></td>
		<td style="width:23px;"><div id="etd2">&nbsp</div></td>
		<td style="width:23px;"><div id="etd3">&nbsp</div></td>
		<td style="width:23px;"><div id="etd4">&nbsp</div></td>
		<td style="width:23px;"><input type="submit" name="clearDigit" value="C" class="button" onClick="clearDigitFunc();" style="height:30px;width:30px;" /></td>
		</tr>
		<tr>
		<td><input type="submit" name="addDigit" value="7" class="button" onClick="addDigitFunc(7);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="8" class="button" onClick="addDigitFunc(8);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="9" class="button" onClick="addDigitFunc(9);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="4" class="button" onClick="addDigitFunc(4);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="5" class="button" onClick="addDigitFunc(5);" style="height:30px;width:30px;font-size:20px;" /></td>
		</tr>
		<tr>
		<td><input type="submit" name="addDigit" value="6" class="button" onClick="addDigitFunc(6);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="1" class="button" onClick="addDigitFunc(1);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="2" class="button" onClick="addDigitFunc(2);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="3" class="button" onClick="addDigitFunc(3);" style="height:30px;width:30px;font-size:20px;" /></td>
		<td><input type="submit" name="addDigit" value="0" class="button" onClick="addDigitFunc(0);" style="height:30px;width:30px;font-size:20px;" /></td>
		</tr>
		</table>
	</td>
	</tr>
	</tbody>
	</table>
	</td>
</tr>
</table>
<table>

</table>
<table>
    <tbody>
        <c:forEach var="row" items="${numberGridExpo.numbers}" varStatus="status">
<!--	<tr><td>${numberGridExpo.gtype}  XXX ${status.index} XXX ${status.index % 2 == 0}</td></tr> -->
	<c:choose>
	<c:when test="${numberGridExpo.gtype == 4 || numberGridExpo.gtype == 1}" >

	    <tr>
	</c:when>
	<c:otherwise>
	    <c:choose>
	    <c:when test="${status.index % 4 == 0}">

	    	<tr>
            </c:when>
	    </c:choose>
	</c:otherwise>

	</c:choose>

<c:forEach var="column" items="${row}" varStatus="status1">
		<c:choose>
		<c:when test="${column.betExposure>=0.0 || column.winExposure>=0.0}">
			<td>
			<a href ="processAnalytic.html?method=viewNumberExposure&number=${column.number}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="red" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
		</c:when>
		<c:otherwise>

			<td>
			<a href ="processAnalytic.html?method=viewDefaultNumberExposure&number=${column.number}" style="text-decoration:none;">
               		<link rel="xyz" type="text/css" href="color.css">
               		<font color="black" size="3">
               			${column.number}
              		</font>
               		</link>
               		</a>
			</td>
		</c:otherwise>

		</c:choose>
	    </c:forEach>
	    <c:choose>
	    <c:when test="${numberGridExpo.gtype == 4 || numberGridExpo.gtype == 1}" >

	    	</tr>
	    </c:when>
	    <c:otherwise>
	    <c:choose>
	    <c:when test="${status.index % 4 == 3}">

	    	</tr>
            </c:when>
	    </c:choose>
	    </c:otherwise>
	    </c:choose>
        </c:forEach>
     </tbody>
</table>

</form:form>
</body>
</html>
