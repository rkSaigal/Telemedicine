var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/call/' + userId, function (message) {
        var callMessage = JSON.parse(message.body);
        handleCallMessage(callMessage);
    });
});

function initiateCall() {
    var toUserId = 1/* replace with recipient's ID */;
    var fromOffer = 2/* offer data */;
    var message = {
        type: 'outgoing:call',
        to: toUserId,
        fromOffer: fromOffer
    };
    stompClient.send('/app/call', {}, JSON.stringify(message));
}

function handleCallMessage(message) {
    if (message.type === 'incoming:call') {
        var offer = message.offer; // Handle offer data
        // Handle incoming call UI update or notification
        document.getElementById('callStatus').innerText = 'Incoming Call from ' + message.from;
    } else if (message.type === 'incoming:answer') {
        var answer = message.answer; // Handle answer data
        // Handle call accepted UI update or notification
        document.getElementById('callStatus').innerText = 'Call accepted by ' + message.from;
    }
}
