function SessionsCtrl($scope) {
    // initialization
    var rand = function(max) { return Math.floor((Math.random() * max) + 1); };
    $scope.sessions = [];
    $scope.generate = function() {
        var coins = [];
        for (var i = 0; i < rand(5); i++) {
            coins.push({'center':{'x':rand(640),'y':rand(480)},'radius':rand(80)});
        }
        $scope.sessions = [{'coins':coins,'succeeded':true}];
    };


    // Connect to the server on path /sockjs and then create the STOMP protocol lient
    var socket = new SockJS('/sockjs');
    var stompClient = Stomp.over(socket);
    stompClient.connect('', '',
        function(frame) {
            // receive notifications on the recog/sessions topic
            stompClient.subscribe("/topic/recog/sessions", function(message) {
                $scope.$apply(function() {
                    $scope.sessions = JSON.parse(message.body);
                });
                console.log("After apply");
            });
        },
        function(error) {
            console.log("STOMP protocol error " + error);
        }
    );

}

