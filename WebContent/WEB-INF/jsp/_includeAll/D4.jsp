<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		
	    
		<c:forEach items="${zmas}" var="member" varStatus="status">
		<tr style="background-color:${member.role.color}">			
			<c:set var="initEditable" value="${editable}" scope="request"/>
			<c:set var="initZmaTab" value="${tab}" scope="request"/>
			
			<c:set var="subMember" value="${currExpandedMembers[member.code]}"/>
			<c:set var="expanded" value="+" />
			<c:choose>
			<c:when test="${subMember != null}">
				<c:set var="expanded" value="-" />
				<c:set var="editable" value="false" scope="request"/>
			</c:when>
			</c:choose>

			<td><a href="processAgent.html?method=expand&code=${member.code}" style="text-decoration: none">
			<link rel="xyz" type="text/css" href="color.css">
				<font color="blue" size="3">
               				${tab}${expanded}${member.code}
              			 </font>
               			</link>
			</a></td>			


			<td>${member.role.shortCode}</td>
			<td>${member.profile.contact}</td>
			<td>${member.username}</td>
			<c:set var="linkColor" value="green" scope="page" />
			<c:choose>
			<c:when test="${member.account.balance < '0'}">
				<c:set var="linkColor" value="green" scope="page"/>
			
</c:when>
			</c:choose>
			<td>
			<font color=${linkColor} size="3">
               				<fmt:formatNumber value="${member.account.balance}" 
							pattern="#0.00" />
              			 </font>			
			</td>

			<c:set var="linkColor" value="green" scope="page" />
			<c:choose>
			<c:when test="${member.account.balance+member.account.creditLimit < '0'}">
				<c:set var="linkColor" value="red" scope="page"/>
			
</c:when>
			</c:choose>

			<td>
			<font color=${linkColor} size="3">
               				<fmt:formatNumber value="${member.account.balance+member.account.creditLimit}" 
							pattern="#0.00" />
              			 </font>			
			</td>

			<c:choose>
			<c:when test="${editable==true}">
			<td><a href="../acc/processAccount.html?method=update&code=${member.code}" >
			<link rel="xyz" type="text/css" href="color.css">
				<font color="DarkMagenta" size="3">
               				Update
              			 </font>
               			</link>
			</a></td>
			</c:when>
			</c:choose>

			<c:choose>
			<c:when test="${subMember != null}">
				<c:set var="editable" value="false" scope="request"/>
				<c:set var="tab" value="${tab}&nbsp&nbsp" scope="request"/>
				<c:set var="zma" value="${subMember}" scope="request"/>
				<jsp:include page="zmaDisplay.jsp"/>
			</c:when>
			</c:choose>
			<c:set var="editable" value="${initEditable}" scope="request"/>
			<c:set var="tab" value="${initZmaTab}" scope="request"/>	
		</tr>
		</c:forEach>

	    