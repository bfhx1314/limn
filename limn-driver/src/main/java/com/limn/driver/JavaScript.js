function getLocatorByNode(node){
	var length=node.attributes.length;
	var dict=new Array(); 
	for(var i=0;i<length;i++){
		dict[node.attributes[i].name]=node.attributes[i].value;
	}
	if(dict["id"]!=undefined){
		return dict["id"];
	}else if(dict["name"]!=undefined){
		if(document.getElementsByName(dict["name"]).length==1){
			return dict["name"];
		}
	}
	return getXPathByNode(node);
}

function getXPathByNode(node){
	var result = "";
	var stop = false;
	//var absolute = Firebug.getPref(Firebug.prefDomain, "firepath.generateAbsoluteXPath");

	var parent = node.ownerDocument;
	while (node && node != parent && !stop) {
		var str = "";
		var position = getNodePosition(node);
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
				//!absolute && 
				if(node.id && node.id != "") {
					str = "//*[@id='" + node.id + "']";
					position = null;
					stop = true;
				} else {
					str = name;
				}
				
			break;
		}
		result = str + (position ? "[" + position + "]" : "") + (result? "/": "") + result;
		if(node instanceof Attr){
			node = node.ownerElement;
		}else{ 
			node = node.parentNode
		};
	}

	return result;
}

function getNodePosition(node) {
	if (!node.parentNode)
		return null;
	var siblings = node.parentNode.childNodes;
	var count = 0;
	var position;
	for (var i = 0; i < siblings.length; i++) {
		var object = siblings[i];
		if(object.nodeType == node.nodeType && object.nodeName == node.nodeName) {
			count++;
			if(object == node) position = count;
		}
	}
	if (count > 1){
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

