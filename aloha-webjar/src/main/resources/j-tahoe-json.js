var j_tahoe_json = new function() {

    var str = "";

    var it = function (sch, cb) {
        for (var i in sch) if (sch.hasOwnProperty(i)) cb(i, sch[i]);
    };

    var j2h = function(sch, name) {
        if (sch._name) {
            str += "<" + sch._name;
            it(sch._attrs, function(name, value) {
                str += " " + name + "=\"" + value + "\"";
            });
            str += ">\n";
        } else if (!sch._) {
            str += sch + "\n";
            return;
        }
        it(sch._, function(name, value) {
            j2h(value, name);
        });
        if (sch._name) str += "</" + sch._name + ">\n";
    };
    
    this.j2h = function(sch) {
        str = "";
        j2h(sch);
        return str;
    }
    
}
var SV = $.extend(SV, j_tahoe_json);
