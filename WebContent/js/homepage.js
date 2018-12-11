var xmlHttp;
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
// 修改密码
function ChangePwd(){
	if($("#old_pwd").val()==''){
		alerm('请输入旧密码');
		$("#old_pwd").focus();
	}
	else if($("#new_pwd").val()==''){
		alerm('请输入旧密码');
		$("#new_pwd").focus();
	}
	else if($("#pwd_again").val()==''){
		alerm('请输入旧密码');
		$("#pwd_again").focus();
	}
	else if($("#pwd_again").val()!=$("#new_pwd").val()){
		alerm('新密码两次输入不一致');
		$("#pwd_again").focus();
	}
	else{
		usrinfo={};
		usrinfo.usrname=sessionStorage.customerId;
		usrinfo.fullname="";
		usrinfo.passwd=$("#new_pwd").val();
		usrinfo.role="";
		usrinfo.email="";
		var body=JSON.stringify(usrinfo);
		var url="User/Update"
		TMS_api(url,"POST",body,function a(){
			if (xmlHttp.readyState==4 && xmlHttp.status==200){
				var resp = JSON.parse(xmlHttp.responseText);
				if(resp.code==200){
					CloseForm('#form_ChangePwd','#overlay');
					alerm("密码更新成功！");
				}
				else alerm(resp.message);
			}
		});
	}
}

$(document).ready(function(){ 
	$("#menu").css('height',($(document).height()-46));
	$("#account").text(sessionStorage.usrfullname);

	if(typeof(sessionStorage.currpage)=='undefined')sessionStorage.currpage="media";
	$("#main").attr("src",sessionStorage.currpage+".html");
	$("#"+sessionStorage.currpage).css("background-color","#F3F4F9");
	$("#"+sessionStorage.currpage).parent().prev().children().eq(0).show();
	$("#subtitle").text($("#"+sessionStorage.currpage).text());
	//点击退出按钮
	$("#butt_exit").click(function b(){	
		window.open("login.html",'_self');
	});
	// 修改密码
	$("#Tochangepwd").click(function b(){	
		$("#form_ChangePwd input").val("");
		open_form("#form_ChangePwd","#overlay");
	});
	//点击标题返回主页
	$("#Title").click(function b(){
		$("#main").attr("src",sessionStorage.currpage+".html");

	});

	//选择模块跳转
	$(".butt_menu").click(function b(e){
		$("#menu img").hide();
		$("#menu button").css("background-color","transparent");
		sessionStorage.currpage=$(e.target).attr("id");
		$(e.target).css("background-color","#F3F4F9");
		$(e.target).parent().prev().children().eq(0).show();
		$("#subtitle").text($(e.target).text());
		$("#main").attr("src",sessionStorage.currpage+".html");
	});
});