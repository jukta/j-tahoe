var aloha_core = new function() {

    var self = this;
    var controllerList = [];

    this.controller = function(name, func) {
        if (func != null && (typeof name === 'string' || name instanceof String)) {
            controllerList[name] = func;    
        } else {
            return name.jquery ? name[0].aController : name.aController;
        }
    };

    this.initControllers = function(el) {
        var clk = el.find('[a-click]').addBack('[a-click]');
        clk.unbind("click");
        clk.bind("click", onClick);
        var c = el.find('[a-controller]').addBack('[a-controller]');
        for (var i = c.length - 1; i >= 0 ; i--) {
            var func = controllerList[$(c[i]).attr('a-controller')];
            if (func) {
                c[i].emit = function(link, data) {
                    fire(this, link, data);
                };
                c[i].aController = new func(c[i]);
            }
        }
    };

    this.destroyControllers = function(el) {
        var c = el.find('[a-controller]').addBack('[a-controller]');
        for (var i = 0; i < c.length; i++) {
            var controller = c[i].aController;
            if (controller && controller.destroy) {
                controller.destroy();
            }
        }
        el.find('[a-click]').unbind("click");
    };

    this.html = function(el, html) {
        var _el = $(el);
        this.destroyControllers(_el);
        try {
            _el.html(html);
        } catch(e){};
        this.initControllers(_el);
    };

    var onClick = function(e) {
        var c = e.currentTarget;
        var link = $(c).attr('a-click');
        if (!fire(c, link, e)) cancelDefaultAction(e);
    };

    var fire = function(el, link, data) {
        var c = $(el).parents('[a-controller]');
        var next = true;
        for (var i = 0; i < c.length ; i++) {
            var cont = c[i].aController;
            if (!cont || !cont.on) continue;
            var handler = null;
            if (link && cont.on[link]) {
                handler = cont.on[link];
            } else if (typeof cont.on == 'function') {
                handler = cont.on;
            } else {
                continue;
            }

            next = false;
            handler(data, function() {
                next = true;
            });
            if (!next) break;

        }
        return next;
    };

    this.emit = function(link, data) {
        var c = $.find('[a-controller]');
        for (var i = 0; i < c.length; i++) {
            if (c[i].aController && c[i].aController.on && c[i].aController.on[link]) {
                c[i].aController.on[link](data, function() {});
            }
        }
    };

    var cancelDefaultAction = function(e) {
        var evt = e ? e:window.event;
        if (evt.preventDefault) evt.preventDefault();
        evt.returnValue = false;
        return false;
    };
};

$(document).ready(function() {
    AA.initControllers($('body'));
});

var AA = $.extend(AA, aloha_core);
