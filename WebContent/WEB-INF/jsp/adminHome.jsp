<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>


<html>
<head>
    <title>adminHome</title>
<style>
th{
text-align:center;

}



body{
font: 20px Arial, sans-serif;
}
</style>
</head>


<body>

  <script type="text/javascript">

  function submitNewDraw(index){

  //    alert("IN with index : " + index);

      oFormObject = document.forms['myForm'];

      var field = "command.changedIndex";
      oformElement = oFormObject.elements[field];
      oformElement.value = index;

      var myin = document.createElement('input');
      myin.type='hidden';
      myin.name='submitNewDraw';
      myin.value='MaHa';
      oFormObject.appendChild(myin);
      oFormObject.submit();
  }

  function cancelUpdate(index){

  //    alert("IN with index : " + index);

      oFormObject = document.forms['myForm'];

      var field = "command.changedIndex";
      oformElement = oFormObject.elements[field];
      oformElement.value = index;

      var myin = document.createElement('input');
      myin.type='hidden';
      myin.name='cancelUpdate';
      myin.value='MaHa';
      oFormObject.appendChild(myin);
      oFormObject.submit();
  }

  </script>


<form:form id="myForm" method="post" action="processAdmin.html" modelAttribute="adminDetailsForm">
<h2 style="background-color:${currUser.role.color}">Hi ${currUser.role.desc}&nbsp; ${currUser.contact}

&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'>
</h2>


<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr><td width="30%">Logon Id:</td><td width="70%">${currUser.email}</td></tr>
<tr><td width="30%">Phone:</td><td width="30%">${adminDetailsForm.profile.phone}</td></tr>
</tbody>
</table>
<br/>
<br/>
<tr>Open Games:</tr>


<table border="0" cellpadding="3" cellspacing="0" width="800">
	<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">

  <input type="hidden" name="command.changedIndex"; value=-1 />
  <c:set var="now" value="<%=new java.util.Date()%>" />
  <fmt:formatDate var="fmtDate" value="${now}" pattern="yyyy-MM-dd" />

	<c:forEach items="${adminDetailsForm.openGameList}" var="dispGame" varStatus="status">
  <input type="hidden" name="command.gamename[${status.index}]"; value="${dispGame.metaGame.name}" />
	<tr>
		<td width="20%">${dispGame.metaGame.name}</td>
		<td width="30%">${dispGame.metaGame.description}</td>
		<c:choose>
		<c:when test="${dispGame.nextPlayGame != null}">
			<td width="50%"><a href="../admGame/edit_Game.html?method=playGame&params=${dispGame.metaGame.name}:${dispGame.nextPlayGame.id}">
			Set Played for : <fmt:formatDate value="${dispGame.nextPlayGame.playDate}" /></a></td>
		</c:when>
		<c:otherwise>
			<td><a href="processAdmin.html?updateExternalResults">Update</a></td>
		</c:otherwise>
		</c:choose>
    <tr><td>Coming Draws</td></tr>
    <c:forEach items="${dispGame.comingDraws}" var="draw" >
      <tr><td><fmt:formatDate pattern="yyyy-MM-dd" value="${draw}"/></td></tr>
    </c:forEach>
    <tr><td width="50%">Schedule (yyyy-MM-dd)</td></tr>
    <tr><td><input id="schedule" style="width:160px; height: 30px;" type="date"
        name="command.drawDate[${status.index}]" value="${fmtDate}" /></td>
    <td><input type="submit" value="Schedule" class="button"
                  onClick="submitNewDraw(${status.index});" style="height:34px;width:152px;" /></td>
    </tr>
	</tr>
	</c:forEach>
	</tbody>

</br>
</table>


</br>

<tr><font color="blue"><td>${adminDetailsForm.infoMessage}</font></td></tr>
<tr><font color="red"><td>${adminDetailsForm.message}</font></td></tr>
</br>
</br>

<tr>
<td>Last Updated Draw Date: <fmt:formatDate value="${adminDetailsForm.lastRecordedDrawDate}"/></td>
<td><a href="processAdmin.html?updateExternalResults">
Update</a></td>
</tr>
</br>
</br>
<tr>
<td><a href="processAdmin.html?edit_admin">Edit Profile</a></td>
<!-- <td><input type="submit" name="viewGames" value="View Games" class="button" style="height:23px;"/></td> -->
<td><a href="processAdmin.html?viewGames">Maintain Games</a></td>
<td><a href="../agnt/processAgent.html?viewCompanies">Maintain Companies</a></td>
<c:if test="${adminDetailsForm.openGameList[0] != null}">
    <td><a href="processAdmin.html?createHedgeBets"
            onclick="return confirm('Will Generate and Email Spreadsheets for Current Open Draw. Proceed?')"/>
            Create Hedge Bets</a></td>
</c:if>
<td><a href="processAdmin.html?updateVersion">Update Version</a></td>
</tr>
</br>
</br>
<tr>
<td>View Historic Results : <a href="../anal/processAnalytic.html?numbers_refresh&type=4">4D</a></td>
<td><a href="../anal/processAnalytic.html?numbers_refresh&type=2">3D</a></td>
<!--
<td><a href="processAnalytic.html?numbers_refresh&type=1">2D</a></td>
-->
</tr>
<tr>
</br>
<td><a href="../logon/signin">Logoff</a></td>
</tr>
<br/>
<br/>
</form:form>
</body>
</html>
