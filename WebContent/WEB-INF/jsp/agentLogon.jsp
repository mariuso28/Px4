<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<html>
<head>
    <title>Dx4 agentLogon</title>
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


<form:form method="post" action="agent_logon.html" modelAttribute="agentLogonForm">

<h2 style="background-color:E3D6FF">DX4   COMPANY..ZMA..SMA..MA..AGENT   LOGON</h2>
</br>
</br>

Enter user name:
<input name="agentLogon.username" style='width:20em' type="text" value="brian@gmail.com"/> 
</br>
</br>
<tr>
<th>${agentLogonForm.agentLogon.message}</th>
</tr>
</br>
<input type="submit"  style='width:20em' name="logon" value="Logon" />

</br>
</br>
<tr>
<td>
   <img width="70" height="70"  src='img/Agent.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/MA.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/SMA.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/ZMA.jpg' border='0'></div></span>
</td>
<td>
   <img width="70" height="70"  src='img/Company.jpg' border='0'></div></span>
</td>
</tr>
</form:form>

</body>
</html>