<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<html>
<head>
    <title>Dx4 multiLogon</title>
<style>
th{
text-align:left;
}

body{
font: 20px Arial, sans-serif;
}



</style>
</head>
<body>

<script type="text/javascript">

function submitRefresh(){
	
    oFormObject = document.forms['myForm'];
   
    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='refresh';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit(); 
} 

</script>


<form:form method="post" id="myForm" action="multi_logon.html" modelAttribute="multiLogonForm">

<th>DX4 MEMBER LOGON</th>
</br>
<tr>
<c:choose>
<c:when test="${multiLogonForm.editMode eq 'on'}">
	<td><a href="multiLogon.html?editMode&mode=off" style="text-decoration:none;color:green">Edit Mode: ON</a></td>
</c:when>
<c:otherwise>
	<td><a href="multiLogon.html?editMode&mode=on" style="text-decoration:none;color:green">Edit Mode: OFF</a></td>
</c:otherwise>
</c:choose>
</tr>
</br>
</br>
<c:forEach items="${multiLogonForm.selected}" var="sel" varStatus="status">
	<c:set var="idx" value="${status.index}" />
	<input type=hidden name="logon.selected[${idx}]" value=
"${sel}" />	
</c:forEach>
	
<table align="left" style="margin:0; padding:10px; width:80%; font-family:verdana; background: #f8f8f8; color:#000;">   
	<tr>
	<c:forEach items="${multiLogonForm.memberList}" var="mhs" varStatus="status">
	<c:set var="idx" value="${status.index}" />
	<c:set var="sel" value="${multiLogonForm.selected[idx]}" />
	<tr style="background-color:${mhs[sel].role.color}">
	<td>${mhs[sel].role.shortCode}</td>
	<td>
		<form:select path="logon.changed[${idx}]" onChange="submitRefresh();">
			<c:forEach items="${mhs}" var="mh" varStatus="num" >
    				<option value="${mh.code}" ${num.index == sel ? 'selected' : ''}>${mh.code}</option>
  			</c:forEach>
	    	</form:select>
	</td>
	<c:set var="code" value="${mhs[sel].code}" />
	<td><a href="memberLogon.html?usercode=${code}">${mhs[sel].username}</a></td>
	<c:choose>
	    <c:when test="${multiLogonForm.deleteEnabled == 'true' && multiLogonForm.editMode eq 'on'}">
		<td><a href="processAdmin.html?deleteMember&code=${mhs[sel].code}" onclick="return confirm('!! WILL DELETE ALL SUBMEMBERS, BETS AND TRANSACTIONS - YOU BETTER BE DAMN SURE!! WELL??')">DELETE</a></td>
	    </c:when>
	</c:choose>
	</tr>
	</c:forEach>
	</table>
</br>
</br>
</br>
</br>
<tr>
<table align="left" style="margin:0; padding:10px; width:80%; font-family:verdana; background: #f8f8f8; color:#000;">   
<tr>
<c:forEach items="${multiLogonForm.icons}" var="icon" varStatus="status">
<td>
   <img width="70" height="70"  src='img/${icon}.jpg' border='0'></div></span>
</td>
</c:forEach>
</tr>
<tr>
<c:forEach items="${multiLogonForm.descs}" var="desc" varStatus="status">
<td>
   ${desc}
</td>
</c:forEach>
</tr>
</table>
</tr>

</form:form>

</body>
</html>