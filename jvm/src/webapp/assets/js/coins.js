function CoinCtrl($scope) {
    $scope.coins = {};

    var socket = new SockJS('/sockjs');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '', function(frame) {
        console.log('Connected ' + frame);

        var id = stompClient.subscribe("/topic/recog/coin.*", function(message) {
            $scope.$apply(function() {
                $scope.coins = JSON.parse(message.body);
            });
        });

        var x = stompClient.send("/app/recog/h264", {"correlationId":"AA9229AF-792E-428D-B123-DAA2DC9EDC20"}, "blobule");
        console.log(x);

    }, function(error) {
        console.log("STOMP protocol error " + error);
    });

}
