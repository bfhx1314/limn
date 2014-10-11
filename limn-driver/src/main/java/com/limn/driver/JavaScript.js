//用例转回中文。
function decodeAscwCurrentStep(CurrentStep){
	// QTP的处理。
//	if (window.navigator.userAgent.indexOf("Firefox")>0){
//		if (qtp_regExpFind(CurrentStep,"\\?")){
//			var typeArr = CurrentStep.split("?");
//			var typeArrLen = typeArr.length;
//			for (var i=0;i<typeArrLen;i++ ){
//				typeArr[i] = String.fromCharCode(typeArr[i]);
//			}
//			CurrentStep = typeArr.join("");
//		}else if(!isNaN(CurrentStep)){
//			CurrentStep = String.fromCharCode(CurrentStep);
//		}
//	}
	return CurrentStep;
}

// 判断IE版本IE9.0。
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

//main
function runQtp(type,step){
	//type
	// 1 openBill (菜单的列表，不包含关键字)
	// 2 getMetaKey ,getNodeIndex
	//
	//
	//
    qtp_currentElement();

	
	var returnQtp  = [];
	switch(type)
	{
		case 1:
			step = decodeAscwCurrentStep(step);
			qtp_openBill(step);
			returnQtp.push(StaticNodeIndex.nodeIndex);
			break
		case 0:
			returnQtp.push(qtp_getNodeIndex());
			returnQtp.push(qtp_getMetaKey());
			break
		case 2:
			returnQtp.push(qtp_serachPanel(step));
			break
		case 3:
			step = decodeAscwCurrentStep(step);
			var steps = step.split(";")
			qtp_uiOperating(step);
			break
		
	}
	return  returnQtp;
}

//记忆
function StaticNodeIndex(){
//nodeIndex
//metaKey
//currentElement
//currentID
}
//****************************************
//各种方法

//兼容性
function qtp_XmlDom(paths){
    var xmlDom = null;
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		var oxmldom = ActiveXObject("Micrsoft.XMLDOM")
		var xmlDom = oxmldom.load(paths);
		xmlDom.async = false;
	}else if(window.navigator.userAgent.indexOf("Chrome")>=1){
        
    }else if(window.navigator.userAgent.indexOf("Firefox")>=1){
    
    }
}

function loadXmlFile(xmlFile){
  var xmlDom = null;
  if (window.ActiveXObject){
    xmlDom = new ActiveXObject("Microsoft.XMLDOM");
    //xmlDom.loadXML(xmlFile);//如果用的是XML字符串
    xmlDom.load(xmlFile);//如果用的是xml文件。
  }else if (document.implementation && document.implementation.createDocument){
    var xmlhttp = new window.XMLHttpRequest();
    xmlhttp.open("GET", xmlFile, false);
    xmlhttp.send(null);
    xmlDom = xmlhttp.responseXML;
  }else{
    xmlDom = null;
  }
  return xmlDom;
}

function qtp_getElementsByClassName(searchClass,node,tag){
	var root;
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		root = qtp_getElementsByClass(searchClass,node,tag);
	}else if(window.navigator.userAgent.indexOf("Chrome")>=1){
		if (node==null||node=="null"){
			root = document.getElementsByClassName(searchClass);
		}else{
			root = node.getElementsByClassName(searchClass);
		}
	}else if(window.navigator.userAgent.indexOf("Firefox")>=1){
		if (node==null||node=="null"){
			root = document.getElementsByClassName(searchClass);
		}else{
			root = node.getElementsByClassName(searchClass);
		}
	}
	return root;
}
function qtp_simulationClick(node){
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		node.fireEvent("onClick");
	}else{
		var me = document.createEvent("MouseEvents");
		me.initEvent("click",true,true);
		node.dispatchEvent(me);
	}
}

function qtp_FireEvent(node,type){
	if (window.navigator.userAgent.indexOf("MSIE")>=1){
		node.fireEvent("on"+type);
	}else{
		var me = document.createEvent("MouseEvents");
		me.initEvent(type,true,true);
		node.dispatchEvent(me);
	}
}

function qtp_getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null || node=="null")
		node = document;
	if ( tag == null ||tag=="null")
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (var i = 0,  j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}
//正则表达式
function qtp_regExp(strng,patten){
	var arrMactches = strng.match(new RegExp(patten,"gi"));
	return arrMactches;
}
function qtp_regExpFind(strng,patten){
	return new RegExp(patten).test(strng);
}
function qtp_getInnerText(str){
	return str.replace(/<[^>]*>/g,"");
}
function qtp_regExpEnglish(str){
    var patten = /[u4e00-u9fa5]/
	return patten.test(str)
}
function qtp_regExpChinese(str){
	var patten = /[^u4e00-u9fa5]/
	return patten.test(str)
}
function qtp_regExpChinese_a(str){
	var patten = /^[\u0391-\uFFE5]+$/
	return patten.test(str)
}
//删除左右两端的空格 
function trim(str){ 
return str.replace(/(^\s*)|(\s*$)/g,""); 
} 
//主要操作
function qtp_openBill(stepCurrent){
	var mactch = qtp_regExp(stepCurrent,"[^:;]{1,}");
	var root = qtp_getElementsByClassName("x-tree-root-node",null,"div")[0];
	return qtp_cascadeFind(mactch,root,0);
}

function ReplaceParentheses(str){
	var r;
	r = str.replace("\(","\\(");
	r = r.replace("\)","\\)");
	return r;
}

function qtp_cascadeFind(mactch,node,stepNum){
    var mactchLen = mactch.length;
	if (mactchLen != stepNum){
	var nodeLen = node.childNodes.length;
	var isFound = false;
		for(var i=0;i<nodeLen;i++){
            var classNode = node.childNodes[i].firstChild;
			var classValue = classNode.className;
			if (qtp_getInnerText(classNode.childNodes[3].innerHTML)==mactch[stepNum]){
				if(mactchLen <= stepNum+1 || qtp_regExpFind(classValue,"x-tree-node-leaf") ){
					//qtp_simulationClick(node.childNodes[i].firstChild.childNodes[3]);
					clickHTML(classNode.childNodes[3]);
					var xNodeValue = classNode.getAttribute("ext:tree-node-id");
					var xNode = qtp_regExp(xNodeValue,"\\d{1,}");
					//var nodeIndex = [];
					//nodeIndex.push(xNode[0]);
					StaticNodeIndex.nodeIndex = xNode[0];
					stepNum++;
					isFound = true;
					i=9999;
				}else{
					if (qtp_regExpFind(classValue,"x-tree-node-collapsed")) {
						//qtp_simulationClick(node.childNodes[i].firstChild.childNodes[1]);
						clickHTML(classNode.childNodes[1]);
					}
					node = node.childNodes[i].lastChild;
					return qtp_cascadeFind (mactch,node, stepNum + 1);
					i=9999;
				}
			}
		}
		if (!isFound){
			isFound = isFound + ":没有找到菜单树：" + mactch[stepNum];
		}
	}
	return isFound;
}

// 返回单据界面的操作按钮跟节点。
function Web_UIOperating_MainNode(){
	var opt = "";
	qtp_currentElement();
	if ("undefined" != typeof ClickNewObject){
		if (ClickNewObject != ""){
			if (document.getElementById(ClickNewObject) != null){
				opt = qtp_getElementsByClassName("x-toolbar-left-row",document.getElementById(ClickNewObject),"tr")[0];
				StaticNodeIndex.currentElement = document.getElementById(ClickNewObject);
			}else{
				opt = qtp_getElementsByClassName("x-toolbar-left-row",StaticNodeIndex.currentElement,"tr")[0];
			}
		}else if(StaticNodeIndex.currentElement==null){
			qtp_currentElement();
			opt = qtp_getElementsByClassName("x-toolbar-left-row",StaticNodeIndex.currentElement,"tr")[0];
		}else{
			opt = qtp_getElementsByClassName("x-toolbar-left-row",StaticNodeIndex.currentElement,"tr")[0];
		}
	}else{
		opt = qtp_getElementsByClassName("x-toolbar-left-row",StaticNodeIndex.currentElement,"tr")[0];
	}
	return opt;
}

function qtp_uiOperating(step){
	// WaitLoad();
	var click = false;
	var list;
	var secondOpt = true;
	var thisStep = decodeAscwCurrentStep(step)
	var eStep = qtp_regExp(thisStep,"[^:;]{1,}");
	// var eStep = step.split(";");
	ClickNewObject = 'main_container'+MAP_BillContext.getBillViewer().viewID;
	var opt = Web_UIOperating_MainNode();
	
	var type=0;
	var i;
	if(eStep.length==2){
	   if(eStep[1]==""){
	       secondOpt = false;
       }
		list = eStep[0];
	}else{
		list = eStep[0];
		secondOpt = false;
	}
	var optchildNodesLen = opt.childNodes.length;
	for (i=0;i<optchildNodesLen;i++ ){
		try{
			var optchildNode = opt.childNodes[i];
			var optchildNodesclassName = trim(optchildNode.className);
			var optchildNodeButtonHTML = optchildNode.getElementsByTagName('button')[0].innerHTML;
			if (optchildNodesclassName=="x-toolbar-cell" && (qtp_regExpFind(qtp_getInnerText(optchildNodeButtonHTML),"^"+list+"$")||qtp_regExpFind(qtp_getInnerText(optchildNodeButtonHTML),"^"+list+"\\(.*"))){
				var stepkey = optchildNode.childNodes[0].id;
				if (secondOpt){
					type = 2;
				}else{
					type = 1;
				}
			}else if ((qtp_regExpFind(qtp_getInnerText(optchildNodeButtonHTML),"^"+list+"$")||qtp_regExpFind(qtp_getInnerText(optchildNodeButtonHTML),"^"+list+"\\(.*"))){
				type = 3;
			}
		}
		catch(err){
			var aa = 1;
		}
	}
	
	switch(type)
	{
		case 1:
			qtp_simulationClick(document.getElementById(stepkey));
			click = true;
			break;
		case 2:
			var menus = Ext.getCmp(stepkey).showMenu();
			var textElements = qtp_getElementsByClassName("x-menu x-menu-floating x-layer ",null,"div");
			if (textElements.length>1 && textElements.length != 0){
				textElements = returnVisibleNode(returnSameClass(textElements,"x-menu x-menu-floating x-layer "));
			}
//			var textElements = document.getElementsByTagName("body")[0].childNodes;
			// qtp_getElementsByClass("x-menu x-menu-floating x-layer ",menus.menu.ul.dom,null);
			var textElementsLen = textElements.length;
			for(var k=0;k<textElementsLen;k++){
//				if (textElements[k].className == "x-menu x-menu-floating x-layer "){
					var textA = textElements[k].getElementsByTagName("a");
					var textALen = textA.length;
					for(var j=0;j<textALen;j++){
						var textAText = get_innerText_OR_textContent(textA[j]);
						if (qtp_regExpFind(textAText,"^"+ReplaceParentheses(eStep[1])+".*")){
							setTimeout(function(){
								qtp_simulationClick(textA[j]);
							},2000)
							click = true
							k=textElementsLen;
							break;
						}
					}
					if (!click){
						var textButton = textElements[k].getElementsByTagName("button");
						var textButtonLen = textButton.length;
						for(var j=0;j<textButtonLen;j++){
							var textButtonText = get_innerText_OR_textContent(textButton[j]);
							if (qtp_regExpFind(textButtonText,"^"+ReplaceParentheses(eStep[1])+".*")){
								setTimeout(function(){
									qtp_simulationClick(textButton[j]);
								},2000)
								click = true
								k=textElementsLen;
								break;
							}
						}
					}
//				}
			}
			 break;
		case 3:
			var cmpId = qtp_getElementsByClassName("x-btn-text x-toolbar-more-icon",StaticNodeIndex.currentElement,null)[0].parentElement.parentElement.parentElement.parentElement.parentElement.id;
			var Menus =  Ext.getCmp(cmpId).showMenu();
			var MenusChildLen = Menus.menu.el.dom.childNodes[1].childNodes.length;
			var findFNode=false;
			for(var listi=0;listi<MenusChildLen;listi++){
				var MenusClickNode = Menus.menu.el.dom.childNodes[1].childNodes[listi];
				if (qtp_regExpFind(qtp_getInnerText(MenusClickNode.innerHTML),"\\(")){
					if(qtp_regExpFind(qtp_getInnerText(MenusClickNode.innerHTML),"^"+list+"\\("))
						findFNode=true;
				}else{
					if(qtp_getInnerText(MenusClickNode.innerHTML)==list)
						findFNode=true;
				}
				if(findFNode){
					if (eStep.length==2){
						qtp_FireEvent(MenusClickNode,"mouseover");
						click = "3";
					}else{
						qtp_simulationClick(MenusClickNode);
						click = true;
						break;
					}
				}
			}
		break
		case 0:
			alert(0);
		break
	}
	if (step[0]=="退出" || step[0]=="关闭界面"){
		//InputNewTableId = "";
		ClickNewObject = "";
	}
	return click;
}

//获取平台信息
function qtp_getMetaKey(){
	try{
		StaticNodeIndex.metaKey = MAP_BillContext.getBillViewer().metaKey;
		return MAP_BillContext.getBillViewer().metaKey;
	}
	catch(err){
		StaticNodeIndex.metaKey = "";
		return "";
	}
}
function qtp_getNodeIndex(){
	try{
		StaticNodeIndex.nodeIndex = qtp_regExp(MAP_BillContext.contextID,"\\d{1,}")[0];
	}
	catch(err){
		StaticNodeIndex.nodeIndex = "";
	}
	return StaticNodeIndex.nodeIndex;
}
function qtp_serachPanel(infor){
	var arr = infor.match(/[^,]{1,}/gi);
	return qtp_getElementsByClassName(arr[0],arr[1],arr[2]).length;
}



