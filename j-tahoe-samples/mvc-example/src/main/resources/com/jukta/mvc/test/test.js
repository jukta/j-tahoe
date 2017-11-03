AA.controller("main", function(el) {
    var testTahoeController = AA.http.stub("testController", ["getMessage"]);

    testTahoeController.getMessage("John Doe").call(function(err, e) {
        console.log(e);
    });
});