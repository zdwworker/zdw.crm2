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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){
		selectActivity(1,2);

		//时间日期选择器
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//点击创建按钮  打开创建市场的模态窗口
		$("#addbtn").click(function (){

			//连接后台 为所有者下拉框动态赋予值
			var html="";
			$.ajax({
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function (data){
					$.each(data,function (i,n){
						html+="<option value='"+n.id+"'> "+n.name+"</option>";
					})
					$("#create-marketActivityOwner").html(html);

					$("#create-marketActivityOwner").val("${users.id}")

					$("#createActivityModal").modal("show");
				}
			})
		})
		//点击保存按钮 将市场活动信息保存到数据库  并刷新列表信息
		$("#savebtn").click(function (){
			$.ajax({
				url : "workbench/activity/savaActivity.do",
				type : "post",
				data :{
					"owner" : $.trim($("#create-marketActivityOwner").val()),
					"name" : $.trim($("#create-marketActivityName").val()),
					"startDate" : $.trim($("#create-startTime").val()),
					"endDate" : $.trim($("#create-endTime").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-describe").val())
				},
				dataType : "json",
				success :function (data){
					if(data.success){
						//创建成功  清除表单数据
						$("#savaAcitivityForm")[0].reset();
						//发送异步请求刷新活动数据页面列表
						selectActivity(1,2);
						selectActivity(1
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else{
						alert("创建失败！");
					}
				}
			})

		})

		//所有需要刷新列表的操作  查询 修改  删除 创建
		//查询
		$("#searchtbh").click(function (){
			//点击查询后将数据保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startTime").val()));
			$("#hidden-endDate").val($.trim($("#search-endTime").val()));

			selectActivity(1
					,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})
		//打开修改活动列表
		$("#search-updata").click(function (){
				//在后台获取数据填充模态窗口
			//获得复选框的值 只允许获得一个值
			//获得的是activity表的id值
			var $xz =$("input[name=xz]:checked").val();

			if($("input[name=xz]:checked").length>1){
				alert("一次操作只能修改一个数据哦！")
			}else{
				if($xz==null){
					alert("请选择数据！");
				}else{
					$.ajax({
						url: "workbench/activity/selectIdActivity.do",
						type: "get",
						dataType: "json",
						data: {
							"id":$xz
						},
						success: function(data){
							//将查询到的数据赋给修改表单
							//将数据所有者的id赋给$xz
						$xz=data.owner;
							$("#edit-marketActivityOwner").val(data.owner);
							$("#edit-marketActivityName").val(data.name);
							$("#edit-startTime").val(data.startDate);
							$("#edit-endTime").val(data.endDate);
							$("#edit-describe").val(data.description);
							$("#edit-cost").val(data.cost);
						}
					})
					var html="";
					//再发送一个异步请求，给模板所有者下了列表动态更新数据
					$.ajax({
						url: "workbench/activity/selectusers.do",
						type: "get",
						dataType: "json",
						success: function(data){
							//将查询到的数据赋给修改表单
							/*$.each(data.usersList,function (i,n){
								html+= "<option value='"+n.id+"'>"+n.name+"</option>"
							})*/
							$.each(data,function(i,n){
								if($xz == n.name){
									//说明是选择框的id
									html += "<option name='op' selected='selected' value='"+n.id+"'>"+n.name+"</option>";
								}else {
									html += "<option name='op' value='" + n.id + "'>" + n.name + "</option>";
								}
							})
							$("#edit-marketActivityOwner").html(html);
						}

					})
					//$("#edit-marketActivityOwner").html(html);
					//打开修改数据的模态窗口
					$("#editActivityModal").modal("show");
				}

			}
		})

		//修改模板打开后，若选择更新 则更新数据
		$("#edit-renew").click(function(){
			var $xz =$("input[name=xz]:checked").val();
			var owner=$("#edit-marketActivityOwner").val();
			var name=$("#edit-marketActivityName").val();
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var description=$("#edit-describe").val();
			var cost=$("#edit-cost").val();
			//在session中取得当前登录的名字   传入后台  为修改人

			$.ajax({
				url : "workbench/activity/updataActivity.do",
				type : "post",
				dataType : "json",
				data: {
					"id":$xz,
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

						selectActivity($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert("修改失败")
					}
				}

			})
		})

		//点击删除按钮 删除选中的数据
		$("#search-del").click(function (){
			delActivity();
		})

        //查询了所有市场活动信息
		function selectActivity(PageNo,PageSize){
			//每次刷新取消全选框的对勾
			$("#qx").prop("checked",false);
			//从隐藏域中取数据到查询框
			$("#search-name").val($.trim($("#hidden-name").val()));
			$("#search-owner").val($.trim($("#hidden-owner").val()));
			$("#search-startTime").val($.trim($("#hidden-startDate").val()));
			$("#search-endTime").val($.trim($("#hidden-endDate").val()));
			var name=  $.trim($("#hidden-name").val())  ;
			var owner= $.trim($("#hidden-owner").val());
			var startTime= $.trim($("#hidden-startDate").val());
			var endTime= $.trim($("#hidden-endDate").val());
			var tdhtml="";
			$.ajax({
				url : "workbench/activity/selectActivity.do",
				type : "get",
				dataType : "json",
				data: {
					"pageno":PageNo,
					"pagesize":PageSize,
					"name":name,
					"owner":owner,
					"startTime":startTime,
					"endTime":endTime
				},
				success : function (data){
					$.each(data.dataList,function(i,n){

						tdhtml=tdhtml +	"<tr class='active' > ";
						tdhtml=tdhtml +	"	<td><input type='checkbox' name='xz' value='"+n.id+"' ' /></td> ";
						tdhtml += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
						//tdhtml += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id="+n.id+"\';">'+n.name+'</a></td>';
						//tdhtml=tdhtml +	"	<td><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href=\'workbench/activity/detail.jsp';'>"+n.name+"</a></td> ";
						tdhtml=tdhtml +	"	<td>"+n.owner+"</td> ";
						tdhtml=tdhtml +	"	<td>"+n.startDate+"</td> ";
						tdhtml=tdhtml +	"	<td>"+n.endDate+"</td> ";
						tdhtml=tdhtml +	"</tr> ";
					})
					$("#selectactive").html(tdhtml);

					//计算总页数
					var totalPages = data.total%PageSize==0?data.total/PageSize:parseInt(data.total/PageSize)+1;

					//数据处理完毕后，结合分页查询，对前端展现分页信息
					$("#activityPage").bs_pagination({
						currentPage: PageNo, // 页码
						rowsPerPage: PageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 3, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						//该回调函数时在，点击分页组件的时候触发的
						onChangePage : function(event, data){
							selectActivity(data.currentPage , data.rowsPerPage);
						}
					});
				}
			})
		}

		//删除活动列表的方法
		function delActivity(){
			var $xz =$("input[name=xz]:checked");
			if($xz.length==0){
				alert("请勾选要删除的数据！")
			}else{

			if(confirm("确定删除吗？")){
				//获得复选框的值
				var para="";



					//有数据，可能是一条也可能是多条
					for(var i=0;i<$xz.length;i++){
						var x = $xz[i].value;

						para += "id="+x;
						if(i<$xz.length-1){
							para += "&";
						}
					}
				$.ajax({
					url : "workbench/activity/delActivity.do",
					type : "post",
					data :para,
					dataType : "json",
					success : function (data){
						if(data.success){
							alert("删除成功！");
							//关闭模态窗口
							selectActivity(1
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							alert("操作失败，未删除！");
						}
					}
				})
				}
			}
		}
		//为复选框绑定全选和取消全选的操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);

		})
		//因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		/*

			动态生成的元素，我们要以on方法的形式来触发事件

			语法：
				$(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)

		 */
		$("#selectactive").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})



	});
	
</script>
</head>
<body>
	<%--创建四个隐藏域--%>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="savaAcitivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								 <%-- <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label ">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="savebtn" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
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
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="edit-renew">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >名称</div>
				      <input class="form-control " type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon " >开始日期</div>
					  <input class="form-control time" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control time" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchtbh">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" <%--data-toggle="modal" data-target="#createActivityModal"--%> id="addbtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" <%--data-toggle="modal" data-target="#editActivityModal"--%> id="search-updata"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus" id="search-del"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="selectactive">
						<%--<tr class="active" >
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>--%>
                       <%-- <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;"  >
				<div  id="activityPage">

				</div>
			<%--	<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>
			</div>
			
		</div>
		
	</div>
</body>
</html>