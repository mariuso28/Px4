<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<head>
<title>numberRefresh</title>

<style>
th{
text-align:center;
}

h2{
font: 20px Arial, sans-serif;
}

h3{
font: 18px Arial, sans-serif;
}


body{
font: 16px Arial, sans-serif;
}

</style>



<link rel="stylesheet" type="text/css" media="all" href="css/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="scripts/jsDatePick.min.1.3.js"></script>



<script type="text/javascript">
	window.onload = function(){


	g_globalObject = new JsDatePick({
			useMode:2,
			target:"startDate",
			dateFormat:"%d-%M-%Y"
			/*,selectedDate:{
				day:5,
				month:9,
				year:2006
			},
			yearsRange:[1978,2020],
			limitToToday:false,
			cellColorScheme:"beige",
			dateFormat:"%m-%d-%Y",
			imgPath:"img/",
			weekStartDay:1*/
		});

	g_globalObject2 = new JsDatePick({
			useMode:2,
			target:"endDate",
			dateFormat:"%d-%M-%Y"/*
			,selectedDate:{
				day:15,
				month:10,
				year:2013
			}
			,
			yearsRange:[1978,2020],
			limitToToday:false,
			cellColorScheme:"beige",
			dateFormat:"%m-%d-%Y",
			imgPath:"img/",
			weekStartDay:1*/
		});

		g_globalObject3 = new JsDatePick({
			useMode:2,
			target:"someDate",
			dateFormat:"%d-%M-%Y"
			/*,selectedDate:{
				day:5,
				month:9,
				year:2006
			},
			yearsRange:[1978,2020],
			limitToToday:false,
			cellColorScheme:"beige",
			dateFormat:"%m-%d-%Y",
			imgPath:"img/",
			weekStartDay:1*/
		});
	};

	function IsValidDate(myDate) {
                var filter = /^([012]?\d|3[01])-([Jj][Aa][Nn]|[Ff][Ee][bB]|[Mm][Aa][Rr]|[Aa][Pp][Rr]|[Mm][Aa][Yy]|[Jj][Uu][Nn]|[Jj][u]l|[aA][Uu][gG]|[Ss][eE][pP]|[oO][Cc]|[Nn][oO][Vv]|[Dd][Ee][Cc])-(19|20)\d\d$/
                                return filter.test(myDate);
            }
	function test() {

		validated = true;				// validation left to server side
		return;

		oFormObject = document.forms['myForm'];

   		var field = "command.startDate";
   		oformElement = oFormObject.elements[field];

		var date= oformElement.value;
		var isValid = IsValidDate(date);
                if (isValid) {
                     // alert('Correct format');
			;
                }
                else {
                    alert('Start Date : ' + date + ' incorrect format, click input box to select correct one');
                }

		var field = "command.endDate";
   		oformElement = oFormObject.elements[field];

		var date1= oformElement.value;
		var isValid1 = IsValidDate(date1);
                if (isValid1) {
                     // alert('Correct format');
			;
                }
                else {
                    alert('End Date : ' + date1 + ' incorrect format, click input box to select correct one');
                }

                return validated= (isValid && isValid1);
            };

	    function validate(theForm)
	    {
		return validated;
	    };

function submitSelectNumber(){


    oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='topNumber';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
};

function sameDayDate(){

    oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='sameDayDate';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
};

function close_window()
{
    close();
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
	    oformElement = oFormObject.elements[field];
		oformElement.value = num4D;

		validated=true;
		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='viewKeyNumber';
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

function testXX()
{
	    alert("CASJKDFSJKDFH");
}


</script>


</head>

<body>

<form:form id="myForm" method="post" action="processAnalytic.html" modelAttribute="numberGridForm" onsubmit="return validate(this)">

<fmt:formatDate value="${currStartDate}" pattern="dd-MMM-yyyy" var="d1" />
<fmt:formatDate value="${currEndDate}" pattern="dd-MMM-yyyy" var="d2" />
<fmt:formatDate value="${currEndDate}" pattern="dd-MMM-yyyy" var="d3" />

<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}
- View Previous Results and Performance</h2>

<input type="hidden" name="command.external" value="${numberGridForm.external}"/>
<input type="hidden" name="command.useNumber" value="" />

<c:choose>
	<c:when test="${numberGridForm.numberGrid.gtype == 4}" >
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

<table align="left" style="margin:0; width:100%; background: #f8f8f8; color:#000;">
<tr/>
<tr>
<c:choose>
<c:when test="${numberGridForm.external == true}">
<td><input type="submit" name="cancel_betrefresh" value="Back" class="button" style="height:30px;"/></td>
<c:set var="div2Offset" value="300px" />
<c:set var="div3Offset" value="600px" />
<c:set var="div4Offset" value="800px" />
</c:when>
<c:otherwise>
<td><input type="submit" name="cancel_refresh" value="Back" class="button" style="height:30px;"/></td>
<c:set var="div2Offset" value="0px" />
<c:set var="div3Offset" value="300px" />
<c:set var="div4Offset" value="500px" />
<c:set var="div5Offset" value="700px" />
</c:otherwise>
</c:choose>
</tr>
</table>

