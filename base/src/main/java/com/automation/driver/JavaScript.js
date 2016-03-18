function getLocatorByNode(nodeId){
    var node = $id(nodeId);
	var thisNodeId = node.id;
	if (thisNodeId != undefined && thisNodeId != '' && thisNodeId.indexOf('EVS-Test.W3T0X2E6Z9Y') == -1){
		return thisNodeId;
	}
	
/* 	var length=node.attributes.length;
	var dict=new Array(); 
	for(var i=0;i<length;i++){
		dict[node.attributes[i].name]=node.attributes[i].value;
	}
	if(dict["id"] != undefined && dict["id"] != '' && dict["id"].indexOf('EVS-Test.W3T0X2E6Z9Y') == -1){
		return dict["id"];
	} */
	
//	else if(dict["name"]!=undefined && dict["name"]!=''){
//		if(document.getElementsByName(dict["name"]).length==1){
//			return dict["name"];
//		}
//	}
	var getXPath = getXPathByNode(node);
	if (getXPath.substring(0,4) == 'html'){
        getXPath = "/" + getXPath;
	}
	return getXPath;
}

function getXPathByNode(node){
	var result = "";
	var stop = false;
	//var absolute = Firebug.getPref(Firebug.prefDomain, "firepath.generateAbsoluteXPath");
	var parent = node.ownerDocument;
	while (node && node != parent && !stop) {
		var str = "";
		var flag = 1;
		switch (node.nodeType) {
			case Node.DOCUMENT_NODE:
				break;
			case Node.ATTRIBUTE_NODE:
				str = "@" + node.name;
				break;
			case Node.COMMENT_NODE:
				str = "comment()";
				break;
			case Node.TEXT_NODE:
				str = "text()";
				break;
			case Node.ELEMENT_NODE:
				var name = getTagName(node);
				var parentNodeName = node.parentNode.nodeName;
				var nodeId = node.id;
				//!absolute && 
/*				if((name!='svg' && name!='g' && name!='path' && (name!='text' && node.parentNode.nodeName!='g')
				                                             && (name!='text' && node.parentNode.nodeName!='svg')
				                                             && (name!='text' && node.parentNode.nodeName!='path')
				                                             && (name!='text' && node.parentNode.nodeName!='rect')
				                                             && (name!='a' && node.parentNode.nodeName!='g')
				                                             ) && node.id && node.id != "") {*/
				if (name!='svg'
				    && name!='g'
				    && name!='path'
				    && parentNodeName != 'svg'
				    && parentNodeName != 'path'
				    && parentNodeName != 'rect'
				    && parentNodeName != 'g'
				    && nodeId && nodeId != ""
				    && !regExpFind(nodeId,'.*\\d+.*')
				    && nodeId.indexOf('EVS-Test.W3T0X2E6Z9Y') == -1)
				{
					str = "//*[@id='" + node.id + "']";
					flag = 2;
					stop = true;
				}
/*				else if(name=='svg' || name=='g' || name=='path' || name=='rect'
				        ||(name=='text' && node.parentNode.nodeName=='g')
				        ||(name=='text' && node.parentNode.nodeName=='svg')
				        ||(name=='text' && node.parentNode.nodeName=='path')
				        ||(name=='text' && node.parentNode.nodeName=='rect')
				        ||(name=='a' && node.parentNode.nodeName=='g'))*/
				else if(name=='svg'
				        || name=='g'
				        || name=='path'
				        || name=='rect'
				        || parentNodeName == 'svg'
				        || parentNodeName == 'path'
				        || parentNodeName == 'rect'
				        || parentNodeName == 'g'
				        || regExpFind(nodeId,'.*\\d+.*')
				        || nodeId.indexOf('EVS-Test.W3T0X2E6Z9Y') != -1)
				{
					str = '*';
					flag = 3;
				} else {
					str = name;
				}
				break;
		}
		var position = null;
		if(2 != flag) {
			position = getNodePosition(node, flag);
		}
		if (3 == flag){
            result = str + (position ? "[name()='"+getTagName(node)+"']" + "[" + position + "]" : "") + (result? "/": "") + result;
		}else {
		    result = str + (position ? "[" + position + "]" : "") + (result? "/": "") + result;
		}

		if(node instanceof Attr){
			node = node.ownerElement;
		}else{ 
			node = node.parentNode
		};
	}
	return result;
}

function getNodePosition(node, flag) {
	if (!node.parentNode)
		return null;
	var siblings = node.parentNode.childNodes;
	var count = 0;
	var position;
	for (var i = 0; i < siblings.length; i++) {
		var object = siblings[i];
		if(object.nodeType == node.nodeType && object.nodeName == node.nodeName && 3==flag){
        	count++;
            if(object == node){
                position = count;
                break;
            }
        }
		else if(object.nodeType == node.nodeType && object.nodeName == node.nodeName) {
			count++;
			if(object == node){
				position = count;
				break;
			}
		}

	}
	if (count >= 1){
		return position;
	}else{
		return null;
	}
}

