<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
  <title>GzPackageModify</title>
  <style>
  </style>
</head>

<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">

  function submitEditPackage(pname,gname){

     alert("IN:" + pname + " " + gname);

      oFormObject = document.forms['myForm'];

      var field = "command.gname";
      oformElement = oFormObject.elements[field];
      oformElement.value = gname;

      var field = "command.pname";
      oformElement = oFormObject.elements[field];
      oformElement.value = pname;

      var myin = document.createElement('input');
      myin.type='hidden';
      myin.name='modify';
      myin.value='MaHa';
      oFormObject.appendChild(myin);
      oFormObject.submit();

      return false;
  }

  </script>


  <div class="main">
    <form:form id="myForm" method="post" action="processPackage" modelAttribute="packageForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      <input type="hidden" name="command.gname" value="xxx" />
      <input type="hidden" name="command.pname" value="xxx" />

    <h2 style="color:Cyan">Modify Package for ${currUser.memberId} - ${currUser.contact}</h2>
    <table border="1" style="width:100%;" align="left">
    <colgroup>
        <col span="1" style="width: 4%;">
        <col span="1" style="width: 8%;">
        <col span="1" style="width: 8%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 50%;">
        <col span="1" style="width: 20%;">
    </colgroup>
      <tr style="font-family:verdana; color:white; background-color:darkblue">
        <td colspan="6" style="text-align:center; vertical-align:middle;">${currPackage.group.name}</td>
      </tr>
        <tr style="font-family:verdana; color:white; background-color:darkblue">
        <td></td>
        <td>Details</td>
        <td>Package Id</td>
        <td>Package Name</td>
        <td style="text-align:center; vertical-align:middle;">Big/Small/4A/3A/3C/2A</td>
        <td style="text-align:center; vertical-align:middle;">Comm</td>
      </tr>
        <tr style="font-family:verdana; color:darkblue; background-color:lightblue">
        <td></td>
        <td></td>
        <c:if test="${packageForm.createNew == false}">
          <td>${currPackage.id}</td>
          <td>${currPackage.name}</td>
        </c:if>
        <c:if test="${packageForm.createNew == true}">
            <td></td>
            <td><input type="text" style='ont-family:verdana;color:black;width:16em' name="command.newPackageName"
                              value="${gameTypePayout.value.commission}" /></td>
        </c:if>
        <td style="text-align:center; vertical-align:middle;">${currPackage.summaryPayouts}</td>
        <td>${currPackage.summaryCommissions}</td>
        <tr>
        <td colspan="6">
        <table border="1" cellpadding="3" cellspacing="0" width="100%">
            <tr style="font-family:verdana; color:white; background-color:darkblue;">
              <td>
              </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
            <td>
              ${gameTypePayout.value.gameType.shortName}
            </td>
            </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             Comm
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.commission}
          </td>
          </c:forEach>
          </tr>

          <tr style="font-family:verdana; color:darkblue; background-color:white;">
          <td>
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].commission"
                              value="${gameTypePayout.value.commission}" />
          </td>
          </c:forEach>
          </tr>

          <tr style="font-family:verdana; color:darkblue; background-color:LightBlue;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             1st
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[0].payOut}
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:white;">
          <td>
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            <c:if test="${gameTypePayout.value.payOuts[0].payOut != null}">
                <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].payOuts[0]"
                              value="${gameTypePayout.value.payOuts[0].payOut}" />
            </c:if>
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             2nd
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[1].payOut}
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:white;">
          <td>
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            <c:if test="${gameTypePayout.value.payOuts[1].payOut != null}">
                <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].payOuts[1]"
                              value="${gameTypePayout.value.payOuts[1].payOut}" />
            </c:if>
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:LightBlue;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             3rd
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[2].payOut}
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:white;">
            <td>
            </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
            <td>
            <c:if test="${gameTypePayout.value.payOuts[2].payOut != null}">
                <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].payOuts[2]"
                              value="${gameTypePayout.value.payOuts[2].payOut}" />
            </c:if>
          </td>
          </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             4th
          </td>
          <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[3].payOut}
          </td>
          </c:forEach>
        </tr>
        <tr style="font-family:verdana; color:darkblue; background-color:white;">
        <td>
        </td>
        <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
        <td>
          <c:if test="${gameTypePayout.value.payOuts[3].payOut != null}">
              <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].payOuts[3]"
                            value="${gameTypePayout.value.payOuts[3].payOut}" />
          </c:if>
        </td>
        </c:forEach>
        </tr>
        <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
        <td style="font-family:verdana; color:white; background-color:darkblue;">
           5th
        </td>
        <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
        <td>
          ${gameTypePayout.value.payOuts[4].payOut}
        </td>
        </c:forEach>
        </tr>
        <tr style="font-family:verdana; color:darkblue; background-color:white;">
        <td>
        </td>
        <c:forEach items="${currPackage.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
        <td>
          <c:if test="${gameTypePayout.value.payOuts[4].payOut != null}">
              <input type="text" style='ont-family:verdana;color:black;width:4em' name="command.gameTypePayoutsEntry[${status2.index}].payOuts[4]"
                            value="${gameTypePayout.value.payOuts[4].payOut}" />
          </c:if>
        </td>
        </c:forEach>
        </tr>
        </table>
        </td>
        </tr>
    </table>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <c:if test="${packageForm.createNew == false}">
      <td><input type="submit" name="modifyPackage" value="Modify Existing" class="button" style="height:23px; background-color:green;"/></td>
      <td><input type="submit" name="saveAsNew" value="Save As New" class="button" style="height:23px; background-color:green;"/></td>
    </c:if>
    <c:if test="${packageForm.createNew == true}">
      <td><input type="submit" name="storeNewPackage" value="Save" class="button" style="height:23px; background-color:green;"/></td>
    </c:if>
    <td><input type="submit" name="cancelModify" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    <br/>
    <br/>
    <tr><td><font color="red" size="3">${packageForm.errMsg}</font></td></tr>
    <tr><td><font color="blue" size="3">${packageForm.infoMsg}</font></td></tr>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
