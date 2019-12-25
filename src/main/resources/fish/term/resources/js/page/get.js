layui.config({
	"base":"/term/res/layui/lay/kit/"
}).use(["laydate", "form", "commons"], function() {
	var form = layui.form, $ = layui.$;
	
	// 监听提交
    form.on('submit(form-submit)', function(data) {
		var field = data.field; //获取提交的字段
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		
		$.ajax({
			url: "/term"
			,type: 'PUT'
			,data: JSON.stringify(field)
			,contentType: 'application/json'
			,beforeSend:function(xhr,opt) {
				this.layerIndex = parent.layer.load(1, {shade: [0.8, '#393D49']});
			}
			,complete: function(xhr, ts) {
				parent.layer.close(this.layerIndex);
			}
			,success: function(data) {
				parent.layui.table.reload('layuiTable'); //重载表格
				parent.layer.msg(data);
				parent.layer.close(index); //再执行关闭 
			}
			,error: function(request, status, errorThrown) {
				if(request.responseJSON) {
					layer.msg(request.responseJSON.message,{time: 3000,offset: '15em'});
				} else {
					layer.msg("系统错误！",{time: 3000,offset: '15em'});
				}
			}
		});
	});
});