<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>


<html>

<head>

<script type="text/javascript" src="../../scripts/jquery.js"></script>
<script type="text/javascript" src="../../scripts/jquery.carouFredSel.js"></script>
<link href="../../css/style.css" rel="stylesheet" type="text/css" />


    <title>memberHome</title>

<style>
th{
text-align:center;
<style type="text/css">
   a {
      text-decoration:none;
   }


}

body{
font: 20px Arial, sans-serif;
background-color:${currUser.role.color};
}
</style>


<script type="text/javascript">
function submitSelectDraw(){

    oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='previousDrawAndWins';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

function submitChangeActiveGame(){

    oFormObject = document.forms['myForm'];
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='changeActiveGame';
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


<form:form method="post" id="myForm" action="processAgent.html"
		modelAttribute="memberHomeForm" >

<c:choose>
<c:when test="${memberHomeForm.parent != null}">				<!-- home page -->

<div id="c-carousel">
          <div id="wrapper">
                <div>
					<dl id="ticker-2">
						<dd>Hot Numbers</dd>
						<c:forEach items="${currHotNumbers}" var="hn">
							<dd style="color:${hn.color}">${hn.tickerString}</dd>
						</c:forEach>
				</div>
		</div>
</div> <!-- /c-carousel -->

<h2 style="background-color:${currUser.role.color}">Hi : ${currUser.role.desc}&nbsp; ${currUser.contact}
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'></h2>
</c:when>
<c:otherwise>													<!-- maintain company -->
	<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}
		- Maintain Companies</h2>
	<table>
		<tr height="20">
		<td>
			<a href="../../Dx4/adm/processAdmin.html?return_admin"><img src="../../../img/back.jpg" width="50" height="30"></a>
		</td>
		</tr>
		<tr height="20">
			<td>&nbsp</td>
		</tr>
	</table>
</c:otherwise>
</c:choose>

<table cellspacing="1" cellpadding="0" border="0"
 bgcolor="white" id="shell" height="100" width="1000">

   <tr height="20px" valign="top" align="left">


   <td bgcolor="white" width="850px">


   	<table border="0" cellpadding="3" cellspacing="0" align="left" >
	<tbody style="font-family:verdana; color:F6DEFF; background-color:GRAY">
	    <tr>
            <td style="color:white;">Username (email):</td>
	    <td>${currUser.email}</td>
	    <td style="color:white;">&nbspContact:</td>
	    <td>${currUser.contact}</td>
	    <td style="color:white;">&nbspPhone:</td>
	    <td>${currUser.phone}</td>
	    </tr>
	</tbody>
        </table>
   </td>
   </tr>

   <tr>
   <td bgcolor="white" width="600px">
   <table border="0" cellpadding="3" cellspacing="0">
	<tr>
	<c:choose>
	<c:when test="${currUser.role.rank <= 5}">
		<c:set var="linkColor" value="green" scope="page" />
		<c:choose>
		<c:when test="${currUser.account.balance < '0'}">
			<c:set var="linkColor" value="red" scope="page"/>


</c:when>
		</c:choose>
		<td>Curr Balance:</td>
		<td>
		<font color=${linkColor} size="3">
               		<fmt:formatNumber value="${currUser.account.balance}"
							pattern="#0.00" />
              	</font>
		</td>
		<c:choose>
		<c:when test="${currUser.role.rank == 5}">
		<td>
		<a href="../acc/processAccount.html?method=updateComp" />
			Update Account
		</td>
		</c:when>
		</c:choose>
	</c:when>
	</c:choose>
	<td>


	</tr>
	</br>
	<tr>
	<c:choose>
	<c:when test="${memberHomeForm.parent != null}">				<!-- home page -->

	<td style="font:20px Arial">

	<form:select path="command.previousDraw" onChange="submitSelectDraw()">
	<option value="wn">Latest Wins</option>
	<c:forEach items="${memberHomeForm.previousDraws}" var="draw">
    		<option value="<fmt:formatDate value="${draw.date}" pattern="dd-MMM-yyyy"/>"}>
		<fmt:formatDate value="${draw.date}" pattern="dd-MMM-yyyy"/>
		- <fmt:formatNumber value="${draw.win}" pattern="#0.00"/>
		</option>
  	</c:forEach>
	</form:select>
	</td>
	<td>
    <select name="XXX">
	<option value="wn">Hot Number Bets</option>
	<c:forEach items="${currNumberExposBet}" var="nr">
    		<option value="${nr.number}"}>${nr.number} - <fmt:formatNumber value="${nr.tbet}" pattern="#0.00" /></option>
  	</c:forEach>
	</select>
    </td>


	<td>
    <select name="YYY">
	<option value="wn">Hot Number Wins</option>
	<c:forEach items="${currNumberExposWin}" var="nr">
    		<option value="${nr.number}"}>${nr.number} - <fmt:formatNumber value="${nr.expo}" pattern="#0.00" /></option>
  	</c:forEach>
	</select>
    </td>

	</td>

	<td>Maintain Numbers :</td>
	<c:choose>
	<c:when test="${memberHomeForm.enable4D == true}">
		<td><a href="../anal/processAnalytic.html?numbers_refresh&type=4">4D</a></td>
	</c:when>
	</c:choose>
	<c:choose>
	<c:when test="${memberHomeForm.enable3D == true}">
		<td><a href="../anal/processAnalytic.html?numbers_refresh&type=2">3D</a></td>
	</c:when>
	</c:choose>
	<c:choose>
	<c:when test="${memberHomeForm.enable2D == true}">
		<td><a href../anal/processAnalytic.html?numbers_refresh&type=1">2D</a></td>
	</c:when>
	</c:choose>
	</c:when>
	</c:choose>
	</tr>


	</table>
    </td>


    </tr>


    <tr><td>&nbsp</td></tr>
    <tr>

    <td bgcolor="white" width="850px">
    <table border="0" cellpadding="3" cellspacing="0" >
	    <tr>
	    <td>Create Member: </td>
	    <td>
	    <form:select path="createRole">
		      <option value="ROLE_PLAY">PLAYER</option>
	    </form:select>
	    </td>
	    <td><input type="submit" name="memberCreate" value="Create"/></td>
	<c:choose>
	<c:when test="${memberHomeForm.parent != null}">

	   <td>Active Game</td>
	   <td>
	   <form:select path="command.activeGame" onChange="submitChangeActiveGame()">
	   <c:forEach items="${memberHomeForm.activeGames}" var="gr">
			<option value="${gr}" ${gr == currActiveGame ? 'selected' : ''}>${gr}</option>
    	</c:forEach>
		</form:select>
		</td>

	   <td>&nbsp&nbsp</td>
		<td><a href="../../currNumExpo/processNumberExpo.html?currNumberExposure">Manage Next Bets</a></td>
	</c:when>
	</c:choose>
  <tr>
  <td>Email (Logon Username):</td>
  <td><input type="text" style='width:20em' name="command.email" value="fan1@test.com"/></td>
  <td><input type="submit" name="memberFind" value="Find"/></td>
  </tr>

  <tr>
  <td>Phone:</td>
  <td><input type="text" style='width:20em' name="command.phone" value=""/></td>
  <td><input type="submit" name="memberFindPhone" value="Find"/></td>
  </tr>

	</tr>


       </table>
    </td>
    </tr>

   <tr><td>&nbsp</td></tr>
   <tr><td>&nbsp</td></tr
   <tr height="200px" valign="top" align="left">
       <td>
       <div style="height:300px;overflow:auto;overflow-x:hidden;">
       <table border="0" style="width:1000px;" align="left">
	    <tr style="font-family:verdana; color:FFF; background-color:B5B5B5">
		<td>Member Code</td>
		<td>Type</td>
		<td>Contact</td>
		<td>email(Dx4 Logon Id)</td>
		<td>Balance</td>
	  </tr>
	    <c:set var="tab" value="" scope="request"/>
	    <c:set var="editable" value="true" scope="request"/>
	    <c:set var="players" value="${currUser.players}" scope="request"/>
		  <jsp:include page="_includeAll/playersDisplay.jsp"/>
	</table>
	</td>
	</tr>
</table>


</form:form>
</br>
<tr>
<c:choose>
<c:when test="${memberHomeForm.parent == null}">
</c:when>
<c:otherwise>
	<td><a href="processAgent.html?edit_agent">Edit Profile</a></td>
	<td><a href="../logon/signin">Logoff</a></td>
</c:otherwise>
</c:choose>
</tr>
</body>
</html>
