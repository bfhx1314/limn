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
			case 9: //Node.DOCUMENT_NODE
			break;
			case 2: //Node.ATTRIBUTE_NODE
				str = "@" + node.name;
			break;
			case 8: //Node.COMMENT_NODE
				str = "comment()";
			break;
			case 3: //Node.TEXT_NODE
				str = "text()";
			break;
			case 1: //Node.ELEMENT_NODE

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
    // var ns = node.namespaceURI;
    // var prefix = node.lookupPrefix(ns);
    //
    // //if an element has a namespace it needs a prefix
    // if(ns != null && !prefix) {
    //     prefix = getPrefixFromNS(ns);
    // }

    if ("IE" == myBrowser()) {
        return node.tagName.toLowerCase();
    }

    var name =node.localName;
    if (isHtmlDocument(node.ownerDocument)) {
        //     //lower case only for HTML document
        return name.toLowerCase();
    }
    // } else {
    //     return (prefix? prefix + ':': '') + name;
    // }
	return name;
}

function getPrefixFromNS(ns) {
    return ns.replace(/.*[^\w](\w+)[^\w]*$/, "$1");
}

function isHtmlDocument(doc) {
    return doc.contentType === 'text/html';
}


function myBrowser(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1;
    if (isOpera) {
        return "Opera"
    }; //判断是否Opera浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
        return "Chrome";
    }
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
        return "IE";
    }; //判断是否IE浏览器
}


