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

Enter user name:
<input name="agentLogon.username" style='width:20em' type="text" value="yoyo@hotmail.com"/> 
</br>
</br>
<tr>
<th>${agentLogonForm.agentLogon.message}</th>
</tr>
</br>
<input type="submit"  style='width:20em' name="logon" value="Logon" />



</form:form>

</body>
</html>