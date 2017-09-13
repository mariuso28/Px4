<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


<html>
  <head>
    <link href="../../../css/style.css" rel="stylesheet" type="text/css" />
    <title>summaryMembers</title>
    <style>
    </style>
  </head>

  <body>
    <div class="main">

        <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
    		<div class="headerWelcome">
    			<h2 style="color:#aaa; font-weight:700;">
    				${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font> - Account Summary
    			</h2>
    		</div>
        <div class="navHeading">
          <a href="cancel"><img src="../../../img/back2.png" width="30" height="30"></a>
        </div>
        <div style="float:left; width:100%;">
          <table border="0" style="width:100%;">
                  <colgroup>
                      <col span="1" style="width: 15%;">
                      <col span="1" style="width: 15%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                      <col span="1" style="width: 10%;">
                  </colgroup>
                  <tr style="color:cyan; background-color:#555;">
                    <td align="left">Member</td>
                    <td align="left">Rank</td>
                    <td align="left">F/R</td>
                    <c:forEach items="${currSummaryMembers.gameTypes}" var="gameType">
                        <td align="left">${gameType.shortName}</td>
                    </c:forEach>
                  </tr>
                    <c:forEach items="${currSummaryMembers.rows}" var="row" varStatus="status">
                    	<c:set var="useRow" value="${row}" scope="request"/>
                      <jsp:include page="_includeAll/summaryMember.jsp"/>
                  </c:forEach>
          </table>
        </div>
  </body>
</html>
