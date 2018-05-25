var listOfInvoices = [];
var maxPageSize;
var currentPage = 1;
var size = 10;
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
            console.log(result);
            maxPageSize = result.pages
            listOfInvoices = result.invoices;
            fillTableWithInvoices();
            setPages();
        }
    });
}

function makeURL(page) {
    var url = "/invoices?page=" + page + "&size=" + size + "&";
    var data = {
        "companyId": $("#companyName").val(),
        "categoryId": $("#categoryName").val(),
        "orderBy": "PAYMENT_DATE",
        "order": $("#orderByDueDate").val(),
        "status": $("#invoiceStatus").val()
    };
    for (key in data) {
        if (data[key] !== "") {
            url += key + "=" + data[key] + "&";
        }
    }
    url = url.substring(0, url.length - 1);
    return url;
}

function fillTableWithInvoices() {
    $("#tableWithInvoices tbody").html("");
    for (var i = 0; i < listOfInvoices.length; i++) {
        var row = "<tr id='invoice" + listOfInvoices[i].invoiceId + "'>";
        row += "<td>";
        if(listOfInvoices[i].invoiceStatus === "SENT") {
            row += "<input type='checkbox' id='" + listOfInvoices[i].invoiceId + "'/>"
        }
        var newDate = new Date(listOfInvoices[i].fromDate);
        row+="</td>";
        row += "<td>" + listOfInvoices[i].companyTitle + "</td>";
        row += "<td>" + listOfInvoices[i].serviceTitle + "</td>";
        row += "<td>" + listOfInvoices[i].price + " USD</td>";
        row += "<td>" + listOfInvoices[i].fromDate + "</td>";
        row += "<td>" + listOfInvoices[i].tillDate + "</td>";
        row += "<td>" + listOfInvoices[i].paymentDate + "</td>";
        if(listOfInvoices[i].invoiceStatus === "SENT") {
            row += "<td class='text-warning' id='status" + listOfInvoices[i].invoiceId + "'><strong>To pay</strong></td>";
            row += '<td><button class="btn btn-primary" onclick="payInvoice(this)" id="' + listOfInvoices[i].invoiceId + '">Pay</button></td>';
        } else if(listOfInvoices[i].invoiceStatus === "PAID"){
            row += "<td class='text-success' id='status" + listOfInvoices[i].invoiceId + "'><strong>Paid</strong></td><td></td>";
        } else {
            row += "<td class='text-danger' id='status" + listOfInvoices[i].invoiceId + "'><strong>Overdue</strong></td><td></td>";
        }
        row += "</tr>";
        $("#tableWithInvoices tbody").append(row);
    }
    verifyIfPreviousExists();
    verifyIfNextExists();
    parseURL();
}

function resetInvoiceFilter(event) {
    event.preventDefault();
    $("#orderByDueDate").val("asc");
    $("#companyName").val("");
    $("#categoryName").val("");
    $("#invoiceStatus").val("");
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
            $("#balance").text(result.balance.toFixed(2) + " USD");
        }
    });
}

function payInvoice(element) {
    var id = element.id;
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "/invoice/payInvoice",
        data: JSON.stringify({id: id}),
        success: function (data) {
            invoicePaidUi(id);
            downloadBalance();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(jqXhr)
            var status = jqXhr.status;
            console.log(textStatus)
            console.log(errorThrown)
            console.log(jqXhr.responseText)
            var response = JSON.parse(jqXhr.responseText)
            if (jqXhr.responseText != null && response != null && response.message != null) {
                displayMessage(response.message)
            } else if (status == STATUS.BAD_REQUEST) {
                displayMessage("Bad request please contact admins ");
            } else {
                displayMessage("Error , please try it latter");
            }
        }
    })

}

function invoicePaidUi(id) {
    displayMessage("Invoice Nr: " + id + " Paid");
    $("#" + id).parent().parent().hide();
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

function parseURL() {
    var url = new URL(window.location);
    var id = url.searchParams.get("id");
    var found = false;
    if (id != null && id !== "null") {
        $("#tableWithInvoices").find("tbody").find("tr").each(function () {
            if ($(this).attr("id") === ("invoice" + id)) {
                console.log("here");
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