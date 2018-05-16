var listOfInvoices = [];
var maxPage;
var currentPage = 1;
var size = 10;

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
    verifyIfPreviousExists();
    verifyIfNextExists();
});

$("#previousPage").click(function () {
    currentPage--;
    getDataForTable();
    verifyIfPreviousExists();
    verifyIfNextExists();
});

function getDataForTable() {
    var url = makeURL(currentPage);
    console.log(url);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            maxPage = result.pages
            listOfInvoices = result.invoices;
            fillTableWithInvoices();
            verifyIfPreviousExists();
            verifyIfNextExists();
        }
    });
}

function makeURL(page) {
    var url = "/invoices?page=" + page + "&size=" + size + "&";
    var data = {
        "companyId": $("#companyName").val(),
        "categoryId": $("#categoryName").val(),
        "orderByDueDate": $("#orderByDueDate").val(),
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
        console.log(listOfInvoices[i]);
        var row = "<tr>";
        row += "<td>" + listOfInvoices[i].companyTitle + "</td>";
        row += "<td>" + listOfInvoices[i].serviceTitle + "</td>";
        row += "<td>" + listOfInvoices[i].price + "</td>";
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
}

function resetInvoiceFilter(event) {
    event.preventDefault();
    $("#orderByDueDate").val("asc");
    $("#companyName").val("");
    $("#categoryName").val("");
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
    if (currentPage === maxPage || maxPage === 0) {
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
            $("#balance").text(result.balance + " MDL");
        }
    });
}

function payInvoice(element) {
    var id = element.id;
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "/invoice/payInvoice",
        data: JSON.stringify({id: parseFloat(id)}),
        success: function (data) {
            $("#balance").text(data.balance);
            invoicePaidUi(id);
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
    $("#" + id).hide();
    $("#status" + id).text("PAID");
}