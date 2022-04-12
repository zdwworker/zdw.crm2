<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>


<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<body>
<script type="text/javascript">
	$(function (){
		//页面刷新后账户文本框获得焦点
		$("#account").focus();
		//实现回车键也可登录功能
		$(window).keydown(function (event){
			if(event.keyCode==13){
				login();
			}
		})
		//实现点击登录按钮登录
		$("#but").click(function (){
			login();
		})


	})
	function login(){
		var account=$.trim($("#account").val());
		var password=$.trim($("#password").val());

		if(account=="" || password==""){
			$("#msg").html("账号和密码不能为空！")
			return false;
		}

		//ajax异步请求执行登录操作
		/*xml=new XMLHttpRequest();
		xml.onreadystatechange=function (){
			if(xml.readyState==4 && xml.status==200){
				var data = xml.responseText;
				alert(data)
			}
		}
		xml.open("post","settings/user/login.do",true);
		xml.send();*/
		$.ajax({

			url : "settings/user/login.do",
			type : "post",
			data : {
				"account":account,
				"password":password
			},
			dataType : "json",
			success : function (data){
				if(data.success){
					// ture 登录成功 跳转下一页面
					window.location.href="workbench/index.jsp";
				}else {
					//登录失败 在密码框下方显示登录失败的原因
					$("#msg").html(data.arg);
				}
			}

		})
	}

</script>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="account">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="password">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" ></span>
						
					</div>
					<button type="button" class="btn btn-primary btn-lg btn-block" id="but" style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>