function qtp_currentElement(){
	var rootObj = qtp_getElementsByClassName('x-tab-panel-body x-tab-panel-body-top',null,'div');
	if (rootObj.length>0){
        var root = rootObj[0];
	    var currentElement;
    	for(var i=0;i<root.childNodes.length;i++){
	   	   if (qtp_regExpFind(trim(root.childNodes[i].className),"x-panel x-panel-noborder$")){
   		       if (root.childNodes[i].firstChild.firstChild.hasChildNodes()){
                    StaticNodeIndex.currentElement = root.childNodes[i].firstChild.firstChild.firstChild;
                }else{
                    StaticNodeIndex.currentElement = root.childNodes[i].firstChild.firstChild;
                }
                StaticNodeIndex.currentID = StaticNodeIndex.currentElement.id;
            }
        }
    }
	qtp_getNodeIndex();
	qtp_getMetaKey();
}
function qtp_showMenu(opt,name){
	for(var i=0;i<opt.getElmentByTagName("button").length;i++){
		if (opt.getElementByTagName("button")[i].innerHTML==name){
			return opt.getElementByTagName("table")[i].id;
		}	
	}
}
function CoordControl(oElement) {
  var x2 = 0;
  var y2 = 0;
  var width = oElement.offsetWidth;
  var height = oElement.offsetHeight;
  if( typeof( oElement.offsetParent ) != 'undefined' ) {
    for( var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent ) {
      posX += oElement.offsetLeft;
      posY += oElement.offsetTop;      
    }
    x2 = posX + width;
    y2 = posY + height;
    return [ posX, posY ,width, height];
   } else{
      x2 = oElement.x + width;
      y2 = oElement.y + height;
      return [ oElement.x, oElement.y, width, height];
  }
}


/////////////////////////////////////////////////////
//getWebPageTitle
//获取网页文档的title
function getWebPageTitle(){
    var PageTitle = document.title;
    return PageTitle;
}

//等待页面加载，获取Billkey
function WaitLoad(){
	// var func=function(){thisWaitTime++;};
	// thisWaitTime = 1;
    // do
    // {   
		// setTimeout(func(),2000);
        var JsVar = MAP_BillContext.getBillViewer();   
        if (typeof(JsVar)=="object" && JsVar!=null){
            var strBillKey = JsVar.metaKey;
        }else{
            var strBillKey = "null";
        }
    // }
    // while (strBillKey == "null")
    return strBillKey;
}


//等待页面加载，获取Billkey
function WaitForPageToLoad(){
	// var func=function(){thisWaitTime++;};
	// thisWaitTime = 1;
    // do
    // {   
		// setTimeout(func(),2000);
        var JsVar = YIGO_BillContext;   
        if (typeof(JsVar)=="object" && JsVar!=null){
            var strBillKey = JsVar.billView.metaKey;
        }else{
            var strBillKey = "null";
        }
    // }
    // while (strBillKey == "null")
    return strBillKey;
}

//web1.6增加界面监听
function beforeYigoStarted1_6(){
	YIGO.ui.GlobalMessageBus.subscribe(YIGO.ui.Messages.YIGOREQUEST,function(){
		YIGO_C_ENV.loading = true;
	});
	YIGO.ui.GlobalMessageBus.subscribe(YIGO.ui.Messages.YIGODEBUGREQUEST,function(){
		YIGO_C_ENV.loading = true;
	});
	YIGO.ui.GlobalMessageBus.subscribe(YIGO.ui.Messages.RESPONSEDEALT,function(){
		delete YIGO_C_ENV.loading;
	});
}

//web1.6判断页面是否加载完成
function WaitForPageToLoad1_6(){
	var ok = true;

	if(window.YIGO_C_ENV.loading!=true){
		ok = false;
	}

	return ok;
}

//web1.6取得当前表单的key
function getBillKey1_6(){
	var strBillKey;
   var JsVar = window.YIGO_BillContext;   
        if (typeof(JsVar)=="object" && JsVar!=null){
			if(JsVar.billView!=null){
				strBillKey = JsVar.billView.metaKey;
			}
			else{
				 strBillKey = "null";
			}
        }else{
            strBillKey = "null";
        }

	return strBillKey;
}



//向节点添加keydown事件
function addEventListener_keydown(node){
    //var oId = document.getElementById(node);
    //alert(typeof(oId));
    if(typeof document.createEvent=="function"){ // Mozilla, Netscape, Firefox
        var customEvent = document.createEvent("KeyEvents");
        customEvent.initKeyEvent("keydown", true, true,document.defaultView, false,false,false, false, 39, 39);
        node.dispatchEvent(customEvent);
        //oId.addEventListener('click',alert('cc'), false);
       // td_value.addEventListener('click', alert('cc'), false);
    } else { // IE
        node.attachEvent('keydown',function(){this.fireEvent("onkeydown");return false;});
      //  td_value.attachEvent('onclick',  function(){alert('changchang');});
      //return true;
    }
	//clickHTML(node);
}

function addEventListener_keyup(node){
    if(typeof document.createEvent=="function"){ // Mozilla, Netscape, Firefox
        var customEvent = document.createEvent("KeyEvents");
        customEvent.initKeyEvent("keyup", true, true,document.defaultView, false,false,false, false, 39, 39);
        node.dispatchEvent(customEvent);
    } else { // IE
        node.attachEvent('keyup',function(){this.fireEvent("onkeyup");return false;});
    }
}

function clickHTML_blur(node){
    try{
		var me = document.createEvent("HTMLEvents");
		me.initEvent("blur",true,false);
		node.dispatchEvent(me);
    }catch(err){
        node.fireEvent("onblur");
    }
}

function clickHTML_mousedown(node){
    try{
		var me = document.createEvent("MouseEvents");
		me.initEvent("mousedown",true,true);
		node.dispatchEvent(me);
    }catch(err){
        node.fireEvent("onmousedown");
    }
}

function clickHTML_dblclick(node){
    try{
		var me = document.createEvent("MouseEvents");
		me.initEvent("dblclick",true,true);
		node.dispatchEvent(me);
    }catch(err){
        node.fireEvent("ondblclick");
    }
}

//点击字典下拉列表选项
function clickDict(DropDownObjLi){
    var objA = DropDownObjLi.getElementsByTagName("a");
    var objAlen = objA.length;
    for (var i=0;i<objAlen;i++)
    {
        var WebCheckDom = objA[i].previousSibling;
        if (WebCheckDom.checked)
        {
            //alert(WebCheckDom.checked)
            //clickMethod(clickMethod,"click");
            clickHTML(WebCheckDom);
        }

    }
}

// 点击"COMBOBOX","CHECKLISTBOX"箭头
function clickDropDownButton(node){
	var nodeImg = node.parentNode.getElementsByTagName("img");
	var nodeImgLen = nodeImg.length -1;
	if (nodeImg[nodeImgLen] != null){
		nodeImg[nodeImgLen].click();
	}
	
}

//点击选项
function clickHTML(node){
try
{
	var me = document.createEvent("MouseEvents");
	me.initEvent("click",true,true);
	node.dispatchEvent(me);
}
catch (err)
{
	// var nodeId = node.id
	// if(nodeId==""){
		// node.id = "testclick";
		// nodeId = "testclick"
	// }
	node.fireEvent("onclick");
    // document.getElementById(nodeId).fireEvent("onclick");
}

//    if (typeof document.createEvent("MouseEvents")=="object"){
//		var me = document.createEvent("MouseEvents");
//		me.initEvent("click",true,true);
//		node.dispatchEvent(me);
//    }else{
//        node.fireEvent("onClick");
//    }
}

// 返回可见节点visibility == 'visible';
function returnVisibleNode(nodeArr){
	var nodeArrLen = nodeArr.length;
	var returnNodeArr = new Array();
	var j = 0;
	for(var i=0;i<nodeArrLen;i++){
		if (get_visibility_JS(nodeArr[i])){
			returnNodeArr[j] = nodeArr[i];
			j++;
		}
	}
	return returnNodeArr;
}

//按照CLASSNAME、用例选项值，找到DOM节点，返回可见的节点。
function Web_searchVisibleDom(thisNodeArr,CurrentStep,tagN){
	var SelectDom = new Array();
	var SelectDomExist = false;
	var clickNode = "";
	var index = "";
	var nodeArr = returnVisibleNode(thisNodeArr);
	var nodeArrLen = nodeArr.length;
//	 if (nodeArrLen==1 && (CurrentStep=="清空") || CurrentStep == "全选"){
//		 var i = 0;
//	 }else{
		for (var i=0;i<nodeArrLen;i++){
			if(get_innerText_OR_textContent(nodeArr[i]).indexOf(CurrentStep) != -1){
				var SelectItemDiv = nodeArr[i].getElementsByTagName(tagN);
				var SelectItemDivLen = SelectItemDiv.length;
				for (var j=0;j<SelectItemDivLen;j++){
					if (get_innerText_OR_textContent(SelectItemDiv[j])==CurrentStep){
						clickNode = SelectItemDiv[j];
						SelectDomExist = true;
						index = i;
						i = nodeArrLen;
						break;
						//clickHTML(SelectItemDiv[j]);
					}
				}
			}
		}
//	 }
	SelectDom[0] = SelectDomExist;
	SelectDom[1] = index;
	SelectDom[2] = clickNode;
	return SelectDom;
}
//
//录入操作-下拉列表：COMBOBOX、CHECKLISTBOX
function Web_InDataObject_COMBOBOX_CheckListBox(CurrentStepStr){
	CurrentStepStr = decodeAscwCurrentStep(CurrentStepStr);
	var bool = false;
	var	strCurrentStep;
	if (CurrentStepStr == "清空"){
		bool = Web_InDataObject_Click(CurrentStep,"清空");
	}else if (qtp_regExpFind(CurrentStepStr,"[:;]{1,}")){
		// var CurrentStep = CurrentStepStr.split(":");
		var CurrentStep = qtp_regExp(CurrentStepStr,"[^:;]{1,}");
		for (var i=2;i<CurrentStep.length;i++){
			strCurrentStep = CurrentStep[i];
			if (strCurrentStep.indexOf(",") != -1){
				var arrStep = strCurrentStep.split(",");
				for(var j=0;j<arrStep.length;j++){
					bool = Web_InDataObject_Click(CurrentStep,arrStep[j]);
				}
			}else{
				bool = Web_InDataObject_Click(CurrentStep,strCurrentStep);
			}
		}		
	}else{
		strCurrentStep = CurrentStepStr;
		bool = Web_InDataObject_Click(CurrentStepStr,strCurrentStep);
	}  
    return bool;
}

function Web_InDataObject_Click(CurrentStep,strCurrentStep){
	var objDivArr = qtp_getElementsByClassName("x-combo-list-inner","null","div");
    var objDivlen = objDivArr.length;
	var SelectItemExist = Web_searchVisibleDom(objDivArr,strCurrentStep,"div");
    if (SelectItemExist[0]){
		if(!SelectItemExist[2].firstChild.checked){
			clickHTML(SelectItemExist[2]);
		}        
	}else if(SelectItemExist[1] >= objDivlen){
		return false;
    }else if(!isNaN(CurrentStep[2])){
        var nodeInput = objDivArr[SelectItemExist[1]].getElementsByTagName("input");
        var nodeInputLen = nodeInput.length;
        for (var m=0;m<nodeInputLen;m++){
            if (nodeInput[m].checked){
                nodeInput[m].click();
            }            
        }
        //if(typeof CurrentStep[3] !="undefined"){
        var m=2;
            do{
                //var nodeInput = objDivArr[SelectDom[1]].getElementsByTagName("input");
                if (!nodeInput[parseInt(CurrentStep[m])].checked){
                    nodeInput[parseInt(CurrentStep[m])].click();
                }
                m++;
            }while (CurrentStep[m] !="" && CurrentStep[m] !=undefined && CurrentStep[m] !=null)
        //}
    }else if(CurrentStep[2]=="清空" || CurrentStep[2]=="全选" || strCurrentStep == "清空"){
		var arrNode = returnVisibleNode(objDivArr);
		if (arrNode.length == 1){
			SelectItemExist[0] = true;
			SelectItemExist[1] = 0;
		}else{
			SelectItemExist[0] = false;
		}
		if (SelectItemExist[0]){
	        var nodeInput = objDivArr[SelectItemExist[1]].getElementsByTagName("input");
	        var nodeInputLen = nodeInput.length;
			if (nodeInputLen==0){
				SelectItemExist[0] = false;
			}else{
				if (CurrentStep[2]=="清空"){
					for (var m=0;m<nodeInputLen;m++){
						if (nodeInput[m].checked){
							nodeInput[m].click();
						}
					}
				}else{
					for (var m=0;m<nodeInputLen;m++){
						if (!nodeInput[m].checked){
							nodeInput[m].click();
						}
					}
				}
			}
		}else{
			// 没有找到可见的下拉列表
		}
    }
	return SelectItemExist[0];
}

