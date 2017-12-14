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
        if (type == null) return;
        var ct = e.getResponseHeader("content-type");
        if (ct && ct.indexOf("application/json") !== -1) {
            AA.emit(type, JSON.parse(resp));
        } else {
            AA.emit(type, resp);
        }
    };

    this.http.stub = function(beanName, methods) {
        var bean = {};
        for (var j = 0; j < methods.length; j++) {
            var method = methods[j];
            bean[method] = function() {
                var args = arguments;
                return {
                    call: function(handler) {
                        call(beanName, method, args, handler);
                    }
                }
            }
        }
        return bean;
    }

    var call = function(beanName, method, arg0, handler) {
       var args = {};
       for (var k=0; k < arg0.length; k++) {
            args['arg'+k] = typeof arg0[k] === 'object' ? JSON.stringify(arg0[k]) : arg0[k];
       }
       $.ajax({
           type: 'POST',
           url: '/__bean/' + beanName + '/' + method,
           data: args
       }).done(function(resp, q, e) {
           handler(null, resp);
       }).fail(function(err) {
           handler(err);
       });
    }
};

var AA = $.extend(AA, aa_http);