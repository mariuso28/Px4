
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<title>numberRefreshX</title>

<style>
th{
text-align:center;
}

h3{
font: 20px Arial, sans-serif;
}


body{
font: 18px Arial, sans-serif;
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.backbutton				{ float:left; width:90px; height:30px; padding:0px; padding-top:5px; }
.topheadingtext			{ float:left; width:1110px; height:30px; padding:0px; font-size:large; padding-top:10px; }
.topheading				{ float:left; width:1200px; height:40px; padding:0px; }
.topspacer 				{ width:1200px; height:16px; background-color:#ffd6d6; padding:0px; }
.spacer_h_16			{ height:16px; float:left; padding:0px; }
.spacer_v_16			{ width:16px; float:left; padding:0px; }
.mainbody 				{ width:1200px; float:left; height:600px; padding:0px; }
.mainbodypanel			{ width:1024px; float:left; height:600px; padding:0px; }
.mainbodypanel2			{ width:1008px; float:left; height:78px; padding:0px; background-color:#ffd6d6; }
.mainbodypaneltop		{ width:1008px; float:left; height:90px; padding:0px; }
.mainbodypanelheading	{ width:1008px; float:left; height:40px; padding:0px; }
.mainbodypanelbutton	{ width:1008px; float:left; height:38px; padding:0px; background-color:#f8f8f8; }
.panel_left				{ width:300px; float:left; height:600px; padding:0px; }
.panel_left_num			{ width:300px; float:left; height:210px; padding:0px; border:solid; border-width:1px; }
.panel_left_buttons		{ width:300px; float:left; height:56px; padding:0px; }
.panel_left_button1		{ width:150px; float:left; height:56px; padding:0px; }
.panel_left_button2		{ width:150px; float:left; height:56px; padding:0px; text-align:right}
.panel_left_content1	{ width:300px; float:left; height: 250px; border:solid; border-width:1px; }
.panel_middle			{ width:390px; float:left; height:540px; padding:0px; }
.panel_middle_1			{ width:390px; float:left; height:170px; padding:0px; border:solid; border-width:1px; }
.panel_middle_3			{ width:390px; float:left; height:103px; padding:0px; border:solid; border-width:1px; }
.panel_middle_4			{ width:390px; float:left; height:238px; padding:0px; }
.panel_middle_spacer	{ width:390px; float:left; height:16px; padding:0px; }
.panel_right			{ width:300px; float:left; height:540px;}
.panel_right_content1	{ width:300px; float:left; height: 250px; border:solid; border-width:1px; }
.panel_right_content2	{ width:300px; float:left; height: 250px; border:solid; border-width:1px; }
.range_selection		{ width:1200px; float:left; }
.picture_cell			{ width:194px; float:left; height:148px; border:solid; border-width:1px; }
.picture_cell_spacer	{ width:47px; float:left; height:148px; }
.picture_row			{ width:1200px; float:left; height:148px; }
.skyscraper				{ width:160px; float:right; height:600px; padding:0px; border:solid; border-width:1px; }

</style>



<link rel="stylesheet" type="text/css" media="all" href="../../css/jsDatePick_ltr.min.css" />

<script type="text/javascript" src="../../scripts/jsDatePick.min.1.3.js"></script>



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
			imgPath:"../../img/",
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
			imgPath:"../../img/",
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
			imgPath:"../../img/",
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

   		var field = "command.startDate"
;
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

		var field = "command.endDate"
;
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


function checkForSubmit(e) {

 e = e || window.event;

 var charCode = (typeof e.which == "number") ? e.which : e.keyCode;

 if (charCode == 13) {
		oFormObject = document.forms['myForm'];
		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='numberSearch';
		myin.value='MaHa';
		oFormObject.appendChild(myin);
		oFormObject.submit();
		validated=true;
    }
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

  <c:choose>
  <c:when test="${currGridType eq '4'}" >
		<input type="hidden" name="command.digits" value="4"/>
  </c:when>
  <c:otherwise>
        <input type="hidden" name="command.digits" value="3" />
  </c:otherwise>
  </c:choose>

<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;${currUser.contact}
	<br/>
	<font color="red">Draw Results</font>
    </h2>
  </div>
  <div class="leaderboard"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>

  <div class="mainbody">
    <div class="mainbodypanel">
      <div class="topheading">
        <div class="backbutton">
          <table align="left" style="margin:0; width:100%; color:#000;">
            <tr/>
            <tr>
              <c:choose>
              <c:when test="${numberGridForm.external == true}">
			  <input type="image" name="cancel_betrefresh" width="50" height="30" src="../../img/back.jpg"
			  onMouseOut="this.src='../../img/back.jpg'" onMouseOver="this.src='../../img/cancel.jpg'">
 <!--             <td><input type="submit" name="cancel_betrefresh" value="Back" class="button" style="height:30px;"/></td>  -->
              <c:set var="div2Offset" value="300px" />
              <c:set var="div3Offset" value="600px" />
              <c:set var="div4Offset" value="800px" />
              </c:when>
              <c:otherwise>
			  <td>
			  <input type="image" name="cancel_refresh" width="50" height="30" src="../../img/back.jpg"
			  onMouseOut="this.src='../../img/back.jpg'" onMouseOver="this.src='../../img/back.jpg'">
              <!-- <td><input type="submit" name="cancel_refresh" value="Back" class="button" style="height:30px;"/>
			  -->
			  </td>
              <c:set var="div2Offset" value="0px" />
              <c:set var="div3Offset" value="300px" />
              <c:set var="div4Offset" value="500px" />
              <c:set var="div5Offset" value="700px" />
              </c:otherwise>
              </c:choose>
            </tr>
          </table>
        </div>
        <div class="topheadingtext" >
          View Previous Results and Performance
        </div>
      </div>

      <div class="panel_left">
        <div class="panel_left_num">
          <c:choose>
            <c:when test="${numberGridForm.external == true}">
              <div style="left:0px;width:300px;float:left;height:480px;overflow:auto;overflow-x:hidden;">
                <table border="0" cellpadding="0" cellspacing="2" >
                    <tbody align="left" style="font-family:verdana; color:purple; background-color:white;">
                    <c:forEach items="${numberBasket}" var="number" varStatus="status">
                    <c:choose>
                        <c:when test="${status.index % 4 == 0}">
                            <tr/>
                        </c:when>
                    </c:choose>
                    <td>
                        <font color="blue">${number}</font>
                    </td>
                    <td>
                    <a href="processAnalytic.html?method=cancelNumber&number=${number}" style="text-decoration:none;color:rgb(255,0,0)">x</a>
                    </td>
                    <td>&nbsp;&nbsp;</td>
                    <c:choose>
                        <c:when test="${status.index % 4 == 3}">
                            <tr/>
                        </c:when>
                    </c:choose>
                    </c:forEach>
                    </tbody>
                </table>
              </div>
            </c:when>
          </c:choose>
        </div>
        <div class="panel_left_buttons">
          <div class="panel_left_button1">
            <c:choose>
              <c:when test="${numberGridForm.external == true}">
                <div style="left:0px;float:left;">
                  <table>
                    <tr>
                    <td><input type="submit" name="submitBasket" value="Use Choices" class="button" style="height:30px;"/></td>
                    </tr>
                  </table>
                </div>
              </c:when>
            </c:choose>
          </div>
          <div class="panel_left_button2">
            <input type="hidden" name="command.external" value="${numberGridForm.external}"/>
			  <input type="hidden" name="command.useNumber" value="" />
            <c:choose>
                <c:when test="${currGridType eq '4'}" >
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
        </div>
		<div class="panel_left_content1">
		  <img src="../../img/rectangle-medium.png" width="300" height="250" />
		</div>
		</div>
      <div class="spacer_v_16" style="height:540px;">
      </div>
      <div class="panel_middle">
        <div class="panel_middle_1">
          <div style="left:${div2Offset};width:390px;float:left;">
            <table border="0" cellpadding="0" cellspacing="2">
              <tr>
                <td style="font-size:large; ">Past Numbers:</td>
              </tr>
              <tr>
                <td style="width:150">Start Date</td>
                <td style="width:150">End Date</td>
              </tr>
              <tr>
                <td style="width:150"><input type="text" size="12" maxlength="0" id="startDate" name="command.startDate" value="${d1}" /></td>
                <td style="width:150"><input type="text" size="12" maxlength="0" id="endDate" name="command.endDate" value="${d2}"/></td>
                <td>
                  <input type="submit" name="numbers_refine" value="Refine Results" class="button" style="height:24px;"/>
                </td>
              </tr>
              <table>
                <tr>&nbsp; </tr>
                <c:choose>
                <c:when test="${currGridType eq '4'}" >
                  <tr style="height:6px" />
                  <tr>
                    <td>Weighted Revenue per 1.00 Stake</td>
                  </tr>
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
        </div>
        <div class="panel_middle_spacer">
        </div>

        <div class="panel_middle_3">
          <div style="left:${div4Offset};width:200px;float:left;">
            <table border="0" cellpadding="0" cellspacing="2">
              <tr>
                <td>Past Draws:</td>
              </tr>
              <tr>
                <td>
                  <input type="text" size="12" maxlength="0" id="someDate" name="command.someDate" value="${d3}" />
                </td>
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
        </div>
        <div class="panel_middle_spacer">
        </div>

        <div class="panel_middle_4">
          <div style="left:${div3Offset};float:left;">
            <table border="0" cellpadding="3" cellspacing="0">
              <tbody align="left" style="font-family:verdana; color:purple; background-color:white">
                <tr>
                  <td>
                    <table id="songlist" border="1">
                      <tr style="height:50px;font-size:24px;" >
                        <td style="width:71px;"><div id="etd1" align="center">&nbsp;</div></td>
                        <td style="width:71px;"><div id="etd2" align="center">&nbsp;</div></td>
                        <td style="width:71px;"><div id="etd3" align="center">&nbsp;</div></td>
                        <td style="width:71px;"><div id="etd4" align="center">&nbsp;</div></td>
                        <td style="width:71px;"><input type="submit" name="clearDigit" value="C" class="button" onClick="clearDigitFunc();" style="height:50px; width:71px; font-size:larger;"  /></td>
                      </tr>
                      <tr>
                        <td><input type="submit" name="addDigit" value="1" class="button" onClick="addDigitFunc(1);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="2" class="button" onClick="addDigitFunc(2);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="3" class="button" onClick="addDigitFunc(3);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="4" class="button" onClick="addDigitFunc(4);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="5" class="button" onClick="addDigitFunc(5);" style="height:68px;width:71px;font-size:30px;" /></td>
                      </tr>
                      <tr>
                        <td><input type="submit" name="addDigit" value="6" class="button" onClick="addDigitFunc(6);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="7" class="button" onClick="addDigitFunc(7);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="8" class="button" onClick="addDigitFunc(8);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="9" class="button" onClick="addDigitFunc(9);" style="height:68px;width:71px;font-size:30px;" /></td>
                        <td><input type="submit" name="addDigit" value="0" class="button" onClick="addDigitFunc(0);" style="height:68px;width:71px;font-size:30px;" /></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="spacer_v_16" style="height:540px;" >
      </div>
      <div class="panel_right">
        <div class="panel_right_content1">
          <img src="../../img/rectangle-medium.png" width="300" height="250" />
        </div>
        <div class="spacer_h_16" style="width:300px;">
        </div>
        <div class="panel_right_content2">
          <img src="../../img/rectangle-medium.png" width="300" height="250" />
        </div>
      </div>
    </div>
    <div class="skyscraper">
      <img src="../../img/skyscraper.png" width="160" height="600" />
    </div>



    <div class="range_selection">
      <c:choose>
      <c:when test="${currGridType eq '4'}" >
      <div style="left:0px;width:1000px;float:left;">
      <table>
      </br>
      <c:forEach var="index" begin="0" end="9">
              <c:choose>
              <c:when test="${currPageElement==index}">
                  <td>${index}00..${index}99&nbsp;&nbsp;</td>
              </c:when>
              <c:otherwise>
              <td>
              <a href ="processAnalytic.html?method=newPage&page=${index}" style="text-decoration:none;">
                              <link rel="xyz" type="text/css" href="color.css">
                              <font color="Blue" size="3">
                                  ${index}00..${index}99&nbsp;&nbsp;
                               </font>
                              </link>
                          </a>
              </td>
              </c:otherwise>
              </c:choose>
      </c:forEach>


			<td>
			<input name="command.searchTerm" style='width:10em' type="text" value="coffee shop" onkeypress="checkForSubmit(event)" />
			</td>
			<td><input type="submit" name="numberSearch" value="Search" class="button" style="height:30px;"/>
			</td>
      </tr>
      </table>
      </div>

      </br>
      <div style="left:0px;width:1000px;float:left;">
      <table>
          <tbody>
          <c:forEach var="pg" items="${numberGridForm.pageElements}" varStatus="status">
          <c:choose>
          <c:when test="${status.index % 5 == 0}">
              <tr>
          </c:when>
          </c:choose>
          <td>
          <table style="width:30px;float:left;">
          <tr>
          <td>
          <table style="width:30px;float:left;">
          <tr>
          <td>
              <table style="width:10px;">
              <c:forEach var="index" begin="0" end="4">
              <tr>
              <c:set var="column" value="${pg.numbers[index]}" scope="request"/>
              <td>
              <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
                              <link rel="xyz" type="text/css" href="color.css">
                              <font color="${column.level.color}" size="3">
                                  ${column.number}
                               </font>
                              </link>
                          </a>
              </td>
              </tr>
              </c:forEach>
              </table>
          </td>
          <td>
              <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
              <img width="100" height="100" src="data:image/png;base64,${pg.image}" alt="No image" border='0'>
              </span>
          </td>
          <td>
              <table style="width:10px;">
              <c:forEach var="index" begin="5" end="9">
              <tr>
              <c:set var="column" value="${pg.numbers[index]}" scope="request"/>
              <td>
              <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
                              <link rel="xyz" type="text/css" href="color.css">
                              <font color="${column.level.color}" size="3">
                                  ${column.number}
                               </font>
                              </link>
                          </a>
              </td>
              </tr>
              </c:forEach>
              </table>
          </td>
          </tr>
          </table>
           <table>
                          <tr>
                              <div align="center">
                                  ${pg.description}
                              </div>
                          </tr>
                      </table>
					  <table>
                          <tr>
                              <div align="center"">
                                  ${pg.descriptionCh}
                              </div>
                          </tr>
                      </table>
          </table>
          </td>
          <c:choose>
          <c:when test="${status.index % 10 == 9}">
                  </tr>
          </c:when>
          </c:choose>
          </c:forEach>
          </tbody>
          </table>
          </c:when>
          <c:otherwise>
          <div style="left:0px;width:1000px;float:left;">
          <table>
          </br>
          <c:forEach var="index" begin="0" end="9">
                  <c:choose>
                  <c:when test="${currPageElement==index}">
                      <td>${index}00..${index}99&nbsp;&nbsp;</td>
                  </c:when>
                  <c:otherwise>
                  <td>
                  <a href ="processAnalytic.html?method=newPage&page=${index}" style="text-decoration:none;">
                                  <link rel="xyz" type="text/css" href="color.css">
                                  <font color="Blue" size="3">
                                      ${index}00..${index}99&nbsp;&nbsp;
                                   </font>
                                  </link>
                              </a>
                  </td>
                  </c:otherwise>
                  </c:choose>
          </c:forEach>
		 	<td>
			<input name="command.searchTerm" style='width:10em' type="text" value="coffee shop"
				onkeypress="checkForSubmit(event)" />
			</td>
			<td><input type="submit" name="numberSearch" value="Search" class="button" style="height:30px;"/></td>
          </tr>
          </table>
          </div>
          </br>
          <div style="left:0px;width:1000px;float:left;">
          <table>
              <tbody>
              <c:forEach var="pg" items="${numberGridForm.pageElements}" varStatus="status">
              <c:choose>
              <c:when test="${status.index % 10 == 0}">
                  <tr>
              </c:when>
              </c:choose>
              <td>
                  <table style="width:150px;float:left;">
                      <tr>
                      <td>
                      <table style="width:150px;float:left;">
                          <tr>
                              <div align="center">
                                  <c:set var="column" value="${pg.numbers[0]}" scope="request"/>
                                  <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
                                              <link rel="xyz" type="text/css" href="color.css">
                                              <font color="${column.level.color}" size="3">
                                                  ${column.number}
                                               </font>
                                              </link>
                                          </a>
                              </div>
                          </tr>
                      </table>
                      <table style="width:150px;float:left;">
                          <tr>
                              <td>
                                  <div align="center">
                                      <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
                                          <img width="100" height="100"
                                            src="data:image/png;base64,${pg.image}" alt="No image" border='0'>
                                      </span>
                                  </div>
                              </td>
                          </tr>
                      </table>
                      <table>
                          <tr>
                              <div align="center">
                                  ${pg.description}
                              </div>
                          </tr>
                      </table>
					  <table>
                          <tr>
                              <div align="center"">
                                  ${pg.descriptionCh}
                              </div>
                          </tr>
                      </table>
                  </table>
              </td>
              <c:choose>
                <c:when test="${status.index % 10 == 9}">
                        <tr/>
                </c:when>
              </c:choose>
              </c:forEach>
              </tbody>
          </table>
          </div>
          </c:otherwise>
          </c:choose>



    </div>

    <div class="picture_row">
    </div>
  </div>










<div style="width:1000px;">
</div>

</form:form>

<body>
