<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
  <title>admMemberManage</title>
  <style>
  </style>
</head>

<body>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

  function changeMemberToChange(digits)
	{
		oFormObject = document.forms['myForm'];
		var field = "command.memberToChangeCode";
		oformElement = oFormObject.elements[field];
		oformElement.value = digits;

		var myin = document.createElement('input');
		myin.type='hidden';
		myin.name='changeMemberToChange';
		myin.value='MaHa';
		oFormObject.appendChild(myin);
		oFormObject.submit();
	}

</script>

  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Manage Member Account For: ${currUser.memberId} - ${currUser.contact}</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="1000">
    <tbody align="left" style="color:purple; background-color:white}">
    <tr>
    <td><font color="#33ff36" size="2">Member Search:</font></td>
    </tr>
     <tr>
     <td width="40%">
         <form:select path="command.memberToChangeCode"  style='width:20em' onchange="changeMemberToChange(value);">
           <c:forEach items="${memberForm.chooseMembers}" var="member" >
              <option value="${member.memberId}"  ${memberForm.inCompleteCommand.memberToChangeCode eq member.memberId ? 'selected' : ''}>
                      ${member.memberId} - ${member.contact} - ${member.role} - superior: ${member.parent.memberId}</option>
           </c:forEach>
          </form:select>
     </td>
     <td width="20%"><input type="text" style='width:20em' name="command.search"
               value="${memberForm.inCompleteCommand.search}"/></td>
     <td width="20%"><input type="submit" name="searchContact" value="Search Contact"class="button" style="height:23px; background-color:blue;"/></td>
     <td width="20%"><input type="submit" name="searchPhone" value="Search Phone"class="button" style="height:23px; background-color:blue;"/></td>
     <td width="20%"><input type="submit" name="searchEmail" value="Search Email"class="button" style="height:23px; background-color:blue;"/></td>
    </tr>
    </tbody>
  </table>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:yellow; background-color:white}">
    <br>
    <br>
    <tr><td width="50%"><font color="cyan" size="3">Member Details:</font></td></tr>
    <tr>
        <td width="50%"><font color="#33ff36" size="2">Member Rank</font></td>
        <td width="50%">${memberForm.inCompleteCommand.role.desc}</td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Contact</font></td>
      <td width="50%">${memberForm.inCompleteCommand.profile.contact}</td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Nick Name</font></td>
      <td width="50%">${memberForm.inCompleteCommand.profile.nickname}</td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Email</font></td>
      <td width="50%">${memberForm.inCompleteCommand.profile.email}</td>
    </tr>
    <tr>
      <td width="50%"><font color="#33ff36" size="2">Phone</font></td>
      <td width="50%">${memberForm.inCompleteCommand.profile.phone}</td>
    </tr>
    </tbody>
    </table>
    <c:if test="${currUser.role != 'ROLE_ADMIN'}">
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
      <td width="50%"><font color="red" size="3">Max Available</font></td>
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
      <td width="50%"><font color="red" size="3">Max Available</font></td>
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
      <td width="50%"><font color="red" size="3">Max Available</font></td>
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
    <c:if test="${memberForm.inCompleteCommand.enabled == false}">
        <td><input type="submit" name="activateMember" value="Activate" class="button" style="height:23px;"/></td>
    </c:if>
    <c:if test="${memberForm.inCompleteCommand.enabled == true}">
        <td><input type="submit" name="deactivateMember" value="Deactivate" class="button" style="height:23px;"
          onClick="return confirm('Deactivation will deactivate all downline members. Are you sure')";/></td>
    </c:if>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <tr><td><font color="blue" size="3">${memberForm.infoMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="memberModifyAccount" value="Modify" class="button" style="height:23px;"/></td>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    <br/>
    <br/>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
