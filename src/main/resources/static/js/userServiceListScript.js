var listOfServices = [];
var currentCompanyName = "";
var currentServiceId;
var currentPage = 1;
var size = 10;
var maxPageSize;
var timer = null;

$(document).ready(function () {
    getDataForTable();
    isUnreadMessages();
    downloadBalance();
    $('[data-toggle="tooltip"]').tooltip();
});

$(document).ajaxStart(function () {
    $("#pleaseWaitDialog").modal('show');
});

$(document).ajaxComplete(function () {
    $("#pleaseWaitDialog").modal('hide');
});

$("#signContract").click(function () {
    var flag = true;
    var data = {
        "companyName": currentCompanyName,
        "serviceId": currentServiceId,
        "startDate": $("#startDate").val(),
        "endDate": $("#endDate").val()
    };
    flag = checkStartDate(data.startDate);
    flag = checkEndDate(data.startDate, data.endDate) && flag;
    if (flag) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/newContract",
            data: JSON.stringify(data),
            success: function () {
                $("#startDate").val("");
                $("#endDate").val("");
                $("#serviceInfo").modal('hide');
                $("#successContract").slideToggle();
                window.setTimeout(function () {
                    $("#successContract").slideToggle();
                }, 3500);
            },
            statusCode: {
                409: function () {
                    $("#serviceInfo").modal('hide');
                    $("#failContract").slideToggle();
                    window.setTimeout(function () {
                        $("#failContract").slideToggle();
                    }, 3500);
                }
            }
        });
    }
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

$("#downloadFile").click(function () {
    window.open("/services/getPDF");
});

function getDataForTable() {
    var url = makeURL(currentPage);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            listOfServices = result.services;
            maxPageSize = result.pages;
            fillTableWithServices();
            setPages();
        }
    });
}

function fillTableWithServices() {
    $("#tableWithServices tbody").html("");
    for (var i = 0; i < listOfServices.length; i++) {
        var row = "<tr><td>";
        if (listOfServices[i].companyUrl !== null && listOfServices[i].companyUrl !== '') {
            row += "<a href=\"http://" + listOfServices[i].companyUrl + "\">";
        }
        if (listOfServices[i].imageName !== null && listOfServices[i].imageName !== '') {
            row += "<img border=\"0\" alt=\"logo\" src=\"/image/" + listOfServices[i].imageName + "\" width=\"100\" height=\"40\">"
        }
        if (listOfServices[i].companyUrl !== null && listOfServices[i].companyUrl !== '') {
            row += "</a>";
        }

        row += "</td>";
        row += "<td>" + listOfServices[i].category + "</td>";
        row += "<td>" + listOfServices[i].companyName + "</td>";
        row += "<td>" + listOfServices[i].title + "</td>";
        row += "<td>" + listOfServices[i].description + "</td>";
        row += "<td>" + listOfServices[i].price + " USD</td>";
        row += "<td><i class=\"fa fa-paw filter\" onclick='showServiceInfo(" + listOfServices[i].id + ")'></i></td>";
        row += "</tr>";
        $("#tableWithServices tbody").append(row);
    }
    verifyIfPreviousExists();
    verifyIfNextExists();
}

function showServiceInfo(id) {
    $("#serviceInfo").modal('show');
    toNeutral("#feedbackStartDate", "#startDate");
    toNeutral("#feedbackEndDate", "#endDate");
    $.ajax({
        type: "GET",
        url: "/services/" + id,
        success: function (result) {
            currentServiceId = id;
            currentCompanyName = result.companyName;
            $("#categoryInModal").text(result.category);
            $("#companyInModal").text(result.companyName);
            $("#titleInModal").text(result.title);
            $("#priceInModal").text(result.price + " USD");
            $("#descriptionInModal").text(result.description);
        }
    });
}

function makeURL(page) {
    var url = "/services?page=" + page + "&size=" + size + "&";
    var data = {
        "min": $("#minPrice").val(),
        "max": $("#maxPrice").val(),
        "orderBy": "PRICE",
        "order": $("#orderByPrice").val(),
        "companyId": $("#companyName").val(),
        "categoryId": $("#categoryName").val()
    };
    for (key in data) {
        if (data[key] !== "") {
            url += key + "=" + data[key] + "&";
        }
    }
    url = url.substring(0, url.length - 1);
    return url;
}

function resetServiceFilter(event) {
    if (event !== null) {
        event.preventDefault();
    }
    $("#minPrice").val("");
    $("#maxPrice").val("");
    $("#orderByPrice").val("asc");
    $("#companyName").val("");
    $("#categoryName").val("");
    currentPage = 1;
    getDataForTable();
}

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
    if (currentPage == maxPageSize || maxPageSize == 0) {
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

function checkStartDate(startDateString) {
    if (startDateString === "") {
        toFail("#feedbackStartDate", "#startDate", "Field can't be empty!");
        return false;
    }
    var currentDate = new Date();
    var startDate = new Date(startDateString);
    if (currentDate > startDate) {
        toFail("#feedbackStartDate", "#startDate", "Start date should be in future!");
        return false;
    } else {
        toSuccess("#feedbackStartDate", "#startDate", "Looks good!");
        return true;
    }
}

function checkEndDate(startDateString, endDateString) {
    var flag = true;
    if (startDateString === "") {
        toFail("#feedbackStartDate", "#startDate", "Field can't be empty!");
        flag = false;
    }
    if (endDateString === "") {
        toFail("#feedbackEndDate", "#endDate", "Field can't be empty!");
        flag = false;
        return flag;
    }
    var startDate = new Date(startDateString);
    var endDate = new Date(endDateString);
    if (startDate > endDate) {
        toFail("#feedbackEndDate", "#endDate", "End date should be greater than start date!");
        flag = false;
    } else {
        toSuccess("#feedbackEndDate", "#endDate", "Looks good!");
    }
    return flag;
}


function toSuccess(feedback, input, message) {
    $(feedback).removeClass("invalid-feedback");
    $(feedback).addClass("valid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-invalid");
    $(input).addClass("is-valid");
}

function toFail(feedback, input, message) {
    $(feedback).removeClass("valid-feedback");
    $(feedback).addClass("invalid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-valid");
    $(input).addClass("is-invalid");
}

function toNeutral(feedback, input) {
    $(feedback).removeClass("invalid-feedback");
    $(feedback).removeClass("valid-feedback");
    $(feedback).html("");
    $(input).removeClass("is-invalid");
    $(input).removeClass("is-valid");
}

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").addClass("balance");
            $("#balance").text("Balance: " + result.balance.toFixed(2) + " USD");
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