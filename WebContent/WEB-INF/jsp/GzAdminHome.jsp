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

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>


<form:form method="post" id="myForm" action="exec" modelAttribute="adminForm">

  <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

<h2 style="background-color:${currUser.role.color}">Hi ${currUser.role.desc}&nbsp; ${currUser.contact}

&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../../img/${currUser.role.shortCode}.png' border='0'>
</h2>

<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr><td width="30%">Member Id:</td><td width="30%">${currUser.memberId}</td>
<tr><td width="30%">Password:</td>
  <td width="30%"><input name="command.profile.password" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr><td width="30%">Verify Password:</td>
  <td width="30%"><input name="command.verifyPassword" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
<tr><td width="30%">Email:<td width="30%"><input name="command.profile.email" type="text" value="${currUser.email}" size="40" maxlength="64"
  style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
</td>
<tr><td width="30%">Contact:</td>
  <td width="30%"><input name="command.profile.contact" type="text" value="${currUser.contact}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
  <td width="30%">Phone:</td><td width="30%">
  <input name="command.profile.phone" type="phone" value="${currUser.phone}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
  <td width="30%">
    <input type="submit" name="modifyAdmin" value="Modify Profile" class="button" style="height:23px;"/>
  </td>
</tr>
</tbody>
</table>
<br/>
<tr><td><font color="red">${adminForm.errMsg}</font></td></tr>
</br>
</br>
<tr>
  <td><a href="../adm/logon?user&memberId=${currUser.memberId}">Member Management</a></td>
</tr>

</br>
<tr>
<td><a href="../../../logon/signin">Logoff</a></td>
</tr>


</form:form>
</body>
</html>
