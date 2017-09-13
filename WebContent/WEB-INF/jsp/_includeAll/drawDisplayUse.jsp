<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="outerbox">
	<table class="resultTable"  border="1" align="center">
	
<tr><td colspan="5"><table class="resultTable2" cellpadding="0" cellspacing="0">
<tr><td class="resultm4dlable" style="width:20%"><img src="${image}">
</td><td class="resultm4dlable">${draw.provider.name}</td>
</tr>
</table></td></tr><tr><td colspan="5">
<table class="resultTable2" cellpadding="0" cellspacing="5">
<tr><td class="resultdrawdate">Date: <fmt:formatDate value="${draw.date}" pattern="dd-MMM-yyyy" /></td>
<td class="resultdrawdate">Draw No: ${draw.drawNo}</td>
</tr></table></td></tr><tr><td colspan="5">

<table class="resultTable2" cellpadding="0" cellspacing="0">
<tr>
<td style="width:150" class="resultprizelable">1st Prize</td>
<td class="resulttop">
	<a href ="processAnalytic.html?method=addNumber&number=${draw.firstPlace}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="00B00F" size="3">
               				${draw.firstPlace}
              			 </font>
               			</link>
               		</a>
</td>
</tr>
<tr>
<td style="width:45%" class="resultprizelable">2nd Prize</td>
<td class="resulttop">
	<a href ="processAnalytic.html?method=addNumber&number=${draw.secondPlace}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="FFA600" size="3">
               				${draw.secondPlace}
              			 </font>
               			</link>
    </a>
</td>
</tr>
<tr>
<td style="width:45%" class="resultprizelable">3rd Prize</td>
<td class="resulttop">
	<a href ="processAnalytic.html?method=addNumber&number=${draw.thirdPlace}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="BA006D" size="3">
               				${draw.thirdPlace}
              			 </font>
               			</link>
    </a>
</td>
</table>
</td>
</tr>
<tr>
<td colspan="5" class="resultprizelable">Special</td></tr>
<tr>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[0]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[0]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[1]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[1]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[2]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[2]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[3]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[3]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[4]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[4]}
              			 </font>
               			</link>
    </a>
</td>
</tr>
<tr>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[5]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[5]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[6]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[6]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[7]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[7]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[8]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[8]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.specials[9]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.specials[9]}
              			 </font>
               			</link>
    </a>
</td>
</tr>


<tr>
<td colspan="5" class="resultprizelable">Consolation</td></tr>
<tr>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[0]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[0]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[1]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[1]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[2]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[2]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[3]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[3]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[4]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[4]}
              			 </font>
               			</link>
    </a>
</td>
</tr>
<tr>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[5]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[5]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[6]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[6]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[7]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[7]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[8]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[8]}
              			 </font>
               			</link>
    </a>
</td>
<td class="resultbottom">
<a href ="processAnalytic.html?method=addNumber&number=${draw.consolations[9]}" style="text-decoration:none;">
               			<link rel="xyz" type="text/css" href="color.css">
               			<font color="black" size="3">
               				${draw.consolations[9]}
              			 </font>
               			</link>
    </a>
</td>
</tr>

</table></div>