//下拉列表多选字典 ThreeState,objClassName,CurrentStep,oNum,iRow,aLen
function Web_SelectDropDownList_JS(CurrentStepStr,nodeId){
	// var nodeId = Web_getDictionaryObjClass_JS(objClassName,CurrentStep)
	// return Web_openall_JS(nodeId,1,0,aLen);
	var i;
	var j;
	var getNodeAText;
	var getNodeATextArr;
	var WebCheckDom;
	var arrCurrentStep;
	var getNode = document.getElementById(nodeId);
	var getNodeLi = getNode.getElementsByTagName("li");
	var getNodeLilen = getNodeLi.length;
	var boolResult = true; // 用于判断下拉列表有效数据。
	if (getNode == null || getNodeLilen == 0){
		boolResult = false;
		return boolResult;
	}
	for(var m=0;m<getNodeLilen;m++){
		var getNodeA = getNodeLi[m].getElementsByTagName("a");
		var getNodeAlen = getNodeA.length;
		var thisCurrentStepStr = decodeAscwCurrentStep(CurrentStepStr);
		var CurrentStep = thisCurrentStepStr.split(":");
		var CurrentStepLen = CurrentStep.length;
		if (CurrentStepLen<3){
			for(i=0;i<CurrentStepLen;i++){
				for(j=0;j<getNodeAlen;j++){
					getNodeAText = get_innerText_OR_textContent(getNodeA[j]);
					arrCurrentStep = CurrentStep[i].split(" ");
					getNodeATextArr = getNodeAText.split(" ");
					if (getNodeATextArr[0]==arrCurrentStep[0].toUpperCase()){
						WebCheckDom = getNodeA[j].previousSibling;
						if (WebCheckDom.nodeName == "INPUT"){
							if (!WebCheckDom.checked){
								WebCheckDom.click();
							}
						}else{
							 getNodeA[j].click();
						}
						break;
					}
				}

			}
		
		}else{
			for(i=2;i<CurrentStepLen;i++){
				if (CurrentStep[i].indexOf(",") != -1){
					var arrStep = CurrentStep[i].split(",");
					for(var k=0;k<arrStep.length;k++){
						arrCurrentStep = arrStep[k].split(" ");
						for(j=0;j<getNodeAlen;j++){
							getNodeAText = get_innerText_OR_textContent(getNodeA[j]);
							getNodeATextArr = getNodeAText.split(" ");
							if (getNodeATextArr[0]==arrCurrentStep[0].toUpperCase()){
								WebCheckDom = getNodeA[j].previousSibling;
								if (WebCheckDom.nodeName == "INPUT"){
									if (!WebCheckDom.checked){
										WebCheckDom.click();
									}
								}else{
									 getNodeA[j].click();
								}
								break;
							}
						}
					}
				}else{
					for(j=0;j<getNodeAlen;j++){
						getNodeAText = get_innerText_OR_textContent(getNodeA[j]);
						getNodeATextArr = getNodeAText.split(" ");
						arrCurrentStep = CurrentStep[i].split(" ");
						if (getNodeATextArr[0]==arrCurrentStep[0].toUpperCase()){
							WebCheckDom = getNodeA[j].previousSibling;
							if (WebCheckDom.nodeName == "INPUT"){
								if (!WebCheckDom.checked){
									WebCheckDom.click();
								}
							}else{
								 getNodeA[j].click();
							}
							break;
						}
					}
				}

			}
		}
	}
	return boolResult;
}

//展开所有汇总节点
//1、下拉列表，3、表格
function Web_openall_JS(domId,domType,iRow,aLen){
	var addImgExist = false;
	// var getNode = node
	var getNode = document.getElementById(domId);
	var nodeImg = "";
	switch (domType){
    case 1:
		var getNodeUl = getNode.getElementsByTagName("ul");
		var getNodeA = getNodeUl[0].getElementsByTagName("a");
		var getNodeLiALen = getNodeA.length;
		if (getNodeLiALen == aLen){
			return true;
		}
		for (var i=iRow;i<getNodeLiALen;i++){
			if (getNodeA[i].previousSibling.nodeName == "INPUT"){
				nodeImg = getNodeA[i].previousSibling.previousSibling.previousSibling;
			}else{
				nodeImg = getNodeA[i].previousSibling.previousSibling;
			}
            if (nodeImg.className.indexOf("elbow-plus") != -1 || nodeImg.className.indexOf("elbow-end-plus") != -1){
               var Row = i+1;
				// var timestamp = new Date().valueOf();
				// var nodeImgId = "BokeTest" + timestamp; //时间戳
				// nodeImg.setAttribute("id",nodeImgId);
				qtp_simulationClick(nodeImg);
				//clickHTML(nodeImg);
				// setTimeout(Web_openall_JS(domId,1,Row,aLen),2000)
                // Web_openall_JS(domId,1,Row,aLen);
				addImgExist = true;
				break;
            }
        }
		break;
	case 2:
		var getNodeUl = getNode.getElementsByTagName("ul");
		var getNodeA = getNodeUl[0].getElementsByTagName("a");
		var getNodeLiALen = getNodeA.length;
		if (getNodeLiALen == aLen){
			return true;
		}
		for (var i=iRow;i<getNodeLiALen;i++){
			if (getNodeA[i].previousSibling.nodeName == "INPUT"){
				nodeImg = getNodeA[i].previousSibling.previousSibling.previousSibling;
			}else{
				nodeImg = getNodeA[i].previousSibling.previousSibling;
			}
            if (nodeImg.className.indexOf("elbow-plus") != -1 || nodeImg.className.indexOf("elbow-end-plus") != -1){
                Row = i+1;
				var returnNodeId = getNodeId(nodeImg);
				getNodeLiALen = getNodeLiALen + ":" +returnNodeId;
				// var timestamp = new Date().valueOf();
				// var nodeImgId = "BokeTest" + timestamp; //时间戳
				// nodeImg.setAttribute("id",nodeImgId);
				// qtp_simulationClick(nodeImg);
				//clickHTML(nodeImg);
				// setTimeout(Web_openall_JS(domId,1,Row,aLen),2000)
                // Web_openall_JS(domId,1,Row,aLen);
				addImgExist = true;
				break;
            }
        }
		break;
    case 3:
		var getNodeTable = getNode.getElementsByTagName("table");
		var getNodeTableTd = getNodeTable[1].getElementsByTagName("td");
		var getNodeLiALen = getNodeTableTd.length;
		if (getNodeLiALen == aLen){
			return true;
		}
		for (var j=iRow;j<getNodeLiALen;j++){
			var nodeImg = getNodeTableTd[j].getElementsByTagName("img");
			var nodeImgClick = nodeImg[nodeImg.length -2];
			var nodeImgClassName = nodeImgClick.className;
			if (nodeImgClassName.indexOf("elbow-plus") != -1 || nodeImgClassName.indexOf("elbow-end-plus") != -1){
				Row = j+1;
				returnNodeId = getNodeId(nodeImgClick);
				getNodeLiALen = getNodeLiALen + ":" +returnNodeId;
				// qtp_simulationClick(nodeImgClick);
				// Web_openall_JS(domId,2,Row);
				addImgExist = true;
				break;
			}
		}
		break;
	}
	// return nodeImgId;
	return getNodeLiALen;
	// return addImgExist;
}

//录入-下拉多选字典操作
function Web_getDictionaryObjClass_JS(searchClassName,CurrentStepStr){
	// var thisCurrentStepStr = decodeAscwCurrentStep(CurrentStepStr);
	// var CurrentStep = thisCurrentStepStr.split(":");
    var objDivArr = qtp_getElementsByClassName(searchClassName,"null","div");
	objDivArr = returnSameClass(objDivArr,searchClassName);
	objDivArr = returnVisibleNode(objDivArr);
	// var SelectItemExist = Web_searchVisibleDom(objDivArr,CurrentStep[2],"a");
	// var timestamp = new Date().valueOf();
	var timestamp; //时间戳
	var objDivArrLen = objDivArr.length;
	if (objDivArrLen==1){
		timestamp = getNodeId(objDivArr[0])//.setAttribute("id",timestamp);
	}else if(objDivArrLen==undefined){
		timestamp = getNodeId(objDivArr)//.setAttribute("id",timestamp);
	}else{
		for(var i=0;i<objDivArrLen;i++){
			if (get_visibility_JS(objDivArr[i])){
				timestamp = getNodeId(objDivArr[i])//.setAttribute("id",timestamp);
				break;
			}
		}
	}
	return timestamp
	// if (SelectItemExist[0]){
		// objDivArr[SelectItemExist[1]].setAttribute("id",timestamp);
		// return timestamp
		// return objDivArr[SelectItemExist[1]];
	// }
    // return objDivArr.length
}

// getElementsByClassName谷歌匹配到多个，继续再次判断相等。
function returnSameClass(nodeArr,strClassName){
	var returnNode = new Array();
	// if(nodeArr.length > 1){
		var nodeArrLen = nodeArr.length;
		var j = 0;
		for (var i=0;i<nodeArrLen;i++){
			if (nodeArr[i].className==strClassName){
				returnNode[j] = nodeArr[i];
				j++;
			}
		}
	// }else{
		// returnNode = nodeArr;
	// }
	return returnNode;
}

// 三态和非三态状态下的清空和全选
function Web_clickdict_JS(ThreeState,domId,CurrentStep){
	var strStep = decodeAscwCurrentStep(CurrentStep);
	var node = document.getElementById(domId);
	var NodeLi = node.getElementsByTagName("ul");
	var NodeA = NodeLi[0].getElementsByTagName("a");
	if (ThreeState.toString().toUpperCase() == "TRUE"){
		var NodeImg = NodeA[0].previousSibling;
		if (strStep=="全选"){
			if (!NodeImg.checked){
				NodeImg.click();
			}
		}else if(strStep=="清空"){
			NodeImg.click();
			if (NodeImg.checked){
				NodeImg.click();
			}
		}
	}else{
		var NodeALen = NodeA.length;
		if (strStep=="全选"){
			for(var i=0;i<NodeALen;i++){
				var WebCheckDom = NodeA[i].previousSibling;
				if (!WebCheckDom.checked){
					WebCheckDom.click();
				}
			}
		}else if(strStep=="清空"){
			for(var i=0;i<NodeALen;i++){
				var WebCheckDom = NodeA[i].previousSibling;
				if (WebCheckDom.checked){
					WebCheckDom.click();
				}
			}
		}
	}
}

// 提示框,校验按钮名
function web_MessageCustom_getButton(thisClassName,thisButtonName){
	var SelectDom = new Array();
	var boolSearch = false;
	var Mess = qtp_getElementsByClassName(thisClassName,null,"div");
	Mess = returnSameClass(Mess,thisClassName);
	if (Mess != null && Mess != undefined){
		if (Mess.length != 0){
			var MessLen = Mess.length;
			for(var i=0;i<MessLen;i++){
				if (get_visibility_JS(Mess[i])){
					var buts = Mess[i].getElementsByTagName("button");
					var butsLen = buts.length;
					for(var j=0;j<butsLen;j++){
						var butsText = get_innerText_OR_textContent(buts[j]);
						if (butsText == thisButtonName){
							boolSearch = true;
							i=MessLen
							break;
						}else if(butsText.indexOf(thisButtonName) != -1){
							boolSearch = true;
							i=MessLen
							break;
						}
					}
				}
			}
		}
	}
	if (!boolSearch){
		j=null;
	}
	SelectDom[0]=boolSearch;
	SelectDom[1]=j;
	return SelectDom;
}

// 提示框
function web_MessageCustom_JS(thisButtonName){
	var errorLevel = 0; // 错误等级。
	var resultINFO = "";
	var strSearch = "";
	var ButtonNameStr = decodeAscwCurrentStep(thisButtonName);
	var ButtonNameArr = ButtonNameStr.split(":");
	if (ButtonNameArr.length == 1){
		var ButtonName = ButtonNameArr[0];
	}else{
		var ButtonName = ButtonNameArr[1];
	}
	strSearch = web_MessageCustom_getButton(" x-window x-window-noborder x-window-plain",ButtonName);
	if (!strSearch[0]){
		strSearch = web_MessageCustom_getButton(" x-window x-window-plain x-window-dlg",ButtonName);
		if (!strSearch[0]){
			strSearch = web_MessageCustom_getButton("x-window",ButtonName);
			if (!strSearch[0]){
				if (ButtonNameArr[0] == "提示"){
					strSearch = web_MessageCustom_getButton(" x-window x-window-plain x-window-dlg",ButtonName);
					var Mess = qtp_getElementsByClassName(" x-window x-window-plain x-window-dlg",null,"div");
				}else{
					resultINFO =  "没有找到提示框。"
					errorLevel = "-100";
					return resultINFO + ":" + errorLevel;
				}
			}else{
				var Mess = qtp_getElementsByClassName("x-window",null,"div");
			}
		}else{
			var Mess = qtp_getElementsByClassName(" x-window x-window-plain x-window-dlg",null,"div");
		}
	}else{
		var Mess = qtp_getElementsByClassName(" x-window x-window-noborder x-window-plain",null,"div");
	}
	if (Mess != undefined){
		if(Mess.length==1){
			// var buts = Mess[0].getElementsByTagName("button").length;
			var i = 0;
		}else if(Mess.length>1){
			var MessLen = Mess.length;
			for (var i=0;i<MessLen;i++){
				if (get_visibility_JS(Mess[i])){
					// var buts = Mess[i].getElementsByTagName("button").length;
					break;
				}
			}
		}
	}
	if (strSearch[1] != null){
		var buts = parseInt(strSearch[1]);
		var oButtons = Mess[i].getElementsByTagName("button");
		var butsText = get_innerText_OR_textContent(oButtons[buts]);
	}
	if (strSearch[1] == null){
		var oButtonsTd = Mess[i].getElementsByTagName("td");
		var oButtonsTdLen = oButtonsTd.length;
		for(var k=0;k<oButtonsTdLen;k++){
			if (oButtonsTd[k].className.indexOf("x-toolbar-cell") != -1){
				if (oButtonsTd[k].className.indexOf("x-hide-offsets") == -1){
					var clickButton = oButtonsTd[k].getElementsByTagName("button");
					clickHTML(clickButton[0]);
					resultINFO = "没有找到"+ButtonName+"按钮，直接点击显示的唯一按钮。" ;
					break;
				}
			}
		}
	}else if (butsText == ButtonName){
		clickHTML(oButtons[buts]);
		resultINFO = "匹配，进行点击。" ;
	}else if(qtp_regExpFind(butsText,"^"+ButtonName+"\\(.*")){
		clickHTML(oButtons[buts]);
		resultINFO = "匹配，进行点击。" ;
	}else{
		resultINFO = "没有找到需要点击的按钮。" ;
		errorLevel = "-99";
	}
	return resultINFO + ":" + errorLevel;
}

