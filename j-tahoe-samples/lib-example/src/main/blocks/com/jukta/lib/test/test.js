AA.controller('button', function(el) {
    this.on = {};

    this.on["clk"] = function(e, next) {
        AA.http.emit("clk", {});
    }

    this.on["clkResponse"] = function(e, next) {
        alert(e);
    }

});