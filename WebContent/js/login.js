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
		alert(e);
	}	
}
$(document).ready(function(){
	$("body").css({"height":getWindowInnerHeight,"width":getWindowInnerWidth});
	var a=$("body").css("height");	
	var b=parseInt(a.replace("px",""))*0.31+"px";
	$("#alertmess").hide();
	// $("#alert_mess").hide();
	$("#loginbox").css("margin-top",b);

	$("#loginuser").focus(function(){
		$("#loginpwd").parent().css("background-color","#FFFFFF");
		$("#loginuser").parent().css("background-color","#F2F6F9");
	});
	$("#loginpwd").focus(function(){
		$("#loginpwd").parent().css("background-color","#F2F6F9");
		$("#loginuser").parent().css("background-color","#FFFFFF");
	});
	$("#loginuser").on('input',function(){
        if($("#loginuser").val()!='' && $("#loginpwd").val()!=''){
            $("#butt_login").css('background-color','#51B1F2');
            $("#butt_login").attr('disabled',false);
        }
        else{
        	$("#butt_login").css('background-color','#A0D8F8');
            $("#butt_login").attr('disabled',true);
        }
    });

	$("#loginpwd").on('input',function(){
        if($("#loginuser").val()!='' && $("#loginpwd").val()!=''){
            $("#butt_login").css('background-color','#51B1F2');
            $("#butt_login").attr('disabled',false);
        }
        else{
        	$("#butt_login").css('background-color','#A0D8F8');
            $("#butt_login").attr('disabled',true);
        }
    });
	
	$("#butt_login").click(function(){	
		var user=$("#loginuser").val();
		var pwd= $("#loginpwd").val();	
		// window.open("home.html",'_self');
		url="User/Authen?user="+user+"&pwd="+encypt(pwd);
		TMS_api(url,"GET","",function(){
			if (xmlHttp.readyState==4 && xmlHttp.status==200){
				var resp = JSON.parse(xmlHttp.responseText);
				if(resp.code==200){	
					sessionStorage.customerId=user;
					sessionStorage.usrfullname=resp.fullname;
					sessionStorage.role=resp.role;
					window.open("home.html",'_self');
				}
				else $("#alertmess").show();
			}
		});		
	});

	$("#alertmess").click(function b(e){	
		if($("#alertmess").is(":visible"))$("#alertmess").hide();
	});
});

$(document).keyup(function(event){
	if(event.keyCode ==13) $("#butt_login").trigger("click");
});