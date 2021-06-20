var token;
const username = 'client#1';
const userpassword = 'client#1';
const room = "00000000-0000-0000-0000-000000000001";
const user = "00000000-0000-0000-0000-000000000001";

function init(){
	$.ajax({
		type: "POST",
		url: "/api/login",
		data: JSON.stringify({login: username , password: userpassword}),
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function(data) {
			token = data.token;
			connect(token);
		},
		error: function(errMsg) {
			alert(errMsg);
		}
	});
}

function connect(key) {
	const prefix = "/topic/";
	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log("Connected: " + frame);
		stompClient.subscribe(prefix+room, function(response) {
			var data = JSON.parse(response.body);
			draw("left", data.text);
		});
	});
}

function draw(side, text) {
	console.log("Drawing...");
    var $message;
    $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.text').html(text);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);

}
function disconnect(){
	stompClient.disconnect();
}

function send(){
	sendMessage(token);
}
function sendMessage(key){
	const prefix = "/app/"
	stompClient.send(prefix+room, {token: key}, JSON.stringify({'roomId': room,'userId': user,'text': $("#message_input_value").val()}));

}