<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>adminVersion</title>
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
<form:form method="post" action="processAdmin.html" modelAttribute="versionForm" enctype='multipart/form-data'>
<h2 style="background-color:${currUser.role.color}">${currUser.role.desc}&nbsp; ${currUser.contact} - Edit Version</h2>

<table border="0" cellpadding="3" cellspacing="0" width="500">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightYellow">
<tr>
<td width="30%">Version code:</td>
<td width="70%"><input type="text" name="command.code" value="${versionForm.code}" /></td>
</tr>
<tr>
<td width="30%">Upload Apk:</td>
<td width="70%"><input type="file" name="command.apk"/></td>
</tr>
</tbody>
</table>
<br/>
<tr><td>${versionForm.message}</td></tr>
<br/>
<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:White">
<tr>
<td><input type="submit" name="saveVersion" value="Save Version" class="button" style="height:23px;"/></td>
<td><input type="submit" name="cancelVersion" value="Cancel" class="button" style="height:23px;"/></td>
</tr>
</tbody>
</table>
</form:form>
</body>
</html>
