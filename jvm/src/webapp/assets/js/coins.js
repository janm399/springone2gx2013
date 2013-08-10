function CoinCtrl($scope) {
    $scope.coins = {};

    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '', function(frame) {
        console.log('Connected ' + frame);

        var id = stompClient.subscribe("/topic/recog/coin.*", function(message) {
            $scope.$apply(function() {
                $scope.coins = message.body;
            });
        });
        console.log(id);

    }, function(error) {
        console.log("STOMP protocol error " + error);
    });

}
