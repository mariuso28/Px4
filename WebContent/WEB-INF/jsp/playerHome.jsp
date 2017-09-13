<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>

<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />

 <script type="text/javascript" src="../../scripts/jquery.js"></script>
 <script type="text/javascript" src="../../scripts/jquery.carouFredSel.js"></script>
   <link href="../../css/style.css" rel="stylesheet" type="text/css" />
    <title>playerHome</title>
<style>
th{
text-align:center;
}

body{
font: 18px Arial, sans-serif;

background-color:${currUser.role.color};
}

.topbar 				{ width:1200px; height:130px; background-color:${currUser.role.color}; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:12px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboardtop			{ float:right; width:728px;  height:90px; padding:20px; }
.playerdetails			{ width:1200px; height:72px; padding:0px; }
.playerdetailsleft		{ float:left; width:600px; height:72px; padding:0px; }
.playerdetailsright		{ float:left; width:600px; height:72px; padding:0px; }
.playerdetails			{ width:1200px; height:72px; padding:0px; }
.createbet				{ width:1200px; height:100px; padding:0px; }
.currenbet				{ width:1200px; padding:0px; }
.leaderboardpanel		{ float:left; width:1200px; height:130px; padding:20px; }
.leaderboard			{ float:center; width:728px;  height:90px; padding:20px; padding-left:236px; }

</style>

<script type="text/javascript">

function submitSelectDraw(){

	oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
	myin.name='previousDraw';
	myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

</script>

</head>

<body>

<script type="text/javascript">
			$(function() {
				var _scroll = {
					delay: 1000,
					easing: 'linear',
					items: 1,
					duration: 0.07,
					timeoutDuration: 0,
					pauseOnHover: 'immediate'
				};
				$('#ticker-1').carouFredSel({
					width: 1000,
					align: false,
					items: {
						width: 'variable',
						height: 50,
						visible: 1
					},
					scroll: _scroll
				});

				$('#ticker-2').carouFredSel({
					width: 1000,
					align: false,
					circular: true,
					items: {
						width: 'variable',
						height: 50,
						visible: 2
					},
					scroll: _scroll
				});

				//	set carousels to be 100% wide
				$('.caroufredsel_wrapper').css('width', '100%');

				//	set a large width on the last DD so the ticker won't show the first item at the end
				$('#ticker-2 dd:last').width(200);
			});
		</script>

<div class="topbar">
  <div class="topbartitlepic"><img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></div>
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp;${currUser.contact}
	<br/>
	<font color="red">Home</font>
    </h2>
  </div>
  <div class="leaderboardtop"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>
<form:form id="myForm" method="post" action="processPlayer.html" modelAttribute="playerDetailsForm">

<div id="c-carousel">
          <div id="wrapper">
                  <div>
                      <dl id="ticker-2">

<dd><img src="../../img/4dx-logo.jpg" width="50" height="35" vertical-align="middle"/></dd>
<c:choose>
<c:when test="${empty playerDetailsForm.lastWin}">
</c:when>
<c:otherwise>
<dd><fmt:formatDate value="${playerDetailsForm.lastWinDate}"/></dd>
<c:forEach items="${playerDetailsForm.lastWin}" var="win" varStatus="status">
<dd>${win.provider}</dd>
<dd>${win.game}</dd>
<dd>${win.choice}</dd>
<dd><img src="data:image/png;base64,${win.image}" alt="No image" width="35" height="35" /></dd>
<dd>${win.place} Prize</dd>
<dd>Bet&nbsp;&nbsp;$<fmt:formatNumber value="${win.stake}" pattern="#0.00" />
&nbsp;&nbsp;&nbsp;&nbsp;Win&nbsp;&nbsp;$<fmt:formatNumber value="${win.win}" pattern="#0.00" /></dd>
</c:forEach>
</c:otherwise>
</c:choose>
<dd>Last Draw Results&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${playerDetailsForm.externalGameResults.draws[0].date}"/></dd>
<dd><img src="../../img/logo_magnum.gif" width="50" height="38" vertical-align="middle"/></dd>
<dd>Draw No: ${playerDetailsForm.externalGameResults.draws[0].drawNo}</dd>

<c:set var="f1" value="${playerDetailsForm.externalGameResults.draws[0].firstPlace}"/>
<c:set var="f2" value="${playerDetailsForm.externalGameResults.draws[0].secondPlace}"/>
<c:set var="f3" value="${playerDetailsForm.externalGameResults.draws[0].thirdPlace}"/>
<dd>${f1}&nbsp;&nbsp;&nbsp;&nbsp;${f2}&nbsp;&nbsp;&nbsp;&nbsp;${f3}&nbsp;&nbsp;&nbsp;&nbsp;</dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[0].firstImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[0].secondImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[0].thirdImage}" alt="No image" width="35" height="35" /></dd>



<dd><fmt:formatDate value="${playerDetailsForm.externalGameResults.draws[1].date}" pattern="dd-MMM-yyyy" /></dd>	<dd><img src="../../img/logo_toto.gif" width="48" height="38"/></dd>

<dd>Draw No: ${playerDetailsForm.externalGameResults.draws[1].drawNo}</dd>

<c:set var="f1" value="${playerDetailsForm.externalGameResults.draws[1].firstPlace}"/>
<c:set var="f2" value="${playerDetailsForm.externalGameResults.draws[1].secondPlace}"/>
<c:set var="f3" value="${playerDetailsForm.externalGameResults.draws[1].thirdPlace}"/>
<dd>${f1}&nbsp;&nbsp;&nbsp;&nbsp;${f2}&nbsp;&nbsp;&nbsp;&nbsp;${f3}&nbsp;&nbsp;&nbsp;&nbsp;</dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[1].firstImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[1].secondImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[1].thirdImage}" alt="No image" width="35" height="35" /></dd>

