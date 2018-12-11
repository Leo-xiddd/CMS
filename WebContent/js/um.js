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
function LoadUserList(filter){
	var item_ppnum=12;
	var url="User/List?"+filter+"&page_count="+item_ppnum+"&page_num="+page_num;;
	TMS_api(url,"GET","",function a(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var resp = JSON.parse(xmlHttp.responseText);
			if(resp.code==200){	
				var usrlist=resp.userlist;
				$("#tbody_MT tr").remove();
				for(var i=0;i<usrlist.length;i++){
					var line='<tr>';
					line=line+'<td>'+usrlist[i].id+'</td>';
					line=line+'<td>'+usrlist[i].usrname+'</td>';
					line=line+'<td>'+usrlist[i].fullname+'</td>';
					line=line+'<td>'+usrlist[i].dept+'</td>';
					line=line+'<td>'+usrlist[i].role+'</td>';
					line=line+'<td>'+usrlist[i].email+'</td>';
					line=line+'<td>'+usrlist[i].mobile+'</td>';
					line=line+'<td>'+usrlist[i].type+'</td>';
					line=line+'</tr>';
					
					$("#tbody_MT").append(line);
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
// 保存用户信息
function save_userInfo(url){
	var usrinfo={};
	usrinfo.usrname=$("#usr_account").val();
	usrinfo.fullname=$("#usr_fullname").val();
	usrinfo.passwd=$("#usr_pwd").val();
	
	if(usrinfo.usrname==""){
		alerm("用户账号不能为空");
		$("#usr_account").focus();
	}
	else if(usrinfo.fullname==""){
		alerm("用户姓名不能为空");
		$("#usr_fullname").focus();
	}
	else if(usrinfo.passwd==""){
		alerm("用户密码不能为空");
		$("#usr_passwd").focus();
	}
	else{
		usrinfo.type=$("#usr_type").val();
		usrinfo.dept=$("#usr_dept").val();
		usrinfo.role=$("#usr_role").val();
		usrinfo.email=$("#usr_mail").val();
		usrinfo.mobile=$("#usr_mobile").val();
		var body=JSON.stringify(usrinfo);
		TMS_api(url,"POST",body,function a(){
			if (xmlHttp.readyState==4 && xmlHttp.status==200){
				var resp = JSON.parse(xmlHttp.responseText);
				if(resp.code==200){
					CloseForm('#form_UserInfo','#overlay');
					if(url.indexOf("Update")>-1)alerm("用户更新成功！");
					else alerm("用户添加成功！");					
					LoadUserList("filter=");
				}
				else alerm(resp.message);
			}
		});
	}		
}
// 查找用户
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
	var usr_opt="";
	page_sum=0;
	page_num=1;
	if(sessionStorage.role!="admin"){
		$("#Add").attr("disabled",true);
		$("#Del").attr("disabled",true);
	}
	// if(typeof(sessionStorage.customerId)=='undefined'){
	// 	var url="login.html";
	// 	window.open(encodeURI(url),'_parent');
	// }
	// //页面初始化
	// LoadUserList("filter=");

	//添加用户
	$("#Add").click(function b(){
		$("#usr_pwd").attr("disabled",false);
		$("#usr_account").attr("disabled",false);
		$("#form_UserInfo input").val("");
		$("#usr_pwd").parent().parent().show();
		open_form("#form_UserInfo","#overlay");
		$("#usr_account").focus();
		usr_opt="Add";
	});
	//弹层按钮 - 保存用户信息
	$("#UserInfo_save").click(function b(){	
		var url="User/"+usr_opt;
		save_userInfo(url);
	});
	//双击表格 - 打开用户编辑弹层
	$("#tbody_user").dblclick(function b(e){
		if(sessionStorage.role=="admin"){
			var tr=$(e.target).parent();
			$("#usr_account").val(tr.children().eq(1).text());						
			$("#fname").val(tr.children().eq(2).text());
			$("#role").val(tr.children().eq(3).text());
			$("#email").val(tr.children().eq(4).text());
			$("#usr_pwd").parent().parent().hide();
			open_form("#form_UserInfo","#overlay");
			usr_opt="Update";
		}		
	});
	//删除用户
	$("#Del").click(function b(){
		if(tr_selected==null)alerm("请先选择要删除的用户");
		else {
			var username=tr_selected.children().eq(1).text();
			var url="User/Delete?user="+username;
			TMS_api(url,"GET","",function a(){
				if (xmlHttp.readyState==4 && xmlHttp.status==200){
					var resp = JSON.parse(xmlHttp.responseText);
					if(resp.code==200){	
						LoadUserList("filter=");
					}
					else alerm(resp.message);
				}
			});
		}
	});
	
	//选择或取消用户选择
	$("#tbody_user").click(function b(e){
		var tr=$(e.target).parent();
		if(tr_selected!=null)tr_selected.children().css("background-color","transparent");
		tr_selected=tr;
		old_bgcolor=tr.children().eq(0).css("background-color");
		tr_selected.children().css("background-color","#FCF4ED");	
	});
	//弹层移动
	$('#title_UserInfo').mousedown(function (event) { 
		var isMove = true; 
		var abs_x = event.pageX - $('#form_UserInfo').offset().left; 
		var abs_y = event.pageY - $('#form_UserInfo').offset().top; 
		$(document).mousemove(function (event) { 
			if (isMove) { 
				var obj = $('#form_UserInfo'); 
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