/**
 * 查找表格列名。
 * @returns 查找结果，true的情况返回列号（0开始），例：true:1
 */ 
function GetAllObject_WebT_JS(thisSearchStr){
	var searchStr = decodeAscwCurrentStep(thisSearchStr);
	var Node = GetAllObject_JS("WebT","grid");
	var nodeText = "";
	if (Node == null){
		return false;
	}
	var boolSearch = false;
	var NodeTr = Node.getElementsByTagName("tr")[1];
	if (NodeTr != null){
		var NodeTd = NodeTr.getElementsByTagName("td");
		var NodeTdLen = NodeTd.length;
		for (var i=0;i<NodeTdLen;i++){
			nodeText = get_innerText_OR_textContent(NodeTd[i]);
//			alert(nodeText);
			if (nodeText==searchStr){
				boolSearch = true;
				break;
			}
		}
	}
	var returnStr = boolSearch+":"+i;
	return returnStr;	
}

//验证操作：获取COMBOBOX控件的所有的内容
function Web_GetControlValue_COMBOBOX(){
	var oNode = returnVisibleNode(qtp_getElementsByClassName("x-combo-list-inner","null","div"));
	if (oNode.length == 1){
		var nodeDiv = oNode[0].getElementsByTagName("div");
		var nodeDivLen = nodeDiv.length;
		var getArr = new Array();
		for (var i=0;i<nodeDivLen;i++){
			if (nodeDiv[i].className.indexOf("x-combo-list-item") != -1){
				getArr[i] = get_innerText_OR_textContent(nodeDiv[i]);
			}
		}
		var getArrStr = getArr.join(":")
		return getArrStr;
	}else{
		return false;
	}
    // var objDivArr = qtp_getElementsByClassName("x-combo-list-inner","null","div")[0];
    // var objDivlen = objDivArr.length;
	// var SelectItemExist = Web_searchVisibleDom(objDivArr,strCurrentStep,"div");
}

// 验证操作：获取DICTIONARY控件的所有的内容
// isMultiItem true:多选字典
function getControlValue_DICTIONARY(nodeId,isMultiItem){
	var obj = document.getElementById(nodeId);
	var objUl = obj.getElementsByTagName("ul");
	var objA = objUl[0].getElementsByTagName("a");
	var objALen = objA.length;
	var getArr = new Array();
	if (isMultiItem){
		for(var i=0;i<objALen;i++){
			getArr[i] = get_innerText_OR_textContent(objA[i]) + ";" + objA[i].previousSibling.checked;
		}
	}else{
		for(var i=0;i<objALen;i++){
			getArr[i] = get_innerText_OR_textContent(objA[i]);
		}
	}

	var getArrStr = getArr.join(":");
	return getArrStr;
}

// 显示查询界面
function Web_ShowDeskUI_JS(thisCurrentStep){
	InputNewTableId = "";
	ClickNewObject = "";
	var boolRe = false;
	// 判断显示消息管理。
		var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
		if (CurrentStep =="消息管理"){
			ClickNewObject = "main_containerMsgManage";
			boolRe = true;
			return boolRe;
		}
		
	var ClickNewObjectObject = null;
	var MAPviewID = MAP_BillContext.getBillViewer().viewID;
	ClickNewObject = "main_container"+MAPviewID;
	ClickNewObjectObject = document.getElementById(ClickNewObject);
	if (ClickNewObjectObject != null){
		var searchTabelID = findNodeByClassNameByRegExp("grid_\\d+"+MAPviewID,"div",ClickNewObjectObject);
		// var searchTabelID = findNodeByClassNameByRegExp("grid_"+InDataTableNum+MAPviewID,"div",ClickNewObjectObject);
		if (searchTabelID != ""){
			InputNewTableId = searchTabelID;
		}
	}else{
		var classNode = qtp_getElementsByClassName(" x-window x-window-noborder x-window-plain x-resizable-pinned","null","div")[0];
		if (classNode!=null){
			var searchNodeArr = classNode.getElementsByTagName('*');
			var searchNodeLen = searchNodeArr.length;
			for (var i=0;i<searchNodeLen;i++){
				if (qtp_regExpFind(searchNodeArr[i].id,"grid_\\d+_xnode_"+qtp_getNodeIndex()+".*")){
					InputNewTableId = searchNodeArr[i].id;
					break;
				}
			}
			for (var j=0;j<searchNodeLen;j++){
				if (qtp_regExpFind(searchNodeArr[j].id,"main_container_xnode_"+qtp_getNodeIndex()+".*")){
					ClickNewObject = searchNodeArr[j].id;
					break;
				}
			}
		}else{
			var classNode = qtp_getElementsByClassName(" x-window x-window-noborder x-window-plain","null","div")[0];
			if (classNode!=null){
				var searchNodeArr = classNode.getElementsByTagName('*');
				var searchNodeLen = searchNodeArr.length;
				for (var i=0;i<searchNodeLen;i++){
					if (qtp_regExpFind(searchNodeArr[i].id,"grid_\\d+_xnode_"+qtp_getNodeIndex()+".*")){
						InputNewTableId = searchNodeArr[i].id;
						break;
					}
				}
				for (var j=0;j<searchNodeLen;j++){
					if (qtp_regExpFind(searchNodeArr[j].id,"main_container_xnode_"+qtp_getNodeIndex()+".*")){
						ClickNewObject = searchNodeArr[j].id;
						break;
					}
				}
			}
		}
	}
	ClickNewObjectObject = document.getElementById(ClickNewObject);
	if (ClickNewObjectObject != null){
		var firstNode = ClickNewObjectObject.firstChild;
		var spanNodes = firstNode.getElementsByTagName("span");
		for(var i=0;i<spanNodes.length;i++){
			if (get_innerText_OR_textContent(spanNodes[i]) == thisCurrentStep){
				boolRe = true;
				break;
			}
		}
	}
	return boolRe;
}

/**
 * 获取查询界面的title
 */
function getShowDeskTitle(){
	var ShowDeskId = "";
	var classNode = qtp_getElementsByClassName(" x-window x-window-noborder x-window-plain x-resizable-pinned","null","div")[0];
	if (classNode==null){
		var classNode = qtp_getElementsByClassName(" x-window x-window-noborder x-window-plain","null","div")[0];
	}
	if (classNode!=null){
		ShowDeskId = getNodeId(classNode.getElementsByTagName('span')[0]);
	}
	return ShowDeskId;
}

function GetAllObject_JS(obj,strCode){
	qtp_getNodeIndex();
	var MAPviewID = MAP_BillContext.getBillViewer().viewID;
	if (typeof InputNewTableId !="undefined" && strCode=="grid"){
		if (InputNewTableId != ""){
			if (document.getElementById(InputNewTableId)!=null){
				switch (obj.toUpperCase()){
					case "WEBT":
						return document.getElementById(InputNewTableId).getElementsByTagName('table')[0];
					case "JELEMENT":
						return document.getElementById(InputNewTableId);
					case "JBODYTEXT":
						return "var Jbody = document.getElementById('"+InputNewTableId+"').getElementsByTagName('table')[1];";
					case "JBODYALLTABLE":
						return document.getElementById(InputNewTableId).getElementsByTagName('table');
				}
			}
		}else{
			var main_containerNodeId = "main_container"+MAPviewID;
			// var main_containerNodeId = "main_container_xnode_"+StaticNodeIndex.nodeIndex+"_MV";
			var main_containerNode = document.getElementById(main_containerNodeId);
			var searchNodeId = "grid_"+InDataTableNum+MAPviewID;
			var searchNode = findNodeByClassNameByRegExp(searchNodeId,"div",main_containerNode);
			if (searchNode!=""){
				switch (obj.toUpperCase()){
					case "WEBT":
						return document.getElementById(searchNode).getElementsByTagName('table')[0];
					case "JELEMENT":
						return document.getElementById(searchNode);
					case "JBODYTEXT":
						return "var Jbody = document.getElementById('"+searchNode+"').getElementsByTagName('table')[1];";
					case "JBODYALLTABLE":
						return document.getElementById(searchNode).getElementsByTagName('table');
				}
			}
		}
	}else if (typeof InputNewTableId =="undefined" && strCode=="grid"){
		var main_containerNodeId = "main_container"+MAPviewID;
		var main_containerNode = document.getElementById(main_containerNodeId);
		var searchNodeId = "grid_"+InDataTableNum+MAPviewID;
		var searchNode = findNodeByClassNameByRegExp(searchNodeId,"div",main_containerNode);
		if (searchNode!=""){
			switch (obj.toUpperCase()){
				case "WEBT":
					return document.getElementById(searchNode).getElementsByTagName('table')[0];
				case "JELEMENT":
					return document.getElementById(searchNode);
				case "JBODYTEXT":
					return "var Jbody = document.getElementById('"+searchNode+"').getElementsByTagName('table')[1];";
				case "JBODYALLTABLE":
					return document.getElementById(searchNode).getElementsByTagName('table');
			}
		}
	}else{
		// var main_containerNodeId = "main_container_xnode_"+StaticNodeIndex.nodeIndex+"_MV";
		// var main_containerNode = document.getElementById(main_containerNodeId);
		try{
			if (ClickNewObject !="" && strCode!="grid"){
				return document.getElementById(ClickNewObject);
			}
		}
		catch(err){
			if (strCode != "grid"){
				var searchNodeId = strCode+MAPviewID;
				var searchNode = document.getElementById(searchNodeId);
				// var searchNodeId = strCode+"_xnode_"+StaticNodeIndex.nodeIndex+"_.*";
				// var searchNode = findNodeByClassNameByRegExp(searchNodeId,"div",null);
				if (searchNode==null){
					var searchNodeId = strCode+".*";
					var searchNode = findNodeByClassNameByRegExp(searchNodeId,"div",null);
					return document.getElementById(searchNode);
				}else{
					return searchNode;
				}
			}
		}
	}
}

// 遍历所有div节点匹配ID。返回该节点。
function findNodeByClassNameByRegExp(searchId,classN,searchFromNode){
	var searchResult = "";
	if (searchFromNode!=null){
		var divNodes = searchFromNode.getElementsByTagName(classN);
	}else{
		var divNodes = document.getElementsByTagName(classN);
	}
	var divNodesLen = divNodes.length;
	for (var i=0;i<divNodesLen;i++){
		var strNodeId = divNodes[i].id;
		if (qtp_regExpFind(strNodeId,searchId)){
			searchResult = strNodeId;
			break;
		}
	}
	return searchResult;
}

// 查看:表格数据
function Web_CheckTableData_JS(thisStr,ControlType,isCDbl){
	var BoolResults = false;
	var gridResult = false;
	var str = decodeAscwCurrentStep(thisStr);
	var ExpectedResult = str.split(":");
	var ExpectedResultLen = ExpectedResult.length;
	ExpectedResult = GetHtmlChr_JS(ExpectedResult);
	var JbodyAllTable = GetAllObject_JS("JbodyAllTable","grid");
	if (JbodyAllTable.length >= 2){
		var TableValueTr = JbodyAllTable[1].getElementsByTagName("tr");
		var TableValueTrLen = TableValueTr.length;
		var isTrAdd = false;
		if (TableValueTr[0].className == "jqgfirstrow"){
			isTrAdd = true;
		}
		if (isTrAdd){
			TableValueTrLen = TableValueTrLen -1;
		}
		if (qtp_regExpFind(ExpectedResult[1],"\(\\d+,\\d+\)")){
			var ExpectedResultArr = ExpectedResult[1].split(",");
			var TrRow = parseInt(qtp_regExp(ExpectedResultArr[0],"\\d+")[0]);
			var TdCol = parseInt(qtp_regExp(ExpectedResultArr[1],"\\d+")[0]);
//			var Table2Tr = JbodyAllTable[1].getElementsByTagName("tr");
//			var isTrAdd = false;
//			if (Table2Tr[0].className == "jqgfirstrow"){
//				isTrAdd = true;
//			}
//			var Table2TrLen = Table2Tr.length;
//			if (isTrAdd){
//				Table2TrLen = Table2TrLen -1;
//			}
			if (TableValueTrLen >= TrRow){
				if (isTrAdd){
					TrRow = TrRow +1;
				}
				var Table2Td = TableValueTr[TrRow -1].getElementsByTagName("td");
				//浏览器区分获取TEXT
				var Table2TdText = get_innerText_OR_textContent(Table2Td[TdCol -1]);
				BoolResults = disposeResult(ExpectedResult[2],Table2TdText,isCDbl);
				ExpectedResult[2] = Table2TdText;
//				if (ExpectedResult[2]=="空"){
//					if (Table2TdText == ""){
//						BoolResults = true;
//					}else{
//						ExpectedResult[2] = Table2TdText;
//					}
//				}else if(Table2TdText == ExpectedResult[2]){
//					BoolResults = true;
//				}else if(Table2TdText != ExpectedResult[2]){
//					ExpectedResult[2] = Table2TdText;
//				}
			}else{
				// BoolResults = false;
				ExpectedResult[2] = "没有数据";
			}
		}else if(ExpectedResult[1] == "行数"){
			if (parseInt(TableValueTrLen) == parseInt(ExpectedResult[2])){
				BoolResults = true;
			}else{
				ExpectedResult[2] = TableValueTrLen;
			}
		}else if(ControlType.toUpperCase() == "GRID"){
			var boolGrid = true;
			var BoolSearchResult = false;
			if(!qtp_regExpChinese(ExpectedResult[0])){
				var NodeId = "grid_" + InDataTableNum + MAP_BillContext.getBillViewer().viewID;
				var len = Ext.getCmp(NodeId).colModel.config.length;
				for(var i=0;i<len;i++){
					if(ExpectedResult[0]==Ext.getCmp(NodeId).colModel.config[i].__key){
						var TableValueTdIndex = i;
						BoolSearchResult = true;
						break;
					}
				}			
			}else{
				var nodeName = "td";
				if (JbodyAllTable[0].firstChild.nodeName.toLowerCase() == "thead"){
					nodeName = "th";
				}
				var TableNameTr = JbodyAllTable[0].getElementsByTagName("tr");
				var TableNameTrLen = TableNameTr.length;
				for (var m=0;m<TableNameTrLen;m++){
					var TableNameTd = TableNameTr[m].getElementsByTagName(nodeName);
					var TableNameTdLen = TableNameTd.length;
					for (var k=0;k<TableNameTdLen;k++){
						var TableNameTdText = get_innerText_OR_textContent(TableNameTd[k]);
						if (TableNameTdText == ExpectedResult[0]){
							var TableValueTdIndex = k;
							m = TableNameTrLen +1;
							BoolSearchResult = true;
							break;
						}
					}
				}
			}
			if (BoolSearchResult){
//				var TableValueTr = JbodyAllTable[1].getElementsByTagName("tr");
//				var TableValueTrLen = TableValueTr.length;
//				var isTrAdd = false;
//				if (TableValueTr[0].className == "jqgfirstrow"){
//					isTrAdd = true;
//				}
//				if (isTrAdd){
//					TableValueTrLen = TableValueTrLen -1;
//				}
				if (TableValueTrLen != ExpectedResult.length -2){
					ExpectedResult[ExpectedResultLen] = "界面:"+TableValueTrLen+"条数据。用例:"+(ExpectedResultLen-2)+"条数据。";
				}else{
					for (var j=1;j<ExpectedResultLen-1;j++){
						gridResult = false;
						if (TableValueTrLen>=j){
							var trIndex = j-1;
							if (isTrAdd){
								trIndex = j;
							}
							var GetTableValue = get_innerText_OR_textContent(TableValueTr[trIndex].getElementsByTagName("td")[TableValueTdIndex])
							gridResult = disposeResult(ExpectedResult[j],GetTableValue,isCDbl);
							if (GetTableValue.indexOf(";") != -1){
								GetTableValue = GetTableValue.replace(/;/g,",");
							}
							ExpectedResult[j] = GetTableValue;
						}else if(TableValueTrLen==0 && ExpectedResult[j]=="空"){
							gridResult = true;
						}else{
							// gridResult = false;
							ExpectedResult[j]="";
						}
						if (!gridResult){
							boolGrid = false;
						}
					}
					BoolResults = boolGrid;
				}
			}else{
				// BoolResults = false;
				ExpectedResult[ExpectedResultLen] = "没有找到Table列"+ExpectedResult[0];
			}
		}
	}else{
		ExpectedResult[ExpectedResultLen-1] = "没有数据表。";
	}
	var MyArr = new Array();
	MyArr[1] = BoolResults;
	MyArr[0] = ExpectedResult.join(" ");
	return MyArr.join(";");
}