<dd><fmt:formatDate value="${playerDetailsForm.externalGameResults.draws[0].date}" pattern="dd-MMM-yyyy" /></dd>	<dd><img src="../../img/logo_damacai.gif" width="50" height="38"/></dd>

<dd>Draw No: ${playerDetailsForm.externalGameResults.draws[2].drawNo}</dd>
<c:set var="f1" value="${playerDetailsForm.externalGameResults.draws[2].firstPlace}"/>
<c:set var="f2" value="${playerDetailsForm.externalGameResults.draws[2].secondPlace}"/>
<c:set var="f3" value="${playerDetailsForm.externalGameResults.draws[2].thirdPlace}"/>
<dd>${f1}&nbsp;&nbsp;&nbsp;&nbsp;${f2}&nbsp;&nbsp;&nbsp;&nbsp;${f3}&nbsp;&nbsp;&nbsp;&nbsp;</dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[2].firstImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[2].secondImage}" alt="No image" width="35" height="35" /></dd>
<dd><img src="data:image/png;base64,${playerDetailsForm.externalGameResults.draws[2].thirdImage}" alt="No image" width="35" height="35" /></dd>


                      </dl>
              </div>
              </div>
		</div> <!-- /c-carousel -->



<div class="playerdetails">
    <div class="playerdetailsleft">
        <table border="0" cellpadding="3" cellspacing="0" width="600">
            <tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
                <tr><td width="30%">Logon Id:</td><td width="70%">${currUser.email}</td></tr>
                <tr><td width="30%">Contact:</td><td width="70%">${currUser.contact}</td></tr>
                <tr><td width="30%">Phone:</td><td width="30%">${currUser.phone}</td></tr>
       </tbody>
        </table>
    </div>
    <div class="playerdetailsright">
        <table border="0" cellpadding="3" cellspacing="0" width="600">
            <tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
                <c:set var="linkColor" value="green" scope="page" />
                <c:choose>
                <c:when test="${currUser.account.balance < '0'}">
                    <c:set var="linkColor" value="red" scope="page"/>
                </c:when>
                </c:choose>
                <tr>
                    <td width="30%">Curr Balance:</td>
                    <td width="70%">
                    <font color=${linkColor} size="3">
                          <fmt:formatNumber value="${currUser.account.balance}" pattern="#0.00" />
                    </font>
                    </td>
                  </tr>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<div class="createbet">
    <br/>
    Create Bets
    <br/>
    <table align="left" style="margin:0; padding:10px; width:100%; font-family:verdana; background: #f8f8f8; color:#000;">
        <tr><td></td></tr>
        <c:forEach items="${playerDetailsForm.displayQuickBetGameList}" var="mgame" varStatus="status">
            <tr>

        <td><a href="../qkBet/create_QuickBet.html?method=createQuickBet&name=${mgame.name}">${mgame.name}</td>

    <!--		<td><a href="create_Bet.html?method=createBets&name=${mgame.name}">${mgame.name}</td> -->
            <td>Next Playing: <fmt:formatDate value="${mgame.playDate}" pattern="dd-MMM-yyyy"/></td>
            <td>Last Bet: <fmt:formatDate value="${mgame.playDate}" pattern="dd-MMM HH:mm"/></td>
        </tr>
        </c:forEach>
    </table>
</div>


