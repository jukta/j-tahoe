var sv_http = new function() {

    this.http = {};

    this.http.emmit = function (type, data) {
        $.ajax({
            type: 'POST',
            url: '/__event/'+type,
            contentType: "application/json",
            dataType: 'html',
            async: true,
            data: JSON.stringify(data)
        }).done(handleResponse);
    };

    var handleResponse = function(resp, q, e) {
        var type = e.getResponseHeader("response-type");
        if (e.getResponseHeader("content-type") == "application/json") {
            SV.emmit(type, JSON.parse(resp));
        } else {
            SV.emmit(type, resp);
        }
    };
};

var SV = $.extend(SV, sv_http);