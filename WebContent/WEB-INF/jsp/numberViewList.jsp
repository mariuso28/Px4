<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>numberViewList</title>


<style type="text/css">
h1{
page-break-before: always;
}

body{
font: 20px Arial, sans-serif;
}

.topbar 				{ width:1200px; height:130px; background-color:#ffd6d6; padding:0px; }
.topbartitle 			{ float:left; width:300px;  height:40px; padding-left:32px; padding-top:24px; vertical-align:middle; }
.topbartitlepic			{ float:left; width:50px;  height:50px; padding-top:40px; padding-bottom:40px; padding-left:16px; padding-right:16px; }
.leaderboard			{ float:right; width:728px;  height:90px; padding:20px; }
.topheading				{ float:left; width:1200px; height:60px; padding:0px; }
.resultspanel			{ float:left; width:1200px; height:120px; padding:0px; }

</style>
</head>

<body>
<div class="topbar">
  <div class="topbartitle">
    <h2 style="background-color:${currUser.role.color};">${currUser.role.desc}&nbsp; ${currUser.contact}</h2>
  </div>
  <div class="topbartitlepic"><img width="50" height="50" src='../../img/${currUser.icon}.jpg' border='0'></div>
  <div class="leaderboard"> <img src="../../img/leaderboard.png" width="728" height="90" /> </div>
</div>
<div class="topheading">
  <div class="topheadingtext">
    <h2>
	  <a href="processAnalytic.html?cancelViewNumber"><img src="../../img/back.jpg" width="50" height="30"></a>
      Matching Numbers For Search Term : ${numberViewListForm.searchTerm}
	</h2>
  </div>
</div>

<form:form method="post" action="processAnalytic.html" modelAttribute="numberViewListForm">

<div class="resultspanel">
   <table>
		  </br>
          <tr>
			<td>Keywords : ${numberViewListForm.keywords}</td>
		  </tr>
	</table>
      </br>
      <c:choose>
	  <c:when test="${numberViewListForm.digits == '4'}" >
      <table>
          <tbody>
          <c:forEach var="pg" items="${numberViewListForm.elements}" varStatus="status">
		  <tr>
          <td>
          <table style="width:30px;float:left;">
          <tr>
          <td>
          <table style="width:30px;float:left;">
          <tr>
          <td>
              <table style="width:10px;">
              <c:forEach var="index" begin="0" end="4">
              <tr>
              <c:set var="column" value="${pg.numbers[index]}" scope="request"/>
              <td>
              <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberViewListForm.external}" style="text-decoration:none;">
                              <link rel="xyz" type="text/css" href="color.css">
                              <font color="${column.level.color}" size="3">
                                  ${column.number}
                               </font>
                              </link>
                          </a>
              </td>
              </tr>
              </c:forEach>
              </table>
          </td>
          <td>
              <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
              <img width="100" height="100" src='../../img/${pg.token}.png' border='0'>
              </span>
          </td>
          <td>
              <table style="width:10px;">
              <c:forEach var="index" begin="5" end="9">
              <tr>
              <c:set var="column" value="${pg.numbers[index]}" scope="request"/>
              <td>
              <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberViewListForm.external}" style="text-decoration:none;">
                              <link rel="xyz" type="text/css" href="color.css">
                              <font color="${column.level.color}" size="3">
                                  ${column.number}
                               </font>
                              </link>
                          </a>
              </td>
              </tr>
              </c:forEach>
              </table>
          </td>
          </tr>
          </table>
           <table>
                          <tr>
                              <div align="center">
                                  ${pg.description}
                              </div>
                          </tr>
                      </table>
					  <table>
                          <tr>
                              <div align="center"">
                                  ${pg.descriptionCh}
                              </div>
                          </tr>
                      </table>
		  </td>
          </tr>
          </table>
          </td>
          </tr>
          </c:forEach>
          </tbody>
          </table>
          </c:when>
		  <c:otherwise>
          <table>
              <tbody>
              <c:forEach var="pg" items="${numberViewListForm.elements}" varStatus="status">
			  <tr>
              <td>
                  <table style="width:150px;float:left;">
                      <tr>
                      <td>
                      <table style="width:150px;float:left;">
                          <tr>
                              <div align="center">
                                  <c:set var="column" value="${pg.numbers[0]}" scope="request"/>
                                  <a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberViewListForm.external}" style="text-decoration:none;">
                                              <link rel="xyz" type="text/css" href="color.css">
                                              <font color="${column.level.color}" size="3">
                                                  ${column.number}
                                               </font>
                                              </link>
                                          </a>
                              </div>
                          </tr>
                      </table>
                      <table style="width:150px;float:left;">
                          <tr>
                              <td>
                                  <div align="center">
                                      <span id="ctl00_ContentPlaceHolder1_GridView1_ctl02_Label1">
                                          <img width="100" height="100" src='../../img/${pg.token}.png' border='0'>
                                      </span>
                                  </div>
                              </td>
                          </tr>
                      </table>
                      <table>
                          <tr>
                              <div align="center" style="height:150px;">
                                  ${pg.description}
                              </div>
                          </tr>
                      </table>
					   <table>
                          <tr>
                              <div align="center"">
                                  ${pg.descriptionCh}
                              </div>
                          </tr>
                      </table>
					  </td>
					  </tr>
                  </table>
              </td>
              </tr>
			  </c:forEach>
              </tbody>
          </table>
		  </c:otherwise>
		  </c:choose>
</div>

</form:form>

<body>
