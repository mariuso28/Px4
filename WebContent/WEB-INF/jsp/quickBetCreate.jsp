<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />


<title>quickBetCreate</title>

<style>

th{
text-align:left;
}

h3{
font: 18px Arial, sans-serif;
}


body{
font: 18px Arial, sans-serif;
}

div#info{
    text-align:left;
    background-color:white;
    position:absolute;
    left:400px;
    padding:10px;
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.topheading				{ float:left; width:100%; height:60px; padding:0px; }
.topheadingtext			{ float:left; width:950px; height:30px; padding:0px; font-size:large; padding-top:10px; }
.backbutton				{ float:left; width:90px; height:30px; padding:0px; padding-top:5px; }
.mainbody 				{ width:1200px; height:600px; padding:0px; }
.mainpanel 				{ float:left; width:1040px; height:600px; padding:0px; }
.wideskyscraper 		{ float:left; width:160px;  height:600px; padding:0px; }
.gameheading 			{ float:left; width:100%;   height:150px; padding:0px; }
.providersbar 			{ float:left; width:100%;   height:36px; padding:0px; }
.providersbarpad 		{ float:left; width:100%;   height:16px; padding:0px; }
.numberspanel			{ float:left; width:100%;   height:450px; padding:0px; }
.numberchoicespanel		{ float:left; width:417px;  height:417px; padding:0px; }
.numberchoicespanelpad	{ float:left; width:25px;   height:417px; padding:0px; }
.numberchoices 			{ float:left; width:417px;  height:322px; padding:0px; }
.numberchoicesbuttons 	{ float:left; width:417px;  height:50px; padding:0px; }
.numberchoicesprev 		{ float:left; width:417px;  height:45px; padding:0px; }
.numberpadpanel			{ float:left; width:210px;  height:417px; padding:0px; }
.digitsdisplay 			{ float:left; width:210px;  height:78px; padding:0px; }
.numberpad 				{ float:left; width:210px;  height:417px; padding:0px; vertical-align:bottom; }
.stakespanel			{ float:left; width:337px;  height:417px; padding:0px; }
.stakes 				{ float:left; width:337px;  height:137px; padding:0px; }
.rectangle-large		{ float:left; width:337px;  height:280px; padding:0px; }
.previousbets			{ width:1200px; height:48px; float:left; padding:0px; }
</style>

</head>
<body id="body">

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
		//alert("Testing : " + field);
   		oformElement = oFormObject.elements[field];
		var value = oformElement.value;
		//alert("Value is : " + value);
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

	function testStakes(index)
	{
		var field = "command.betMappings[" + index + "].qstake"
		if (!testNumber(field,'Stake',true))
		{
			return false;
		}
		return true;
	}

function setPlayDate(){

    oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='setPlayDate';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}


