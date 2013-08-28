function SessionsCtrl($scope) {
    $scope.sessions = {};

    var socket = new SockJS('/sockjs');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '', function(frame) {
        console.log('Connected ' + frame);

        stompClient.subscribe("/topic/recog/sessions", function(message) {
            $scope.$apply(function() {
                $scope.sessions = JSON.parse(message.body);
            });
        });


    }, function(error) {
        console.log("STOMP protocol error " + error);
    });

}
