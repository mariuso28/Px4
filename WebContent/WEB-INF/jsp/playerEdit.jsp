<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>playerEdit</title>
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
<form:form method="post" action="processPlayer.html" modelAttribute="playerDetailsForm">
<h2>Hi : ${currUser.contact} - Edit Profile</h2>


<table border="0" cellpadding="3" cellspacing="0" width="500">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr>
<td width="30%">Contact:</td>
<td width="70%"><input type="text" name="newProfile.contact" value="${playerDetailsForm.profile.contact}" /></td>
</tr>
<tr>
<td width="30%">Password:</td>
<td width="70%"><input type="password" name="password" value="" style='width:20em'/></td>
</tr>
<tr>
<td width="30%">Verify Password:</td>
<td width="70%"><input type="password" name="vPassword" value="" style='width:20em'/></td>
</tr>
<tr>
<td width="30%">Phone:</td>
<td width="70%"><input type="text" name="newProfile.phone" value="${playerDetailsForm.profile.phone}"/></td>
</tr>
</tbody>
</table>
<br/>
<tr><td>${playerDetailsForm.message}</td></tr>
<br/>
<table border="0" cellpadding="3" cellspacing="0" width="300">
<tbody align="left" style="font-family:verdana; color:purple; background-color:White">
<tr>
<td><input type="submit" name="save_player" value="Save Profile" class="link"/></td>
<td><input type="submit" name="cancel_player" value="Cancel" class="button""/></td>
</tr>
</tbody>
</table>
</form:form>
</body>
</html>