function getTagName(node) {
	var ns = node.namespaceURI;
	var prefix = node.lookupPrefix(ns);
	
	//if an element has a namespace it needs a prefix
	if(ns != null && !prefix) {
		prefix = getPrefixFromNS(ns);
	}
	
	var name = node.localName;
	if (isHtmlDocument(node.ownerDocument)) {
		//lower case only for HTML document
		return name.toLowerCase();
	} else {
		return (prefix? prefix + ':': '') + name;
	}
}

function getPrefixFromNS(ns) {
	return ns.replace(/.*[^\w](\w+)[^\w]*$/, "$1");
}

function isHtmlDocument(doc) {
	return doc.contentType === 'text/html';
}

function regExpFind(strng,patten){
	return new RegExp(patten).test(strng);
}


//触发事件  type  mouseover .....
function fireEvent(node,type){
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		node.fireEvent("on"+type);
	}else{
		var me = document.createEvent("MouseEvents");
		me.initEvent(type,true,true);
		node.dispatchEvent(me);
	}
}

//判断IE版本IE9.0。
function getIeVersion(){
	var browser=navigator.appName
	var b_version=navigator.appVersion
	var version=b_version.split(";");
	var boolRe = -1;
	for(var i=0;i<version.length;i++){
		if (version[i].indexOf("MSIE") != -1){
			var trim_Version=version[i].replace(/[ ]/g,"");
			if(browser=="Microsoft Internet Explorer"){
				if (trim_Version=="MSIE9.0"){
					boolRe = 9;
				}else if(trim_Version=="MSIE8.0"){
					boolRe = 8;
				}else if(trim_Version=="MSIE7.0"){
					boolRe = 7;
				}else if(trim_Version=="MSIE6.0"){
					boolRe = 6;
				}else if(trim_Version=="MSIE10.0"){
					boolRe = 10;
				}else if(trim_Version=="MSIE11.0"){
					boolRe = 11;
				}
			}
			break;
		}
	}
	return boolRe;
}

// 日期插件录入日期
function setDatePluginsValue(htmlAttribute,attributeValue,setValue){
	var datePlugins = setDatePluginsType();
	switch(datePlugins){
		case 1:
			$("input["+htmlAttribute+"='"+attributeValue+"']")[0].setAttribute('value',setValue);
			break;
		case 2:
			$("input["+htmlAttribute+"='"+attributeValue+"']").datepicker('setDate',setValue);
			break;
	}
}

function setDatePluginsType(){
	var type = -1;
	var arrScript = document.head.getElementsByTagName('script');
	var arrScriptLen = arrScript.length;
	for(var i=0;i<arrScriptLen;i++){
		var scriptSrc = arrScript[i].src;
		if (scriptSrc.indexOf('datetimepicker.js') != -1){
			type = 1;
			break;
		}
	}
	if (type == -1){
		for(var i=0;i<arrScriptLen;i++){
			var scriptSrc = arrScript[i].src;
			if(scriptSrc.indexOf('datepicker.js') != -1){
				type = 2;
				break;
			}
		}
	}
	return type;
}

function $id(str){
	return document.getElementById(str);
}

// 获取单个WebElement的xpath全路径
function getWebElementFullXPath(nodeId){
    var node = $id(nodeId);
	var getXPath = getXPathByNode(node);
	if (getXPath.substring(0,4) == 'html'){
        getXPath = "/" + getXPath;
	}
	return getXPath;
}

// 获取多个WebElement的xpath全路径
function getWebElementsFullXPath(tagName){
	var strReturnData = new Array;
    var tempArr = "[";
    var tagNodes = document.getElementsByTagName(tagName);
    var tagNodesLen = tagNodes.length;
    for(var i=0;i<tagNodesLen;i++){
		var node = tagNodes[i];
		// 如果是rect元素，height属性为0就不显示。
		if (tagName == "rect"){
			var nodeHeight = node.getAttribute("height");
			if (nodeHeight == "0"){
				continue;
			}
		}
		var nodeId = node.id;
		var nodeNameStr = node.name;
		var nodeClassName = node.className;

		var parentTagName = node.parentNode.tagName;
		var nodeTypeStr = node.type;
		// 元素描述
		var strNodeText = tagName+"{";
		if (tagName.toLowerCase == 'input'){
            strNodeText += " type="+node.type;
		}
        if (nodeId.indexOf('EVS-Test.W3T0X2E6Z9Y') != -1
			|| regExpFind(nodeId,'.*\\d+.*')){
            nodeId = "";
        }
		if (nodeId != ""){
		    strNodeText += " id="+nodeId;
		}
        if (nodeNameStr != "" && nodeNameStr != undefined){
            strNodeText += " name="+nodeNameStr;
        }
        if (nodeClassName != ""){
			if (typeof(nodeClassName) == "object"){
				var nodeAttributes = node.attributes;
				var length = nodeAttributes.length;
				for(var j=0;j<length;j++){
					strNodeText += nodeAttributes[j].name + "=" + nodeAttributes[j].value + " ";
				}
			}else {
				strNodeText += " class="+nodeClassName;
			}
        }
		if (parentTagName.toLowerCase == 'g'
		    ||parentTagName.toLowerCase == 'svg'
		    ||parentTagName.toLowerCase == 'path'
		    ||parentTagName.toLowerCase == 'rect'){
		    var textContentTemp = node.textContent;
		    if (textContentTemp == ""){
                textContentTemp = node.outerHTML;
		    }
            strNodeText += textContentTemp;
		}else {
		    strNodeText += " " + node.textContent;
		}
		strNodeText += "}";
		// xpath
		var isVisable = false;
		var nodeHidden = node.hidden;
		var nodeDisplay = document.defaultView.getComputedStyle(node,null).display;
		var inputHidden = "";
		if (!nodeHidden && nodeDisplay != "none"){
            if (nodeTypeStr == ""){
                isVisable = true;
            }else{
                if (nodeTypeStr != "hidden"){
                    isVisable = true;
                }
            }
		}
		if (isVisable){

            var getXPath = getXPathByNode(node);
    /*		if (getXPath.indexOf("highcharts") != -1){
                console.log(getXPath);
            }*/
            if (getXPath.substring(0,4) == 'html'){
                getXPath = "/" + getXPath;
            }

            if (nodeId == ''){
                nodeId = getNodeId(node);
            }
    //		var strDataObj = new Map();
    //		strDataObj.put("isVisiable",isVisable);
    //		strDataObj.put("nodeDescribe",strNodeText);
    //		strDataObj.put("xpath",getXPath);
    //        var strDataObj = '{"isVisiable"'+':"'+isVisable+'", "nodeDescribe"'+':"'+strNodeText+'", "xpath"'+':"'+getXPath+'"}';
    //        tempArr += strDataObj + ",";
            strReturnData.push(strNodeText + "@#%" + getXPath + "@#%" + nodeId);
		}
    }
//    tempArr = tempArr.substring(0,tempArr.length-1) + "]"
	return strReturnData;
}

