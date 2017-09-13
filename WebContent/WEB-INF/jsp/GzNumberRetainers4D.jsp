<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
  <title>numberRetainers4D</title>
  <style>
  </style>
</head>

<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">

  function modify4DNumber(changeIndex){

  //    alert(changeIndex);

      oFormObject = document.forms['myForm'];

      var field = "command.changeIndex";
      oformElement = oFormObject.elements[field];
      oformElement.value = changeIndex;

      var myin = document.createElement('input');
      myin.type='hidden';
      myin.name='modify4DNumber';
      myin.value='MaHa';
      oFormObject.appendChild(myin);
      oFormObject.submit();

      return false;
  }

  </script>


  <div class="main">
    <form:form id="myForm" method="post" action="processNumbers" modelAttribute="numberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      <input type="hidden" name="command.changeIndex" value="-1" />

    <h2 style="color:Cyan">Manage Numbers for ${currNumberRetainerSet.digits} digit games:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="1000">
    <tbody align="left" style="color:Yellow; background-color:white}">
    <tr>
    <td><font color="#33ff36" size="2">Default Retains:</font></td>
    </tr>
    <tr>
    <c:forEach items="${currNumberRetainerSet.defaults}" var="nr" >
      <td width="20%">${nr.gameType}</td>
    </c:forEach>
    </tr>
    <tr>
    <c:forEach items="${currNumberRetainerSet.defaults}" var="nr" varStatus="status">
      <td width="20%"><input type="text" style='width:6em' name="command.defaults[${status.index}]"
                  value="${nr.retain}" />
    </td>
    </c:forEach>
    <td><input type="submit" name="modify4DDefaults" value="Modify" class="button" style="height:23px;"/></td>
    </tr>
    <tr>
    <td><font color="#33ff36" size="2">Individual Retains:</font></td>
    </tr>
    <c:forEach items="${currNumberRetainerSet.individuals}" var="indi" varStatus="status">
      <td width="20%">${indi.value[0].number}</td>
      <tr>
      <c:forEach items="${indi.value}" var="nr" varStatus="status1">
        <td width="20%"><input type="text" style='width:6em' name="command.modifyValues[${status.index}][${status1.index}]"
                    value="${nr.retain}" />
      </td>
      </c:forEach>
      <td><input type="submit" name="xxxx" value="Modify" class="button"
                      onClick="return modify4DNumber(${status.index})" style="height:23px;"/></td>
      </tr>
    </c:forEach>
    <tr><td>Add Number</td>
      <td width="20%"><input type="text" style='width:4em' name="command.newNumber"
                value="" />
    </tr>
    <tr>
    <c:forEach items="${currNumberRetainerSet.defaults}" var="nr" varStatus="status">
      <td width="20%"><input type="text" style='width:6em' name="command.newValues[${status.index}]"
                  value="${nr.retain}" />
    </td>
    </c:forEach>
    <td><input type="submit" name="create4DNumber" value="Submit" class="button" style="height:23px;"/></td>
    </tr>
    </tbody>
    </table>
    <tr><td><font color="red" size="3">${numberForm.errMsg}</font></td></tr>
    <tr><td><font color="blue" size="3">${numberForm.infoMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="numberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    <br/>
    <br/>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
