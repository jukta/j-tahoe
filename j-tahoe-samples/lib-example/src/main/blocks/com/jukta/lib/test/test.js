SV.controller('button', function(el) {
    this.on = {};

    this.on["clk"] = function(e, next) {
        alert("You've clicked the button!!!");
    }

});