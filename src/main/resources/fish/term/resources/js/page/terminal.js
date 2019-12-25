document.onkeydown = function (e) {
    var ev = window.event || e;
    var code = ev.keyCode || ev.which;
    if (code == 116) {
        if(ev.preventDefault) {
            ev.preventDefault();
        } else {
            ev.keyCode = 0;
            ev.returnValue = false;
        }
    }
}
$('.btn-send').click(function(){
	$cmd = $('#cmd').val();
	webSocket.send($cmd + "\n");
	$('#cmd').val("");
})

$('.btn-stop').click(function(){
	webSocket.send("exit");
	term.write("\nConnection closed.");
})

var wsPath = "";
var id = $("input[name='id']").val();
if (window.location.protocol === 'http:') {
	wsPath = 'ws://' + window.location.host;
} else {
	wsPath = 'wss://' + window.location.host;
}

var webSocket = new WebSocket(wsPath + "/term/terminal/"+id);

webSocket.onerror = function(event) {
	term.write("\nConnection closed.");
};

webSocket.onopen = function(event) {
	
};

webSocket.onmessage = function(event) {
	term.write(event.data);
};

webSocket.onclose = function(event) {
	term.write("\nConnection closed.");
};

var keepalive = function() {
	setTimeout(function () {
		webSocket.send("keepalive");
		keepalive();
	}, 20000);
}
keepalive();

var width = $('#term').width();
var height = $(window).height() - 65;
var term = new Terminal({
    cols: Math.floor(width / 7.25),
    rows: Math.floor(height / 18),
    screenKeys: false,
    useStyle: true,
    cursorBlink: true,
    convertEol: true
});
term.open($("#term").empty()[0]);
term.on('data', function(data) {
	webSocket.send(data);
});

$(window).resize(function() {
	var width = $('#term').width();
	var height = $(window).height() - 65;
	term.resize(Math.floor(width / 7.25), Math.floor(height / 18));
});