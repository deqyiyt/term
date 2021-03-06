layui.config({
	"base":"/term/res/layui/lay/kit/"
}).use(['laypage','layer', 'table', "upload", 'jquery', "commons"], function(){
	var layer = layui.layer
	  ,table = layui.table
	  ,upload = layui.upload
	  ,$ = layui.jquery
	  ,sid=$("input[name='sid']").val()
	  ,id=$("input[name='id']").val();
	
	
	var loadTable = function(data) {
		table.render({
			elem: '#layuiTable'
			,height: "full-135"
			,toolbar: '#toolbar'
			,defaultToolbar:['exports']
			,data: data
			,limit:data.length
			,page:false
			,cols: [[ //表头
				{type: 'checkbox', fixed: 'left'}
				,{field: 'filename', title: '文件名', sort: true, fixed: 'left',templet:function(d) {
					if(d["up"]) {
						return '<a lay-event="up" style="color:#10A8FF;cursor:pointer;">'+d.filename+'</a>';
					} else if(d.directory) {
						return '<a lay-event="folder" style="color:#10A8FF;cursor:pointer;"><i class="layui-icon">&#xe61d;</i> '+d.filename+'</a>';
					} else {
						return '<i class="layui-icon">&#xe621;</i> '+d.filename;
					}
				}}
				,{field: 'size', title: '大小', sort: true}
				,{field: 'mtime', title: '修改时间', sort: true}
				,{field: 'strPermissions', title: '权限', sort: true}
				,{fixed:"right",title:"操作",width:160,templet:function(d) {
					var _btns = new Array();
					if(!d["up"]) {
						_btns.push('<a class="layui-btn layui-btn-xs" lay-event="update">修改</a>');
						if(!d.directory) {
							_btns.push('<a target="_blank" href="/term/sftp/download/'+sid+'/'+d.filename+'" class="layui-btn layui-btn-warm layui-btn-xs">下载</a>');
						}
						_btns.push('<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>');
					}
					
					return _btns.join("");
				}}
			]]
			,done: function(res, curr, count){
				var uploadInst = upload.render({
				    elem: '#uploadfile' //绑定元素
				    ,url: '/term/sftp/upload/'+sid //上传接口
				    ,accept:'file'
				    ,done: function(res, index, upload){
						layer.msg(res.msg);
					}
					,error:function(index,upload){
						layer.msg("上传失败");
					}
				});
			}
		});
	}
	loadTable(new Array());
	
	$("#btn-search").on("click", function() {
		webSocket.send(JSON.stringify({
			"tyue":"query"
			,"dir":$("input[name='path']").val()
		}));
	});
	
	// 监听行工具事件
	table.on("tool(layuiTable)", function(obj) {
		var data = obj.data;
		switch(obj.event){
			case 'update':
				layer.open({
					type: 1
					,title: "修改文件名 - " + data.filename
					,shadeClose: true
					,shade: 0.3
					,maxmin: false
					,btn: ['确定', '取消']
					,area: ['350px', '200px']
					,content: '<div class="layui-form-item" style="padding-top: 20px;">'
							+'	<div class="layui-inline">'
							+'			<label class="layui-form-label">新文件名称</label>'
							+'			<div class="layui-input-inline">'
							+'				<input type="text" name="newfileName"  placeholder="请输入新文件名称" autocomplete="off" class="layui-input">'
							+'			</div>'
							+'		</div>'
							+'</div>'
					,yes: function(index, layero){
						var fileName = $("input[name='newfileName']").val();
						if(fileName == "") {
							layer.tips('新文件名称不能为空！', "input[name='newfileName']");
						} else {
							layer.close(index);
							webSocket.send(JSON.stringify({
								"tyue":"update"
								,"fileName":data.filename
								,"newName":fileName
							}));
						}
					}
				});
				break;
			case 'delete':
				var msg = "确定删除 <span style='color:red'>" + data.filename + "</span> 吗？";
				if(data.directory) {
					msg += "<br/><font style='color:red'>提示：只能删除空文件夹</font>"
				}
				layer.msg(msg, {
					time : 0 // 不自动关闭
					,btn : ["确定","取消"]
					,yes : function(ind) {
						layer.close(ind);
						webSocket.send(JSON.stringify({
							"tyue":"remove"
							,"fileName": data.filename
							,"directory": data.directory
						}));
					}
				});
				break;
			case 'up':
				webSocket.send(JSON.stringify({
					"tyue":"up"
				}));
				break;
			case 'folder':
				webSocket.send(JSON.stringify({
					"tyue":"query"
					,"dir":$("input[name='path']").val() + "/" + data.filename
				}));
				break;
			default:
				break;
		}
	});
	
	//监听头工具栏事件
	table.on('toolbar(layuiTable)', function(obj){
		var checkStatus = table.checkStatus(obj.config.id)
		,data = checkStatus.data; //获取选中的数据
		switch(obj.event){
			case 'addfile':
				layer.open({
					type: 1
					,title: "创建文件"
					,shadeClose: true
					,shade: 0.3
					,maxmin: false
					,btn: ['确定', '取消']
					,area: ['350px', '200px']
					,content: '<div class="layui-form-item" style="padding-top: 20px;">'
							+'	<div class="layui-inline">'
							+'			<label class="layui-form-label">文件名称</label>'
							+'			<div class="layui-input-inline">'
							+'				<input type="text" name="addfileName"  placeholder="请输入新文件名称" autocomplete="off" class="layui-input">'
							+'			</div>'
							+'		</div>'
							+'</div>'
					,yes: function(index, layero){
						var fileName = $("input[name='addfileName']").val();
						if(fileName == "") {
							layer.tips('文件名不能为空！', "input[name='addfileName']");
						} else {
							layer.close(index);
							webSocket.send(JSON.stringify({
								"tyue":"addfile"
								,"fileName":fileName
							}));
						}
					}
				});
				break;
			case 'addfolder':
				layer.open({
					type: 1
					,title: "创建文件夹"
					,shadeClose: true
					,shade: 0.3
					,maxmin: false
					,btn: ['确定', '取消']
					,area: ['350px', '200px']
					,content: '<div class="layui-form-item" style="padding-top: 20px;">'
							+'	<div class="layui-inline">'
							+'			<label class="layui-form-label">文件夹名称</label>'
							+'			<div class="layui-input-inline">'
							+'				<input type="text" name="addfolderName"  placeholder="请输入新文件夹名称" autocomplete="off" class="layui-input">'
							+'			</div>'
							+'		</div>'
							+'</div>'
					,yes: function(index, layero){
						var fileName = $("input[name='addfolderName']").val();
						if(fileName == "") {
							layer.tips('文件夹名称不能为空！', "input[name='addfolderName']");
						} else {
							layer.close(index);
							webSocket.send(JSON.stringify({
								"tyue":"addfolder"
								,"fileName":fileName
							}));
						}
					}
				});
				break;
			case 'delete':
				layer.msg("确定删除选择的文件吗？<br/><font style='color:red'>提示：只能删除空文件夹</font>", {
					time : 0 // 不自动关闭
					,btn : ["确定","取消"]
					,yes : function(ind) {
						layer.close(ind);
						var array = new Array();
						$.each(data, function() {
							if(data.filename != ".." && data.filename != ".") {
								webSocket.send(JSON.stringify({
									"tyue":"remove"
									,"fileName": this.filename
									,"directory": this.directory
								}));
							}
						});
						
					}
				});
				break;
			default:
				break;
		}
	});
	
	var wsPath = "";
	if (window.location.protocol === 'http:') {
		wsPath = 'ws://' + window.location.host;
    } else {
    	wsPath = 'wss://' + window.location.host;
    }

	var webSocket = new WebSocket(wsPath + "/term/sftp/"+sid+"/"+id);
	
	webSocket.onerror = function(event) {
		console.log("Connection error.");
	};
	
	webSocket.onopen = function(event) {
		console.log("Connection open.");
	};
	
	webSocket.onmessage = function(event) {
		var json = JSON.parse(event.data);
		var data = json["files"];
		if(json["currentCatalog"]) {
			$("input[name='path']").val(json["currentCatalog"]);
		}
		if(data) {
			if(json["currentCatalog"] != "/") {
				data.unshift({
					"filename": "..",
					"size": "",
					"intPermissions": "",
					"strPermissions": "",
					"octalPermissions": "",
					"mtime": "",
					"up":true
				});
			}
			loadTable(data);
		} else {
			loadTable(new Array());
		}
	};
	
	webSocket.onclose = function(event) {
		console.log("Connection close.");
	};
	var keepalive = function() {
		setTimeout(function () {
			webSocket.send(JSON.stringify({
				"tyue":"keepalive"
			}));
			keepalive();
		}, 20000);
	}
	keepalive();
});