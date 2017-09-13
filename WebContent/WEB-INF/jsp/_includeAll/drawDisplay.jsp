<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="outerbox">
	<table class="resultTable"  border="1" align="center">
	<tr>
	<tr><td colspan="5">
		<table class="resultTable2" cellpadding="0" cellspacing="0">
		<tr>
			<td class="resultm4dlable" style="width:20%">	<img src="data:image/gif;base64,${draw.provider.image}" alt="No image"/>
			</td><td class="resultm4dlable">${draw.provider.name}</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td colspan="5">
	<table class="resultTable2" cellpadding="0" cellspacing="5">
		<tr>
			<td class="resultdrawdate">Date: <fmt:formatDate value="${draw.date}" pattern="dd-MMM-yyyy" /></td>
			<td class="resultdrawdate">Draw No: ${draw.drawNo}</td>
		</tr>
	</table>
	</td>
	</tr>

	<tr><td colspan="5">
	<table id="songlist" class="resultTable2" cellpadding="0" cellspacing="0">
	<tr><td style="width:100" class="resultprizelable">1st Prize</td>
		<td class="resulttop" style="${drw.fpw.color}">${draw.firstPlace}<div id="${drw.fpw.flag}">${drw.fpw.str}</div></td>
	</tr>
	<tr><td style="width:45%" class="resultprizelable">2nd Prize</td>
		<td class="resulttop" style="${drw.spw.color}">${draw.secondPlace} <div id="${drw.spw.flag}">${drw.spw.str}</div></td>
	</tr>
	<tr><td style="width:45%" class="resultprizelable">3rd Prize</td>
		<td class="resulttop" style="${drw.tpw.color}">${draw.thirdPlace} <div id="${drw.tpw.flag}">${drw.tpw.str}</div></td></tr>
	</table>
	</td>
	</tr>

	<tr><td colspan="5">
	<table id="songlist" border="1" >
	<tr>
	<td colspan="5" class="resultprizelable">Special</td></tr>
	<tr>
		<td class="resultbottom" style="${drw.sws[0].color}">${draw.specials[0]} <div id="${drw.sws[0].flag}">${drw.sws[0].str}</div></td>
		<td class="resultbottom" style="${drw.sws[1].color}">${draw.specials[1]} <div id="${drw.sws[1].flag}">${drw.sws[1].str}</div></td>
		<td class="resultbottom" style="${drw.sws[2].color}">${draw.specials[2]} <div id="${drw.sws[2].flag}">${drw.sws[2].str}</div></td>
		<td class="resultbottom" style="${drw.sws[3].color}">${draw.specials[3]} <div id="${drw.sws[3].flag}">${drw.sws[3].str}</div></td>
		<td class="resultbottom" style="${drw.sws[4].color}">${draw.specials[4]} <div id="${drw.sws[4].flag}">${drw.sws[4].str}</div></td>
		</tr><tr><td class="resultbottom" style="${drw.sws[5].color}">${draw.specials[5]} <div id="${drw.sws[5].flag}">${drw.sws[5].str}</div></td>
		<td class="resultbottom" style="${drw.sws[6].color}">${draw.specials[6]} <div id="${drw.sws[6].flag}">${drw.sws[6].str}</div></td>
		<td class="resultbottom" style="${drw.sws[7].color}">${draw.specials[7]} <div id="${drw.sws[7].flag}">${drw.sws[7].str}</div></td>
		<td class="resultbottom" style="${drw.sws[8].color}">${draw.specials[8]} <div id="${drw.sws[8].flag}">${drw.sws[8].str}</div></td>
		<td class="resultbottom" style="${drw.sws[9].color}">${draw.specials[9]} <div id="${drw.sws[9].flag}">${drw.sws[9].str}</div></td>
	</tr>
	</table>
	</td></tr>
	<tr><td colspan="5">
	<table id="songlist" border="1" >
	<tr>
	<td colspan="5" class="resultprizelable">Consolation</td></tr>
	<tr>
		<td class="resultbottom" style="${drw.cws[0].color}">${draw.consolations[0]} <div id="${drw.cws[0].flag}">${drw.cws[0].str}</div></td>
		<td class="resultbottom" style="${drw.cws[1].color}">${draw.consolations[1]} <div id="${drw.cws[1].flag}">${drw.cws[1].str}</div></td>
		<td class="resultbottom" style="${drw.cws[2].color}">${draw.consolations[2]} <div id="${drw.cws[2].flag}">${drw.cws[2].str}</div></td>
		<td class="resultbottom" style="${drw.cws[3].color}">${draw.consolations[3]} <div id="${drw.cws[3].flag}">${drw.cws[3].str}</div></td>
		<td class="resultbottom" style="${drw.cws[4].color}">${draw.consolations[4]} <div id="${drw.cws[4].flag}">${drw.cws[4].str}</div></td>
		</tr><tr><td class="resultbottom" style="${drw.cws[5].color}">${draw.consolations[5]} <div id="${drw.cws[5].flag}">${drw.cws[5].str}</div></td>
		<td class="resultbottom" style="${drw.cws[6].color}">${draw.consolations[6]} <div id="${drw.cws[6].flag}">${drw.cws[6].str}</div></td>
		<td class="resultbottom" style="${drw.cws[7].color}">${draw.consolations[7]} <div id="${drw.cws[7].flag}">${drw.cws[7].str}</div></td>
		<td class="resultbottom" style="${drw.cws[8].color}">${draw.consolations[8]} <div id="${drw.cws[8].flag}">${drw.cws[8].str}</div></td>
		<td class="resultbottom" style="${drw.cws[9].color}">${draw.consolations[9]} <div id="${drw.cws[9].flag}">${drw.cws[9].str}</div></td>
	</tr>
	</table>
	</td></tr>

</table></div>