/**
 * 对比结果，返回true || false
 * @param expectedResult 预期结果
 * @param actulResult 实际结果
 */
function disposeResult(expectedResult,actulResult,isCDbl){
	var gridResult = false;
	if (expectedResult == "空" || expectedResult == "-"){
		if (actulResult == ""){
			gridResult = true;
			expectedResult = actulResult;
		}
	}else{
		if (expectedResult == ""){
			if (actulResult == ""){
				gridResult = true;
			}
		}else{
			if (isCDbl){
				if (parseFloat(expectedResult) == parseFloat(actulResult)){
					gridResult = true;
				}
			}else{
				// 根据中间的空格分隔，可能是特殊字符。
				if(actulResult.indexOf(String.fromCharCode(160)) != -1){
					actulResult = ConvertStrBlankSpace(actulResult,160);
				}
				if (actulResult == expectedResult){
					gridResult = true;
				}else{
					var doesSplit = false;
					var arrExpected = expectedResult.split(" ");
					var arrExpectedLen = arrExpected.length;
					if (arrExpectedLen == 1){
						doesSplit = true;
					}
					if(expectedResult.indexOf(",")){
						var arrExpectedResult = expectedResult.split(",");
						var expectedResultLen = arrExpectedResult.length;
						var arrActulResult = actulResult.split(";");
						var actulResultLen = arrActulResult.length;
						if (expectedResultLen == actulResultLen){
							var boolRe = true;
							for(var i=0;i<expectedResultLen;i++){
								var boolR = false;
								for(var j=0;j<actulResultLen;j++){
									if (doesSplit){
										arrActulResultEx = arrActulResult[j].split(" ");
										arrActulResult[j] = arrActulResultEx[0];
									}
									if (arrExpectedResult[i] == arrActulResult[j]){
										boolR = true;
										break;
									}
								}
								if (!boolR){
									boolRe = false;
								}
							}
							if (boolRe){
								gridResult = true;
							}
						}
					}else{
						if (doesSplit){
							arrActulResult = actulResult.split(" ");
							actulResult = arrActulResult[0];
						}
						if (actulResult == expectedResult){
							gridResult = true;
						}
					}
				}
			}
		}
	}
	return gridResult;
}

/**
 * 获取某列的所有数据
 * @param column
 * @returns {String}
 */
function getTableColumnData(column,isMultiDict){
	var str = "";
	try{
		eval(GetAllObject_JS("JbodyText","grid"));
		var tableTr = Jbody.getElementsByTagName("tr");
		var isTrAdd = false;
		if (tableTr[0].className == "jqgfirstrow"){
			isTrAdd = true;
		}
		var tableTrLen = tableTr.length;
		var tableTrIndex = 0;
		if (isTrAdd){
			tableTrIndex = 1;
		}
		for(var i=tableTrIndex;i<tableTrLen;i++){
			var tableTd = tableTr[i].getElementsByTagName("td");
			var tableTdText = get_innerText_OR_textContent(tableTd[column]);
			if (tableTdText == ""){
				if (str == ""){
					str = ";";
				}
			}else if(isMultiDict){
				if (tableTdText.indexOf(";") != -1){
					tableTdText = tableTdText.replace(/;/g,",");
				}
			}
			if (str == ""){
				str = tableTdText;
			}else{
				str = str + ";" + tableTdText;
			}
		}
	}
	catch(err){
	}
	return str;
}

//浏览器区分获取visibility
function get_visibility_JS(node){
	if (window.navigator.userAgent.indexOf("MSIE")>0){
		return node.currentStyle.visibility =="visible";
	}else{
		return document.defaultView.getComputedStyle(node,null).visibility =="visible"
    }
}

//浏览器区分获取TEXT
function get_innerText_OR_textContent(node){
	if (window.navigator.userAgent.indexOf("MSIE")>0){
		return trim(node.innerText);
	}else{
		return trim(node.textContent);
    }
}

// 展开字典选择界面的所有汇总节点
function Web_opendict(){
	var strReturn = "";
	try{
		eval(GetAllObject_JS("JbodyText","grid"))
		Jbody
	}
	catch(err){
		return "";
	}
	var DropDownObjTr = Jbody.getElementsByTagName("tr");
	var DropDownObjTrLen = DropDownObjTr.length;
	for (var i=0;i<DropDownObjTrLen;i++){
		var DropDownObjSp = DropDownObjTr[i].getElementsByTagName("span");
		if (DropDownObjSp.length != 0){
			var Webimg = DropDownObjSp[1].previousSibling.previousSibling;
			if (Webimg.className.indexOf("elbow-plus") != -1 || Webimg.className.indexOf("elbow-end-plus") != -1){
				strReturn = getNodeId(Webimg);
			}
		}else{
			strReturn = "";
		}
	}
	return strReturn;
}

//表格菜单  返回表格列名的下拉按钮 ID
function gridMenuColId(name){
//	var arrStep = strStep.split(":");
	var Node = GetAllObject_JS("WebT","grid");
	var gridTr = Node.getElementsByTagName("tr");
	var gridTrLen = gridTr.length;
	var addNodeId = "";
	for(var i=0;i<gridTrLen;i++){
		var gridTd = gridTr[i].getElementsByTagName("td");
		var gridTdLen = gridTd.length;
		for(var j=0;j<gridTdLen;j++){
			if (name == get_innerText_OR_textContent(gridTd[j])){
				var gridDiv = gridTd[j].getElementsByTagName("div");
				addNodeId = getNodeId(gridDiv[0]);
				i = gridTrLen;
				break;
			}
		}
	}
	return addNodeId;
}

// 表格菜单 返回表格列名下拉框里面的“列”节点ID
function gridMenuColListColId(){
	
}

// 获取当前时间戳
function getTimesTamp(){
	return new Date().getTime();
}

// 表格录入、表格修改。DOUBLECLICK
function Web_InputData_DoubleClick(Row,Col){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	if (Col == "null"){
		Row = parseInt(Row)-1;
		Col = 1;		
	}
	var DBclickNodeTr = Jbody.getElementsByTagName("tr");
	var DBclickNodeTd = DBclickNodeTr[Row].getElementsByTagName("td");
	var DBclickNode = DBclickNodeTd[Col];
	qtp_FireEvent(DBclickNode,"dblclick");
}

//表格录入、表格修改。CLICK
function Web_InputData_Click(Row,Col){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	if (Col == "null"){
		Row = parseInt(Row)-1;
		Col = 1;		
	}
	var clickNodeTr = Jbody.getElementsByTagName("tr");
	var clickNodeTd = clickNodeTr[Row].getElementsByTagName("td");
	var clickNode = clickNodeTd[Col];
	qtp_FireEvent(clickNode,"click");
}

// ctrlType：0 验证字典 1选择字典
// '**汇总节点需要点击展开
// '**************************
function Web_SelectDictionary_JS(thisText,ctrlType){
	var contrastText = decodeAscwCurrentStep(thisText);
	var BoolSearch = false;
	var isChained = false; // 链式字典
	var nodeId = "";
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var searchTr = Jbody.getElementsByTagName("tr");
	var searchTrLen = searchTr.length;
	//2个td：链式字典,isChained = true
	var isChainedTd = searchTr[0].getElementsByTagName("td");
	if(isChainedTd.length == 2){
		var isChained = true
	}
	for(var i=0;i<searchTrLen;i++){
		if (isChained){
			var searchTd = searchTr[i].getElementsByTagName("td");
			var getText = get_innerText_OR_textContent(searchTd[0])+" "+get_innerText_OR_textContent(searchTd[1])
		}else{
			var searchSpan = searchTr[i].getElementsByTagName("span");
			var getText = get_innerText_OR_textContent(searchSpan[searchSpan.length-1]);
		}
		if (qtp_regExpFind(contrastText,"^\\[.*\\]$")){
			if (getText.indexOf(String.fromCharCode(160)) != -1){
				getText = ConvertStrBlankSpace(getText,160);
			}
			if (contrastText.indexOf(getText) != -1){
				BoolSearch = true;
				nodeId = getNodeId(searchTr[i]);
				break;
			}
		}else{
			// 根据中间的空格分隔，可能是特殊字符。
			if (getText.indexOf(String.fromCharCode(160)) != -1){
				getText = ConvertStrBlankSpace(getText,160);
			}
			var getTextArr = getText.split(" ");
			// 删除已使用过的字典，以"* "开头。
			if (getTextArr.length >2){
				if (getTextArr[0]+" "+getTextArr[1]==contrastText){
					nodeId = getNodeId(searchTr[i]);
					BoolSearch = true;
					break;
				}else if(ctrlType == 1){
					if (getTextArr[1]==contrastText){
						nodeId = getNodeId(searchTr[i]);
						BoolSearch = true;
						break;
					}
				}else if(getTextArr[0] == contrastText){
					nodeId = getNodeId(searchTr[i]);
					BoolSearch = true;
					break;
				}
			}else if (getTextArr != null){
				if (getTextArr[0]==contrastText){
					nodeId = getNodeId(searchTr[i]);
					BoolSearch = true;
					break;
				}
			}
		}
	}
	switch (ctrlType){
		case 0:
			return BoolSearch + ":" + nodeId;
		case 1:
			if (BoolSearch){
				// var timestamp = new Date().valueOf();
				// timestamp = "BokeTest" + timestamp; //时间戳
				// searchTr[i].setAttribute("id",timestamp);
				return getNodeId(searchTr[i]);
			}else{
				return "";
			}
	}
}

// 用于字典code与name之间的特殊字符替换成空格
function ConvertStrBlankSpace(str,codeNum){
	var spcCode = String.fromCharCode(codeNum);
	if (str.indexOf(spcCode) != 0){
		return str.replace(new RegExp(spcCode,'g')," ");
	}else{
		return str;
	}
}

/**
 * 获取HTML节点的getBoundingClientRect属性。
 * @param nodeId
 * @returns {String}
 */
function objCovered_JS(nodeId){
	if (nodeId == ""){
		eval(GetAllObject_JS("JbodyText","grid"))
		Jbody
		var objNew = Jbody.parentNode.parentNode;
	}else{
		var objNew = document.getElementById(nodeId);
	}
		var arr0 = objNew.getBoundingClientRect().left;  // qtp_regExp(parJbody.style.height,"\\d+");
		var arr1 = objNew.getBoundingClientRect().right;
		var arr2 = objNew.getBoundingClientRect().top;
		var arr3 = objNew.getBoundingClientRect().bottom;
	return arr0 + ":" + arr1 + ":" + arr2 + ":" + arr3;
}

// 转换转义符
function GetHtmlChr_JS(thisStr){
	// var str = decodeAscwCurrentStep(thisStr);
	if (typeof thisStr == "object"){
		var str = new Array();
		var thisStrLen = thisStr.length;
		for(var j=0;j<thisStrLen;j++){
			if (qtp_regExpFind(thisStr[j],"&#\\d{1,}@")){
				var arr = qtp_regExp(thisStr[j],"&#\\d{1,}@");
				var arrLen = arr.length;
				for(var i=0;i<arrLen;i++){
					var ascCode = qtp_regExp(arr[i],"\\d{1,}")[0];
					thisStr[j] = thisStr[j].replace(arr[i],String.fromCharCode(ascCode));
				}
			}else{
				thisStr[j] = thisStr[j];
			}
		}
		str = thisStr;
	}else{
		var str = thisStr;
		var arr = qtp_regExp(thisStr,"&#\\d{1,}@");
		var arrLen = arr.length;
		for(var i=0;i<arrLen;i++){
			var ascCode = qtp_regExp(arr[i],"\\d{1,}")[0];
			str = str.replace(arr[i],String.fromCharCode(ascCode));
		}
	}
	return str;
}

