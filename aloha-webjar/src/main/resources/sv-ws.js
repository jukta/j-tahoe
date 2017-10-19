var sv_ws = new function() {

    this.ws = {};
    var q = [];

    this.ws.emmit = function (type, data) {
        var req;
        if (typeof data === "object") {
            req = type + ";;;1;;;" + JSON.stringify(data);
        } else {
            req = type + ";;;0;;;" + data;
        }

        if (isOpened) {
            sock.send(req);
        } else {
            q.push(req);
            connect();
        }
    };

    var sock;
    var isOpened = false;

    var onopen = function() {
        isOpened = true;
        while (q.length > 0) {
            sock.send(q.shift());
        }
    };

    var onmessage = function(e) {
        var m = e.data.split(';;;');
        if (m[1] == "0") {
            SV.emmit(m[0], m[2]);
        } else {
            SV.emmit(m[0], JSON.parse(m[2]));
        }
    };

    var onclose = function() {
        isOpened = false;
        SV.emmit("ws-error", {});
    };

    var onheartbeat = function() {
        console.log('heartbeat');
    };

    var onerror = function (e) {
        console.log('error ' + e);
    };

    var connect = function () {
        // var req = {
        //     type:"ws-init",
        //     data: {}
        // };
        // q.push(req);
        sock = new SockJS('/__ws-event');
        sock.onopen = onopen;
        sock.onmessage = onmessage;
        sock.onclose = onclose;
        sock.onheartbeat = onheartbeat;
        sock.onerror = onerror;
    };

    // connect();

};

var SV = $.extend(SV, sv_ws);