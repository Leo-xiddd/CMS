var xmlHttp;
var page_num;
var page_sum;
var tr_selected;
function TMS_api(url,med,dats,cfunc){
	var hostpath=getHostUrl();
	try{
		url=hostpath+url;
		xmlHttp = new XMLHttpRequest();
		xmlHttp.onreadystatechange=cfunc;		
		xmlHttp.open(med,url,true);
		if(med=="GET")xmlHttp.send();
		else xmlHttp.send(dats);	
	}catch(e){
		alerm(e);
	}	
}
// 获取列表
function LoadList(filter){
	var item_ppnum=12;
	var url="CMS/Listmedia?"+filter+"&page_count="+item_ppnum+"&page_num="+page_num;
	TMS_api(url,"GET","",function a(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var resp = JSON.parse(xmlHttp.responseText);
			if(resp.code==200){	
				var items=resp.medialist;
				$("#tbody_medialist tr").remove();
				for(var i=0;i<items.length;i++){
					var line='<tr>';
					line=line+'<td>'+items[i].id+'</td>';
					line=line+'<td>'+items[i].media+'</td>';
					line=line+'<td>'+items[i].interface_label+'</td>';
					line=line+'<td>'+items[i].note+'</td>';
					line=line+'</tr>';
					
					$("#tbody_medialist").append(line);
				}
				var item_sum=parseInt(resp.total_num);
				page_sum=Math.ceil(item_sum/item_ppnum);
				$("#page_num").text(page_sum);
				$("#curr_page").text(page_num);
				if(page_num==1){
					$("#Fir_page").attr("disabled",true);
					$("#Pre_page").attr("disabled",true);
				}
				else{
					$("#Fir_page").attr("disabled",false);
					$("#Pre_page").attr("disabled",false);
				}
				if(page_num==page_sum){
					$("#Next_page").attr("disabled",true);
					$("#Las_page").attr("disabled",true);
				}
				else{
					$("#Next_page").attr("disabled",false);
					$("#Las_page").attr("disabled",false);
				}

				// $("#last_sync_time").text(resp.last_sync_time);
			}
			else alerm(resp.message);
		}
	});
}
// 查找
function filt(){
	var fts="filter=";
	var filter=$("#filter").val();
	var phase=$("#phase option:selected").attr("value");
	if(filter!="")fts="filter="+phase+" like '*"+filter+"*'";
	LoadUserList(fts);
}

// 翻页
function Topage(num){
	if(page_num!=num){
		if(num==0)page_num=page_sum;
		else page_num=num;
		filt();
	}
}
function Nextpage(tag){
	if(tag=="+" && page_num!=page_sum) page_num++;
	else if(tag=="-" && page_num!=1) page_num--;		
	filt();	
}
// *********************主程序**********************
$(document).ready(function(){ 
	var old_bgcolor="";
	tr_selected=null;	
	page_sum=0;
	page_num=1;
	LoadList("filter=");
	//添加媒体
	$("#AddMedia").click(function b(){
		$("#usr_account").val("");	
		$("#note").val("");
		$("#usr_acc").val("");
		open_form("#form_MediaInfo","#overlay");
		$("#usr_account").focus();
	});
	
	//双击表格 - 打开用户编辑弹层
	$("#tbody_medialist").dblclick(function b(e){
		var tr=$(e.target).parent();
		$("#usr_account").val(tr.children().eq(1).text());	
		$("#usr_acc").val(tr.children().eq(2).text());					
		$("#note").val(tr.children().eq(3).text());
		open_form("#form_MediaInfo","#overlay");	
	});
	//删除媒体
	$("#DelMedia").click(function b(){
		if(tr_selected==null)alerm("请先选择要删除的媒体!");
		else {
			tr_selected.remove();
		}
	});
	//选择或取消媒体选择
	$("#tbody_medialist").click(function b(e){
		var tr=$(e.target).parent();
		if(tr_selected!=null)tr_selected.children().css("background-color","transparent");
		tr_selected=tr;
		old_bgcolor=tr.children().eq(0).css("background-color");
		tr_selected.children().css("background-color","#FCF4ED");	
	});

	//弹层移动
	$('#title_MediaInfo').mousedown(function (event) { 
		var isMove = true; 
		var abs_x = event.pageX - $('#form_MediaInfo').offset().left; 
		var abs_y = event.pageY - $('#form_MediaInfo').offset().top; 
		$(document).mousemove(function (event) { 
			if (isMove) { 
				var obj = $('#form_MediaInfo'); 
				var rel_left=event.pageX - abs_x;
				if(rel_left<0)rel_left=0;
				if(rel_left>1080)rel_left=1080;
				var rel_top=event.pageY - abs_y;
				if(rel_top<0)rel_top=0;
				if(rel_top>740)rel_top=740;
				obj.css({'left':rel_left, 'top':rel_top}); 
			} 
		}).mouseup( function () { 
			isMove = false; 
		}); 
	});
	
	$('#form_alert_title').mousedown(function (event) { 
		var isMove = true; 
		var abs_x = event.pageX - $('#form_alert').offset().left; 
		var abs_y = event.pageY - $('#form_alert').offset().top; 
		$(document).mousemove(function (event) { 
			if (isMove) { 
				var obj = $('#form_alert'); 
				var rel_left=event.pageX - abs_x;
				if(rel_left<0)rel_left=0;
				if(rel_left>1080)rel_left=1080;
				var rel_top=event.pageY - abs_y;
				if(rel_top<0)rel_top=0;
				if(rel_top>740)rel_top=740;
				obj.css({'left':rel_left, 'top':rel_top}); 
			} 
		}).mouseup( function () { 
			isMove = false; 
		}); 
	});
});