<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>externalAgentWinResult</title>

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
 /*   width:200px;
    height:150px;
*/
    background-color:#ffffff;
    position:absolute;
    left:500px;
    padding:10px;

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


function submitRefreshPlacings(){

    oFormObject = document.forms['myForm'];

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refreshPlacings';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
}

</script>

<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}

</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
<body>

<form:form method="post" id="myForm" action="processAgent.html" modelAttribute="agentWinForm" >
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc} - ${currUser.contact}</h2>
<h2>Winning Bet Results for Draw: <fmt:formatDate value="${agentWinForm.drawDate}"/></h2>
<tr>
<td>
<input type="image" name="returnPreviousDrawAndWins" width="50" height="30" src="../../img/back.jpg"
			  onMouseOut="this.src='../../img/back.jpg'" onMouseOver="this.src='../../img/back.jpg'"/>
</td>
</tr>

<table align="left" style="margin:0; width:100%; height:30; background: #f8f8f8; color:#000;">
<br/>
	<input type="hidden" name="command.drawDateStr" value="${agentWinForm.drawDateStr}" />

	<c:set var="slen" value="${fn:length(agentWinForm.wph.placings)}" />
	<c:choose>
	<c:when test="${slen == 0}">
	<tr>
		<td>No Lucky Players This Time</td>
		<input type="hidden" name="command.userPlace" value="${currUser.code}" />	</tr>
	</c:when>
	<c:otherwise>
	<tr style="font-family:verdana; color:purple; background-color:LightYellow">
	<td align="left">

<c:set var="placing" value="${agentWinForm.wph.placings[0]}" />
	<input type="hidden" name="command.userPlace" value="${placing}" />


	<form:select path="command.winPlaceHolder.placings[0]"
		onChange="submitRefreshPlacings()" >
	<c:forEach items="${agentWinForm.wnwl}" var="wnw" varStatus="status">
		<option value="${wnw.placingCode}" ${wnw.placingCode eq placing ? 'selected' : ''}>
		${wnw.placingCode}
		</option>
  	</c:forEach>
	</form:select>
    	</td>

	<c:choose>
	<c:when test="${slen > 1}">

	<c:set var="placing1" value="${agentWinForm.wph.placings[1]}" />
	<input type="hidden" name="command.subUserPlace" value="${placing1}" />

	<td align="left">
	<form:select path="command.winPlaceHolder.placings[1]"
		onChange="submitRefreshPlacings()" >
	<c:forEach items="${agentWinForm.subWnwl}" var="wnw" varStatus="status">
    		<option value="${wnw.placingCode}" ${wnw.placingCode eq placing1 ? 'selected' : ''}>
		${wnw.placingCode}
		</option>
  	</c:forEach>
	</form:select>
    	</td>

	<td><input type="submit" name="subPreviousDrawAndWins" value="View"/></td>
	</c:when>
	</c:choose>
	</tr>
	</c:otherwise>
	</c:choose>

</table>

<table width="100%">
<!-- first row -->
<tr>
<!-- first column -->
<c:set var="drw" value="${agentWinForm.drw[0]}" scope="request"/>
<c:set var="draw" value="${currXGR.draws[0]}" scope="request" />
<c:set var="image" value="../../img/logo_magnum.gif" scope="request" />

<td style="width:50%;vertical-align:top">

<jsp:include page="_includeAll/drawDisplay.jsp"/>

<!-- end first column -->
</td>
<!-- second column -->
<c:set var="drw" value="${agentWinForm.drw[1]}" scope="request"/>
<c:set var="draw" value="${currXGR.draws[1]}" scope="request" />
<c:set var="image" value="../../img/logo_toto.gif" scope="request" />

<td style="width:50%;vertical-align:top">

<jsp:include page="_includeAll/drawDisplay.jsp"/>

<!-- end second column -->
</td>

<!-- end first row -->
</tr>

<!-- second row -->
<tr>
<!-- first column -->
<c:set var="drw" value="${agentWinForm.drw[2]}" scope="request"/>
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


</form:form>
</body>
</html>
