<?xml version="1.0" encoding="gb2312"?>


<xsl:stylesheet
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
 xpath-default-namespace="http://www.w3.org/1999/xhtml">

 <xsl:output method="html" 
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-
transitional.dtd" 
  doctype-public="-//W3C//DTD XHTML 1.0 Transitional//
EN" indent="yes"/>
 
	<xsl:template match = "Summary">
		<table width='100%' class='Information'>
			<tr><td colSpan='2' class="maintitle">System Information</td></tr>				
			<!--  <xsl:for-each select="ReportRoot/Summary/*">
				<tr>
					<td class="Info_Item"><xsl:value-of select="local-name()"/></td>
					<td class="Info_Value"><xsl:value-of select="current()"/></td>
				</tr>		
			</xsl:for-each>	-->
			<tr><td class="Info_Item">产品名称</td><td class="Info_Value"><xsl:value-of select="ProductName"/></td></tr>
			<!--  <tr><td class="Info_Item">产品版本</td><td class="Info_Value"><xsl:value-of select="ProductVersion"/></td></tr>	-->
			<tr><td class="Info_Item">测试环境</td><td class="Info_Value"><xsl:value-of select="TestEnvironment"/></td></tr>
			<!-- <tr><td class="Info_Item">Company Code</td><td class="Info_Value"><xsl:value-of select="CompanyCode"/></td></tr> 	-->
			<tr><td class="Info_Item">运行模式</td><td class="Info_Value"><xsl:value-of select="RunMode"/></td></tr>
			<!-- <xsl:if test="RunMode != 'Local Host'">
				<tr><td class="Info_Item">ALM TestSet</td><td class="Info_Value"><xsl:value-of select="ALMTestSet"/></td></tr>
				<tr><td class="Info_Item">ALM Instance</td><td class="Info_Value"><xsl:value-of select="ALMInstance"/></td></tr>
			</xsl:if>		-->			
			<tr><td class="Info_Item">测试用例名</td><td class="Info_Value"><xsl:value-of select="TestName"/></td></tr>
			<tr><td class="Info_Item">运行环境</td><td class="Info_Value"><xsl:value-of select="ExecutedOn"/></td></tr>
			<!-- <tr><td class="Info_Item">代码总行数</td><td class="Info_Value"><xsl:value-of select="TotalScripts"/></td></tr> -->
			<!-- <tr><td class="Info_Item">时区</td><td class="Info_Value"><xsl:value-of select="TimeZone"/></td></tr> -->
			<tr><td class="Info_Item">执行开始时间</td><td class="Info_Value"><xsl:value-of select="TestStartTime"/></td></tr>	
			<tr><td class="Info_Item">执行结束时间</td><td class="Info_Value"><xsl:value-of select="TestEndTime"/></td></tr>	
			<tr><td class="Info_Item">日志</td><td class="Info_Value"><a href="TestLog.xml" target = '_blank'>点击这里查看详细日志</a></td></tr>	
		</table>	
	</xsl:template>


	<xsl:template match = "InputData|OutputData">
		<td class='Info_Item'><xsl:value-of select="FieldName"/></td>
		<td class='parameter_Value'><xsl:value-of select="FieldValue"/></td>
	</xsl:template>

	<xsl:template match = "CheckPoint">
		<tr class='checkpointrow' onMouseOver='mouseover(this)' onMouseOut='mouseout(this)'>
		<td class='centertext'><xsl:value-of select = "CPSN + 1" /></td>
		<td class='detailtext'><xsl:value-of select = "CPName" /></td>
		<td class='detailtext'><xsl:value-of select = "CPTime" /></td>
		<xsl:choose>
			<xsl:when test="CPStatus = 'Pass'">
			<td class='passedcentertext'><a id="checkpoint" href="{CPSnapshot}"  >&#x2713;Pass</a></td>
			</xsl:when>
			<xsl:when test="CPStatus = 'Fail'">
			<td class='failedcentertext'><a id="checkpoint" class='two' href="{CPSnapshot}" >&#x2717;Fail</a></td>
			</xsl:when>
			<xsl:when test="CPStatus = 'Warning'">
			<td class='warningcentertext'><a id="checkpoint" class='three' href="{CPSnapshot}" >&#x2227;Warning</a></td>
			</xsl:when>			
		</xsl:choose>
		<td class='detailtext'><xsl:value-of select = "CPExpected" /></td>
		<td class='detailtext'><xsl:value-of select = "CPActual" /></td></tr>	
	</xsl:template>

	<xsl:template match = "ColumnNames">
		<tr>	
			<xsl:for-each select="ColumnName" >
			<th class='Info_Item' ><xsl:value-of select = "." /></th>
			</xsl:for-each>	
		</tr>		
	</xsl:template>
	
	<xsl:template match = "ColumnValues">
		<tr>	
			<xsl:for-each select="ColumnValue" >
			<td class='parameter_Value' ><xsl:value-of select = "." /></td>
			</xsl:for-each>	
		</tr>
	</xsl:template>


	<xsl:template match="/">
	
		<html>		
			<head >  
			<meta http-equiv="Content-Type" content="text/html; charset=GB2312"/>		
			<title>自动化测试报告Compass Automation Summary Report</title>  
			<link REL='Stylesheet'  type='text/css' HREF="css/Report.css" media="all" />
			<link rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />			
			<script language="Javascript" type="text/javascript" src="js/Report.js"> </script>			
			<script type="text/javascript" src="js/jquery.min.js"></script>
			<script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>
			
			
			</head> 	
		
			<body  bgColor="aliceblue" align="top">
			<script type="text/javascript" >

			$(document).ready(function() { 
				/* This is basic - uses default settings */ 
				$("a#checkpoint").fancybox({
				
					'showCloseButton' :	true,
					'transitionIn' : 'elastic',
					'transitionOut' : 'fade',
					'zoomSpeedIn': 500, 
					'zoomSpeedOut': 300,
					'titleShow' : true,					
					'hideOnContentClick': true 
									
				}); 				
				
			});

			</script>
			
			
			
			
				<CENTER>
					<H1 class="ReportTitle">自动化测试报告</H1>
				</CENTER>
				<div id="logo">
					<h1 align='right'>					
						<img src='image\HW_logo.png' height='30'/>
					</h1>
				</div>
				<table class="author">
					<tr>
						<td>Developed by TCOE_COMPASS Team</td>
					</tr>
				</table>
				
				<!-- Summary information -->
				<xsl:apply-templates select="ReportRoot/Summary">
				</xsl:apply-templates>
				
				<!-- detail  -->	
				<table class='Information' width='100%'> 	
				<tr>
		  		<td class="maintitle" colSpan='2'>Execution Report</td>
				</tr>
				</table>
				
				<xsl:for-each select="ReportRoot/Detail/TestCase">
				<table class='Testcase'>
				<tr><td width ='100%'>
				
					<!-- Test Case General -->
					<table width='100%'>
					<tr><td width ='15%' class ="Case_Item">
					<div class='foldingbutton' id="suite{Number}_foldlink" onclick="toggle_child_visibility('suite{Number}');" style='display: none;'>-</div>
					<div class='foldingbutton' id="suite{Number}_unfoldlink" onclick="toggle_child_visibility('suite{Number}');" style='display: block;'>+</div>
					<span class='filename'>用例编号:  <xsl:value-of select="Number"/></span></td>
					<td class = 'Case_value' width ='15%'><xsl:value-of select="CaseName"/></td> 
				<!-- <td class ='Case_Item' width ='7%'>Asset:</td>
					<td class = 'Case_value' width ='16%'><xsl:value-of select="Asset"/></td> -->
					<td class ='Case_Item' width ='3%'>检查点:</td>
					<td class ='Case_value' width ='7%'><xsl:value-of select="count(CheckPoints/CheckPoint)"/></td>
					<td class ='Case_Item' width ='10%'>执行状态:</td>
					<xsl:choose>
						<xsl:when test="CaseStatus = 'Pass'">
						<td width = '11%' class='passedcentertext'><a >&#x2714;</a></td>
						</xsl:when>
						<xsl:when test="CaseStatus = 'Fail'">
						<td width = '11%' class='failedcentertext'><a class="two" >&#x2718;</a></td>
						</xsl:when>						
						<xsl:when test="CaseStatus = 'Error'">
						<td width = '11%' class='failedcentertext'><a id="checkpoint" class="two"  href="{ErrorSnapshot}" >&#x2718;Error</a></td>
						
						
						</xsl:when>						
					</xsl:choose>
					</tr>
					</table>
				</td></tr>
				
				<tr><td width ='100%'><div id='suite{Number}_children' style='display: none;'>
				
				
				<!-- Input Data -->
				<!-- <table width = '100%' bordercolor="#99CCFF" class='CheckPoint'>
				<tr class = "inputoutput">Input Data:</tr>				
				<xsl:choose>					
					<xsl:when test="count(InputDatas/InputData) > 0 ">
						<xsl:for-each select="InputDatas/InputData">
							<xsl:if test="position() mod 2=1">
								<tr>
								<xsl:apply-templates select="."/>
								<xsl:apply-templates select="following-sibling::InputData[position()=1]"/>
								</tr>
							</xsl:if>					
						</xsl:for-each>					
					</xsl:when>	
					<xsl:otherwise>
						<tr><td class='parameter_Value'>No input data</td></tr>
					</xsl:otherwise>				
				</xsl:choose>
				</table> -->
				
				<!-- Item list -->
				<!-- 
				<xsl:if test = "count(ItemList) > 0 "> 
					<table width = '100%' bordercolor="#99CCFF" class='CheckPoint'>
						<tr class = "inputoutput" >Item List:</tr>
						<xsl:apply-templates select="ItemList/ColumnNames"/>	
						<xsl:for-each select="ItemList/ColumnValues">
							<xsl:apply-templates select="."/>
						</xsl:for-each>										
					</table>				
				</xsl:if>
				 -->
				
				
				<!-- Out Data  -->
				<!-- <table width = '100%' bordercolor="#99CCFF" class='CheckPoint'>
				<tr class = "inputoutput" >Output Data:</tr>				
				<xsl:choose>					
					<xsl:when test="count(OutputDatas/OutputData) > 0 ">
						<xsl:for-each select="OutputDatas/OutputData">
							<xsl:if test="position() mod 2=1">
								<tr>
								<xsl:apply-templates select="."/>
								<xsl:apply-templates select="following-sibling::OutputData[position()=1]"/>
								</tr>
							</xsl:if>					
						</xsl:for-each>					
					</xsl:when>	
					<xsl:otherwise>
						<tr><td class='parameter_Value'>No Output data</td></tr>
					</xsl:otherwise>				
				</xsl:choose>
				</table>-->
				
				
				<!-- CheckPoint -->
				<table width = '100%' bordercolor='#99CCFF' class='CheckPoint'>
				<tr class = 'inputoutput'>Check Point:</tr>
				<tr><td class='CP_title' width='4%'>SN</td>
				<td class='CP_title' width='17%'>CheckPoint Name</td>
				<td class='CP_title' width='13%'>Executed Time</td>
				<td class='CP_title' width='6%'>Status</td>
				<td class='CP_title' width='30%'>Expected Result</td>
				<td class='CP_title' width='30%'>Actual Result</td></tr>
				
				<xsl:for-each select="CheckPoints/CheckPoint">
					<xsl:apply-templates select="." />				
				</xsl:for-each>
				
				<!-- Error Message -->
				<xsl:if test="CaseStatus = 'Error'">
					<tr><td colspan='6' class='centertext'><div align='left' class='LOG'>Product Log: <xsl:value-of select ="ProductMessage"/></div></td></tr>
					<tr><td colspan='6' class='centertext'><div align='left' class='LOG'>Selenium Log: <xsl:value-of select ="ErrorLog"/></div></td></tr>
				</xsl:if>
				</table>
				
				
				
				</div></td></tr>			
				</table>
				</xsl:for-each>		
				<!-- foot print -->


			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>