var username = document.getElementById("username").value;
var wsUri ="ws://127.0.0.1:9000/ws/"+username ;
var output;
var websocket
function init() {
    output = document.getElementById("output");
    testWebSocket();
}
function testWebSocket() {
     websocket = new WebSocket(wsUri);
    alert(wsUri)
    websocket.onopen = function(evt) {
        onOpen(evt)
    };
    websocket.onclose = function(evt) {
        onClose(evt)
    };
    websocket.onmessage = function(evt) {
        onMessage(evt)
    };
    websocket.onerror = function(evt) {
        onError(evt)
    };
}
function onOpen(evt) {
    alert("open")
    writeToScreen("CONNECTED");
   // doSend("WebSocket rocks");
}
function onClose(evt) {
    writeToScreen("DISCONNECTED");
}
function onMessage(evt) {
    writeToScreen('<span style="color: blue;">RESPONSE: '+ evt.data+'</span>');
   // websocket.close();
}
function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> '+ evt.data);
}
function doSend(message) {

    var json = new Object();
    json.message = "hello";
    json.ato = "ni@163.com"
    alert("hello");
    writeToScreen("SENT: " + json.ato +"-->"+json.message);
    websocket.send(JSON.stringify(json));
}
function writeToScreen(message) {
    var pre = document.createElement ( "p" ) ;
    pre.style.wordWrap = "break-word" ;
    pre.innerHTML = message ;
    output.appendChild ( pre ) ;
}

$(document).ready(init());