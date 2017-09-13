<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


            	  <tr style="background: #f8f8f8; color:#000;">
                  <c:choose>
                  <c:when test="${useRow.value.expanded==true}">
                      <td align="left"><a href="../summary/collapse?id=${useRow.value.id}">${useRow.value.member.memberId}</a></td>
                  </c:when>
                  <c:otherwise>
                      <td align="left"><a href="../summary/expand?id=${useRow.value.id}">${useRow.value.member.memberId}</a></td>
                  </c:otherwise>
                </c:choose>
                  <td align="left">${useRow.value.member.role.rank}</td>
                  <td align="left">Fight</td>
                  <c:forEach items="${useRow.value.entries}" var="entry">
               		   <td align="left"><fmt:formatNumber value="${entry.value.flight}"
                        type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
                  </c:forEach>
                  </tr>
                  <tr style="background: #f8f8f8; color:#000;">
                		<td align="left"></td>
                   <td align="left"></td>
                   <td align="left">Retain</td>
                   <c:forEach items="${useRow.value.entries}" var="entry">
                		   <td align="left"><fmt:formatNumber value="${entry.value.retain}"
                         type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
                   </c:forEach>
                   </tr>
                   <c:if test="${useRow.value.expanded==true && fn:length(useRow.value.subMembers)>0}">
                     <c:forEach items="${useRow.value.subMembers}" var="row" varStatus="status">
                       	<c:set var="useRow" value="${row}" scope="request"/>
                        <jsp:include page="summaryMember.jsp"/>
                     </c:forEach>
                   </c:if>
