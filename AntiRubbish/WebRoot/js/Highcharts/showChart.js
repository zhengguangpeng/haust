// JavaScript Document
function ajaxGetColumnData(url){
	var data;
	if(url!="someUrl.html"){
	$.ajax({
		type: "GET",
		url: url,
		//data: "name=中文-德文&location=Boston",
		success: function(data){
			if(data.success == "success"){
				return data;
			}else{
				
			}
		}
	});
	}
	
	data = {"success":"success",
	"categories":['04-16','04-17','04-18','04-19','04-20',
	'04-21','04-22','04-23','04-24','04-25','04-26','04-27'],
	"seriesData":[5.8, 4.6, 7.9, 8.1, 7.2, 5.1, 4.9, 3.6, 6.2, 5.4,6.5, 4.8]//柱状图
	//或者
	//"seriesData":[['工作中',50],['匹配中',45],['闲置中',5],['异常',5]]//饼图
  };
//	return data;
}

//生成图表方法
function newChart(url,renderTo,type,tooltipText,seriesName,categories,seriesData){
	var data,categories,seriesData;
	data = ajaxGetColumnData(url);//获取图表数据
	
	if(seriesData==null){
		categories = data.categories;
		seriesData = data.seriesData;
	}else{
		categories = categories;
		seriesData = seriesData;
	}
	
	chart = new Highcharts.Chart({
	chart: {
	renderTo: renderTo,
	type: type
	},
	title: {
	text: ''
	},
	subtitle: {
	text: ''
	},
	xAxis: {
	categories: categories
	},
	yAxis: {
	min: 0,
	title: {
	text: ''
	}
	},
	legend: {
	layout: 'vertical',
	backgroundColor: '#FFFFFF',
	align: 'left',
	verticalAlign: 'top',
	x: 250,
	y: 0,
	floating: true,
	shadow: true
	},
	tooltip: {
	formatter: function() {
	return ''+
	this.x +': '+ this.y + tooltipText;
	}
	},
	plotOptions: {
	column: {
	pointPadding: 0.2,
	borderWidth: 0
	}
	},
	series: [{
	name: seriesName,
	data: seriesData
	}]
	});
}

/**
 * 按时间请求总产能数据生成柱状图表
 * @param url 请求地址
 * @param dateType day:日；week:周；month:月；quarter：季度
 * @param datePeriod 周期，默认为14；
 */
function ajaxGetColumnDataTime(url,dateType,datePeriod){
	var dataTime;
	if(url!="someUrl.html"){
		$.ajax({
			type: "post",
			url: url,
			async:false,
			data: {dateType:dateType,datePeriod:datePeriod},
			success: function(data2){	
			//	if(data2.success == "success"){
					dataTime = data2;
				//	return dataTime;
			//	}else{
					
			//	}
			}
		});
	}else{
		switch(dateType){
				case "day":
				dataTime = {"success":"success",
				categories:['04-16','04-17','04-18','04-19','04-20','04-21','04-22'],
				seriesData:[{
					name: '全部',
					data: [
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:8,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:9,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}}
						],
					marker: {
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[3],
					fillColor: 'white'
					}
					},{
					name: '中文-英文',
					data: [3, 2, 1, 3, 4, 2, 6]
					}, {
					name: '中文-德文',
					data: [2, 3, 5, 7, 6, 1, 4]
					}, {
					name: '德文-中文',
					data: [4, 3, 3, 9, 0, 4, 2]
					}] 
					}
				break;
				case "week":
				dataTime = {"success":"success",
				categories:['12-01','12-02','12-03','12-04','12-05','12-06','12-07'],
				seriesData:[{
					name: '全部',
					data: [
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:8,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:9,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}}
						],
					marker: {
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[3],
					fillColor: 'white'
					}
					},{
					name: '中文-英文',
					data: [3, 2, 1, 3, 4, 2, 6]
					}, {
					name: '中文-德文',
					data: [2, 3, 5, 7, 6, 1, 4]
					}, {
					name: '德文-中文',
					data: [4, 3, 3, 9, 0, 4, 2]
					}] 
					}
				break;
				case "month":
				dataTime = {"success":"success",
				categories:['12年04月','12年05月','12年06月','12年07月','12年08月','12年09月','12年10月'],
				seriesData:[{
					name: '全部',
					data: [
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:8,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:9,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}}
						],
					marker: {
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[3],
					fillColor: 'white'
					}
					},{
					name: '中文-英文',
					data: [3, 2, 1, 3, 4, 2, 6]
					}, {
					name: '中文-德文',
					data: [2, 3, 5, 7, 6, 1, 4]
					}, {
					name: '德文-中文',
					data: [4, 3, 3, 9, 0, 4, 2]
					}] 
					}
				break;
				case "quarter":
				dataTime = {"success":"success",
				categories:['11年02季','11年03季','11年04季','12年01季','12年02季','12年03季','12年04季'],
				seriesData:[{
					name: '全部',
					data: [
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:8,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:9,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}}
						],
					marker: {
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[3],
					fillColor: 'white'
					}
					},{
					name: '中文-英文',
					data: [3, 2, 1, 3, 4, 2, 6]
					}, {
					name: '中文-德文',
					data: [2, 3, 5, 7, 6, 1, 4]
					}, {
					name: '德文-中文',
					data: [4, 3, 3, 9, 0, 4, 2]
					}] 
					}
				break;
				case "year":
				dataTime = {"success":"success",
				categories:['11年02季','11年03季','11年04季','12年01季','12年02季','12年03季','12年04季'],
				seriesData:[{
					name: '全部',
					data: [
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:8,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:7,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:6,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}},
						{y:9,drilldown: {
							data: [['中文-英文',2], ['中文-德文',4], ['德文-中文',1]]
						}}
						],
					marker: {
					lineWidth: 2,
					lineColor: Highcharts.getOptions().colors[3],
					fillColor: 'white'
					}
					},{
					name: '中文-英文',
					data: [3, 2, 1, 3, 4, 2, 6]
					}, {
					name: '中文-德文',
					data: [2, 3, 5, 7, 6, 1, 4]
					}, {
					name: '德文-中文',
					data: [4, 3, 3, 9, 0, 4, 2]
					}] 
					}
				break;
		}
	}
	return dataTime;
}

