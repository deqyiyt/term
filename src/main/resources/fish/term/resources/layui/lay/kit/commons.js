layui.define(["jquery"], function(exports) {
var $ = layui.jquery;
	
    $(document).ajaxSend(function (e, xhr, options) {
    	xhr.setRequestHeader("X-CSRF-TOKEN", fish.csrf.csrfToken);
    });
	
	exports('commons', layui.table);
});