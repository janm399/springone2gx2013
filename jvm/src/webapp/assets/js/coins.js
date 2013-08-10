function CoinCtrl($scope) {
    $scope.model = "";

    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '', function(frame) {
        console.log('Connected ' + frame);

        var id = stompClient.subscribe("/topic/recog/coin.*", function(message) {
            console.log("***");
            $scope.coins = message.body;
            console.log($scope.coins);
        });
        console.log(id);

    }, function(error) {
        console.log("STOMP protocol error " + error);
    });


    $scope.coins = function() {
        return "sdfsdf" + $scope.model;
    }

}