function submitViewCalc(){

oFormObject = document.forms['myForm'];

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='viewCalcRefresh';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

function submitViewPrev(){

oFormObject = document.forms['myForm'];

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='viewPrev';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}


function submitRefreshQuickPayouts(index){

    oFormObject = document.forms['myForm'];

    var field = "command.changedIndex"
;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var field1 = "command.betMappings[" + index + "].use"
;
    oformElement1 = oFormObject.elements[field1];
    oformElement1.value = "on";

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshQuickPayouts';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

function submitRefreshQuickPayoutsQ(index){

/*	alert("In : " + index );

     var elem = document.getElementById('myForm').elements;
        for(var i = 0; i < elem.length; i++)
        {
	    alert(elem[i].type + " : " + elem[i].type + " : " + elem[i].value);
        }
*/

    if (testStakes(index)==false)
		return false;

    oFormObject = document.forms['myForm'];

    var field = "command.changedIndex"
;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var field1 = "command.betMappings[" + index + "].use"
;
    oformElement1 = oFormObject.elements[field1];
    oformElement1.value = "on";

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshQuickPayoutsQ';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();

}

function cancelGame(index){

    oFormObject = document.forms['myForm'];

    var field = "command.changedIndex"
;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var field1 = "command.betMappings[" + index + "].use"
;
    oformElement1 = oFormObject.elements[field1];
    oformElement1.value = "off";

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='cancelUseGame';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}


function submitUse(index){

    oFormObject = document.forms['myForm'];
/*
    var elem = document.getElementById('myForm').elements;
        for(var i = 0; i < elem.length; i++)
        {
	    alert(elem[i].name + " : " + elem[i].type + " : " + elem[i].value);
        }
*/
    var field = "command.changedIndex"
;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshUseGame';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

function refreshProviders(index){

    oFormObject = document.forms['myForm'];
 /*
    var elem = document.getElementById('myForm').elements;
        for(var i = 0; i < elem.length; i++)
        {
	    alert(elem[i].type + " : " + elem[i].type + " : " + elem[i].value);
        }
*/
    var field = "command.changedIndex"
;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshProviders';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

function addRandomNumber(){
	oFormObject = document.forms['myForm'];

	var field = "command.number";
//	alert("Testing1 : " + field);

	oformElement = oFormObject.elements[field];
	oformElement.value = num4D;

//	alert("Testing2 : " + num4D);

	field2 = "command.number4D";
//	alert("Testing3 : " + field2);
	oformElement = oFormObject.elements[field2];
	oformElement.value = digitSize;
//	alert("Testing4 : " + digitSize);

	var myin = document.createElement('input');
	myin.type='hidden';
	myin.name='addRandomNumber';
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
		var field = "command.number";
		validated=true;
		// alert('HERE ');
		oformElement = oFormObject.elements[field];
		oformElement.value = num4D;

		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='addNumber';
		myin.value='MaHa';
		oFormObject.appendChild(myin);

//		alert('SUBMITTING ');
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
	validated=true;
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

//	alert("digitSize : " + digitSize);
//	document.getElementById("number4D").innerHTML=digitSize;
    clearDigitFunc();
}

function lookup(){

    oFormObject = document.forms['myForm'];
    validated=true;

		var myin = document.createElement('input');
		myin.type='hidden';
		if (digitSize == '3D')
			myin.name='lookup3D';
		else
			myin.name='lookup4D';
		myin.value='MaHa';
		oFormObject.appendChild(myin);

		oFormObject.submit();
	}

	function change4DFunc(digits)
	{
		oFormObject = document.forms['myForm'];
		var field = "command.number4D";
		oformElement = oFormObject.elements[field];
		oformElement.value = digits;

		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='switchDigits';
		myin.value='MaHa';
		oFormObject.appendChild(myin);
		oFormObject.submit();
	}

	function validate(theForm)
	{
		// alert('VALIDATED ' + validated);
		return validated;
	}



</script>



<form:form id="myForm" method="post" action="quickBet.html" modelAttribute="quickBetForm"
	onsubmit="return validate(this)">
<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;${currUser.contact}
	<br/>
	<c:choose>
	<c:when test="${currUser.enabled==false}" >
		<font color="red">DISABLED FOR BETS - CONTACT AGENT</font>
	</c:when>
	<c:otherwise>
		<font color="red">Create Bets</font>
	</c:otherwise>
	</c:choose>
    </h2>
  </div>
  <div class="leaderboard"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>

<div class="mainbody">
  <div class="mainpanel">
    <div class="gameheading">
	  <div class="topheading">
	  <c:choose>
		<c:when test="${(quickBetForm.availableFunds<'0.0') || (quickBetForm.quickBet.numberMaxed=='true') || (quickBetForm.validBet=='false') || (currPlayer.enabled==false)}" >
			 <div class="backbutton">
			   <a href="quickBet.html?method=cancelQuickBet" ><img src="../../img/back.jpg" width="50" height="30"></a>
			 </div>
		</c:when>
	    <c:otherwise>
		  <div class="backbutton">
		    <a href="quickBet.html?method=cancelQuickBet" onClick="return confirm('Abandon unconfirmed bets?')"><img src="../../img/back.jpg" width="50" height="30"></a>
		  </div>
	    </c:otherwise>
	  </c:choose>
	  <div class="topheadingtext">
	    Game: ${quickBetForm.quickBet.metaGame.name} - ${quickBetForm.quickBet.metaGame.description} &nbsp&nbsp Next Playing: <fmt:formatDate value="${quickBetForm.nextDrawDate}" pattern="dd-MMM-yy HH:mm"/>
      </div>
	  </div>
	  <table cellpadding="6">
        <c:choose>
        <c:when test="${quickBetForm.number4D eq '3D'}">
            <script type="text/javascript">submitRefreshDigits3()</script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">submitRefreshDigits4()</script>
        </c:otherwise>
        </c:choose>

        <tr>
        <c:set var="linkColor" value="green" scope="page" />
        <c:choose>
        <c:when test="${currPlayer.account.balance < '0'}">
            <c:set var="linkColor" value="red" scope="page"/>

        </c:when>
        </c:choose>
        <td>Your Account Balance:
        <font color=${linkColor} size="3">
        <fmt:formatNumber value="${currPlayer.account.balance}" pattern="#0.00" />
        </font>
        </td>
        <!--
        <td>Your Account Balance: <fmt:formatNumber value="${currPlayer.account.balance}" pattern="#0.00" /></td>

        -->

      <c:set var="linkColor" value="green" scope="page" />
        <c:choose>
        <c:when test="${quickBetForm.availableFunds < '0'}">
            <c:set var="linkColor" value="red" scope="page"/>

        </c:when>
        </c:choose>
        <td>&nbsp&nbsp&nbspAvailable Funds:
        <font color=${linkColor} size="3">
        <fmt:formatNumber value="${quickBetForm.availableFunds}" pattern="#0.00" />
        </font>
        </td>

        </tr>
        <tr>
        <td>Total Stake: <fmt:formatNumber value="${quickBetForm.totalStake}" pattern="#0.00" /></td>

        </tr>
        <tr></tr>
        <tr></tr>
      </table>
    </div>

	<input type="hidden" name="command.changedIndex" value="-1" />
	<input type="hidden" name="command.number" value="" />
	<input type="hidden" name="command.number4D" value=""/>
	<input type="hidden" name="command.confirmCommand" value="${quickBetForm.quickBet.confirmCommand}" />

    <div class="providersbar">
	  <table border="0" cellpadding="6" cellspacing="0" width="1040">
        <tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">

        <tr>
        <c:forEach items="${quickBetForm.quickBet.metaGame.providers}" var="provider" varStatus="status">
        <c:set var="cnt" value="${status.index}"/>
        <td>${provider}
        <c:choose>
        <c:when test="${quickBetForm.quickBet.providers[cnt] eq provider}">
        <input type="checkbox" size="8" name="command.useProviders[${cnt}]"
                    value="${provider}" checked  onClick="refreshProviders(${status.index})"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" size="8" name="command.useProviders[${cnt}]"
                    value="${provider}" onClick="refreshProviders(${status.index})"/>
        </c:otherwise>
        </c:choose>
        </td>
        </c:forEach>


        <td>

        <c:choose>
        <c:when test="${quickBetForm.quickBet.enable4D == 'true' && quickBetForm.quickBet.enable3D == 'true'}">
        <form:select path="${command.number4D}" onchange="change4DFunc(value);">
            <option value='4D' ${quickBetForm.number4D eq '4D' ? 'selected' : ''}>4 Digits</option>
            <option value='3D' ${quickBetForm.number4D eq '3D' ? 'selected' : ''}>3 Digits</option>
        </form:select>
        </c:when>
        </c:choose>
        </td>

        <td>Draw Date</td>
        <td>
            <form:select path="command.playDate" onChange="setPlayDate()">
                <c:forEach items="${quickBetForm.availableDates}" var="date">
                    <fmt:formatDate value="${date}" pattern="dd-MMM-yyyy" var="dstr" />
                    <option value="${dstr}" ${date eq quickBetForm.quickBet.playGame.playDate ? 'selected' : ''}>
                    ${dstr} </option>
                </c:forEach>
            </form:select>
        </td>


        <c:choose>
        <c:when test="${quickBetForm.quickBet.viewCalc == 'true'}">
        <td>
        ${quickBetForm.totalStakeSingleProvider
        } x ${quickBetForm.providerCount} = ${quickBetForm.totalStake
        }
        </td>
        </c:when>
        </c:choose>


        </tr>
        <tr></tr>
        <tr></tr>
        </tbody>
      </table>
	</div>

    <div class="providersbarpad"> </div>

    <div class="numberspanel">
      <div class="numberchoicespanel">
        <div class="numberchoices">
		  <table border="0" cellpadding="0" cellspacing="2">
            <tr><td>Number Choices</td></tr>
          </table>

          <div style="height:200px; overflow:auto; overflow-x:hidden;">
          <table border="0" cellpadding="0" cellspacing="2" >
          <tbody align="left" style="font-family:verdana; color:purple; background-color:white;">
          <c:forEach items="${quickBetForm.quickBet.numbers}" var="number" varStatus="status">
          <c:choose>
          <c:when test="${status.index % 5 == 0}">
              <tr>
          </c:when>
          </c:choose>
              <c:set var="slen" value="${fn:length(number)}" />
              <c:choose>
              <c:when test="${slen == 4}">
                  <c:set var="prefix" value="${fn:substring(number, 0, 1)}"/>
                  <c:set var="suffix" value="${fn:substring(number, 1, 4)}"/>
                  <c:choose>
                  <c:when test="${quickBetForm.quickBet.enable4D == false}" >
                      <c:choose>
                      <c:when test="${quickBetForm.quickBet.enable3D == false}" >
                          <td>
                          <font color="lightgrey">${number}</font>
                          </td>
                      </c:when>
                      <c:otherwise>
                      <td>
                      <font color="lightgrey">${prefix}</font>
                      <font color="green">${suffix}</font>
                      </td>
                      </c:otherwise>
                      </c:choose>
                  </c:when>
                  <c:otherwise>
                      <c:choose>
                      <c:when test="${quickBetForm.quickBet.enable3D == false}" >
                          <td>
                          <font color="green">${prefix}</font>
                          <font color="lightgrey">${suffix}</font>
                          </td>
                      </c:when>
                      <c:otherwise>
                          <td>
                          <font color="green">${number}</font>
                          </td>
                      </c:otherwise>
                      </c:choose>
                  </c:otherwise>
                  </c:choose>
              </c:when>
              <c:otherwise>
                  <c:choose>
                  <c:when test="${quickBetForm.quickBet.enable3D == false}" >
                      <td>
                      <font color="lightgrey">${number}</font>
                      </td>
                  </c:when>
                  <c:otherwise>
                      <td>
                      <font color="green">${number}</font>
                      </td>
                  </c:otherwise>
                  </c:choose>
              </c:otherwise>
              </c:choose>
              <td>
              <a href="maintainNumber.html?method=cancelNumber&number=${number}" style="text-decoration:none;color:rgb(255,0,0)">x
              </td>
              <td>&nbsp&nbsp</td>
          <c:choose>
          <c:when test="${status.index % 5 == 4}">
              </tr>
          </c:when>
          </c:choose>
          </c:forEach>
          </tbody>
          </table>
          </div>
		</div>
        <div class="numberchoicesbuttons">
          <table>
            <tr><td><font color="red" size="3">${quickBetForm.message}</font></td></tr>
            <c:choose>
              <c:when test="${quickBetForm.availableFunds < '0'}">
              <tr><td><font color="red" size="3">Insufficient funds to support current bet</font></td></tr>
              </c:when>
            </c:choose>
            <br/>
            <tr><td>
              <c:choose>
                <c:when test="${quickBetForm.quickBet.confirmCommand != 'None'}" >
                <input type="hidden" name="command.confirmCommandObject" value="${quickBetForm.quickBet.confirmCommandObject}" />
                <input type="submit" name="confirmQuickBetCommand" value="Confirm" class="button" style="height:23px;"/>
                <input type="submit" name="cancelQuickBetCommand" value="Cancel" class="button" style="height:23px;"/>
                </c:when>
              <c:otherwise>
                <c:choose>
				<c:when test="${(quickBetForm.availableFunds<'0.0') || (quickBetForm.quickBet.numberMaxed=='true') || (quickBetForm.validBet=='false') || (currPlayer.enabled==false)}" >
				  <input type="submit" disabled="" name="confirmQuickBet" value="Confirm Bets" class="button" style="height:23px;"/>
                  <input type="submit" name="cancelQuickBet" value="Done" class="button" style="height:23px;"/>
                  </c:when>
                  <c:otherwise>
                    <input type="submit" name="confirmQuickBet" value="Confirm Bets" class="button" style="height:23px;"/>
                    <input type="submit" name="cancelQuickBet" value="Done" class="button" style="height:23px;" onClick="return confirm('Abandon unconfirmed bets?')"/>
                  </c:otherwise>
                </c:choose>
              </c:otherwise>
              </c:choose>
            </td></tr>
          </table>
		</div>
      </div>
      <div class="numberchoicespanelpad"> </div>
      <div class="numberpadpanel">
        <div class="numberpad">
		  <table border="0" cellpadding="3" cellspacing="0">
            <tbody align="left" style="font-family:verdana; color:purple; background-color:white">
                <!--
                <input type="text" size="3px" maxlength="4px" name="command.number"
                        value=""
                        onkeypress='return (event.charCode >= 48 && event.charCode <= 57)'/>
                <input type="submit" name="addNumber" value="use" class="button" style="height:23px;"/>
                -->

                <tr>
                <td colspan="5">
                <table border="1">
                  <tr style="height:36px;font-size:24px;" >
                    <td style="width:34px;"><div id="etd1">&nbsp</div></td>
                    <td style="width:34px;"><div id="etd2">&nbsp</div></td>
                    <td style="width:34px;"><div id="etd3">&nbsp</div></td>
                    <td style="width:34px;"><div id="etd4">&nbsp</div></td>
                    <td style="width:32px;" align="center"><input type="submit" name="clearDigit" value="C" class="button"
                                    onClick="clearDigitFunc();" style="height:34px;width:32px;" /></td>
                  </tr>
                </table>
                </td>
                </tr>
                <tr>
                <td colspan="6">
                  <table table id="songlist" border="1">
                    <tr>
                      <td><input type="submit" name="addDigit" value="7" class="button" onClick="addDigitFunc(7);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="8" class="button" onClick="addDigitFunc(8);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="9" class="button" onClick="addDigitFunc(9);" style="height:60px;width:60px;font-size:30px;" /></td>
                    </tr>
                    <tr>
                      <td><input type="submit" name="addDigit" value="4" class="button" onClick="addDigitFunc(4);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="5" class="button" onClick="addDigitFunc(5);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="6" class="button" onClick="addDigitFunc(6);" style="height:60px;width:60px;font-size:30px;" /></td>
                    </tr>
                    <tr>
                      <td><input type="submit" name="addDigit" value="1" class="button" onClick="addDigitFunc(1);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="2" class="button" onClick="addDigitFunc(2);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" name="addDigit" value="3" class="button" onClick="addDigitFunc(3);" style="height:60px;width:60px;font-size:30px;" /></td>
                    </tr>
                    <tr>
                      <td><input type="submit" name="addDigit" value=&reg; class="button" onClick="addRandomNumber();"style="height:60px;width:60px;font-size:36px;" />
                      <div id="info">Create or Complete Random Number</div></td>
                      <td><input type="submit" name="addDigit" value="0" class="button" onClick="addDigitFunc(0);" style="height:60px;width:60px;font-size:30px;" /></td>
                      <td><input type="submit" value="?" class="button" onClick="lookup();" style="height:60px;width:60px;font-size:30px;" />
                      <div id="info">Query Number from Previous Results</div></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
		</div>
      </div>
      <div class="numberchoicespanelpad"> </div>
      <div class="stakespanel">
        <div class="stakes">
		  <div style="left:500px;width:500px;float:left;">
            <table border="0" cellpadding="3" cellspacing="0">
              <tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
                <c:set var="gNum" value="${fn:length(quickBetForm.quickBet.metaGame.games)}" />
                <c:forEach items="${quickBetForm.quickBet.metaGame.games}" var="game" varStatus="status">
                <tr>
                    <c:set var="cnt" value="${status.index}" />
                    <c:set var="qstake" value="${quickBetForm.quickBet.stakes[cnt]}" />
                    <c:set var="pnum" value="${fn:length(quickBetForm.payOuts[cnt])}" />
                    <input type="hidden" name="command.betMappings[${status.index}].gameId" value="${game.id}" />
                    <input type="hidden" name="command.betMappings[${status.index}].use" value="${quickBetForm.quickBet.useGames[cnt]}" />
                    <c:choose>
                      <c:when test="${quickBetForm.quickBet.useGames[cnt] eq 'on'}">
                        <td align="left">
                            ${game.gtype}
                    
                        </td>
                      </c:when>
                      <c:otherwise>
                        <td align="left"><font color="lightgrey">
                            ${game.gtype}
                            </font>
                        </td>
                      </c:otherwise>
                    </c:choose>
                    <td align="left">

                    <form:select path="command.betMappings[${status.index}].stake" style="width: 60px; float: left;"
                            value="${qstake}" onChange="submitRefreshQuickPayouts(${status.index})"
                            >
                    <c:forEach items="${quickBetForm.stakeLists[cnt]}" var="stake" varStatus="status1">
                            <c:choose>
                            <c:when test="${quickBetForm.quickBet.stakes[cnt] == stake}">
                            <option value="${stake}" ${stake == qstake ? 'selected' : ''}>
                                <fmt:formatNumber value="${stake}" pattern="#0.00" />
                            </option>
                            </c:when>
                            <c:otherwise>
                            <option value="${stake}"}>
                                <fmt:formatNumber value="${stake}" pattern="#0.00" />
                            </option>
                            </c:otherwise>
                            </c:choose>
                    </c:forEach>
                    </form:select>

                    <c:set var="showStake"><fmt:formatNumber value="${qstake}" pattern="#0.00"/></c:set>
                    <input style="width: 45px; margin-left: -59px; margin-top: 1px; border: none; float: left;"
                        type="text" size="4px" maxlength="5px" name="command.betMappings[${status.index}].qstake"
                        value="${showStake}"
                        onkeypress='return (event.charCode >= 48 && event.charCode <= 57)
                                || (event.charCode == 46)'/>

                    <c:set var="tick" value="&#10003"/>
                    <input type="submit" name="setStake" value="${tick}"
                                class="button" style="height:23px;"
                        onClick="return submitRefreshQuickPayoutsQ(${status.index})"/>

                    <input type="submit" name="cancelGameX" value="x"
                                class="button" style="height:23px;"
                        onClick="return cancelGame(${status.index})"/>

                    </td>
                    <td>
                      <select name="payOutPath" style="width:150px;">
                      <c:forEach items="${quickBetForm.payOuts[cnt]}" var="payOut" varStatus="status1">
                              <option value="XX"}>
                              ${payOut.type}
                              <fmt:formatNumber value="${payOut.payOut}" pattern="#0.00" />
                              </option>
                          </c:forEach>
                      </select>
                    </td>
                    <c:choose>
                    <c:when test="${quickBetForm.quickBet.viewCalc == 'true'}">
                    <div float="right">
                    <tr><td></td><td></td>
                    <td></td>
                    <td>
                    <c:choose>
                    <c:when test="${game.gtype.digits == '3'}">
                    <c:set var="tstake" value = "${quickBetForm.quickBet.number3D * qstake * mnum}"/>

                        <td><font color="black" size="2">x${quickBetForm.quickBet.number3D}=${tstake}=<fmt:formatNumber value="${tstake}" pattern="#0.00" /></font></td>
                    </c:when>
                    <c:otherwise>
                    <c:set var="tstake" value = "${quickBetForm.quickBet.number4D * qstake * mnum}"/>

                        <td><font color="black" size="2">x${quickBetForm.quickBet.number4D}=${tstake}=<fmt:formatNumber value="${tstake}" pattern="#0.00" /></font></td>
                    </c:otherwise>
                    </c:choose>
                    </td>
                    <tr>
                    </div>
                    </c:when>
                    </c:choose>
                </tr>
                </c:forEach>
                <c:choose>
                <c:when test="${quickBetForm.quickBet.viewCalc == 'true'}">
                <tr>
                    <td>&nbsp&nbsp</td><td>&nbsp&nbsp</td>
                    <td>&nbsp&nbsp</td><td>&nbsp&nbsp</td>
                    <td>&nbsp&nbsp</td>
                    <td>
                        Total: ${quickBetForm.totalStakeSingleProvider
                }

                    </td>
                </tr>
                </c:when>
                </c:choose>
              </tbody>
            </table>
            </div>
		</div>
<!--        <div class="rectangle-large"> <img src="../../img/rectangle-large.png" width="336" height="280" /> </div> -->
      </div>
    </div>
  </div>
<!--  <div class="wideskyscraper"> <img src="../../img/skyscraper.png" width="160" height="600" /> </div> -->
  <div class="previousbets">
    <c:set var="displayImages" value="true"/>
      <c:choose>
        <c:when test="${empty quickBetForm.displayMetaBetList}">
        </c:when>
        <c:otherwise>
        <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
          <tr><td width="250">View Previous Bets:</td>
            <td>
                <c:choose>
                  <c:when test="${quickBetForm.quickBet.viewPrev == 'true'}">
                    <input type="checkbox" size="8" name="command.viewPrev" value="false" checked onClick="submitViewPrev()"/>
                    <c:choose>
                      <c:when test="${quickBetForm.currentPage>1}">
                          <td><a href="quickBet.html?prevBetsLast"><img src="../../img/monthBackward_normal.gif"/></a></td>
                      </c:when>
                    </c:choose>
                    <c:choose>
                      <c:when test="${quickBetForm.currentPage<quickBetForm.lastPage}">
                        <td><a href="quickBet.html?prevBetsNext"><img src="../../img/monthForward_normal.gif"/></a></td>
                      </c:when>
                    </c:choose>
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox" size="8" name="command.viewPrev" value="true" onClick="submitViewPrev()"/>
                  </c:otherwise>
                </c:choose>
              </td>
            </td>
          </tr>
        </table>

        </br>
        <c:choose>
        <c:when test="${quickBetForm.quickBet.viewPrev == 'true'}">
        <c:set var="displayImages" value="false"/>


        <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
            <tr style="font-family:verdana; color:purple; background-color:LightYellow">
              <td style="width:20%;">Placed</td>
              <td style="width:10%;">Draw Date</td>
              <td style="width:10%;" align="right">Total Stake</td>
              <td style="width:10%;" align="right">Commission</td>
              <td style="width:15%;" align="right">Total Stake ExComm</td>
              <td style="width:10%;" align="right">Total Win</td>
              <td style="width:7%;"></td>
            </tr>
        </table>
        </br>
        <c:forEach items="${quickBetForm.displayMetaBetList}" var="dispBet" varStatus="status">
        <div style="left:0px">
          <table align="left" style="margin:0; padding:0px; width:100%; background: #f8f8f8; color:#000;">
                <tr>
                <td style="width:20%;"><a href="quickBet.html?method=expandBets&id=${dispBet.metaBet.id}">${dispBet.expanded}<fmt:formatDate value="${dispBet.metaBet.placed}" pattern="dd-MMM-yy  HH:mm:ss"/></td>
                <td style="width:10%;"><fmt:formatDate value="${dispBet.metaBet.playGame.playDate}" pattern="dd-MMM-yy"/></td>
                <td style="width:10%;" align="right"><fmt:formatNumber value="${dispBet.totalStake}" pattern="#0.00" /></td>
                <td style="width:10%;" align="right"><fmt:formatNumber value="${dispBet.totalWin}" pattern="#0.00" /></td>
                <td style="width:7%;" align="right">
                <a href="quickBet.html?method=useMetaBet&id=${dispBet.metaBet.id}">use
                </td>
            </tr>
          </table>
        </div>
            <c:choose>
              <c:when test="${dispBet.expanded eq '-'}">
              <tr>
              <div style="float:left; width:30%; font-size: 15px;">
              <table>
              <thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
                <tr>
                  <th>Bet Type</th>
                  <th>Stake</th>
                </tr>
              </thead>
              <tbody align="left" style="font-family:arial; font-size:15px; color:purple; background-color:LightCyan;">
                <c:forEach items="${dispBet.metaBet.bets}" var="bet" varStatus="status1">
                  <tr>
                    <td>${bet.game.gtype}</td>
                    <td><fmt:formatNumber value="${bet.stake}" pattern="#0.00" /></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            </div>
            <div style="float:left; width:30%; font-size: 15px;">
              <table>
                <thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
                  <tr>
                    <th>Providers</th>
                  </tr>
                </thead>
                <tbody align="left" style="font-family:verdana; font-size:15px; color:purple; background-color:LightCyan">
                  <c:forEach items="${dispBet.providers}" var="provider" varStatus="status1">
                          <tr>
                      <td width="100%">${provider}</td>
                      </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <div style="float:left; width:30%; font-size: 15px;">
              <table>
                <thead style="font-family:arial; font-size:15px; color:grey; background-color=white;">
                  <tr>
                    <th>Numbers</th>
                  </tr>
                </thead>
                <tbody align="left" style="font-family:verdana; font-size:15px; color:purple; background-color:LightCyan">
                  <c:forEach items="${dispBet.metaBet.choices}" var="choice" varStatus="status1">
                      <c:choose>
                        <c:when test="${status1.index % 5 == 0}">
                            <tr><td align="right">${choice}</td>
                        </c:when>
                        <c:otherwise>
                          <td align="left">${choice}</td>
                        </c:otherwise>
                      </c:choose>
                      <c:choose>
                        <c:when test="${status1.index % 5 == 4}">
                          </tr>
                        </c:when>
                      </c:choose>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <div style="float:left; width:10%; font-size: 15px;">
              <c:choose>
                <c:when test="${dispBet.totalWin > 0}">
                    <td><a href="quickBet.html?method=expandWins&id=${dispBet.metaBet.id}">View wins</td>
                </c:when>
              </c:choose>
            </div>
            </tr>
            </c:when>
            </c:choose>
        </c:forEach>
        <br/>
        </c:when>
        </c:choose>
        </c:otherwise>
      </c:choose>
	  <c:choose>
        <c:when test="${displayImages == true}">
          <table align="left" style="margin:0; padding:10px; width:80%; background: #f8f8f8; color:#000;">
			 <c:set var="nlen" value="${fn:length(quickBetForm.quickBet.numbers)}" />
			 <c:forEach items="${quickBetForm.quickBet.numbers}" var="number" varStatus="status">
              <c:choose>
                <c:when test="${status.index % 6 == 0}">
                  <tr>
                </c:when>
              </c:choose>
              <c:set var="use" value="${number}"/>
              <c:set var="slen" value="${fn:length(number)}" />
              <c:choose>
                <c:when test="${slen == 4}">
                  <c:set var="use" value="${fn:substring(number, 1, 4)}"/>
                </c:when>
              </c:choose>
			  <td>
                  <table style="width:150px;float:left;">
                      <tr>
                      <td>
                      <table style="width:150px;float:left;">
                          <tr>
                              <div align="center">
                                  ${number}
                              </div>
                          </tr>
                      </table>
                      <table style="width:150px;float:left;">
                          <tr>
                              <td>
                                  <div align="center">
                                      <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
                                          <img width="100" height="100" src='../../img/${use}.png' border='0'>
                                      </span>
                                  </div>
                              </td>
                          </tr>
                      </table>
                      <table>
                          <tr>
                              <div align="center">
                                  ${quickBetForm.quickBet.numberDescs[status.index]}
                              </div>
                          </tr>
                      </table>
					  <table>
                          <tr>
                              <div align="center"">
                                  ${quickBetForm.quickBet.numberDescsCh[status.index]}
                              </div>
                          </tr>
                      </table>
                  </table>
              </td>

              <c:choose>
                <c:when test="${status.index % 6 == 5}">
					</br>
					</tr>
                </c:when>
				<c:otherwise>
				<c:choose>
                <c:when test="${status.index == (nlen-1)}">
					<c:forEach var="index" begin="${status.index % 6}" end="5">
						<td>
						<table style="width:150px;float:left;">
                      <tr>
                      <td>
                      <table style="width:150px;float:left;">
                          <tr>
                              <div align="center">
                                 &nbsp
                              </div>
                          </tr>
                      </table>
						</td>
					</table>
					</td>

					</c:forEach>
				</c:when>
				</c:choose>
			  </c:otherwise>
			  </c:choose>

            </c:forEach>
          </table>
        </c:when>
      </c:choose>
  </div>
</div>














</form:form>

</body>
</html>
