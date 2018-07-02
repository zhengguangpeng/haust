// JavaScript Document
$(".leftNav ul").die().live("click",function(){
	$(".leftNav").find("div.secNav").hide();
	$(".leftNav").find("a.aBolder").removeClass("aBolder");
	//$(this).find("div.secNav a:eq(0)").addClass("active");
	$(this).find("div.secNav").show();
	$(this).find("a:eq(0)").addClass("aBolder");
	$(this).addClass("slided");
});
$(".secNav a").die().live("click",function(){
	$(this).parents(".secNav").find("a").removeClass("active");
	$(this).addClass("active");
});

$(".pageSelect").die().live("click",function(){
	$(this).find(".selects").slideToggle();
});

/**
 * 弹出层方法开始
 */
var topDocument = document;

//创建背景层，ifream层及弹出层
//获取滚动条的高度
function getPageScroll(){
    var yScroll;
    if (self.pageYOffset) {
        yScroll = self.pageYOffset;
    } else if (topDocument.documentElement && topDocument.documentElement.scrollTop){     // Explorer 6 Strict
        yScroll = topDocument.documentElement.scrollTop;
    } else if (topDocument.body) {// all other Explorers
        yScroll = topDocument.body.scrollTop;
    }
    arrayPageScroll = new Array('',yScroll)
    return arrayPageScroll;
}
//获取页面实际大小
function getPageSize(){
    var xScroll, yScroll;
    if (window.innerHeight && window.scrollMaxY) {
        xScroll = topDocument.body.scrollWidth;
        yScroll = window.innerHeight + window.scrollMaxY;
    } else if (topDocument.body.scrollHeight > topDocument.body.offsetHeight){ // all but Explorer Mac
        xScroll = topDocument.body.scrollWidth;
        yScroll = topDocument.body.scrollHeight;
    } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
        xScroll = topDocument.body.offsetWidth;
        yScroll = topDocument.body.offsetHeight;
    }
    var windowWidth, windowHeight;
    if (self.innerHeight) {    // all except Explorer
        windowWidth = self.innerWidth;
        windowHeight = self.innerHeight;
    } else if (topDocument.documentElement && topDocument.documentElement.clientHeight) { // Explorer 6 Strict Mode
        windowWidth = topDocument.documentElement.clientWidth;
        windowHeight = topDocument.documentElement.clientHeight;
    } else if (topDocument.body) { // other Explorers
        windowWidth = topDocument.body.clientWidth;
        windowHeight = topDocument.body.clientHeight;
    }
    // for small pages with total height less then height of the viewport
    if(yScroll < windowHeight){
        pageHeight = windowHeight;
    } else {
        pageHeight = yScroll;
    }
    // for small pages with total width less then width of the viewport
    if(xScroll < windowWidth){
        pageWidth = windowWidth;
    } else {
        pageWidth = xScroll;
    }
    arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight)
    return arrayPageSize;
}

/**
 * 创建背景层
 */
function createBodyBg(mybody){
	if($("#bodybg").length>0){
		$("#bodybg").show();
	}else{
		var arrayPageSize = getPageSize();
	    var arrayPageScroll = getPageScroll();
	    //创建半透明背景
	    var bodyBack = topDocument.createElement("div");
	    bodyBack.setAttribute("id","bodybg");
	    bodyBack.style.position = "absolute";
		bodyBack.style.background = "#000";
		bodyBack.style.zIndex = "1002";
		bodyBack.style.top = "0";
		bodyBack.style.left = "0";
		bodyBack.style.width = "100%" || ((arrayPageSize[0]- 22) + 'px');
	    bodyBack.style.height = ((arrayPageSize[1])+ 'px');
	    bodyBack.style.filter = "alpha(opacity=20)";
	    bodyBack.style.opacity = 0.2;
	    //创建iframe
	    var frameBody = topDocument.createElement("iframe");
	    frameBody.setAttribute("id","frameBody");
	    frameBody.style.position = "absolute";
		frameBody.style.zIndex = "1001";
		frameBody.style.top = "0";
		frameBody.style.left = "0";
		frameBody.style.width = ((arrayPageSize[0]- 22) + 'px');
	    frameBody.style.height = ((arrayPageSize[1]-10)+ 'px');
	    frameBody.style.filter = "alpha(opacity=0)";
	    frameBody.style.opacity = 0;
		//手工插入到目标元素之后
	    var mybody1 = mybody || "body";
	    mybody = $(mybody1);    
	    mybody.append(frameBody);
	    mybody.append(bodyBack);
	}
}

/**
 * 创建空弹出层
 *popId 自定义pop的ID
 *title 自定义pop的title,空则为默认值“弹出层”
 *width；height  默认值分别为400；300
 *closeBtnId true:表示要使用自定义关闭按钮，需要在自定义JS里引用关闭函数；false或者不填表示使用默认关闭按钮
 */
