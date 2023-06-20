var stompClient = null;

/*** #send => nut gui tin nhan
 * #messages => hop hien thi tin nhan
 */
$(document).ready(function() {
    console.log("Index page is ready");
    connect();

    $("#send").click(function() {
        sendMessage();
    });


});

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // updateNotificationDisplay();
        stompClient.subscribe('/topic/{chat_room_id}', function (message) {
            showMessage(JSON.parse(message.body).content);
        });
    });
}
function disconnect(){
    if(stompClient!= null){
        stompClient.disconnect();
    }
    console.log("Disconected from socket");
}

function showMessage(message) {
    var messageType = message.messageType;
    var localDateTime = message.localDateTime;
    var text = message.text;
    var senderId = message.senderId;

    var messageContent = "Message Type: " + messageType + "<br>"
        + "Local Date Time: " + localDateTime + "<br>"
        + "Text: " + text + "<br>"
        + "Sender ID: " + senderId;

    $("#messages").append("<tr><td>" + messageContent + "</td></tr>");
}


function sendMessage() {
    console.log("sending message");
    stompClient.send("/app/{chat_room_id}", {}, JSON.stringify({'messageContent': $("#message").val()}));
}

