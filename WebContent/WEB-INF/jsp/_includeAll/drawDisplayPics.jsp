<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="outerbox">
<table class="resultTable"  border="1" align="center">
<tr>
<td>
<table id="songlist" class="resultTable"  border="0" align="center">
<br/>
<tr>
<c:set var="f1" value="${draw0.firstPlace}"/>
<c:set var="f2" value="${draw1.firstPlace}"/>
<c:set var="f3" value="${draw2.firstPlace}"/>
<td style="color:00B00F">${fn:substring(f1, 1, 4)}
<div id="winner">${draw0.provider.name} ${f1} ${draw0.firstDesc} ${draw0.firstDescCh}</div>
</td>
<td style="color:00B00F">${fn:substring(f2, 1, 4)}
<div id="winner">${draw1.provider.name} ${f2} ${draw1.firstDesc} ${draw1.firstDescCh}</div>
</td>
<td style="color:00B00F">${fn:substring(f3, 1, 4)}
<div id="winner">${draw2.provider.name} ${f3} ${draw2.firstDesc} ${draw2.firstDescCh}</div>
</td>
</tr>

<tr>
<td style="width:10%;">
  <div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw0.firstImage}" alt="No image" border='0'></div>
</td>
<td style="width:10%;">
  	<div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw1.firstImage}" alt="No image" border='0'></div>
</td>
<td style="width:10%;">
 	<div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw2.firstImage}" alt="No image" border='0'></div>
</td>
</tr>


<tr>
<c:set var="f1" value="${draw0.secondPlace}"/>
<c:set var="f2" value="${draw1.secondPlace}"/>
<c:set var="f3" value="${draw2.secondPlace}"/>
<td style="color:FFA600">${fn:substring(f1, 1, 4)}
<div id="winner">${draw0.provider.name} ${f1} ${draw0.secondDesc} ${draw0.secondDescCh}</div>
</td>
<td style="color:FFA600">${fn:substring(f2, 1, 4)}
<div id="winner">${draw1.provider.name} ${f2} ${draw1.secondDesc} ${draw1.secondDescCh}</div>
</td>
<td style="color:FFA600">${fn:substring(f3, 1, 4)}
<div id="winner">${draw2.provider.name} ${f3} ${draw2.secondDesc} ${draw2.secondDescCh}</div>
</td>
</tr>

<tr>
<td style="width:10%;">
  <div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw0.secondImage}" alt="No image" border='0'></div>
</td>
<td style="width:10%;">
  	<div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw1.secondImage}" border='0'></div>
</td>
<td style="width:10%;">
 	<div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw1.thirdImage}" border='0'></div>
</td>
</tr>

<tr>
<c:set var="f1" value="${draw0.thirdPlace}"/>
<c:set var="f2" value="${draw1.thirdPlace}"/>
<c:set var="f3" value="${draw2.thirdPlace}"/>
<td style="color:BA006D">${fn:substring(f1, 1, 4)}
<div id="winner">${draw0.provider.name} ${f1} ${draw0.thirdDesc} ${draw0.thirdDescCh}</div>
</td>
<td style="color:BA006D">${fn:substring(f2, 1, 4)}
<div id="winner">${draw1.provider.name} ${f2} ${draw1.thirdDesc} ${draw1.thirdDescCh}</div>
</td>
<td style="color:BA006D">${fn:substring(f3, 1, 4)}
<div id="winner">${draw2.provider.name} ${f3} ${draw2.thirdDesc} ${draw2.thirdDescCh}</div>
</td>
</tr>
<tr>
<td style="width:10%;">
 	<div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw0.thirdImage}" border='0'></div>
</td>
<td style="width:10%;">
   <div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw1.thirdImage}" border='0'></div>
</td>
<td style="width:10%;">
   <div class='m4d-num-list'>
	<img width="70" height="70" src="data:image/png;base64,${draw2.thirdImage}" border='0'></div>
</td>
</tr>

</tr>
</table>
</td>
</td>
</table>
</div>
