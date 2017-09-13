<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true" %>


<html>
<head>
    <title>Dx4 adminLogon</title>
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


<form:form method="post" action="admin_logon.html" modelAttribute="adminLogonForm">

<h2 style="background-color:F7D6FF">DX4 ADMIN LOGON</h2>

</br>
</br>

Enter user name:
<input name="adminLogon.username" style='width:20em' type="text" value="dx4admin@hotmail.com"/> 
</br>
</br>
<tr>
<th>${adminLogonForm.adminLogon.message}</th>
</tr>
</br>
<input type="submit"  style='width:20em' name="logon" value="Logon" />
</br>
</br>
<tr>
<td style="width:10%;">
   <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1"><div class='m4d-num-list'><img src='img/Admin.jpg' border='0'></div></span>
</td>
</tr>



</form:form>

</body>
</html>