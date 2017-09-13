<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>playerRegister</title>
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
<form:form method="post" action="processPlayer.html" modelAttribute="playerRegisterForm">
<h2>Dx4 Registration :</h2>
<table border="0" cellpadding="3" cellspacing="0" width="700">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr>
<td width="30%">Email (Dx4 Username)*:</td>
<td width="70%"><input type="text" style='width:20em' name="newUsername" value="${playerRegisterForm.username}"/></td>
</tr>
<tr>
<td width="30%">Contact:</td>
<td width="70%"><input type="text" name="newProfile.contact" value="${playerRegisterForm.profile.contact}" /></td>
</tr>
<tr>
<td width="30%">Phone:</td>
<td width="70%"><input type="text" name="newProfile.phone" value="${playerRegisterForm.profile.phone}"/></td>
</tr>
</tbody>
</table>
<tr><td>*required field</td></tr>
<br/>
<br/>
<input type="submit" name="register" value="Register" class="button" style="height:30px;"/>
<br/>
<br/>
<tr><td>${playerRegisterForm.message}</td></tr>
</form:form>
<td><a href="get.html">Cancel</a></td>


</body>
</html>