// 获取节点ID，如果没有setAttribute
function getNodeId(node){
	var nodeId = node.id;
	if (nodeId==""){
        var num="";
        for(var i=0;i<10;i++){
            num += Math.floor(Math.random()*10);
        }
		var timestamp = new Date().getTime();
		nodeId = "EVS-Test.W3T0X2E6Z9Y" + num + timestamp; //时间戳
		node.setAttribute("id",nodeId);
	}
	return nodeId;
}

// 设置高亮
function setHighLightById(nodeId){
    var node = $id(nodeId);
    if (null != node){
        var nodeName = node.nodeName;
        var parentNodeName = node.parentNode.nodeName;
        if (nodeName=='svg'
            || nodeName=='g'
            || nodeName=='path'
            || nodeName=='rect'
            || parentNodeName == 'svg'
            || parentNodeName == 'path'
            || parentNodeName == 'rect'
            || parentNodeName == 'g')
        {
            node.style.stroke = 'yellow';
            node.style.strokeWidth = '2px';
        }else {
            node.style.border = '2px solid yellow';
        }
    }
}
// 取消高亮
function cancelHighLightById(nodeId){
    var node = $id(nodeId);
    if (null != node){
        var nodeName = node.nodeName;
        var parentNodeName = node.parentNode.nodeName;
        if (nodeName=='svg'
            || nodeName=='g'
            || nodeName=='path'
            || nodeName=='rect'
            || parentNodeName == 'svg'
            || parentNodeName == 'path'
            || parentNodeName == 'rect'
            || parentNodeName == 'g')
        {
            node.style.stroke = '';
            node.style.strokeWidth = '';
        }else {
            node.style.border = '';
        }
    }
}

// 设置高亮
function setHighLightByNode(node){
    if (null != node){
        var nodeName = node.nodeName;
        var parentNodeName = node.parentNode.nodeName;
        if (nodeName=='svg'
            || nodeName=='g'
            || nodeName=='path'
            || nodeName=='rect'
            || parentNodeName == 'svg'
            || parentNodeName == 'path'
            || parentNodeName == 'rect'
            || parentNodeName == 'g')
        {
            node.style.stroke = 'yellow';
            node.style.strokeWidth = '2px';
        }else {
            node.style.border = '2px solid yellow';
        }
    }
}
// 取消高亮
function cancelHighLightByNode(node){
    if (null != node){
        var nodeName = node.nodeName;
        var parentNodeName = node.parentNode.nodeName;
        if (nodeName=='svg'
            || nodeName=='g'
            || nodeName=='path'
            || nodeName=='rect'
            || parentNodeName == 'svg'
            || parentNodeName == 'path'
            || parentNodeName == 'rect'
            || parentNodeName == 'g')
        {
            node.style.stroke = '';
            node.style.strokeWidth = '';
        }else {
            node.style.border = '';
        }
    }
}

function getMap(){
    var a = new Array;
    a.push(1);
    a.push(2);
    a.push(3);
    return a;
}

function click_INV(index){
	var svg = document.getElementById('svg_content_svg_name');
	var frozen = svg.getElementsByClassName("frozen-bdiv ui-jqgrid-bdiv")[0];
	var table = frozen.firstChild;
	var tableTr = table.getElementsByTagName("tr");
	tableTr[index].firstChild.firstChild.click();
}

