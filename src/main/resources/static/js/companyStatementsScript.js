var listOfStatements = [];

$(document).ready(function () {
    isUnreadMessages();
    downloadBalance();
});

function downloadStatements(ev) {
    ev.preventDefault();
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

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").text(result.balance.toFixed(2) + " MDL");
        }
    });
}