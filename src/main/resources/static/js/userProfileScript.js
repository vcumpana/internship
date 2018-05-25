var listOfContracts = [];
var currentPage = 1;
var size = 10;
var maxPageSize;
var timer = null;

$(document).ready(function () {
    getDataForTable();
    isUnreadMessages();
    downloadBalance();
});

$(document).ajaxStart(function () {
    $("#pleaseWaitDialog").modal('show');
});

$(document).ajaxComplete(function () {
    $("#pleaseWaitDialog").modal('hide');
});

$("#activateFilter").click(function (event) {
    event.preventDefault();
    currentPage = 1;
    getDataForTable();
});

$("#nextPage").click(function () {
    currentPage++;
    getDataForTable();
});

$("#previousPage").click(function () {
    currentPage--;
    getDataForTable();
});

function getDataForTable() {
    var url = makeURL(currentPage);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            listOfContracts = result.contracts;
            maxPageSize = result.pages;
            setPages();
            fillTableWithContracts();
        }
    });
}

function makeURL(page) {
    var url = "/user/contracts?page=" + page + "&size=" + size + "&";
    var data = {
        "status": $("#status").val(),
        "companyId": $("#companyName").val(),
        "categoryId": $("#categoryName").val(),
        "fromStartDate": $("#fromStartDate").val(),
        "tillStartDate": $("#tillStartDate").val()
    };
    for (key in data) {
        if (data[key] !== "") {
            url += key + "=" + data[key] + "&";
        }
    }
    url = url.substring(0, url.length - 1);
    return url;
}

function fillTableWithContracts() {
    $("#tableWithContracts tbody").html("");
    for (var i = 0; i < listOfContracts.length; i++) {
        var row = "<tr id = 'contract" + listOfContracts[i].id + "'>";
        row += "<td>" + listOfContracts[i].categoryName + "</td>";
        row += "<td>" + listOfContracts[i].companyName + "</td>";
        row += "<td>" + listOfContracts[i].serviceTitle + "</td>";
        row += "<td>" + listOfContracts[i].servicePrice + " USD</td>";
        row += "<td>" + listOfContracts[i].startDate + "</td>";
        row += "<td>" + listOfContracts[i].endDate + "</td>";
        var status = listOfContracts[i].contractStatus;
        if (status === "SIGNEDBYCLIENT") {
            row += "<td class='text-warning'><strong>Waiting</strong></td>";
        } else if (status === "ACTIVE") {
            row += "<td class='text-success'><strong>Active</strong></td>";
        } else if (status === "DENIED") {
            row += "<td class='text-success'><strong>Denied</strong></td>";
        } else {
            row += "<td class='text-danger'><strong>Inactive</strong></td>";
        }
        row += "</tr>";
        $("#tableWithContracts tbody").append(row);
    }
    parseURL();
    verifyIfPreviousExists();
    verifyIfNextExists();
}

function resetContractFilter(event) {
    if (event !== null) {
        event.preventDefault();
    }
    $("#status").val("");
    $("#companyName").val("");
    $("#categoryName").val("");
    $("#fromStartDate").val("");
    $("#tillStartDate").val("");
    currentPage = 1;
    getDataForTable();
}

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
            $("#balance").text(result.balance + " USD");
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

$("#currentPageButton").click(function () {
    $("#currentPage").focus();
});

function parseURL() {
    var url = new URL(window.location);
    var id = url.searchParams.get("id");
    var found = false;
    if (id != null && id !== "null") {
        $("#tableWithContracts").find("tbody").find("tr").each(function () {
            if ($(this).attr("id") === ("contract" + id)) {
                found = true;
                $(this).addClass("table-success");
                $('html, body').animate({
                    scrollTop: $(this).offset().top
                }, 2000);
            }
            if (found) {
                return false;
            }
        });
        if (!found && currentPage < maxPageSize) {
            currentPage++;
            getDataForTable();
        } else if (!found && currentPage === maxPageSize) {
            window.history.pushState(null, null, '?id=null');
            currentPage = 1;
            getDataForTable();
        } else if (found) {
            window.history.pushState(null, null, '?id=null');
        }
    }
}