<div class="currenbet">
</br>
<c:choose>
<c:when test="${empty playerDetailsForm.displayMetaBetList}">
<tr>No Current Bets</tr>
</c:when>
<c:otherwise>
<tr>Current Bets:</tr>
</table>
<table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td style="width:15%;">Game</td>
    <td style="width:15%;">Placed</td>
	<td style="width:10%;">Draw Date</td>
	<td style="width:10%;" align="right">Total Stake</td>
	</tr>
</table>
	<c:forEach items="${playerDetailsForm.displayMetaBetList}" var="dispBet" varStatus="status">
<table align="left" style="margin:0; padding:0px; width:100%; background: #f8f8f8; color:#000;">
      	<tr>
		<td style="width:15%;"><a href="expandBet.html?method=expandBets&id=${dispBet.metaBet.id}">${dispBet.expanded}${dispBet.metaBet.metaGame.name}</td>
		<td style="width:15%;"><fmt:formatDate value="${dispBet.metaBet.placed}" pattern="dd-MMM-yy HH:mm:ss"/></td>
		<td style="width:10%;"><fmt:formatDate value="${dispBet.metaBet.playGame.playDate}" pattern="dd-MMM-yy"/></td>
		<td style="width:10%;" align="right"><fmt:formatNumber value="${dispBet.totalStake}" pattern="#0.00" /></td>
	</tr>
</table>
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
	</tr>
	</c:when>
	</c:choose>
</c:forEach>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${empty playerDetailsForm.displayMetaBetWinList}">
<tr></tr>
</c:when>
<c:otherwise>
<div class="leaderboardpanel">
	<div class="leaderboard">
    	<img src="../../img/leaderboard.png" width="728" height="90" />
    </div>
</div>
<div style="float:left; width:100%;">
<tr>Previous Wins:</tr>
<table align="left" style="margin:0; width:100%; background: #f8f8f8; color:#000;">
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td>Game</td>
	<td>Placed</td>
    <td>Played On</td>
	<td align="right">Total Stake</td>
	<td align="right">Total Win</td>
	</tr>
	<c:forEach items="${playerDetailsForm.displayMetaBetWinList}" var="dwin" varStatus="status">
      	<tr>
		<td><a href="expandWin.html?method=expandWins&id=${dwin.metaId}">${dwin.expanded}${dwin.metaName}</td>
		<td><fmt:formatDate value="${dwin.placed}" pattern="dd-MMM-yy HH:mm:ss"/></td>
		<td><fmt:formatDate value="${dwin.playedAt}" pattern="dd-MMM-yy"/></td>
		<td align="right"><fmt:formatNumber value="${dwin.totalStake}" pattern="#0.00" /></td>
		<td align="right"><fmt:formatNumber value="${dwin.totalWin}" pattern="#0.00" /></td>
		<c:choose>


		<c:when test="${dwin.expanded eq '-'}">
		<tr>
		<div style="float:left; width:100%; font-size: 15px;">
		<table>
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
		</div>
		</tr>
		</c:when>
		</c:choose>
	</tr>

	</c:forEach>
</table>
</div>
</c:otherwise>
</c:choose>
</br>
<div style="float:left; width:100%;">
</br>
</br>
<tr>
<td>View Historic Results :</td>
<c:choose>
<c:when test="${playerDetailsForm.enable4D == 'true'}">
<a href="../anal/processAnalytic.html?numbers_refresh&type=4">4D</a></td>
</c:when>
</c:choose>
<c:choose>
<c:when test="${playerDetailsForm.enable3D == 'true'}">
<a href="../anal/processAnalytic.html?numbers_refresh&type=2">3D</a></td>
</c:when>
</c:choose>

<!-- <td><a href="processAnalytic.html?numbers_refresh&type=1">2D</a></td> -->

<td style="font:20px Arial">

<form:select path="command.previousDraw" onChange="submitSelectDraw()">
	<option value="wn">LAST DRAWS</option>
	<c:forEach items="${playerDetailsForm.previousDraws}" var="date">
    		<option value="<fmt:formatDate value="${date}" pattern="dd-MMM-yyyy"/>"}><fmt:formatDate value="${date}" pattern="dd-MMM-yyyy"/></option>
  	</c:forEach>
	</form:select>
</td>


</tr>
<div class="leaderboardpanel">
	<div class="leaderboard">
    	<img src="../../img/leaderboard.png" width="728" height="90" />
    </div>
</div>

</br>
<tr>
<td><a href="processPlayer.html?edit">Edit Profile</a></td>
</tr>
<tr>
<td><a href="../logon/signin">Logoff</a></td>
</tr>
</div>

</div>

</form:form>
</body>
</html>
