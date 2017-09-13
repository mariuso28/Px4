<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	

<td>IONCLDUEE3</td>
	
		<c:choose>
		<c:when test="${column.occurences != '0'}">
			<c:choose>
			<c:when test="${column.level eq 'FIRST'}">
			<td>
			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="00B00F" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${column.level eq 'SECOND'}">
			<td>
			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="FFA600" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${column.level eq 'THIRD'}">
			<td>
			<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="BA006D" size="3">
               				${column.number}
              			 </font>
               			</link>
               		</a>
			</td>
			</c:when>
			</c:choose>
		</c:when>
		<c:otherwise>
				<td>
				<a href ="processAnalytic.html?method=viewNumber&number=${column.number}:${numberGridForm.external}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${column.number}
              			 </font>
               			</link>
               			</a>
				</td>

		</c:otherwise>		
		</c:choose>
		
		