// 表格录入COMBOLIST
function Web_InputData_COMBOLIST(){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	return Jbody.parentNode.parentNode.id;
}

// 点击DICTIONARY的查找按钮
function AdvancedSearch(nodeId){
	var searchNode = document.getElementById(nodeId);
	var oSearchImg = searchNode.parentNode.getElementsByTagName("img");
	var oSearchImgLen = oSearchImg.length;
	for(var i=0;i<oSearchImgLen;i++){
		if (oSearchImg[i].className.indexOf("x-form-search-trigger") != -1){
			oSearchImg[i].click();
			break;
		}
	}
}

// 表格录入：点击 X 
function Web_InputData_ClickX_ReturnId(iRow){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var timestamp = new Date().valueOf();
	var JbodyTr = Jbody.getElementsByTagName("tr")[iRow];
	var JbodyTd = JbodyTr.getElementsByTagName("td");
	var JbodyTdLen = JbodyTd.length;
	for(var i=0;i<JbodyTdLen;i++){
		var JbodyTdText = get_innerText_OR_textContent(JbodyTd[i]);
		if (JbodyTdText.indexOf("×") != -1){
			var JbodyTdA = JbodyTd[i].getElementsByTagName("a")[0].firstChild;
			timestamp = "BokeTest" + timestamp; //时间戳
			JbodyTdA.setAttribute("id",timestamp);
			// JbodyTdA.click();
			break;
		}
	}
	return timestamp;
}

// 点击操作 单选控件：radio
function Web_ClickObject_CHECKBOX_Radio(RadioName,thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var objRadio = document.getElementsByName(RadioName);
	var objRadioLen = objRadio.length;
	for(var i=0;i<objRadioLen;i++){
		if (objRadio[i].id.indexOf(CurrentStep) != -1){
			objRadio[i].checked = "checked";
			break;
		}else if(get_innerText_OR_textContent(objRadio[i].parentNode).indexOf(CurrentStep) != -1){
			objRadio[i].checked = "checked";
			break;
		}
	}
}

// 表格单元格ID
function returnTabelCellId(iRow,iCol,isCheckBox){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var JbodyTr = Jbody.getElementsByTagName("tr")[iRow];
	if (JbodyTr != undefined){
		var JbodyTd = JbodyTr.getElementsByTagName("td")[iCol];
		var JbodyDiv = JbodyTd.getElementsByTagName("div")[0];
		if (isCheckBox){
			if (JbodyDiv.firstChild.nodeName == "PRE"){
				JbodyDiv = JbodyDiv.firstChild.firstChild;
			}else{
				JbodyDiv = JbodyDiv.firstChild;
			}
		}
		var JbodyDivId = getNodeId(JbodyDiv);
		return JbodyDivId;
	}else{
		return "";
	}

}

//表格单元格的值
function returnTabelCellText(iRow,iCol){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var JbodyTr = Jbody.getElementsByTagName("tr")[iRow];
	var JbodyTd = JbodyTr.getElementsByTagName("td")[iCol];
	return get_innerText_OR_textContent(JbodyTd);
}

// 获取节点ID，如果没有setAttribute
function getNodeId(node){
	var nodeId = node.id;
	if (nodeId==""){
		var timestamp = getTimesTamp();
		nodeId = "BokeTest" + timestamp; //时间戳
		node.setAttribute("id",nodeId);
	}
	return nodeId;
}

// 获取节点className
function getNodeClassName(nodeId){
	return document.getElementById(nodeId).className;
}

// 表格录入DICTIONARY点击“放大镜”按钮。
function Web_InputData_DICTIONARY_Choose(searchTagName,searchHtmlType,tableCellLeft,tableCellTop){
	var getAllInput = document.getElementsByTagName(searchTagName);
	var InputLen = getAllInput.length;
	for(var i=0;i<InputLen;i++){
		if (getAllInput[i].type == searchHtmlType){
			if (get_visibility_JS(getAllInput[i])){
				var inputLeft = getAllInput[i].getBoundingClientRect().left;
				var inputTop = getAllInput[i].getBoundingClientRect().top;
				if (Math.abs(inputLeft-parseInt(tableCellLeft))<10 && Math.abs(inputTop-parseInt(tableCellTop))<10){
					return getNodeId(getAllInput[i]);
					break;
				}
			}
		}
	}
}

// var PostClickNode;
// var nextSetpnum;
// var postCurrentStepArr;
// var iLen;
// 权限设置
// isVer true：验证业务权限，false：操作勾选。
function Web_Permissions_Change(thisCurrentStep,Setpnum,iRow,isVer){
	if (typeof thisCurrentStep =="string"){
		var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
		var CurrentStepArr = CurrentStep.split(":");
	}else{
		var CurrentStepArr = thisCurrentStep;
	}
	var objNode = qtp_getElementsByClassName(SetPerTree,'null','ul')[0];
	var objNodeAText = "";
	var timestamp = "";
	var isVerResult = ""; //验证业务权限的结果。
	var nodeId = "";
	if (CurrentStepArr[Setpnum]!=""){
		if (CurrentStepArr[Setpnum]!='0' && CurrentStepArr[Setpnum]!='1'){
			var objNodeA = objNode.getElementsByTagName("a");
			var objNodeALen = objNodeA.length;
			for(var i=iRow;i<objNodeALen;i++){
				objNodeAText = get_innerText_OR_textContent(objNodeA[i]);
				if (Web_Permissions_Change_Different(CurrentStepArr[1],objNodeAText,CurrentStepArr[Setpnum])){
					if (CurrentStepArr[Setpnum+1]=="0"){
						var objNodeInput = objNodeA[i].previousSibling;
						if (!isVer){
							if (objNodeInput.checked){
								objNodeInput.click();
							}
						}else{
							isVerResult = false;
							if (!objNodeInput.checked){
								isVerResult = true;
							}
						}
						break;
					}else if(CurrentStepArr[Setpnum+1]=="1"){
						var objNodeInput = objNodeA[i].previousSibling;
						if (!isVer){
							if (!objNodeInput.checked){
								objNodeInput.click();
							}
						}else{
							isVerResult = false;
							if (objNodeInput.checked){
								isVerResult = true;
							}
						}
						break;
					}else if(CurrentStepArr[Setpnum]=="数据权限" && CurrentStepArr[Setpnum+1]==""){
						var objNodeImg = objNodeA[i].previousSibling.previousSibling;
						if (objNodeImg.className.indexOf("elbow-plus") != -1 || objNodeImg.className.indexOf("elbow-end-plus") != -1){
							timestamp = getNodeId(objNodeImg);
							return timestamp+":"+Setpnum+":"+i;
						}
					}else if(CurrentStepArr[Setpnum+1]==""){
						// objNodeA[i].click();
						return getNodeId(objNodeA[i])+":"+"";
					}else{
						if (objNodeA[i].previousSibling.nodeName == "INPUT"){
							var objNodeImg = objNodeA[i].previousSibling.previousSibling.previousSibling;
						}else{
							var objNodeImg = objNodeA[i].previousSibling.previousSibling;
						}
						if (objNodeImg.className.indexOf("elbow-plus") != -1 || objNodeImg.className.indexOf("elbow-end-plus") != -1){
							timestamp = getNodeId(objNodeImg);
							// 返回加号节点ID 用例步数 节点坐标
							return timestamp+":"+Setpnum+":"+i;
							// objNodeImg.click();
						}
						Setpnum++;
						// Web_Permissions_Change(CurrentStepArr,nextSetpnum,i);
						// document.onreadystatechange = function(){
							// if(document.readyState == "complete"){ //当页面加载状态 
								// Web_Permissions_Change(CurrentStepArr,nextSetpnum,i);
							// }
						// }
					}
				}
			}
		}
	}
	if (isVer){
		return timestamp + ";" + isVerResult;
	}else{
		return timestamp;
	}
	
}

