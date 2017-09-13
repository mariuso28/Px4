<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>numberView</title>


<style type="text/css">
h1{
page-break-before: always;
}

body{
font: 20px Arial, sans-serif;
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.backbutton				{ float:left; width:90px; height:30px; padding:0px; padding-top:5px; }
.topheading				{ float:left; width:1200px; height:60px; padding:0px; }
.topheadingtext			{ float:left; width:1110px; height:30px; padding:0px; font-size:large; padding-top:10px; }
.numberpicpanel			{ float:left; width:1200px; height:120px; padding:0px; }
.numberpicndesc			{ float:left; width:300px; height:120px; padding:0px; }
.numberpic				{ float:left; width:300px; height:100px; padding:0px; }
.numberpicdesc			{ float:left; width:300px; height:20px; padding:0px; }
.leaderboard2			{ float:left; width:728px;  height:90px; padding:15px; }
.weightedpanel			{ float:left; width:1200px; height:20px; padding:0px; }
.spacer_h_16			{ height:16px; width:1200px; float:left; padding:0px; }
.resultspanel			{ float:left; width:1200px; height:120px; padding:0px; }

</style>
</head>

<body>
<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;${currUser.contact}
	<br/>
	<font color="red">Winning Draws</font>
    </h2>
  </div>
  <div class="leaderboard"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>
<div class="topheading">
    <fmt:formatDate value="${numberViewForm.startDate}" pattern="dd-MMM-yyyy" var="d1" />
    <fmt:formatDate value="${numberViewForm.endDate}" pattern="dd-MMM-yyyy" var="d2" />
    <div class="backbutton">
	  <a href="processAnalytic.html?cancelViewNumber"><img src="../../img/back.jpg" width="50" height="30"></a>
	</div>
    <div class="topheadingtext">
	  Winning Draws For : ${numberViewForm.number} from ${d1} to ${d2}
	</div>

</div>

<form:form method="post" action="processAnalytic.html" modelAttribute="numberViewForm">




<input type="hidden" name="command.external" value="${numberViewForm.external}"/>
<div class="numberpicpanel">
  <div class="numberpicndesc">
    <div class="numberpic">
      <c:choose>
        <c:when test="${numberViewForm.digits > '2'}">
          <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
          <div class='m4d-num-list'>
          <img width="100" height="100"
            src="data:image/png;base64,${numberViewForm.image}" alt="No image" border='0' ></div></span>
        </c:when>
      </c:choose>
    </div>
    <div class="numberpicdesc">
      ${numberViewForm.numberDesc}&nbsp;&nbsp;&nbsp; ${numberViewForm.numberDescCh}
    </div>
  </div>
  <div class="leaderboard2">
    <img src="../../img/leaderboard.png" width="728" height="90" />
  </div>
</div>
<div class="spacer_h_16">&nbsp;</div>
<div class="weightedpanel">
  <c:choose>
    <c:when test="${numberViewForm.digits > '3'}">

      Weighted revenue for date range : <fmt:formatNumber value="${numberViewForm.revenue}" pattern="#0.00" />

    </c:when>

  </c:choose>

  <c:choose>
  <c:when test="${numberViewForm.external == true}">
      <a href ="processAnalytic.html?method=addNumber&number=${numberViewForm.number}" style="text-decoration:none;">
              <link rel="xyz" type="text/css" href="color.css">
               <font color="blue" size="4">
                   &nbsp;&nbsp;Use Number : ${numberViewForm.number}
                </font>
                </link>
           </a>
  </c:when>
  </c:choose>

</div>
<div class="spacer_h_16"></div>

<div class="resultspanel">
<c:forEach var="dr" items="${numberViewForm.drawResults}" varStatus="status">
<td style="width:50%;vertical-align:top" valign="top">
<div class="outerbox">
<table class="resultTable"  border="1" align="left" width="250"><tr>
<td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="0">
	<tr>
	    <c:choose>
	    <c:when test="${dr.provider.name eq 'Magnum 4D'}">
		<td class="resultm4dlable" style="width:20%"><img src="../../img/logo_magnum.gif"/>
	    </c:when>
	    </c:choose>
	    <c:choose>
	    <c:when test="${dr.provider.name eq 'Da  Ma Cai 1+3D'}">
		<td class="resultm4dlable" style="width:20%"><img src="../../img/logo_damacai.gif"/>
	    </c:when>
	    </c:choose>
	    <c:choose>
	    <c:when test="${dr.provider.name eq 'Sports Toto 4D'}">
		<td class="resultm4dlable" style="width:20%"><img src="../../img/logo_toto.gif"/>
	    </c:when>
	    </c:choose>

	    <td class="resultm4dlable">${dr.provider.name}</td>
	</tr>
	</table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="5">
	<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${dr.date}" pattern="dd-MMM-yy" /></td>
	<td class="resultdrawdate">Draw No: ${dr.drawNo}</td>
	</tr></table></td></tr><tr><td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="0">
	<tr><td style="width:100" class="resultprizelable">1st Prize</td>
	<c:set var="first" value="${dr.firstPlace}"/>
	<c:set var="second" value="${dr.secondPlace}"/>
	<c:set var="third" value="${dr.thirdPlace}"/>
	<c:set var="number" value="${numberViewForm.number}"/>
	<c:choose>
	<c:when test="${fn:endsWith(first,number)}">
		<td class="resulttop" style="color:00B00F;">${dr.firstPlace}</td>
	</c:when>
	<c:otherwise>
		<td class="resulttop">${dr.firstPlace}</td>
	</c:otherwise>
	</c:choose>

	</tr><tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
	<c:choose>
	<c:when test="${fn:endsWith(second,number)}">
		<td class="resulttop" style="color:FFA600;">${dr.secondPlace}</td>
	</c:when>
	<c:otherwise>
		<td class="resulttop">${dr.secondPlace}</td>
	</c:otherwise>
	</c:choose>
	<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
	<c:choose>
	<c:when test="${fn:endsWith(third,number)}">
		<td class="resulttop" style="color:BA006D;">${dr.thirdPlace}</td>
	</c:when>
	<c:otherwise>
		<td class="resulttop">${dr.thirdPlace}</td>
	</c:otherwise>
	</c:choose>
	</table>
</td>
</div>
</table>
</c:forEach>
</div>
<p class="break">
<c:choose>
<c:when test="${numberViewForm.digits == '4'}">
</p>
</br>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<tr><td>Historic Performance</td><tr>
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
		<td>Date</td>
		<td>Draw No</td>
		<td>Prize</td>
		<td>Provider</td>


	</tr>
<tr>

<c:forEach var="pl" items="${numberViewForm.placings}" varStatus="status">
<tr>
<td><fmt:formatDate value="${pl.date}" pattern="dd-MMM-yy" /></td>
<td>${pl.drawNo}</td>
<td>${pl.payOutType}</td>


<td>${pl.provider}</td>


</tr>
</c:forEach>
</table>
</div>
</c:when>
</c:choose>

</form:form>

<body>
