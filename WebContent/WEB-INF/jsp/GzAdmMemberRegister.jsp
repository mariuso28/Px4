<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>admMemberRegister</title>
    <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
<style>
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>


</head>

<body>
  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Register Member</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple; background-color:white}">
    <tr>
        <td width="50%"><font color="#33ff36" size="2">Member Rank</font></td>
        <td width="50%">
         <form:select path="command.role" style='width:20em'>
           <c:forEach items="${memberForm.roles}" var="role" >
              <option value="${role}"  ${memberForm.inCompleteCommand.role eq role ? 'selected' : ''}>
                       ${role.desc}</option>
           </c:forEach>
          </form:select>
       </td>
     </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Contact</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.profile.contact"
                  value="${memberForm.inCompleteCommand.profile.contact}" /></td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Nick Name</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.profile.nickname"
                  value="${memberForm.inCompleteCommand.profile.nickname}" /></td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Email</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.profile.email"
                  value="${memberForm.inCompleteCommand.profile.email}" /></td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Phone</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.profile.phone"
                  value="${memberForm.inCompleteCommand.profile.phone}" /></td>
    </tr>
    </tbody>
    </table>
    <c:if test="${currUser.role.shortCode != 'adm'}">
    <br>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    <tr><td width="50%"><font color="cyan" size="3">Account Details:</font></td></tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Bet Commission (%)</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.betCommission"
                  value="${memberForm.inCompleteCommand.betCommission}" /></td>
    </tr>
    <tr>
      <td width="50%">Max Available</td>
      <td width="50%">
      <font color="red" size="3">
                    <fmt:formatNumber value="${currUser.account.betCommission}"
                 type="number" maxFractionDigits="2" minFractionDigits="2" />
      </font>
      </td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Win Commission (%)</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.winCommission"
                  value="${memberForm.inCompleteCommand.winCommission}" /></td>
    </tr>
    <tr>
      <td width="50%">Max Available</td>
      <td width="50%">
      <font color="red" size="3">
                    <fmt:formatNumber value="${currUser.account.winCommission}"
                 type="number" maxFractionDigits="2" minFractionDigits="2" />
      </font>
      </td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Credit ($)</font></td>
      <td width="50%"><input type="text" style='width:20em' name="command.credit"
                  value="${memberForm.inCompleteCommand.credit}" /></td>
    </tr>
    <tr>
      <td width="50%">Max Available</td>
      <td width="50%">
      <font color="red" size="3">
                    <fmt:formatNumber value="${memberForm.maxCredit}"
                 type="number" maxFractionDigits="2" minFractionDigits="2" />
      </font>
      </td>
    </tr>
    </tbody>
    </table>
    </c:if>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="memberRegister" value="Register" class="button" style="height:23px;"/></td>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
