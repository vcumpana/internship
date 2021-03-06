var listOfStatements = [];
var sumOnTheStart = 0;
var sumOnTheEnd = 0;
var maxPageSize;
var currentPage = 1;
var activeAJAX = 0;
var timer;

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
    activeAJAX--;
    if(activeAJAX === 0) {
        $( "#pleaseWaitDialog" ).modal('hide');
    }
});

function downloadStatements(ev) {
    if(ev !== null) {
        ev.preventDefault();
        currentPage = 1;
    }
    var data = {
        "date": $("#startDate").val(),
        "dateTo": $("#endDate").val()
        //,
        //"pages": currentPage
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/bank/statements",
        data: JSON.stringify(data),
        beforeSend: function(){
          activeAJAX++;
        },
        success: function(result){
            sumOnTheStart = result.balanceBefore;
            listOfStatements = result.listOfTransactions;
            fillTableWithStatements();
            verifyIfPreviousExists();
            verifyIfNextExists();
            setPages();
        }
    });
}

$("#nextPage").click(function () {
    currentPage++;
    downloadStatements(null);
});

$("#previousPage").click(function () {
    currentPage--;
    downloadStatements(null);
});

function fillTableWithStatements() {

    $("#balanceBefore").text("Balance in the begin of period: " + sumOnTheStart.toFixed(2) + " USD");
    var before=sumOnTheStart;
    $("#tableWithStatements tbody").html("");
    if(listOfStatements.length !== 0) {
        for (var i = 0; i < listOfStatements.length; i++) {
            var row = "<tr>";
            var date = new Date(listOfStatements[i].date);
            row += "<td>" + moment(date).format("DD-MM-YYYY HH:mm:ss") + "</td>";
            if (listOfStatements[i].sum > 0) {
                row += "<td class='text-success'>+ " + listOfStatements[i].sum.toFixed(2) + " USD</td>";
            } else {
                row += "<td class='text-danger'>- " + Math.abs(listOfStatements[i].sum).toFixed(2) + " USD</td>";
            }

            row += "<td>" + listOfStatements[i].description + "</td>";
            row += "<td>" + before.toFixed(2) + " USD</td>";
            before+=listOfStatements[i].sum;
            row += "<td>" + before.toFixed(2) + " USD</td>";
            row += "</tr>";
            $("#balanceEnd").text("Balance on the end of period: " + before.toFixed(2) + " USD");
            $("#balanceEnd").append("</br></br>");
            $("#tableWithStatements tbody").append(row);
            
        }
    } else {
        var row = "<tr><td colspan='5'>No results found for this period</td></tr>";
        $("#tableWithStatements tbody").append(row);
    }
    // $("#balanceEnd").text("Balance on the end of period: " + sumOnTheEnd.toFixed(2) + " USD");
}

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        beforeSend: function(){
            activeAJAX++;
        },
        success: function (result) {
            $("#balance").addClass("balance");
            $("#balance").text("Balance: " + result.balance.toFixed(2) + " USD");
        }
    });
}

function fillDate(){
    var currentDay = new Date();
    var firstDay = new Date(currentDay.getFullYear(), currentDay.getMonth(), 1);
    currentDay.setDate(currentDay.getDate() + 1);
    $("#startDate").val(moment(firstDay).format("YYYY-MM-DD"));
    $("#endDate").val(moment(currentDay).format("YYYY-MM-DD"));
}

function isUnreadMessages() {
    $.ajax({
        type: "GET",
        url: "/notification/getNumberOfUnread",
        beforeSend: function(){
            activeAJAX++;
        },
        success: function (result) {
            if (result > 0) {
                $("#unreadMessages").css('display', 'inline');
            }
        }
    });
}

function setPages() {
    $("#currentPageButton").html("Page <input type='text' id='currentPage' style='width: 15%; text-align: center' min='1' max='" + maxPageSize + "'> from " + maxPageSize);
    $("#currentPage").val(currentPage);
}

$(document).on("input change paste", "#currentPage", function () {
    var page = $(this).val();
    $(this).val(page.replace(/[^\d]/, ''));
    if (/[^\d]/.test(page)) {
        return;
    }
    if(currentPage == page){
        return;
    }
    window.clearTimeout(timer);
    if (page < 1) {
        page = 1;
    }
    if (page > maxPageSize) {
        page = maxPageSize;
    }
    timer = window.setTimeout(function () {
        currentPage = page;
        downloadStatements(null);
    }, 1000);
});

$("#currentPageButton").click(function(){
    $("#currentPage").focus();
});

function verifyIfPreviousExists() {
    if (currentPage == 1) {
        $("#previousPage").addClass("disabled");
        $("#previousPage").attr("disabled", true);
    } else {
        $("#previousPage").removeClass("disabled");
        $("#previousPage").attr("disabled", false);
    }
}

function verifyIfNextExists() {
    if (currentPage == maxPageSize || maxPageSize === 0) {
        $("#nextPage").addClass("disabled");
        $("#nextPage").attr("disabled", true);
    } else {
        $("#nextPage").removeClass("disabled");
        $("#nextPage").attr("disabled", false);
    }
}