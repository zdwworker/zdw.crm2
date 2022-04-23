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

<script type="text/javascript">




	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){

		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

        showRemar();
/*
        $("#remarkBody").on("mouseover",".remarkDiv",function(){
            $(this).children("div").children("div").show();
        })
        $("#remarkBody").on("mouseout",".remarkDiv",function(){
            $(this).children("div").children("div").hide();
        })*/

        //点击编辑打开模态窗口，连接数据库生成使用者下拉框数据
		$("#getmodal").click(function (){

			var owner=$("#getowner").val();
			//发送请求获得users的数据
			var html="";
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType : "json",
				success:function (data){
					$.each(data,function (i,n){
						if(n.name==owner){
							html+="<option name='xz' value='"+n.id+"' selected='selected'>"+n.name+"</option>"
						}else{
							html+="<option name='xz' value='"+n.id+"'>"+n.name+"</option>";
						}

					})
					$("#edit-marketActivityOwner").html(html);
				}
			})

			$("#editActivityModal").modal("show");
		})

		//在修改市场活动的模态窗口点击了更新后
		$("#upActivity").click(function (){
			//var $xz =$("input[name=xz]:selected").val();
			var options=$("#edit-marketActivityOwner option:selected");
			var owner=options.val();
			var id  = $("#getid").val();
			//var owner=$("#edit-marketActivityOwner").val();//不行  他的value是汉字 不是代表名字的id
			var name=$("#edit-marketActivityName").val();
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var description=$("#edit-describe").val();
			var cost=$("#edit-cost").val();

			$.ajax({
				url : "workbench/activity/updataActivity.do",
				type : "post",
				dataType : "json",
				data: {
					"id":id,
					"owner":owner,
					"name":name,
					"startDate":startDate,
					"endDate":endDate,
					"description":description,
					"cost":cost
				},
				success :function (data){
					if(data.success){
						//修改成功
						alert("修改成功！");

						//关闭模态窗口  打开并且刷新详细页面
						$("#editActivityModal").modal("hide");
						/*selectActivity($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));*/
						window.location.href= "workbench/activity/detail.do?id="+id;
					}else{
						alert("修改失败")
					}
				}
			})
		})

		//在修改市场活动的模态窗口中点击了删除后的操作
		$("#delbtn").click(function (){
			if(confirm("确定删除吗？")){
				//获得数据id的值
				var id  = $("#getid").val();
					$.ajax({
					url : "workbench/activity/delDetailActivity.do",
					type : "post",
					data :{"id":id},
					dataType : "json",
					success : function (data){
						if(data.success){
							alert("删除成功！");
							//关闭模态窗口
							$("#editActivityModal").modal("hide");
							window.location.href="workbench/activity/index.jsp";
						}else {
							alert("操作失败，为删除！");
						}
					}
				})

			}

		})

        $("#saveRemarkBtn").click(function (){

            var savanoteContent= $("#remark").val();
            var htmlremark="";
            $.ajax({
                url:"workbench/activity/savaRemark.do",
                type:"post",
                dataType : "json",
                data:{"activityId":'${a.id}',"savanoteContent":savanoteContent},
                success:function (data){
                    if(data.success){
                        alert("保存成功！");
                        htmlremark+='   <div class="remarkDiv" style="height: 60px;">';
                        htmlremark+='       <img title="'+n.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                        htmlremark+='           <div style="position: relative; top: -40px; left: 40px;" >';
                        htmlremark+='               <h5>'+n.noteContent+'</h5>';
                        htmlremark+='               <font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;"> '+(n.editFlag==0?n.createTime:n.editTime)+'由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
                        htmlremark+='               <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        htmlremark+='                   <a class="myHref" href="javascript:void(0);" onclick="updataRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>';
                        htmlremark+='                   &nbsp;&nbsp;&nbsp;&nbsp;';
                        // htmlremark+='                   <a class="myHref" href="javascript:void(0);" onclick="delRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: red;"></span></a>';
                        htmlremark +=   '                   <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                        htmlremark+='               </div>';
                        htmlremark+='           </div>';
                        htmlremark+='   </div>';
                        htmlremark+="<hr>";
                        $("#dohtml").html(htmlremark);

                    }
                }

            })
            $("#remark").val("");
            showRemar();
        })
        //点击取消按钮   情况内容框
        $("#cancelBtn").click(function (){
            $("#remark").val("");
        })

        /*$("#remark").keydown(function (even){
            if(even.keyCode==13){

            }
        })*/

	});


    function showRemar(){

        var htmlremark="";

        $.ajax({
            url:"workbench/activity/getRemarkList.do",
            type:"get",
            dataType : "json",
            data:{"id":'${a.id}'},
            success:function (data){
                $.each(data,function (i,n){
                    htmlremark+='   <div class="remarkDiv" style="height: 60px;">';
                    htmlremark+='       <img title="'+n.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                    htmlremark+='           <div style="position: relative; top: -40px; left: 40px;" >';
                    htmlremark+='               <h5>'+n.noteContent+'</h5>';
                    htmlremark+='               <font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;"> '+(n.editFlag==0?n.createTime:n.editTime)+'由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
                    htmlremark+='               <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                    htmlremark+='                   <a class="myHref" href="javascript:void(0);" onclick="updataRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>';
                    htmlremark+='                   &nbsp;&nbsp;&nbsp;&nbsp;';
                    // htmlremark+='                   <a class="myHref" href="javascript:void(0);" onclick="delRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: red;"></span></a>';
                    htmlremark +=   '                   <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                    htmlremark+='               </div>';
                    htmlremark+='           </div>';
                    htmlremark+='   </div>';
                    htmlremark+="<hr>";
                })

                $("#dohtml").html(htmlremark);
            }

        })

        $("#remarkBody").on("mouseover",".remarkDiv",function(){
            $(this).children("div").children("div").show();
        })
        $("#remarkBody").on("mouseout",".remarkDiv",function(){
            $(this).children("div").children("div").hide();
        })
    }

