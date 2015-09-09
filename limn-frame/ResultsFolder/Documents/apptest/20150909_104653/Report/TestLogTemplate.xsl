<?xml version="1.0" encoding="gb2312"?>

<xsl:stylesheet
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
 xpath-default-namespace="http://www.w3.org/1999/xhtml">

 <xsl:output method="html" 
  doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-
transitional.dtd" 
  doctype-public="-//W3C//DTD XHTML 1.0 Transitional//
EN" indent="yes"/>


	
	<xsl:template match="/">		
		<html>
		
			<head >  
				<meta http-equiv="Content-Type" content="text/html; charset=GB2312"/>		
				<title>Compass Automation Summary Report</title>  
				<link rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
				<link REL='Stylesheet'  type='text/css' HREF="css/Report.css" media="all" />				
				<script language="Javascript" type="text/javascript" src="js/Report.js"> </script>			
				<script type="text/javascript" src="js/jquery.min.js"></script>
				<script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>			
			</head> 			
			<body  bgColor="aliceblue" align="top">
			<script type="text/javascript" >

			$(document).ready(function() { 
				/* This is basic - uses default settings */ 
				$("a#logStatus").fancybox({
				
					'showCloseButton' :	true,
					'transitionIn' : 'elastic',
					'transitionOut' : 'fade',
					'zoomSpeedIn': 500, 
					'zoomSpeedOut': 300,
					'titleShow' : true,					
					'hideOnContentClick': true 
									
				}); 				
				
			});
			$(function(){ 
				$("a[rel=group]").fancybox({ 
					'titlePosition' : 'over', 
					'cyclic'        : true, 
					'titleFormat'    : function(title, currentArray, currentIndex, currentOpts) { 
								return '<span id="fancybox-title-over">' + (currentIndex + 1) + 
			 ' / ' + currentArray.length + (title.length ? '   ' + title : '') + '</span>'; 
							} 
				}); 
			}); 
			</script>
				<CENTER><H1 class="STYLE1">Compass Automation Execution Log</H1></CENTER>
				<div id="logo">
					<h1 align='right'>
						<img src='image\HW_logo.png' height='30'/>
					</h1>
				</div>
				<table class="author"><tr><td>Developed by TCOE_COMPASS Team</td></tr></table>
				<table class='Information' width='100%'><tr><td class="maintitle" colSpan="2">Execution Log Per Action</td></tr></table>
				
				<xsl:for-each select="TestLog/Action">
					 <xsl:call-template name="Action">
   						 <xsl:with-param name="ActionIndex" select="position()" />
  					</xsl:call-template>							
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name ="Action" match="Action" >
		<xsl:param name="ActionIndex" ></xsl:param>
		<div class = "Action" id = "{$ActionIndex}" >		
			<div class='foldingbutton' id="suite{$ActionIndex}_foldlink" onclick="toggle_child_visibility('suite{$ActionIndex}');" style='display: none;'>-</div>
			<div class='foldingbutton' id="suite{$ActionIndex}_unfoldlink" onclick="toggle_child_visibility('suite{$ActionIndex}');" style='display: block;'>+</div>
			<span class='filename'>NO. <xsl:value-of select="$ActionIndex"/></span>      <span class = 'Case_Name' ><xsl:value-of select="@Name"/></span>		
		
			<div id='suite{$ActionIndex}_children' style='display: none;'>
				<ul id = "log-list" >
					<li class = "log-title" style="width:100%;">
						<span style="width:150px;" class = "CurrentTimeTitle">Time</span>
						<span style="width:60px;" class ="EventStatusTitle">Status</span>
						<span style="width:30%;" class = "EventNameTitle">Step Name</span>
						<span style="width:30%;" class = "EventContentTitle">Step Content</span>
					</li>
					<xsl:for-each select="Log">
						<xsl:apply-templates select="."/>				
					</xsl:for-each>	
				</ul>
			</div>				
		</div>
	</xsl:template>
	
	<xsl:template match="Log" >
		<xsl:variable name="LogSnapshotPaht"><xsl:value-of select="@LogSnapshot"/></xsl:variable>
		<li class = "log-line" onMouseOver='mouseover(this)' onMouseOut='mouseout(this)' style="width:100%;">
			<span style="width:150px;" class = "CurrentTime"><xsl:value-of select="@CurrentTime"/></span>	
			<xsl:choose>
				<xsl:when test="@Status='Pass'">		
					<a rel="group" class = "EventPass" style="width:60px;" id="logStatus" href="{$LogSnapshotPaht}"  >Pass</a>				
						<!-- <span style="width:60px;"  class = "EventPass">Pass</span> -->			
				</xsl:when>
				<xsl:when test="@Status='Fail'">			
					<a  rel="group" class = "EventFail" style="width:60px;" id="logStatus" href="{$LogSnapshotPaht}"  >Fail</a>
						<!-- <span style="width:60px;" class = "EventFail">Fail</span> -->							
				</xsl:when>
				<xsl:when test="@Status='Warning'">					
					<a  rel="group" class = "EventWarning" style="width:60px;" id="logStatus" href="{$LogSnapshotPaht}"  >Warning</a>
						<!-- <span style="width:60px;" class = "EventWarning">Warning</span> -->							
				</xsl:when>		
				<xsl:otherwise>
					<a  rel="group" class = "EventDone" style="width:60px;color:gray;" id="logStatus" href="{$LogSnapshotPaht}"  >Done</a>
					<!-- <span style="width:60px;" class = "EventDone">Done</span> -->
				</xsl:otherwise>			
			</xsl:choose>
			<span style="width:30%;" class="EventName"><xsl:value-of select="@EventName"/></span>
			
			<xsl:choose>
				<xsl:when test="@Status='Pass'">					
						<span style="width:40%;"  class = "EventContent"><xsl:value-of select="."/></span> 						
				</xsl:when>
				<xsl:when test="@Status='Fail'">					
						<span style="width:40%;color:red;" class = "EventContent"><xsl:value-of select="."/></span> 						
				</xsl:when>
				<xsl:when test="@Status='Warning'">					
						<span style="width:40%;color:yellow;" class = "EventContent"><xsl:value-of select="."/></span> 						
				</xsl:when>		
				<xsl:otherwise>
						<span style="width:40%;" class = "EventContent"><xsl:value-of select="."/></span> 
				</xsl:otherwise>			
			</xsl:choose>
			
			<!--<span style="width:43%;" class="EventContent"><xsl:value-of select="."/></span> -->
		</li>
	</xsl:template>	
</xsl:stylesheet>