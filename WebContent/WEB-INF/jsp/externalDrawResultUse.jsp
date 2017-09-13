<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Dx4 externalDrawResultUse</title>

<link rel="stylesheet" type="text/css" media="all" href="../css/jsDatePick_ltr.min.css" />

<style>

div#winner{
    text-align:left;
    background-color:white;
    position:absolute;
    left:500px;
    padding:10px;

}

table{
    width:300px;
    margin:0 auto;
    background-color:#eee;
    text-align:center;
    cursor:pointer;
}
th{
    background-color:#333;
    color:#FFF;
}

body{
font: 18px Arial, sans-serif;
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.topheading				{ float:left; width:1200px; height:60px; padding:0px; }
.topheadingtext			{ float:left; width:1110px; height:60px; font-size:large; padding-top:10px;  }
.backbutton				{ float:left; width:90px; height:30px; padding:0px; padding-top:5px; }
.resultboxpanel			{ float:left; width:1200px; padding:0px; }
.skyboxleft				{ float:left; width:236px; padding:0px; text-align:center; vertical-align:middle; }
.skyboxright			{ float:left; width:236px; padding:0px; text-align:center; }
.resultboxes			{ float:left; width:728px; padding:0px; }
.dateselector			{ float:center; width:700px; height:180px; padding:0px; }
.panel_left				{ width:1000px; float:left; height:230px; padding:0px; }
.panel_left_num			{ width:300px; float:left; height:210px; padding:0px; border:solid; border-width:1px; }

</style>


<script type="text/javascript" src="scripts/jsDatePick.min.1.3.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>

<script type="text/javascript">
	window.onload = function(){

	g_globalObject = new JsDatePick({
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
			imgPath:"../img/",
			weekStartDay:1*/
		});
	};


$(document).ready(function(){
    $("div#winner").hide();

    $("#songlist td").mouseover(function(){
        $(this).css("background-color","#ccc");
        $("#winner",this).show();
    }).mouseout(function(){
        $(this).css("background-color","#eee");
        $("#winner",this).hide();
    });

});

</script>


</head>
<body>

<form:form method="post" action="processAnalytic.html" modelAttribute="drawResultForm">

<fmt:formatDate value="${currXGR.draws[0].date}" pattern="dd-MMM-yyyy" var="d1" />

<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50"  src='../img/${currUser.icon}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;${currUser.contact} 
	<br/>
	<font color="red">Draw Results</font>
    </h2>
  </div>
  <div class="leaderboard"> <img src="../img/leaderboard.png" width="728" height="90" /> </div>
</div>
<div class="topheading">
  <div class="backbutton">
	<a href="${previousDrawReturnAddress}"><img src="../img/back.jpg" width="50" height="30"></a>
	</div>
  <div class="topheadingtext">
	Draw Results for: <fmt:formatDate value="${currXGR.draws[0].date}"/>
  </div>
</div>

<div class="panel_left">
        <div class="panel_left_num">
             <div style="left:0px;width:300px;float:left;height:480px;overflow:auto;overflow-x:hidden;">
                <table border="0" cellpadding="0" cellspacing="2" background-color="white">
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
        </div>


<div class="dateselector">
	<table border="0" cellpadding="0" cellspacing="2" style="background-color:white;">
	<tr><td colspan="3" style="width:300px;" align="center">Past Draws:</td></tr>
	<tr>
	<c:choose>
		<c:when test="${currXGR.prevDate!=null}">
		<td align="center" style="width:100px;"><a href="processAnalytic.html?prevDraw"><img src="../img/monthBackward_normal.gif"/></a></td>
		</c:when>
		<c:otherwise>
		<td align="center" style="width:100px;">&nbsp;<td>
		</c:otherwise>
	</c:choose>
		<td align="center" style="width:100px;">
			<input type="text" size="12" maxlength="0" id="someDate" name="someDate" value="${d1}" />
		</td>
	<c:choose>
		<c:when test="${currXGR.nextDate!=null}">
		<td align="center" style="width:100px;"><a href="processAnalytic.html?nextDraw"><img src="../img/monthForward_normal.gif"/></a></td>
		</c:when>
		<c:otherwise>
		<td align="center" style="width:100px;">&nbsp;<td>
		</c:otherwise>
	</c:choose>
	</tr>
	<tr>
		<td colspan="3" style="width:300px;" align="center">
		<input type="submit" name="chooseDateFromDraw" value="Go To Draw" class="button" style="height:30px;"/>
		</td>
	</tr>
	<tr>
		<td colspan="3" style="width:300px;"><font color="red" size="3">${drawResultForm.message}</font></td>
	</tr>
</table>

</div>

</div>

<div class="resultboxpanel">
  <div class="skyboxleft">
    <img src="../img/skyscraper.png" width="160" height="600" align="center" />
  </div>
  <div class="resultboxes">
    <table width="100%">
      <!-- first row -->
      <tr>
        <!-- first column -->
        <c:set var="draw" value="${currXGR.draws[0]}" scope="request" />
		<c:set var="image" value="../img/logo_magnum.gif" scope="request" />
        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplayUse.jsp"/>

        <!-- end first column -->
        </td>
        <!-- second column -->
<c:set var="draw" value="${currXGR.draws[1]}" scope="request" />
<c:set var="image" value="../img/logo_toto.gif" scope="request" />

        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplayUse.jsp"/>

        <!-- end second column -->
        </td>

      <!-- end first row -->
      </tr>
      <tr>
	    <td colspan="2"><img src="../img/leaderboard.png" width="728" height="90" /></td>
	  </tr>
      <!-- second row -->
      <tr>
        <!-- first column -->
        <c:set var="drw" value="${metaBetWinForm.drw[2]}" scope="request"/>
        <c:set var="draw" value="${currXGR.draws[2]}" scope="request" />
        <c:set var="image" value="../img/logo_damacai.gif" scope="request" />
        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplayUse.jsp"/>

        <!-- end first column -->
        </td>

        <c:set var="draw0" value="${currXGR.draws[0]}" scope="request" />
        <c:set var="draw1" value="${currXGR.draws[1]}" scope="request" />
        <c:set var="draw2" value="${currXGR.draws[2]}" scope="request" />

        <td style="width: 50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplayPics.jsp"/>

        </td>
      <!-- end first row -->
      </tr>
    </table>
  </div>
  <div class="skyboxright">
    <img src="../img/skyscraper.png" width="160" height="600" align="center" />
  </div>
</div>


<!-- end first row -->
</tr>


</form:form>
</body>
</html>
