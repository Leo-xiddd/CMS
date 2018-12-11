var xmlHttp;
var page_num;
var page_sum;
var tr_selected;
var mediaName;
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
	var url="CMS/Listcontent?"+filter+"&page_count="+item_ppnum+"&page_num="+page_num+"&media="+mediaName;
	TMS_api(url,"GET","",function a(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var resp = JSON.parse(xmlHttp.responseText);
			if(resp.code==200){	
				var items=resp.loglist;
				$("#tbody_medialist tr").remove();
				for(var i=0;i<items.length;i++){
					var line='<tr>';
					line=line+'<td>'+items[i].position+'</td>';
					line=line+'<td>'+items[i].size+'</td>';
					line=line+'<td>'+items[i].type+'</td>';					
					line=line+'<td>'+items[i].policy+'</td>';
					line=line+'<td>'+items[i].status+'</td>';
					line=line+'<td>'+items[i].url+'</td>';
					line=line+'<td><a href="javascript:void(0)" onclick="reviewpic('+items[i].name+','+items[i].size_h+','+items[i].size_w+')">预览</a></td>';
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
function reviewpic(picname,size_h,size_w){
	open_form("#pic_review","#overlay");
	$("#picv").attr("src",picname);
	$("#picv").attr({"width":(size_w+"px"),"height":(size_h+"px")});
}

function addFile(){
	var sourceId='picfile_select';
	var url;
	if (navigator.userAgent.indexOf("MSIE")>=1) { // IE 
		url = document.getElementById(sourceId).value; 
	} else if(navigator.userAgent.indexOf("Firefox")>0) { // Firefox 
		url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0)); 
	} else if(navigator.userAgent.indexOf("Chrome")>0) { // Chrome 
		url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0)); 
	} 
	$("#pic_sm_pic").attr("src",url);
}
// 查找用户
function filt(){
	var fts="filter=";
	var filter=$("#filter").val();
	var phase=$("#phase option:selected").attr("value");
	if(filter!="")fts="filter="+phase+" like '*"+filter+"*'";
	LoadList(fts);
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
	mediaName=$("#meidas").val();
	// LoadList("filter=");
	// 变更媒体
	$("#meidas").change(function(){
		mediaName=$("#meidas").val();
		$("#filter").val("");
		$("#phase").val("位置");
		LoadList("filter=");
	});

	$("#closepic").click(function b(){
		CloseForm('#pic_review','#overlay')
	});
	//编辑内容
	$("#AddCont").click(function b(){
		if(tr_selected==null)alerm("请先选择要编辑内容的位置");
		else {
			var tr=tr_selected;
			$("#posi").text(tr.children().eq(0).text());	
			$("#wh").text(tr.children().eq(1).text());				
			$("#policy").val(tr.children().eq(3).text());
			var policy=tr.children().eq(3).text();
			if(policy=='轮播')$("#lunbo").show();
			else $("#lunbo").hide();
			var tt=tr.children().eq(2).text();
			$("#type").text(tt);
			$("#linkpage").text(tr.children().eq(5).text());
			if(tt.indexOf(",")>-1)tt=tt.substr(0,tt.indexOf(","));
			else if(tt.indexOf("，")>-1)tt=tt.substr(0,tt.indexOf("，"));
			$("#tyy").val(tt);

			if(tt=='文本'){
				$(".img").hide();
				$(".media").hide();
				$(".hlink").hide();
				$(".txt").show();
			}
			else if(tt=='图片'){
				$(".txt").hide();
				$(".media").hide();
				$(".img").show();
				$(".hlink").show();
				$(".img img").attr("src",tr.children().eq(6).children().eq(0).attr("src"));
				$(".img img").attr("width",tr.children().eq(6).children().eq(0).attr("width"));
				$(".img img").attr("height",tr.children().eq(6).children().eq(0).attr("height"));
			}
			else if(tt=='视频'){
				$(".img").hide();
				$(".txt").hide();
				$(".media").show();
				$(".hlink").show();
			}
			open_form("#form_Content","#overlay");	
		}
	});
	
	$("#tyy").change(function b(){
		var tt=$("#tyy").val();
		if(tt=='文本'){
			$(".img").hide();
			$(".media").hide();
			$(".hlink").hide();
			$(".txt").show();
		}
		else if(tt=='图片'){
			$(".txt").hide();
			$(".media").hide();
			$(".img").show();
			$(".hlink").show();
		}
		else if(tt=='视频'){
			$(".img").hide();
			$(".txt").hide();
			$(".media").show();
			$(".hlink").show();
		}
	});
	$("#policy").change(function b(){
		var policy=$("#policy").val();
		if(policy=='轮播')$("#lunbo").show();
		else $("#lunbo").hide();
	});
	//发布内容
	$("#ReleaseCont").click(function b(){
		if(tr_selected==null)alerm("请先选择要发布内容的位置");
		else {
			$("#warn_mess").html("确认要发布该内容吗？发布之后前端将<br>获取到新发布的内容。");
			$("#warn_mess").css({"line-height":"25px","text-align":"left"});
			open_form('#form_confirm','#overlay');
			tr_selected.children().eq(4).text("已发布");
		}
	});
	// 选择文件
	$("#picfile_selector").click(function b(){
		$("#picfile_selector").next().click();
	});
	//选择或取消内容选择
	$("#tbody_contlist").click(function b(e){
		var tr=$(e.target).parent();
		if(tr_selected!=null)tr_selected.children().css("background-color","transparent");
		tr_selected=tr;
		old_bgcolor=tr.children().eq(0).css("background-color");
		tr_selected.children().css("background-color","#FCF4ED");	
	});

	//弹层移动
	$('#title_Content').mousedown(function (event) { 
		var isMove = true; 
		var abs_x = event.pageX - $('#form_Content').offset().left; 
		var abs_y = event.pageY - $('#form_Content').offset().top; 
		$(document).mousemove(function (event) { 
			if (isMove) { 
				var obj = $('#form_Content'); 
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