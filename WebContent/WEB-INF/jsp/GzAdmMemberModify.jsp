<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>admMemberCreate</title>
    <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
<style>
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

function changeMemberRank()
{
  oFormObject = document.forms['myForm'];

  var myin = document.createElement('input');
  myin.type='hidden';
  myin.name='changeMemberRank';
  myin.value='MaHa';
  oFormObject.appendChild(myin);
  oFormObject.submit();
}

</script>

</head>

<body>
  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Profile Details:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="800">
    <tbody align="left" style="color:yellow; background-color:white}">
    <tr>
      <td width="30%"><font color="#33ff36" size="2">Member Id:</font></td>
      <td width="70%">${memberForm.memberSummary.memberId}</td>
    </tr>
    <tr>
    <td width="30%"><font color="#33ff36" size="2">Contact:</font></td>
    <td width="70%"><input type="text" style='width:20em' name="command.contact"
              value="${memberForm.inCompleteCommand.profile.contact}"/></td>
    </tr>
    <tr>
      <td width="30%"><font color="#33ff36" size="2">Password:</font></td>
      <td width="70%"><input type="password" style='width:20em' name="command.password"
                value="${memberForm.inCompleteCommand.profile.password}"/></td>
    <tr>
    </tr>
      <td width="30%"><font color="#33ff36" size="2">Verfiy Password:</font></td>
      <td width="70%"><input type="password" style='width:20em' name="command.vPassword"
                value="${memberForm.inCompleteCommand.vPassword}"/></td>
    </tr>
    <tr>
      <td width="30%"><font color="#33ff36" size="2">email:</font></td>
    <td width="70%"><input type="text" style='width:20em' name="command.profile.email"
                        value="${memberForm.inCompleteCommand.profile.email}" /></td>
    </tr>
    <tr>
      <td width="30%"><font color="#33ff36" size="2">Phone:</font></td>
    <td width="70%"><input type="text" style='width:20em' name="command.profile.phone"
                  value="${memberForm.inCompleteCommand.profile.phone}" /></td>
    </tr>
    </tbody>
    </table>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="memberModify" value="Modify" class="button" style="height:23px;"/></td>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    </tbody>
    </table>
    </form:form>
  </div>

</body>
</html>
