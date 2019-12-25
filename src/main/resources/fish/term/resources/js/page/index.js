layui.config({
	"base":"/term/res/layui/lay/kit/"
}).use(['layer', 'table', 'jquery', 'commons'], function(){
	var layer = layui.layer //弹层
	  ,table = layui.table //表格
	  ,$ = layui.jquery;
	//执行一个 table 实例
	table.render({
		elem: '#layuiTable'
		,height: "auto"
		,method:"post"
		,even: true //开启隔行背景
		,url: '/term.json' //数据接口
		,title: '终端列表'
		,size:"lg"
		,page: false //开启分页
		,toolbar: '<div class="layui-table-tool-temp"><div class="layui-inline" lay-event="add"><i class="layui-icon layui-icon-add-1"></i></div><div class="layui-inline" lay-event="delete"><i class="layui-icon layui-icon-delete"></i></div></div><div class="layui-table-tool-self"></div>' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
		,defaultToolbar: [/* 'filter', 'print', 'exports' */]
		,cols: [[ //表头
			{type: 'checkbox', fixed: 'left'}
			,{field: 'serverName', title: '服务器名称', sort: true, fixed: 'left'}
			,{field: 'hostName', title: '地址', sort: true}
			,{field: 'port', title: '端口', sort: true}
			,{field: 'userName', title: '用户名', sort: true}
			,{fixed:"right",title:"操作",width:330,templet:function(d) {
				var _btns = new Array();
				_btns.push('<a class="layui-btn layui-btn-normal" lay-event="terminal">terminal</a>');
				_btns.push('<a class="layui-btn layui-btn-normal" lay-event="sftp">sftp</a>');
				_btns.push('<a class="layui-btn layui-btn-warm" lay-event="update">修改</a>');
				_btns.push('<a class="layui-btn layui-btn-danger" lay-event="delete">删除</a>');
				return _btns.join("");
			}}
		]]
	});
	
	var remove = function(id) {
		layer.msg("确定删除选中的服务吗？", {
			time : 0 // 不自动关闭
			,btn : ["确定","取消"]
			,yes : function(ind) {
				$.ajax({
					url: "/term/"+id
					,type: 'DELETE'
					,beforeSend:function(xhr,opt) {
						this.layerIndex = layer.load(1, {shade: [0.8, '#393D49']});
					}
					,complete: function(xhr, ts) {
						layer.close(this.layerIndex);
					}
					,success: function(data) {
						layui.table.reload('layuiTable'); //重载表格
						layer.msg(data);
					}
					,error: function(request, status, errorThrown) {
						if(request.responseJSON) {
							layer.msg(request.responseJSON.message,{time: 3000,offset: '15em'});
						} else {
							layer.msg("系统错误！",{time: 3000,offset: '15em'});
						}
					}
				});
			}
		});
	}
	
	// 监听行工具事件
	table.on("tool(tool)", function(obj) {
		var data = obj.data;
		switch(obj.event){
			case 'update':
				layer.open({
					type: 2
					,title: "编辑终端 - " + data["serverName"]
					,shadeClose: false
					,shade: 0.3
					,maxmin: false
					,btn: ['确定', '取消']
					,area: ['550px', '500px']
					,content: "/term/get/"+data["id"]
					,yes: function(index, layero){
						var submit = layero.find('iframe').contents().find("#btn-submit");
						submit.click();
					}
				});
				break;
			case 'delete':
				remove(data["id"]);
				break;
			case 'terminal':
				layer.open({
					type: 2
					,title: "shell - " + data["serverName"]
					,shadeClose: false
					,shade: false
					,maxmin: true
					,area: ['750px', '630px']
					,content: "/term/terminal/" + data["id"]
				});
				break;
			case 'sftp':
				layer.open({
					type: 2
					,title: "sftp - " + data["serverName"]
					,shadeClose: false
					,shade: false
					,maxmin: true
					,area: ['750px', '600px']
					,content: "/term/sftp/" + data["id"]
				});
				break;
		}
	});
	  
	//监听头工具栏事件
	table.on('toolbar(tool)', function(obj){
		var checkStatus = table.checkStatus(obj.config.id)
		,data = checkStatus.data; //获取选中的数据
		switch(obj.event){
			case 'add':
				layer.open({
					type: 2
					,title: "新增终端"
					,shadeClose: false
					,shade: 0.3
					,maxmin: false
					,btn: ['确定', '取消']
					,area: ['550px', '500px']
					,content: "/term/get"
					,yes: function(index, layero){
						var submit = layero.find('iframe').contents().find("#btn-submit");
						submit.click();
					}
				});
				break;
			case 'delete':
				if(data.length === 0){
					layer.msg('请选择行');
				} else {
					var array = new Array();
					$.each(data, function() {
						array.push(this.id);
					});
					remove(array.join(","));
				}
				break;
		};
	});
});