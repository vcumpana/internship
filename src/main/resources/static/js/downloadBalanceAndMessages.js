$(document).ready(function () {
    isUnreadMessages();
    downloadBalance();
});

function downloadBalance(){
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").addClass("balance");
            $("#balance").text("Balance: " + result.balance.toFixed(2) + " USD");
        }
    });
}

function isUnreadMessages() {
    $.ajax({
        type: "GET",
        url: "/notification/getNumberOfUnread",
        success: function (result) {
            if (result > 0) {
                $("#unreadMessages").css('display', 'inline');
            }
        }
    });
}