<div style="width:1000px;">
<c:choose>
<c:when test="${numberGridForm.external == true}">
<div style="left:0px;width:300px;float:left;height:150px;overflow:auto;overflow-x:hidden;">
<table border="0" cellpadding="0" cellspacing="2" >
	<tbody align="left" style="font-family:verdana; color:purple; background-color:white;">
	<c:forEach items="${numberBasket}" var="number" varStatus="status">
	<c:choose>
		<c:when test="${status.index % 4 == 0}">
			<tr>
		</c:when>
	</c:choose>
	<td>
		<font color="blue">${number}</font>
	</td>
	<td>
	<a href="processAnalytic.html?method=cancelNumber&number=${number}" style="text-decoration:none;color:rgb(255,0,0)">x
	</td>
	<td>&nbsp&nbsp</td>
	<c:choose>
		<c:when test="${status.index % 4 == 3}">
			</tr>
		</c:when>
	</c:choose>
	</c:forEach>
	</tbody>
</table>
</div>
</c:when>
</c:choose>

<div style="left:${div2Offset};width:300px;float:left;">
<table border="0" cellpadding="0" cellspacing="2">
<tr><td>Past Numbers:</td></tr>
<tr><td style="width:150">Start Date</td>
<td style="width:150">End Date</td>
</tr>
<tr>
<td style="width:150"><input type="text" size="12" maxlength="11" id="startDate" name="command.startDate" value="${d1}" /></td>
<td style="width:150"><input type="text" size="12" maxlength="11" id="endDate" name="command.endDate" value="${d2}"/></td>
</tr>
<table>
	<tr>
	<td>
	<input type="submit" name="numbers_refine" value="Refine Results" class="button" style="height:24px;"/>
	</td>
	</tr>
	<c:choose>
	<c:when test="${numberGridForm.numberGrid.gtype == 4}" >
		<tr><td>Weighted Revenue per 1.00 Stake</td></tr>
		<tr>
			<td>
			<form:select path="command.topNumber" onChange="submitSelectNumber()">
				<c:forEach items="${numberGridForm.topNumbers}" var="nr">
					<option value="${nr.number}">${nr.number} - <fmt:formatNumber value="${nr.revenue}" pattern="#0.00" /></option>
				</c:forEach>
			</form:select>
			</td>
		</tr>
	</c:when>
	</c:choose>
	<tr>
		<td><font color="red" size="3">${numberGridForm.message}</font></td>
	</tr>
</table>
</table>
</div>

<div style="left:${div3Offset};width:200px;float:left;">
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
</div>

<div style="left:${div4Offset};width:200px;float:left;">
<table border="0" cellpadding="0" cellspacing="2">
	<tr><td>Past Draws:</td></tr>
	<tr>
		<td><input type="text" size="12" maxlength="11" id="someDate" name="command.someDate" value="${d3}" />
	</td>
	</tr>
	<tr>
		<td>
		<input type="submit" name="chooseDate" value="Go To Draw" class="button" style="height:30px;"/>
		</td>
	</tr>
	<tr style="height:20px" />
	<tr>
		<td>
		<form:select path="command.sameDayDate" onChange="sameDayDate()">
			<option value="">Same Day Draws</option>
			<c:forEach items="${numberGridForm.sameDayDrawDates}" var="date">
				<fmt:formatDate value="${date}" pattern="dd-MMM-yyyy" var="dstr" />
				<option value="${dstr}">${dstr}</option>
			</c:forEach>
		</form:select>
		</td>
	</tr>
</table>
</div>

<c:choose>
<c:when test="${currUser.role eq 'ROLE_PLAY' || currUser.role eq 'ROLE_ADMIN'}">
</c:when>
<c:otherwise>
	<c:choose>
	<c:when test="${numberGridForm.external == false}">
	<div style="left:${div5Offset};width:200px;float:left;">
	<table border="0" cellpadding="0" cellspacing="2">
	<tr>
		<td>
		<input type="submit" name="maintainExposures" value="Maintain Exposures" class="button" style="height:30px;"/>
		</td>
	</tr>
	</table>
	</div>
	</c:when>
	</c:choose>
</c:otherwise>
</c:choose>

</div>

<c:choose>
<c:when test="${numberGridForm.external == true}">
<div style="left:0px;width:1000px;float:left;">
<table>
<tr>
<td><input type="submit" name="submitBasket" value="Use Choices" class="button" style="height:30px;"/></td>
</tr>
</table>
</div>
</c:when>
</c:choose>

<table>
    <tbody>
	<c:forEach var="row" items="${numberGridForm.numberGrid.numbers}" varStatus="status">
	<c:choose>
	<c:when test="${numberGridForm.numberGrid.gtype == 4 || numberGridForm.numberGrid.gtype == 1}" >
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
		<c:when test="${column.occurences != '0'}">
			<c:choose>
			<c:when test="${column.level eq 'FIRST'}">
			<td>

			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="00B00F" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${column.level eq 'SECOND'}">
			<td>
			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="FFA600" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${column.level eq 'THIRD'}">
			<td>
			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="BA006D" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
		</c:when>
		<c:otherwise>

				<td>
				<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
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
	    <c:when test="${numberGridForm.numberGrid.gtype == 4 || numberGridForm.numberGrid.gtype == 1}" >

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

<body>