// 权限设置:判断"业务权限" 还是 "数据权限"
function Web_Permissions_Change_Different(PermissionsName,NodeText,CurrentStep){
	var returnInfo = false;
	if (PermissionsName == "数据权限"){
		if (NodeText.indexOf("(") != -1){
			if (/\(([^\(]*)\)/.exec(NodeText)[1] == CurrentStep){
				returnInfo = true;
			}
		}else{
			if (NodeText == CurrentStep){
				returnInfo = true;
			}
		}
	}else if(PermissionsName == "业务权限"){
		if (NodeText == CurrentStep){
			returnInfo = true;
		}
	}
	return returnInfo;
}

// 复选框操作
function Web_CheckBox_JS(thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
	var MAPviewID = MAP_BillContext.getBillViewer().viewID;
	var main_containerNodeId = "main_container"+MAPviewID;
	var objNodeArr = qtp_getElementsByClassName(" x-panel x-border-panel",document.getElementById(main_containerNodeId),'div');
	var objNode = returnSameClass(objNodeArr," x-panel x-border-panel")[0];
	var inputNodeArr = objNode.firstChild.firstChild.childNodes;
	var inputNodeArrLen = inputNodeArr.length;
	for(var i=0;i<inputNodeArrLen;i++){
		var inputNodeText = get_innerText_OR_textContent(inputNodeArr[i]);
		if (inputNodeText==CurrentStepArr[1]){
			var inputNode = inputNodeArr[i].firstChild;
			if (CurrentStepArr[2].toUpperCase()=="ON"){
				if (inputNode.checked == false){
					inputNode.click();
					break;
				}
			}else if(CurrentStepArr[2].toUpperCase()=="OFF"){
				if (inputNode.checked == true){
					inputNode.click();
					break;
				}
			}
		}
	}
}

// 权限设置-业务权限 面板选择：
function Web_SelectTabPermissions(thisCurrentStep){
	var searchTabNode = false;
	var timestamp = "";
	var listNodeId = ""
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var MAPviewID = MAP_BillContext.getBillViewer().viewID;
	var main_containerNodeId = "main_container"+MAPviewID;
	// 获取"操作权限"、"字段权限"面板节点。
	var tabNode = qtp_getElementsByClassName("x-tab-strip x-tab-strip-top",document.getElementById(main_containerNodeId),'ul')[1];
	var tabNodeEm = tabNode.getElementsByTagName("em");
	var tabNodeEmLen = tabNodeEm.length;
	for(var i=0;i<tabNodeEmLen;i++){
		var tabText = get_innerText_OR_textContent(tabNodeEm[i]);
		if (tabText == CurrentStep){
			// tabNodeEm[i].click();
			searchTabNode = true;
			timestamp = getNodeId(tabNodeEm[i].parentNode);
			listNodeId = getNodeId(tabNode.parentNode.parentNode.parentNode); // 在此ID下找，提高效率。
			// var listIndex = i; // 记录第几个面板，与面板下选项节点对应。
			break;
		}
	}
	return searchTabNode+":"+timestamp+":"+listNodeId+":"+i;
}


// 权限设置：
// 勾选业务权限下的"操作权限"、"字段权限"的选项。
function Web_OptPermissions_JS(thisCurrentStep,listNodeId,i){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
		// 获取面板下所有选项节点。
	var boolSearchItem = false; // 是否找到用例选项。
	var boolItemCN = true; // 判断用例选项中文或英文。
	var listNode = qtp_getElementsByClassName("x-grid3-viewport",document.getElementById(listNodeId),'div')[i];
	var listNodeTable = listNode.getElementsByTagName("table");
	var listNodeTableLen = listNodeTable.length;
	if (CurrentStepArr[1]=="操作权限"){
		if (CurrentStepArr[1] == CurrentStepArr[2]){
			for(var j=1;j<listNodeTableLen;j++){
				var listNodeTableTd = listNodeTable[j].getElementsByTagName("td")[1];
				var listNodeTableTdDiv = listNodeTableTd.firstChild.firstChild;
				if (CurrentStepArr[3]==0 && listNodeTableTdDiv.className.indexOf("check-col-on") != -1){
					clickHTML_mousedown(listNodeTableTdDiv);
				}else if(CurrentStepArr[3]==1 && listNodeTableTdDiv.className.indexOf("check-col-on") == -1){
					clickHTML_mousedown(listNodeTableTdDiv);
				}
			}
		}else{
			var CurrentStepLen = CurrentStepArr.length -1;
			for(var CurStep=2;CurStep<CurrentStepLen;CurStep++){
				boolSearchItem = false;
				boolItemCN = true;
				if (/^[a-zA-Z]+$/.test(CurrentStepArr[CurStep])){
					boolItemCN = false;
				}
				for(var j=1;j<listNodeTableLen;j++){
					var listNodeTableNameTd = listNodeTable[j].getElementsByTagName("td")[0];
					if (boolItemCN){
						if (qtp_regExpFind(get_innerText_OR_textContent(listNodeTableNameTd),"^"+CurrentStepArr[CurStep]+"\\(")){
							boolSearchItem = true;
							break;
						}
					}else{
						if (get_innerText_OR_textContent(listNodeTableNameTd).indexOf("("+CurrentStepArr[CurStep]+")") != -1){
							boolSearchItem = true;
							break;
						}
					}
				}
				if (boolSearchItem){
					boolSearchItem = false;
					var listNodeTableTd = listNodeTable[j].getElementsByTagName("td")[1];
					var listNodeTableTdDiv = listNodeTableTd.firstChild.firstChild;
					if (CurrentStepArr[0] == "选择"){
						if (CurrentStepArr[CurrentStepLen-1]==0 && listNodeTableTdDiv.className.indexOf("check-col-on") != -1){
							clickHTML_mousedown(listNodeTableTdDiv);
						}else if(CurrentStepArr[CurrentStepLen-1]==1 && listNodeTableTdDiv.className.indexOf("check-col-on") == -1){
							clickHTML_mousedown(listNodeTableTdDiv);
						}
					}else{
						CurStep = CurrentStepLen;
						if (CurrentStepArr[CurrentStepLen-1]==0 && listNodeTableTdDiv.className.indexOf("check-col-on") == -1){
							boolSearchItem = true;
						}else if(CurrentStepArr[CurrentStepLen-1]==1 && listNodeTableTdDiv.className.indexOf("check-col-on") != -1){
							boolSearchItem = true;
						}
					}
					CurStep = CurrentStepLen;
				}
			}
		}
	}else if(CurrentStepArr[1]=="字段权限"){
		var CurrentStepLen = CurrentStepArr.length -1;
		if (CurrentStepArr[2]=="字段"){
			for(var j=1;j<listNodeTableLen;j++){
				var listNodeTableTd_visible = listNodeTable[j].getElementsByTagName("td")[1];
				var listNodeTableTdDiv_visible = listNodeTableTd_visible.firstChild.firstChild;
				if (CurrentStepArr[CurrentStepLen-2]==0 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") != -1){
					clickHTML_mousedown(listNodeTableTdDiv_visible);
				}else if(CurrentStepArr[CurrentStepLen-2]==1 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") == -1){
					clickHTML_mousedown(listNodeTableTdDiv_visible);
				}
				var listNodeTableTd_editable = listNodeTable[j].getElementsByTagName("td")[2];
				var listNodeTableTdDiv_editable = listNodeTableTd_editable.firstChild.firstChild;
				if (CurrentStepArr[CurrentStepLen-1]==0 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") != -1){
					clickHTML_mousedown(listNodeTableTdDiv_editable);
				}else if(CurrentStepArr[CurrentStepLen-1]==1 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") == -1){
					clickHTML_mousedown(listNodeTableTdDiv_editable);
				}
			}
		}else{
			for(var CurStep=2;CurStep<CurrentStepLen;CurStep++){
				boolSearchItem = false;
				boolItemCN = true;
				if (/^[a-zA-Z1-9]+$/.test(CurrentStepArr[CurStep])){
					boolItemCN = false;
				}
				for(var j=1;j<listNodeTableLen;j++){
					var listNodeTableNameTd = listNodeTable[j].getElementsByTagName("td")[0];
					if (boolItemCN){
						if (qtp_regExpFind(get_innerText_OR_textContent(listNodeTableNameTd),"^"+CurrentStepArr[CurStep]+"\\(")){
							boolSearchItem = true;
							break;
						}
					}else{
						if (get_innerText_OR_textContent(listNodeTableNameTd).indexOf("("+CurrentStepArr[CurStep]+")") != -1){
							boolSearchItem = true;
							break;
						}
					}
				}
				if (boolSearchItem){
					var listNodeTableTd_visible = listNodeTable[j].getElementsByTagName("td")[1];
					var listNodeTableTdDiv_visible = listNodeTableTd_visible.firstChild.firstChild;

					if (CurrentStepArr[0] == "选择"){
						if (CurrentStepArr[CurrentStepLen-2]==0 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") != -1){
							clickHTML_mousedown(listNodeTableTdDiv_visible);
						}else if(CurrentStepArr[CurrentStepLen-2]==1 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") == -1){
							clickHTML_mousedown(listNodeTableTdDiv_visible);
						}
						var listNodeTableTd_editable = listNodeTable[j].getElementsByTagName("td")[2];
						var listNodeTableTdDiv_editable = listNodeTableTd_editable.firstChild.firstChild;
						if (CurrentStepArr[CurrentStepLen-1]==0 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") != -1){
							clickHTML_mousedown(listNodeTableTdDiv_editable);
						}else if(CurrentStepArr[CurrentStepLen-1]==1 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") == -1){
							clickHTML_mousedown(listNodeTableTdDiv_editable);
						}
					}else{
						var boolSearchItem_visible = false;
						var boolSearchItem_editable = false;
						CurStep = CurrentStepLen;
						if (CurrentStepArr[CurrentStepLen-2]==0 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") == -1){
							boolSearchItem_visible = true;
						}else if(CurrentStepArr[CurrentStepLen-2]==1 && listNodeTableTdDiv_visible.className.indexOf("check-col-on") != -1){
							boolSearchItem_visible = true;
						}
						var listNodeTableTd_editable = listNodeTable[j].getElementsByTagName("td")[2];
						var listNodeTableTdDiv_editable = listNodeTableTd_editable.firstChild.firstChild;
						if (CurrentStepArr[CurrentStepLen-1]==0 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") == -1){
							boolSearchItem_editable = true;
						}else if(CurrentStepArr[CurrentStepLen-1]==1 && listNodeTableTdDiv_editable.className.indexOf("check-col-on") != -1){
							boolSearchItem_editable = true;
						}
						if (boolSearchItem_visible && boolSearchItem_editable){
							boolSearchItem = true;
						}else{
							boolSearchItem = false
						}
					}
					CurStep = CurrentStepLen;
				}
			}
		}
	}
	return boolSearchItem;
}

// 验证:菜单
function Web_VerificationJTree(thisCurrentStep,iStep){
	if (typeof thisCurrentStep =="string"){
		var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
		var CurrentStepArr = CurrentStep.split(":");
	}else{
		var CurrentStepArr = thisCurrentStep;
	}
	// var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	// var CurrentStepArr = CurrentStep.split(":");
	var CurrentStepArrLen = CurrentStepArr.length-1;
	var node = qtp_getElementsByClassName('x-tree-root-node',null,"div")[0];
	var nodeA = node.getElementsByTagName("a");
	var nodeALen = nodeA.length;
	var boolResult = false; // 最终结果。
	var boolSearchStep = false; // 展开过程中是否找到链接。
	var nodeId = ""; // 节点的HtmlId
	for(var j=iStep;j<CurrentStepArrLen;j++){
		if (CurrentStepArr[j].toUpperCase() !="TRUE" && CurrentStepArr[j].toUpperCase() !="FALSE"){
			for(var i=0;i<nodeALen;i++){
				var nodeLiAText = get_innerText_OR_textContent(nodeA[i]);
				if (nodeLiAText==CurrentStepArr[j]){
					boolSearchStep = true;
					var nodeLiImg = nodeA[i].previousSibling.previousSibling;
					if (CurrentStepArr[j+1].toUpperCase() !="TRUE" && CurrentStepArr[j+1].toUpperCase() !="FALSE"){
						if (nodeLiImg.className.indexOf("elbow-plus") != -1 || nodeLiImg.className.indexOf("elbow-end-plus") != -1){
							nodeLiImg.click();
						}
						boolResult = Web_VerificationJTree(thisCurrentStep,j+1);
						j = CurrentStepArrLen;
						break;
					}else{
						if (boolSearchStep.toString().toUpperCase() == CurrentStepArr[j+1].toUpperCase()){
							boolResult = true;
							nodeId = getNodeId(nodeA[i]);
						}
						if (!boolResult){
							CurrentStepArr[j+1] = boolSearchStep;
						}	
						boolResult = boolResult + ":" + CurrentStepArr.join(" ") + ":" + nodeId;
						j=CurrentStepArrLen
						break;
					}
				}
			}
			if (boolResult.toString().indexOf(":") != -1){
				break;
			}
			if (!boolSearchStep){
				if (CurrentStepArr[0]=="空" && nodeALen==0){
					boolResult = true+":空";
					break;
				}else{
					if (CurrentStepArr[j+1].toUpperCase() =="TRUE" || CurrentStepArr[j+1].toUpperCase() =="FALSE"){
						if (CurrentStepArr[j+1].toUpperCase() =="FALSE"){
							boolResult = true;
						}else{
							boolResult = false;
							CurrentStepArr[j+1] = boolSearchStep;
						}
						boolResult = boolResult + ":" + CurrentStepArr.join(" ") + ":" + nodeId;
						break;
					}else{
						boolResult = "false"+":没有找到LINK："+CurrentStepArr[j] + ":" + nodeId;
						break;
					}
				}
			}
		// }else{
			// var conStep = CurrentStepArr(CurrentStepArr.length-3);
		}
	}
	return boolResult;
}

// 验证:操作界面。
function Web_UIOperating_Verification(thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
	ClickNewObject = 'main_container'+MAP_BillContext.getBillViewer().viewID;
	var optNode = Web_UIOperating_MainNode();
	var optNodeTd = optNode.getElementsByTagName("td");
	var optNodeTdLen = optNodeTd.length;
	var boolSearch = false; // 搜索按钮结果
	var boolResult = false; // 最终结果。
	var optNodeTdText = "";
	var optMenusButtonText = "";
	var buttonOneisDisabled = "";
	var nodeId = ""; // 节点的HtmlId
	var nodeName = ""; // 节点名
	for(var i=0;i<optNodeTdLen;i++){
		if (optNodeTd[i].className.indexOf("x-toolbar-cell") != -1){
			optNodeTdText = get_innerText_OR_textContent(optNodeTd[i]);
			if (CurrentStepArr[2].indexOf("-") == -1){
				if (optNodeTd[i].className.indexOf("x-toolbar-cell") != -1){
					if (optNodeTdText.indexOf(CurrentStepArr[2]+"(") == 0 || optNodeTdText == CurrentStepArr[2]){ //qtp_regExpFind(optNodeTdText,CurrentStepArr[2]+"\\(") || (optNodeTdText == CurrentStepArr[2])
						boolSearch = true;
						nodeId = getNodeId(optNodeTd[i]);
						nodeName = "td";
						if (CurrentStepArr[1] == "启用"){
							// var optNodeButton = optNodeTd[i].getElementsByTagName("button")[0];
							boolResult = getAttribute_disabled(optNodeTd[i].firstChild);
						}else if(CurrentStepArr[1] == "可见"){
							boolResult = true;
						}
						break;
					}
				}
			}else{
				var buttonArr = CurrentStepArr[2].split("-");
				//buttonArr[0] == optNodeTdText || qtp_regExpFind(optNodeTdText,"^"+buttonArr[0]+"\\(")
				if (buttonArr[0] == optNodeTdText || optNodeTdText.indexOf(buttonArr[0]+"(") == 0){
					buttonOneisDisabled = getAttribute_disabled(optNodeTd[i].firstChild);
					if (buttonOneisDisabled){
						Ext.getCmp(optNodeTd[i].firstChild.id).showMenu();
						var textElements = document.getElementsByTagName("body")[0].childNodes;
						var textElementsLen = textElements.length;
						for(var m=0;m<textElementsLen;m++){
							if (textElements[m].className == "x-menu x-menu-floating x-layer "){
								// var optMenus = textElements[m].getElementsByTagName("button");
								var optMenusLi = textElements[m].getElementsByTagName("li");
								var optMenusLen = optMenusLi.length;
								for(var n=0;n<optMenusLen;n++){
									optMenusButtonText = get_innerText_OR_textContent(optMenusLi[n]);
									// (qtp_regExpFind(optMenusButtonText,"^"+buttonArr[1]+"$")||qtp_regExpFind(optMenusButtonText,"^"+buttonArr[1]+"\\(.*"))&& qtp_regExpFind(optMenusLi[n].childNodes[1].id,"xnode_"+StaticNodeIndex.nodeIndex)
									if((optMenusButtonText == buttonArr[1]||qtp_regExpFind(optMenusButtonText,"^"+buttonArr[1]+"\\(.*")) && optMenusLi[n].childNodes[1].id.indexOf("xnode_"+StaticNodeIndex.nodeIndex) != -1){
										nodeId = getNodeId(optMenusLi[n]);
										nodeName = "li";
										if (CurrentStepArr[1] == "启用"){
											boolResult = getAttribute_disabled(optMenusLi[n].childNodes[1]);
										}else if(CurrentStepArr[1] == "可见"){
											boolResult = true;
										}
										boolSearch = true;
										i = optNodeTdLen;
										m = textElementsLen;
										break;
									}
								}
							}
						}
					}else{
						boolResult = buttonOneisDisabled;
					}
				}
			}
				// if (optNodeTd[i].className,"x-hide-display"){
				// }
		}
	}
	return boolResult + ":" + nodeId + ":" + nodeName;
}

// 判断“是否能与用户交互”。
function getAttribute_disabled(node){
	var boolResult = true;
	if (window.navigator.userAgent.indexOf("MSIE")>0){
		boolResult = !node.isDisabled;
	}else{
		if (node.className.indexOf("disabled") != -1){
			boolResult = false;
		}
    }
	return boolResult;
}

// excel导入，返回input按钮的ID。
function Web_ExcelImport_JS(thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
	var ShowTableArr = qtp_getElementsByClassName(" x-window ext-ux-uploaddialog-dialog x-window-noborder x-window-plain x-resizable-pinned",null,"div");
	var ShowTable = returnVisibleNode(ShowTableArr);
	var ShowTableLen = ShowTable.length;
	var inputNodeId = ""; // 上传空间ID。
	// var boolResult = false;
	if (ShowTableLen == 1){
		var tableTr = ShowTable[0].getElementsByTagName("tr");
		if (CurrentStepArr[3] != ""){
			var tableTd = tableTr[0].getElementsByTagName("td");
			var tableTdLen = tableTd.length;
			if (CurrentStepArr[3].toString().toUpperCase() =="TRUE"){
				for(var i=0;i<tableTdLen;i++){
					if (get_innerText_OR_textContent(tableTd[i]) == "忽略界面错误"){
						if (!tableTd[i].firstChild.checked){
							tableTd[i].firstChild.click();
							break;
						}
					}
				}
			}else{
				for(var i=0;i<tableTdLen;i++){
					if (get_innerText_OR_textContent(tableTd[i]) == "忽略界面错误"){
						if (tableTd[i].firstChild.checked){
							tableTd[i].firstChild.click();
							break;
						}
					}
				}
			}
			if (CurrentStepArr.length >3){
				if (CurrentStepArr[4].toString().toUpperCase() =="TRUE"){
					for(var j=0;j<tableTdLen;j++){
						if (get_innerText_OR_textContent(tableTd[j]) == "是否触发字段联动"){
							if (!tableTd[j].firstChild.checked){
								tableTd[j].firstChild.click();
								break;
							}
						}
					}
				}else{
					for(var j=0;j<tableTdLen;j++){
						if (get_innerText_OR_textContent(tableTd[j]) == "是否触发字段联动"){
							if (tableTd[j].firstChild.checked){
								tableTd[j].firstChild.click();
								break;
							}
						}
					}
			}
			}
		}
		var inputNode = ShowTable[0].getElementsByTagName("button");
		if (inputNode.length>0){
			for(var k=0;k<inputNode.length;k++){
				if (get_innerText_OR_textContent(inputNode[k]) =="添加"){
					if (get_visibility_JS(inputNode[k])){
						inputNodeId = getNodeId(inputNode[k]);
						break;
					}
				}
			}
		}
	}else{
		// 存在多个窗口。
	}
	return inputNodeId;
}

// 等待节点显示。
function waitNodeVisible(nodeClassName,parentN,nodeTagName){
	var nodeArr = qtp_getElementsByClassName(nodeClassName,parentN,nodeTagName);
	var visibleNodeArr = returnVisibleNode(nodeArr);
	if (visibleNodeArr.length == 1){
		return true;
	}else{
		return false;
	}
}

//// 验证:控件 GRID控件可见性。
// function Web_FindControlInTable_JS(thisCurrentStep){
	// var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	// var JbodyAllTable = GetAllObject_JS("JbodyAllTable","grid");
	// var tableNameTr = JbodyAllTable(0).getElementsByTagName("tr");
	// var tableNameTrLen = tableNameTr.length;
	// for(var i=0;i<tableNameTrLen;i++){
		// var tableNameTd = tableNameTr[i].getElementsByTagName("td");
		// var tableNameTdLen = tableNameTd.length;
		// for(var j=0;j<tableNameTdLen;j++){
			// var tableNameText = get_innerText_OR_textContent(tableNameTd[j]);
			// if (tableNameText = CurrentStep){
				// i = tableNameTrLen;
				// break;
			// }
		// }
	// }
	
// }

// 验证:控件 TAB:可见性
function Web_TABVisible_JS(UIPanelKey,thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var nodeId = UIPanelKey + MAP_BillContext.getBillViewer().viewID;
	var UIPanelNode = document.getElementById(nodeId);
	var nodeUl = UIPanelNode.getElementsByTagName("ul");
	var nodeLi = nodeUl[0].getElementsByTagName("li");
	var nodeLiLen = nodeLi.length;
	var boolResult = false;
	var TABText = "";
	for(var i=0;i<nodeLiLen;i++){
		TABText = get_innerText_OR_textContent(nodeLi[i]);
		if (TABText == CurrentStep){
			boolResult = true;
		}
	}
	return boolResult;
}

// 验证:字典:字典KEY
// function Web_VerDictionaryKey_JS(nodeId){
	// var objNode = document.getElementById(nodeId);
	// var arrText = get_outerText(objNode);
	// return arrText.join(":");
// }

// 获取outerText属性,以String.fromCharCode(10)分割成数组。
function get_outerText(nodeId){
	var objNode = document.getElementById(nodeId);
	var nodeTable = objNode.getElementsByTagName("table");
	if (nodeTable.length > 1){
		var nodeText = nodeTable[1].outerText;
	}else{
		var nodeText = objNode.outerText;
	}
	var arrText = nodeText.split(String.fromCharCode(10));
	var arrTextLen = arrText.length;
	if (arrText[arrTextLen -1] == "" || arrText[arrTextLen -1] == " " || arrText[arrTextLen -1] == String.fromCharCode(160)){
		arrText.splice(arrTextLen -1,1);
	}
	arrTextLen = arrText.length;
	for(var i=0;i<arrTextLen;i++){
		if (arrText[i].indexOf(String.fromCharCode(160)) != -1){
			arrText[i] = ConvertStrBlankSpace(arrText[i],160);
		}
	}
	return arrText.join(":");
}

// 验证:多选字典。
function Web_VerificationDictionary_JS(CurrentStepStr,nodeListId){
	var boolResult;
	var boolReturn = true;
	var nodeId = "";
	var getNode = document.getElementById(nodeListId);
	var getNodeLi = getNode.getElementsByTagName("li");
	var getNodeA = getNodeLi[0].getElementsByTagName("a");
	var getNodeAlen = getNodeA.length;
	var thisCurrentStepStr = decodeAscwCurrentStep(CurrentStepStr);
	var CurrentStep = thisCurrentStepStr.split(":");
	var CurrentStepLen = CurrentStep.length;
	for(var i=0;i<CurrentStepLen -2;i++){
		boolResult = false;
		if (CurrentStep[i].indexOf(String.fromCharCode(160)) != -1){
			CurrentStep[i] = ConvertStrBlankSpace(CurrentStep[i],160);
		}
		for(var j=0;j<getNodeAlen;j++){
			var getNodeAText = get_innerText_OR_textContent(getNodeA[j]);
			var getNodeATextArr = getNodeAText.split(" ");
			if (getNodeATextArr[0]==CurrentStep[i].toUpperCase()){
				// var WebCheckDom = getNodeA[j].previousSibling;
				// if (WebCheckDom.checked.toString().toUpperCase() == CurrentStep[CurrentStepLen-2].toUpperCase()){
					boolResult = true;
					nodeId = getNodeId(getNodeA[j]);
					break;
				// }else{
					// CurrentStep[CurrentStepLen-2] = WebCheckDom.checked.toString();
				// }
			}
		}
		if (boolResult.toString().toUpperCase() != CurrentStep[CurrentStepLen-2].toUpperCase()){
			boolReturn = false;
			CurrentStep[CurrentStepLen-2] = boolReturn;
		}
	}
	return boolReturn + ":" + CurrentStep.join(" ") + ":" + nodeId;
}


// 把每个字典项以" "分割，返回全部

// 平台方法获取HTML ID 后部字符串，结合XML读取BillMetaUIPanel的KEY，拼接成ID，直接定位主界面。
// 然后在主界面操作。
// MAP_BillContext.getBillViewer().viewID

//读取单选框的checked属性
function Web_Checkbox_checked(ReadPanelKeys){
	var CheckBool = false;
	var BoxId = "HC_" + ReadPanelKeys + MAP_BillContext.getBillViewer().viewID;
	var CheckboxId = document.getElementById(BoxId);
	if(CheckboxId.checked){
		CheckBool = true;
	}
	return CheckBool;
}

// 选择凭证
function Web_SelectPingZheng_JS(thisCurrentStep,isFinal){
	var nodeId = "";
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var nodeTr = Jbody.getElementsByTagName("tr");
	var nodeTrLen = nodeTr.length;
	var nodeSpan;
	var nodeSpanLen;
	var nodeSpanText = "";
	for(var i=0;i<nodeTrLen;i++){
		nodeSpan = nodeTr[i].getElementsByTagName("span");
		nodeSpanLen = nodeSpan.length;
		for(var j=0;j<nodeSpanLen;j++){
			nodeSpanText = get_innerText_OR_textContent(nodeSpan[j]);
			if (nodeSpanText == CurrentStep){
				if (nodeSpan[j].previousSibling.nodeName == "INPUT"){
					var objNodeImg = nodeSpan[j].previousSibling.previousSibling.previousSibling;
				}else{
					var objNodeImg = nodeSpan[j].previousSibling.previousSibling;
				}
				if (isFinal.toString().toUpperCase() == "FALSE"){
					if (objNodeImg.className.indexOf("elbow-plus") != -1 || objNodeImg.className.indexOf("elbow-end-plus") != -1){
						// 返回加号节点ID
						nodeId = getNodeId(objNodeImg);
					}
				}else{
					nodeId = getNodeId(nodeSpan[j]);
				}

			}
		}
	}
	return nodeId;
}


//定位Table
function Web_LocateTable_JS(){
	eval(GetAllObject_JS("JbodyText","grid"))
	Jbody
	var oTr = Jbody.getElementsByTagName("tr");
	var oTrLen = oTr.length;
	var TdText;
	var TdTextAll = get_innerText_OR_textContent(oTr[0].getElementsByTagName("td")[0]);
	for(i=1;i<oTrLen;i++){
		var oTd = oTr[i].getElementsByTagName("td");
		TdText = get_innerText_OR_textContent(oTd[0]);
		TdTextAll = TdTextAll + ";" + TdText;
	}
	return oTrLen + ":" + TdTextAll;
}

//获得表格控件列名
function Web_getTableName_JS(ClickCol){
	var NodeId = "grid_" + InDataTableNum + MAP_BillContext.getBillViewer().viewID;
	return Ext.getCmp(NodeId).colModel.config[ClickCol-1].__key;
}

// 单选框操作。
function Web_RadioGroup_JS(RadioGroupName,thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
	var radioGroup = document.getElementsByName(RadioGroupName);
	var radioGroupLen = radioGroup.length;
	for(var i=0;i<radioGroupLen;i++){
		if (radioGroup[i].id == "HC_" + CurrentStepArr[1].toLowerCase() + MAP_BillContext.getBillViewer().viewID){
			radioGroup[i].click();
			break;
		}else if(get_innerText_OR_textContent(radioGroup[i].parentNode) == CurrentStepArr[1]){
			radioGroup[i].click();
			break;
		}
	}
}

//录入复选框
function Web_InDataObjectCheckBox_JS(CheckBoxId,thisCurrentStep){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CheckBox = document.getElementById(CheckBoxId);
	if ((!CheckBox.checked && CurrentStep =="1")||(CheckBox.checked && CurrentStep =="0")){
		CheckBox.click();
	}else if((!CheckBox.checked && CurrentStep.toString().toUpperCase() =="TRUE")||(CheckBox.checked && CurrentStep.toString().toUpperCase() =="FALSE")){
		CheckBox.click();
	}
}

// 把每个字典项以" "分割，返回全部
function Web_SelectDataPermissions_JS(thisCurrentStep,domId){
	var CurrentStep = decodeAscwCurrentStep(thisCurrentStep);
	var CurrentStepArr = CurrentStep.split(":");
	var CurrentStepLen = CurrentStepArr.length;
	var boolflag = false;
	var nodeChecked;
	var expItem = CurrentStepArr[2];
	if (qtp_regExpFind(expItem,"\\[.*\\]")){
		expItem = /\[([^\(]*)\]/.exec(CurrentStepArr[2])[1];
	}
	var nodeATextArr;
	var node = document.getElementById(domId);
	var nodeA = node.getElementsByTagName("a");
	var nodeALen = nodeA.length;
	for(var i=0;i<nodeALen;i++){
		var nodeAText = get_innerText_OR_textContent(nodeA[i]);
		if (nodeAText.indexOf(String.fromCharCode(160)) != -1){
			nodeAText = ConvertStrBlankSpace(nodeA[i],160);
		}
		nodeATextArr = nodeAText.split(" ");
		if (nodeATextArr[0] == expItem){
			nodeChecked = nodeA[i].previousSibling.checked;
			if (nodeChecked.toString().toUpperCase() != CurrentStepArr[CurrentStepLen-2].toString().toUpperCase()){
				nodeA[i].previousSibling.click();
			}
			break;
		}
	}
// /\[([^\(]*)\]/.exec("[sad]")[1]
}

/**
 * 获取分页button按钮的“可交互属性”，IE下isDisabled，FF下需要找到父节点table的className属性中判断是否存在disabled，存在代表不可交互，不存在代表可交互
 * IE下isDisabled，false代表可以交互，true代表不可交互
 */
function Web_page_JS(pageButtonId){
	var boolPage;
	var pageButton = document.getElementById(pageButtonId);
	if (window.navigator.userAgent.indexOf("MSIE")>0){
		var boolPage = pageButton.isDisabled;
    }else if(window.navigator.userAgent.indexOf("Firefox")>0){
    	var pageButtonTable = pageButton.parentNode.parentNode.parentNode.parentNode.parentNode;
    	var pageTableClassName = pageButtonTable.className;
    	if (pageTableClassName.indexOf("disabled") == -1 ){
    		boolPage = false;
    	}else{
    		boolPage = true;
    	}
	}else if(window.navigator.userAgent.indexOf("Chrome")>0){
		
    }
	return boolPage;
}

// 获取表头控件的isContentEditable属性，判断可编辑性。
function get_isContentEditable_JS(nodeId){
	return document.getElementById(nodeId).isContentEditable;
}

/**
 * 在表格中查找input框,找到返回true
 */
function doseFoundTableInput(tableId){
	var doseFound = false;
	var tableNode = document.getElementById(tableId);
	var tableInputs = tableNode.getElementsByTagName('input');
	var tableInputLen = tableInputs.length;
	for(var i=0;i<tableInputLen;i++){
		if (tableInputs[i].className.indexOf('focus') != -1){
			doseFound = true + ":" + getNodeId(tableInputs[i]);
			break;
		}else{
			if (get_visibility_JS(tableInputs[i].parentNode)){
				doseFound = true + ":" + getNodeId(tableInputs[i]);
				break;
			}
		}
	}
	return doseFound;
}

// 把每个字典项以" "分割，返回全部

// 平台方法获取HTML ID 后部字符串，结合XML读取BillMetaUIPanel的KEY，拼接成ID，直接定位主界面。
// 然后在主界面操作。
// MAP_BillContext.getBillViewer().viewID

//读取单选框的checked属性






















