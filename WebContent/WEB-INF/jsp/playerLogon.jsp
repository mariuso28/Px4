<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Dx4 Player Logon</title>
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
 

<form:form method="post" action="submit.html" modelAttribute="playerLogonForm">

<h2 style="background-color:FFD6D6">DX4 PLAYER LOGON</h2>

</br>
</br>

Enter user name:
<input name="playerLogon.username"  style='width:20em' type="text" value="fan1@gmail.com"/> 
</br>
</br>
<tr>
<th>${playerLogonForm.playerLogon.message}</th>
</tr>
</br>
<tr><input type="submit"  style='width:20em' name="logon" value="Logon" /></tr>

</br>
</br>
<tr>
<td>
   <img width="70" height="70"  src='img/Player1.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/Player2.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/Player3.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/Player4.jpg' border='0'></div></span>
</td>
</tr>
</br>
</br>
<!-- <td><a href="registerNew.html">Register New Player</a></td> -->
</tr>

</form:form>
</html>