function newChartTime(url,dateType,datePeriod,renderTo,type,tooltipText,categories,seriesData){
	var data,categories,seriesData;
	var datetime = ajaxGetColumnDataTime(url,dateType,datePeriod);//获取图表数据
    //var datetime = $.parseJSON(data);
	if(seriesData==null){  
		categories = datetime.categories;//datetime[0];//$.parseJSON('{"data":"[05-22,04-23]"}').data;//$.parseJSON("{'data':'[05-22,04-23]'}").data;//data.categories;
		seriesData = datetime.seriesData;//$.parseJSON('{"data":"[0,0]"}').data;//data.seriesData;		
//alert(categories+"\n"+seriesData); 		
	}else{
		categories = categories;
		seriesData = seriesData;
	}
	chart = new Highcharts.Chart({
	chart: {
	renderTo: renderTo,
	type: type
	},
	title: {
	text: ''
	},
	subtitle: {
	text: ''
	},
	xAxis: {
	categories: categories
	},
	yAxis: {
	min: 0,
	title: {
	text: ''
	}
	},
	legend: {
		
	},
	tooltip: {
	formatter: function() {
		var myText = '';
		if(this.name){myText+=this.name}
		if(this.point.name){myText+=this.point.name}
		if(this.x){myText+=this.x}
		myText += this.y + tooltipText;
		return myText;
	}
	},
	exporting:{
		 enabled:false //用来设置是否显示‘打印’,'导出'等功能按钮，不设置时默认为显示
	},
	plotOptions: {
		line: {
			pointPadding: 0.2,
			borderWidth: 0,
			point: {
				events: {
					click: function() {
						var drilldown = this.drilldown;
						if (drilldown) { // drill down
							setChartPie("manage_work_report_pie",drilldown.data,tooltipText);
						} else { // restore
							return false;
						}
					}
				}
			}
		}
	},
	series: seriesData
	});
	setChartPie("manage_work_report_pie",seriesData[0].data[0].drilldown.data,"个");
}

//改变饼图
function setChartPie(renderTo,data,tooltipText){	
	chart = new Highcharts.Chart({
	chart: {
		renderTo: renderTo,
		plotBackgroundColor: null,
		plotBorderWidth: null,
		plotShadow: false
	},
	title: {
		text: ''
	},
	tooltip: {
		formatter: function() {
			return '<b>'+ this.point.name+this.y+tooltipText +'</b>: '+ Math.round(this.percentage) +' %';
		}
	},
	exporting:{
		 enabled:false //用来设置是否显示‘打印’,'导出'等功能按钮，不设置时默认为显示
	},
	plotOptions: {
	},
	series: [{
		type: 'pie',
		name: 'Browser share',
		data: data
	}]
});
}