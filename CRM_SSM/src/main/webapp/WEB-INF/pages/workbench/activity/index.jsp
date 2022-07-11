<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		//日历框架
		$(".time").datetimepicker({
			minView: "month",		//可以选择的最小视图
			language:  'zh-CN',		//语言
			format: 'yyyy-mm-dd',	//日期的格式
			autoclose: true,		//设置选择完日期或时间后，是否自动关闭日历
			todayBtn: true,			//设置是否显示“今天”按钮
			clearBtn:true,			//设置是否显示“清空”按钮
			pickerPosition: "bottom-left"
		});

		//实现点击创建按钮，打开模态窗口
		$("#createActivityBtn").click(function (){
			//清空表单内容
			//转为DOM对象才能调用此函数
			$("#createActivityForm")[0].reset()

			//打开模态窗口
			$("#createActivityModal").modal("show")
		})
		//给保存按钮绑定事件
		$("#saveBtn").click(function (){
			var owner = $.trim($("#create-owner").val());
			var name = $.trim($("#create-activityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());

			//表单验证
			if (owner==""){
				alert("所有者不能为空");
				return;
			}
			if (name==""){
				alert("市场活动名称不能为空");
				return;
			}
			if (startDate!=""&&endDate!=""){
				if (endDate<startDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			//非负整数的正则表达式
			var reg=/^(([1-9]\d*)|0)$/;
			if (!reg.test(cost)){
				alert("成本只能是非负整数");
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data){
					//添加成功
					if (data.code == "1"){
						//关闭模态窗口
						$("#createActivityModal").modal("hide")
						//刷新市场活动列表
						pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'))
					}else {
						alert(data.message);
					}
				}
			})

		})

		//当页面加载完毕，查询所有数据的第一页以及所有数据的总条数
		pageList(1,5);

		//实现查询按钮操作
		$("#selectBtn").click(function (){
			pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
		})

		//给全选按钮绑定事件，实现全选效果
		$("#checkAll").click(function (){
			if ($("#checkAll").prop("checked")){
				$("input[name=xz]").prop("checked",true);
			}else {
				$("input[name=xz]").prop("checked",false);
			}
		})
		//为选择复选框绑定事件，控制全选复选框
		/*
		* 动态生成的元素，我们要以on方法的形式来触发事件
		* 语法：
		* $(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)*/
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			$("#checkAll").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		//给删除按钮绑定事件
		$("#deleteActivityBtn").click(function (){
			var $xz = $("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择要删除的信息");
				return;
			}else {
				if (confirm("确定删除所选中的信息吗?")){
					var ids = "";
					for (var i = 0; i < $xz.length; i++){
						ids += "id="+$($xz[i]).val()

						if (i < $xz.length-1){
							ids += "&";
						}
					}

					$.ajax({
						url:"workbench/activity/deleteActivityByIds.do",
						data: ids,
						type: "post",
						dataType: "json",
						success:function (data){
							if (data.code = "1"){
								pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'));
							}else {
								alert(data.message)
							}
						}
					})
				}
			}
		})

		//点击修改按钮打开模态窗口
		$("#editActivityBtn").click(function (){
			var $xz = $("input[name=xz]:checked")

			if ($xz.length == 0){
				alert("请选择要修改的市场活动");
				return;
			}else if($xz.length > 1){
				alert("只能选择一个市场活动进行修改");
				return;
			}else {
				var id = $xz.val()
				$.ajax({
					url:"workbench/activity/selectActivityById.do",
					data: {
						id:id
					},
					type: "post",
					dataType: "json",
					success:function (data){
						$("#edit-id").val(data.id);
						$("#edit-owner").val(data.owner)
						$("#edit-name").val(data.name);
						$("#edit-startDate").val(data.startDate)
						$("#edit-endDate").val(data.endDate)
						$("#edit-cost").val(data.cost)
						$("#edit-description").val(data.description);

						$("#editActivityModal").modal("show")
					}
				})
			}
		})
		//给更新按钮绑定事件
		$("#updateActivityBtn").click(function (){
			var id = $.trim($("#edit-id").val());
			var owner = $.trim($("#edit-owner").val())
			var name =  $.trim($("#edit-name").val())
			var startDate = $.trim($("#edit-startDate").val())
			var endDate = $.trim($("#edit-endDate").val())
			var cost = $.trim($("#edit-cost").val())
			var description = $.trim($("#edit-description").val())

			//表单验证
			if (owner==""){
				alert("所有者不能为空");
				return;
			}
			if (name==""){
				alert("市场活动名称不能为空");
				return;
			}
			if (startDate!=""&&endDate!=""){
				if (endDate<startDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			//非负整数的正则表达式
			var reg=/^(([1-9]\d*)|0)$/;
			if (!reg.test(cost)){
				alert("成本只能是非负整数");
				return;
			}

			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				data: {
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type: "post",
				dataType: "json",
				success:function (data){
					if (data.code == "1"){
						$("#editActivityModal").modal("hide")

						pageList($("#activityPage").bs_pagination('getOption','currentPage'),$("#activityPage").bs_pagination('getOption','rowsPerPage'));
					}else {
						alert(data.message)
					}
				}
			})
		})

		//给批量导出按钮绑定事件
		$("#exportActivityAllBtn").click(function (){
			window.location.href="workbench/activity/exportAllActivity.do"
		})

		//给选择导出按钮绑定事件
		$("#exportActivityXzBtn").click(function (){
			var $xz = $("input[name=xz]:checked");

			if ($xz.length == 0){
				alert("请选择要导出的市场活动")
				return
			}else {
				var param = ""

				for (var i = 0; i < $xz.length; i++){
					param += "id="+$($xz[i]).val()

					if (i<$xz.length-1){
						param += "&"
					}
				}

				window.location.href="workbench/activity/exportChooseActivity.do?"+param
			}
		})

		//给导入按钮添加单击事件
		$("#importActivityBtn").click(function (){
			//获取文件名称
			var activityFileName = $("#activityFile").val()
			//截取文件后缀名
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();//全部转为小写
			//验证后缀名是否为“xls”
			if (suffix != "xls"){
				alert("只支持xls文件");
				return;
			}
			//获取文件本身
			var activityFIle = $("#activityFile")[0].files[0];
			//获取文件大小
			if (activityFIle.size>5*1024*1024){
				alert("文件大小不能超过5MB")
				return;
			}

			//FormData是ajax提供的接口，可以模拟键值对向后台提供参数
			//FormData最大的优势是可以提交二进制数据
			var formData = new FormData();
			formData.append("activityFile",activityFIle);

			//发送请求
			$.ajax({
				url:"workbench/activity/importActivity.do",
				data:formData,
				processData:false,//设置ajax向后台提交参数之前，是否把参数统一转换成字符串：true---是（默认）；false---否
				contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数按照urlencoded编码：true---是（默认）；false---否
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.code == "1"){
						alert(data.message);
						$("#importActivityModal").modal("hide")
						pageList(1,$("#activityPage").bs_pagination('getOption','rowsPerPage'))
					}else {
						alert(data.message)
					}
				}
			})
		})
	});

	/*
	* pageNo：页码
	* pageSize：每页展现的记录数*/
	function pageList(pageNo,pageSize){
		//将全选的复选框的√干掉
		$("#checkAll").prop("checked",false);

		//收集参数
		var name = $("#name").val();
		var owner = $("#owner").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();

		$.ajax({
			url:"workbench/activity/selectActivityByConditionForPage.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:"post",
			dataType: "json",
			success:function (data){
				//显示市场活动列表
				var html = ""

				$.each(data.activityList,function (i,n){
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'" "/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})

				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize==0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo,	 	// 页码
					rowsPerPage: pageSize, 		// 每页显示的记录条数
					maxRowsPerPage: 20, 		// 每页最多显示的记录条数
					totalPages: totalPages, 	// 总页数
					totalRows: data.total,		// 总记录条数

					visiblePageLinks: 3, 		// 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数在点击分页组件的时候触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})

	}

</script>
</head>
<body>

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

					<form id="createActivityForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  <c:forEach var="u" items="${userList}">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-activityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
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
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
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

						<input type="hidden" id="edit-id">

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<c:forEach var="u" items="${userList}">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
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
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="endDate">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="selectBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">

				</div>

			</div>

		</div>

	</div>
</body>
</html>
