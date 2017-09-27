<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
  <title>GzPackage</title>
  <style>
  </style>
</head>

<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">

  function submitEditPackage(pname,gname){

//     alert("IN:" + pname + " " + gname);

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

    <h2 style="color:Cyan">Manage Packages for ${currUser.memberId} - ${currUser.contact}</h2>
    <table border="1" style="width:100%;" align="left">
    <colgroup>
        <col span="1" style="width: 4%;">
        <col span="1" style="width: 8%;">
        <col span="1" style="width: 8%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 50%;">
        <col span="1" style="width: 20%;">
    </colgroup>
    <c:forEach items="${currGroupMap}" var="group" varStatus="status">
      <tr style="font-family:verdana; color:white; background-color:darkblue">
        <td colspan="6" style="text-align:center; vertical-align:middle;">${group.value.name}</td>
      </tr>
        <tr style="font-family:verdana; color:white; background-color:darkblue">
        <td></td>
        <td>Details</td>
        <td>Package Id</td>
        <td>Package Name</td>
        <td style="text-align:center; vertical-align:middle;">Big/Small/4A/3A/3C/2A</td>
        <td style="text-align:center; vertical-align:middle;">Comm</td>
      </tr>
      <c:forEach items="${group.value.packages}" var="package1" varStatus="status1">
        <c:set var="pname" value="${package1.value.name}" />
        <c:set var="gname" value="${group.value.name}" />
        <tr style="font-family:verdana; color:darkblue; background-color:lightblue">
        <td></td>
        <c:choose>
          <c:when test="${package1.value.expanded == false}">
              <td><a href="expand?expand&mode=true&gname=${group.value.name}&pname=${package1.value.name}">Open</a></td>
          </c:when>
        	<c:otherwise>
              <td><a href="expand?expand&mode=false&gname=${group.value.name}&pname=${package1.value.name}">Close</a></td>
          </c:otherwise>
        </c:choose>
        <td>${package1.value.id}</td>
        <td>${package1.value.name}</td>
        <td style="text-align:center; vertical-align:middle;">${package1.value.summaryPayouts}</td>
        <td>${package1.value.summaryCommissions}</td>
        <c:if test="${package1.value.expanded == true}">
        <tr>
        <td colspan="6">
        <table border="1" cellpadding="3" cellspacing="0" width="100%">
            <tr style="font-family:verdana; color:white; background-color:darkblue;">
              <td>
                <input type="submit" name="xxx" value="Modify"
                    class="button" onClick="return submitEditPackage('${pname}','${gname}')"
                    style="height:16px; background-color:green;"/></td>

          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
            <td>
              ${gameTypePayout.value.gameType.shortName}
            </td>
            </c:forEach>
          </tr>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             Comm
          </td>
          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.commission}
          </td>
          </c:forEach>
          <tr style="font-family:verdana; color:darkblue; background-color:LightBlue;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             1st
          </td>
          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[0].payOut}
          </td>
          </c:forEach>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             2nd
          </td>
          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[1].payOut}
          </td>
          </c:forEach>
          <tr style="font-family:verdana; color:darkblue; background-color:LightBlue;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             3rd
          </td>
          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[2].payOut}
          </td>
          </c:forEach>
          <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
          <td style="font-family:verdana; color:white; background-color:darkblue;">
             4th
          </td>
          <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
          <td>
            ${gameTypePayout.value.payOuts[3].payOut}
          </td>
          </c:forEach>
        </tr>
        <tr style="font-family:verdana; color:darkblue; background-color:LightYellow;">
        <td style="font-family:verdana; color:white; background-color:darkblue;">
           5th
        </td>
        <c:forEach items="${package1.value.gameTypePayouts}" var="gameTypePayout" varStatus="status2">
        <td>
          ${gameTypePayout.value.payOuts[4].payOut}
        </td>
        </c:forEach>
        </tr>
        </table>
        </td>
        </tr>
      </c:if>
      </c:forEach>
      <tr style="font-family:verdana; color:white; background-color:darkblue">
        <td colspan="6" style="text-align:center; vertical-align:middle;"><a href="createNewPackage?create&gname=${group.value.name}">Create New Package For Group</a></td></td>
      </tr>
    </c:forEach>
    </table>
    <tr><td><font color="red" size="3">${packageForm.errMsg}</font></td></tr>
    <tr><td><font color="blue" size="3">${packageForm.infoMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="cancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    <br/>
    <br/>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