function createPopDiv(popId,title,width,height,closeBtnId,move,mybody){
	if($("#"+popId).length>0){
		$("#"+popId).show();
	}else{
		//创建DIV
		var popTitle = title || "弹出层";
		var popWidth = width || 400;
		var popHeight = height || 300;
		var moveHtml = "";
		var arrayPageSize = getPageSize();
		var arrayPageScroll = getPageScroll();
	    var popupDiv = topDocument.createElement("div");
	    var mybody1 = mybody || "body";
	    mybody = $(mybody1);
	    //给ID与样式
	    popupDiv.setAttribute("id",popId);
		popupDiv.className = "popBgClass";
	    popupDiv.style.position = "absolute";
	    popupDiv.style.zIndex = "1003";
	    popupDiv.style.width = (popWidth + 'px');
	    popupDiv.style.height = (popHeight + 'px');
		//弹出层在页面中垂直水平居中
	    popupDiv.style.top = (arrayPageScroll[1] + ((arrayPageSize[3] - popHeight) / 2 -20) + 'px');
	    popupDiv.style.left = (((arrayPageSize[0] - 20 - popWidth) / 2)  + 'px');
		//如果允许拖动
		if(move){moveHtml = ' style="cursor:move;" onmousedown="mousedownPop(arguments[0])" '}
		popupDiv.innerHTML+='<div class="pr"><div class="popTop" '+moveHtml+'>'+popTitle+'</div><input type="button" class="btn_pop_daochu" /><input type="button" class="btn_pop_close" />'
				+'<div id="popCount"><div class="popLoadClass">正在努力加载中...</div></div></div>';
	    mybody.append(popupDiv);
		//如果没有自定义关闭按钮
		//if(!closeBtnId){$(".btn_pop_close").remove();}
		//if(!title){$(".popTop").remove();}
	}
}

//拖动函数
function mousedownPop(e)
{
    var obj = $(".popBgClass")[0];
    var e = window.event ? window.event : e;
    obj.startX = e.clientX - obj.offsetLeft;
    obj.startY = e.clientY - obj.offsetTop;
    topDocument.onmousemove = mousemovePop;
    var temp = topDocument.attachEvent ? topDocument.attachEvent("onmouseup",mouseup) : topDocument.addEventListener("mouseup",mouseup,"");
}
function mousemovePop(e)
{
	var obj = $(".popBgClass")[0];
	var e = window.event ? window.event : e;
	with(obj.style)
	{
		left = e.clientX - obj.startX + "px";
		top = e.clientY - obj.startY + "px";
	}
}
function mouseup(e)
{
	topDocument.onmousemove = "";
	var temp = topDocument.detachEvent ? topDocument.detachEvent("onmouseup",mouseup) : topDocument.addEventListener("mouseup",mouseup,"");
}
//END拖动函数

/**
 * 关闭窗口和弹出层
 */
function hideDivPop(noRemove){
	$(".popBgClass").animate({
    	opacity: 0,
        top: '-=20'
    },400,function(){
    	if(noRemove){
    		$(this).hide();
    		$(topDocument).find("#bodybg,#frameBody").hide();
    	}else{
	    	$(this).remove();
	    	$(topDocument).find("#bodybg,#frameBody").remove();
    	}
    });
	$(".moreInfo").attr("style","display: none;");
	$(".viewMoreBtn").text("点击展开").removeClass("active");
}

$(".btn_pop_close").die().live("click",function(){
	hideDivPop();
});

/**
 * 弹出层方法结束
*/

$("#openPop").die().live("click",function(){
	var popCountHtml = $("#popCountHtml").html();
	createBodyBg();
	createPopDiv("loadingPopId","IP:192.168.1.1","600","520",true,true,"");
	$("#popCount").html(popCountHtml);
});
$(".viewMoreBtn").die().live("click",function(){
	if($(this).hasClass("active")){
		$(".moreInfo").fadeOut();
		$(this).text("点击展开").removeClass("active");
	}else{
		$(".moreInfo").fadeIn();
		$(this).text("点击收起").addClass("active");
	}
});

//createBodyBg();
//createPopDiv("loadingPopId","&nbsp","300","127",true,true,"");

var addDelHtml ='<span class="del"></span>';
$(".tagsDiv li").die().live("mouseover",function(){
	var delObj = $(this).find(".del");
	if(delObj.length>0){
		delObj.show();
	}else{
		$(this).append(addDelHtml);
	}
}).live("mouseout",function(){
	$(this).find(".del").hide();
});
$(".tagsDiv li .del").die().live("click",function(){
	$(this).parents("li").remove();
});