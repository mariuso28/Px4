<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>adminEdit</title>
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
<form:form method="post" action="processAdmin.html" modelAttribute="adminDetailsForm">
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact}
- Edit Profile
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.jpg' border='0'>
</h2>

<table>
<tr height="20">
<td>
		<a href="processAdmin.html?cancel_admin"><img src="../../img/back.jpg" width="50" height="30"></a>
	</td>
</tr>
<tr height="20">
	<td>&nbsp</td>
</tr>
</table>


<table border="0" cellpadding="3" cellspacing="0" width="500">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr>
<td width="30%">Contact:</td>
<td width="70%"><input type="text" name="newProfile.contact" value="${adminDetailsForm.profile.contact}" /></td>
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
<td width="70%"><input type="text" name="newProfile.phone" value="${adminDetailsForm.profile.phone}"/></td>
</tr>
</tbody>
</table>
<br/>
<tr><td>${adminDetailsForm.message}</td></tr>
<br/>
<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:White">
<tr>
<td><input type="submit" name="save_admin" value="Save Profile" class="button" style="height:23px;"/></td>
</tr>

</tbody>
</table>
</form:form>
</body>
</html>
