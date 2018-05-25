var listOfStatements = [];
var sumOnTheStart = 0;
var sumOnTheEnd = 0;
var maxPageSize;
var currentPage = 1;

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
        "dateTo": $("#endDate").val(),
        "pages": currentPage
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/bank/statements",
        data: JSON.stringify(data),
        success: function(result){
            console.log(result.listOfTransactions);
            sumOnTheStart = result.balanceBefore;
            sumOnTheEnd = result.balanceAfter;
            listOfStatements = result.listOfTransactions;
            maxPageSize = result.pages;
            fillTableWithStatements(result.balanceBeforeCurrentPage);
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

function fillTableWithStatements(currentSum) {
    $("#balanceBefore").text("Balance on the beginning of period: " + sumOnTheStart.toFixed(2) + " USD");
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
            row += "<td>" + currentSum.toFixed(2) + " USD</td>";
            currentSum += listOfStatements[i].sum;
            row += "<td>" + currentSum.toFixed(2) + " USD</td>";
            row += "</tr>";
            $("#tableWithStatements tbody").append(row);
        }
    } else {
        var row = "<tr><td colspan='4'>No results found for this period</td></tr>";
        $("#tableWithStatements tbody").append(row);
    }
    $("#balanceEnd").text("Balance on the end of period: " + sumOnTheEnd.toFixed(2) + " USD");
}

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").text(result.balance.toFixed(2) + " USD");
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
    clearTimeout(timer);
    if (page < 1) {
        page = 1;
    }
    if (page > maxPageSize) {
        page = maxPageSize;
    }
    timer = setTimeout(function () {
        currentPage = page;
        getDataForTable();
    }, 1000);
});

$("#currentPageButton").click(function(){
    $("#currentPage").focus();
});

function verifyIfPreviousExists() {
    if (currentPage === 1) {
        $("#previousPage").addClass("disabled");
        $("#previousPage").attr("disabled", true);
    } else {
        $("#previousPage").removeClass("disabled");
        $("#previousPage").attr("disabled", false);
    }
}

function verifyIfNextExists() {
    if (currentPage === maxPageSize || maxPageSize === 0) {
        $("#nextPage").addClass("disabled");
        $("#nextPage").attr("disabled", true);
    } else {
        $("#nextPage").removeClass("disabled");
        $("#nextPage").attr("disabled", false);
    }
}