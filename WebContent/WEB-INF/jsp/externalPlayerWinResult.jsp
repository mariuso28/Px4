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
<title>externalPlayerWinResult</title>

<style>
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
div#winner{
    text-align:left;
    background-color:white;
    position:absolute;
    left:500px;
    padding:10px;

}
body{
font: 18px Arial, sans-serif;
}

</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

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

<style>
th{
text-align:left;
}

body{
font: 18px Arial, sans-serif;
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.topheading				{ float:left; width:1200px; height:60px; padding:0px; }
.resultboxpanel			{ float:left; width:1200px; padding:0px; }
.skyboxleft				{ float:left; width:236px; padding:0px; text-align:center; vertical-align:middle; }
.skyboxright			{ float:left; width:236px; padding:0px; text-align:center; }
.resultboxes			{ float:left; width:728px; padding:0px; }
.backbutton				{ float:left; width:90px; height:30px; padding:0px; padding-top:5px; }
.topheadingtext			{ float:left; width:1110px; height:60px; font-size:large; padding-top:10px;  }

</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
<body>
<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50" src='../../img/${currUser.role.shortCode}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color};">
	  ${currUser.role.desc}&nbsp;${currUser.contact}
	  <br/>
	  <font color="red">Winning Bet Results</font>
	</h2>
  </div>

  <div class="leaderboard"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>
<div class="topheading">
  <div class="backbutton">
	<a href="${metaBetWinForm.returnTarget}"><img src="../../img/back.jpg" width="50" height="30"></a>
	</div>
  <div class="topheadingtext">
	Winning Bet Results for Draw: <fmt:formatDate value="${currXGR.draws[0].date}"/>
  </div>
</div>
<form:form method="post" action="processPlayer.html" modelAttribute="metaBetWinForm" >


<td>
</td>

<br/>

<table align="left" style="margin:0; width:100%; height:100; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td>Game</td>
	<td>Placed</td>
        <td>Played On</td>
	<td>Total Stake</td>
	<td>Total Win</td>
	</tr>
	<c:set var="dwin" value="${metaBetWinForm.displayMetaBetWin}" />
	<c:choose>


	<c:when test="${dwin.expanded eq '-'}">
		<c:set var="sign" value="minus"
	/>
	</c:when>
	<c:otherwise>
		<c:set var="sign" value="plus"
	/>
	</c:otherwise>
	</c:choose>
	<tr>
		<td style="width:15%;"><a href="expandWin.html?method=expandWins&id=${dwin.metaId}:${sign}|${metaBetWinForm.returnHrefCode}">${dwin.expanded}${dwin.metaName}</td>
		<td><fmt:formatDate value="${dwin.placed}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatDate value="${dwin.playedAt}" pattern="dd-MMM-yy"/></td>
		<td><fmt:formatNumber value="${dwin.totalStake}" pattern="#0.00" /></td>
		<td><fmt:formatNumber value="${dwin.totalWin}" pattern="#0.00" /></td>
	</tr>
	</br>

	<c:choose>
		<c:when test="${dwin.expanded eq '-'}">
		<table style="width:100%" cellspacing="0">
		<tbody style="width:100%;font-family:arial; font-size:15px; color:purple; background-color:LightCyan;">
		<tr style="color:grey; background-color:white;">
		<td>Bet Type</td>
		<td>Provider</td>
		<td>Stake</td>
		<td>Choice</td>
		<td>Drawn</td>
		<td>Place</td>
		<td>Win</td
		></tr>
		<c:forEach items="${dwin.expandedWins}" var="ewin" varStatus="status1">
      		<tr>
			<td>${ewin.game}
</td>
			<td>${ewin.provider}
</td>
			<td><fmt:formatNumber value="${ewin.stake}" pattern="#0.00" /></td>
			<td>${ewin.choice}
</td>
			<td>${ewin.number}
</td>
			<td>${ewin.place}
</td>

			<td><fmt:formatNumber value="${ewin.win}" pattern="#0.00" /></td>
		</tr>
		</c:forEach>
		</tbody>
		</table>
		</c:when>
	</c:choose>




	<!--
	<c:choose>


		<c:when test="${dwin.expanded eq '-'}">
		<tr>
		<div style="float:left; width:100%; font-size: 15px;">
		<table style="width:100%;">
		<thead style="width:100%;font-family:arial; font-size:15px; color:grey; background-color=white;">


		<th>Bet Type</th>
		<th>Provider</th>
		<th>Stake</th>
		<th>Choice</th>
		<th>Drawn</th>
		<th>Place</th>

		<th>Win</th>


		</tr>
		</thead>
		<tbody style="width:100%;font-family:arial; font-size:15px; color:purple; background-color:LightCyan;">
		<c:forEach items="${dwin.expandedWins}" var="ewin" varStatus="status1">
      		<tr>
			<td>${ewin.game}
</td>
			<td>${ewin.provider}
</td>
			<td><fmt:formatNumber value="${ewin.stake}" pattern="#0.00" /></td>
			<td>${ewin.choice}
</td>
			<td>${ewin.number}
</td>
			<td>${ewin.place}
</td>

			<td><fmt:formatNumber value="${ewin.win}" pattern="#0.00" /></td>
		</tr>
		</c:forEach>
		</tbody>
		</table>
		</c:when>

	</c:choose>
-->


</table>
<div class="resultboxpanel">
  <div class="skyboxleft">
    <img src="../../img/skyscraper.png" width="160" height="600" align="center" />
  </div>
  <div class="resultboxes">
    <table width="100%">
      <!-- first row -->
      <tr>
        <!-- first column -->
        <c:set var="drw" value="${metaBetWinForm.drw[0]}" scope="request"/>
        <c:set var="draw" value="${currXGR.draws[0]}" scope="request" />
        <c:set var="image" value="../../img/logo_magnum.gif" scope="request" />

        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplay.jsp"/>

        <!-- end first column -->
        </td>
        <!-- second column -->
        <c:set var="drw" value="${metaBetWinForm.drw[1]}" scope="request"/>
        <c:set var="draw" value="${currXGR.draws[1]}" scope="request" />
        <c:set var="image" value="../../img/logo_toto.gif" scope="request" />

        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplay.jsp"/>

        <!-- end second column -->
        </td>

      <!-- end first row -->
      </tr>
      <tr>
	    <td colspan="2"><img src="../../img/leaderboard.png" width="728" height="90" /></td>
	  </tr>
      <!-- second row -->
      <tr>
        <!-- first column -->
        <c:set var="drw" value="${metaBetWinForm.drw[2]}" scope="request"/>
        <c:set var="draw" value="${currXGR.draws[2]}" scope="request" />
        <c:set var="image" value="../../img/logo_damacai.gif" scope="request" />

        <td style="width:50%;vertical-align:top">

          <jsp:include page="_includeAll/drawDisplay.jsp"/>

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
    <img src="../../img/skyscraper.png" width="160" height="600" align="center" />
  </div>
</div>

</form:form>
</body>
</html>