//点击了删除备注的操作
    function deleteRemark(id){
        if(confirm("确定删除吗？")){
            $.ajax({
                url:"workbench/activity/delRemark.do",
                type:"post",
                dataType : "json",
                data:{"id":id},
                success:function (data){
                    if(data.success){
                        alert("删除成功");

                        showRemar();
                    }else{
                        alert("操作有误，删除失败！")
                    }
                }
            })
        }

    }
    function updataRemark(id){

        $("#editRemarkModal").modal("show");

        $("#updateRemarkBtn").click(function (){
            var noteContent=$("#noteContent").val();
            $.ajax({
                url:"workbench/activity/updataRemark.do",
                type:"post",
                dataType : "json",
                data:{"id":id,"noteContent":noteContent},
                success:function (data){
                    if(data.success){
                        alert("修改成功");
                        $("#editRemarkModal").modal("hide");
                        showRemar();
                    }else{
                        alert("操作有误，修改失败！")
                    }
                }
            })
        })
    }
	
</script>

</head>
<body>
<%--隐藏域，工具--%>
	<input type="hidden" value="${a.owner}" id="getowner">
	<input type="hidden" value="${a.id}" id="getid">

	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改市场活动的模态窗口 -->
    <div class="modal fade" id="editActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;" >
                                <select class="form-control" id="edit-marketActivityOwner" >
                                    <%--<option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>--%>
                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="${a.name}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-startTime" value="${a.startDate}">
                            </div>
                            <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-endTime" value="${a.endDate}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-cost" value="${a.cost}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-describe">${a.description}</textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" <%--data-dismiss="modal"--%> id="upActivity">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${a.name} <small>${a.startDate} ~ ${a.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" <%--data-toggle="modal" data-target="#editActivityModal"--%> id="getmodal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger" id="delbtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${a.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${a.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${a.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${a.createBy}&nbsp;</b><small style="font-size: 10px; color: gray;">${a.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${a.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${a.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					<%--市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等--%>
					${a.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 30px; left: 40px;" id="remarkBody" >
		<div class="page-header" >
			<h4>备注</h4>
		</div>
		<div id="dohtml">

        </div>
		<%--<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;" id="remark">

		</div>--%>
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>

        <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
            <form role="form" style="position: relative;top: 10px; left: 10px;">
                <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
                <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                    <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                    <button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
                </p>
            </form>
        </div>

	</div>
	<div style="height: 200px;"></div>
</body>
</html>