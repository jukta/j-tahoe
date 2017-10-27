var aa_http = new function() {

    this.http = {};

    this.http.emit = function (type, data) {
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
            AA.emit(type, JSON.parse(resp));
        } else {
            AA.emit(type, resp);
        }
    };
};

var AA = $.extend(AA, aa_http);