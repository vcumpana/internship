var listOfStatements = [];

$(document).ready(function () {
    downloadBalance();
    fillDate();
    downloadStatements(null);
    isUnreadMessages();
});

$( document ).ajaxStart(function() {
    $( "#pleaseWaitDialog" ).modal('show');
});

$( document ).ajaxComplete(function() {
    $( "#pleaseWaitDialog" ).modal('hide');
});

function downloadStatements(ev) {
    if(ev !== null) {
        ev.preventDefault();
    }
    var data = {
        "date": $("#startDate").val(),
        "dateTo": $("#endDate").val()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/bank/statements",
        data: JSON.stringify(data),
        success: function(result){
            listOfStatements = result;
            fillTableWithStatements();
        }
    });
}

function fillTableWithStatements() {
    $("#tableWithStatements tbody").html("");
    if(listOfStatements.length !== 0) {
        for (var i = 0; i < listOfStatements.length; i++) {
            var row = "<tr>";
            var date = new Date(listOfStatements[i].date);
            row += "<td>" + moment(date).format("DD-MM-YYYY HH:mm") + "</td>";
            if (listOfStatements[i].sum > 0) {
                row += "<td class='text-success'>+ " + listOfStatements[i].sum + " MDL</td>";
            } else {
                row += "<td class='text-danger'>- " + Math.abs(listOfStatements[i].sum) + " MDL</td>";
            }
            row += "<td>" + listOfStatements[i].description + "</td>";
            row += "<td></td>";
            row += "</tr>";
            $("#tableWithStatements tbody").append(row);
        }
    } else {
        var row = "<tr><td colspan='4'>No results found for this period</td></tr>";
        $("#tableWithStatements tbody").append(row);
    }
}

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").text(result.balance + " MDL");
        }
    });
}

function fillDate(){
    var currentDay = new Date();
    var firstDay = new Date(currentDay.getFullYear(), currentDay.getMonth(), 1);
    $("#startDate").val(moment(firstDay).format("YYYY-MM-DD"));
    $("#endDate").val(moment(currentDay).format("YYYY-MM-DD"));
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