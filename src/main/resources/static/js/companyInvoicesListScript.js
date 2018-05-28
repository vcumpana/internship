var listOfInvoices = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();
var maxPage;
var currentPage = 1;
var size = 10;
var chekedAll = false;
var timer = null;

$(document).ready(function () {
    downloadInvoices();
    isUnreadMessages();
    downloadBalance();
    if($("#successCreating").text()!==''){
        $("#successCreating").show();
        window.setTimeout(function () {
            $("#successCreating").hide();
        }, 5000);
}
});

$(document).ajaxStart(function () {
    $("#pleaseWaitDialog").modal('show');
});

$(document).ajaxComplete(function () {
    $("#pleaseWaitDialog").modal('hide');
});

$("#activateFilter").click(function () {
    currentPage = 1;
    downloadInvoices();
});

$("#nextPage").click(function () {
    currentPage++;
    downloadInvoices();
});

$("#previousPage").click(function () {
    currentPage--;
    downloadInvoices();
});

$('#select_all').change(function() {
    if (chekedAll == false){
        chekedAll = true;
        getAllInvoicesIds();
    } else {
        chekedAll = false;
        arr.length = 0;
    }
    var checkboxes = $(this).closest('table').find(':checkbox');
    checkboxes.prop('checked', $(this).is(':checked'));
});

function getAllInvoicesIds() {
    $.ajax({
        type: "GET",
        url: "/company/invoices/allIds",
        success: function (result) {
            arr = [];
            arr = $.merge(arr, result);
        }
    });
}

function setCheckboxes(){
    for(var index in arr){
        $("input[type='checkbox'][name='idInvoice'][id='" + arr[index] + "']").each(function(){
            $(this).attr("checked", true);
        });
    }
}

$(document).on("click", "input[type='checkbox'][name='idInvoice']", function(){
    if($(this).is(":checked")){
        arr.push(parseInt($(this).attr("id")));
    } else {
        $("#select_all").prop("checked", false);
        chekedAll = false;
        arr.splice( $.inArray(parseInt($(this).attr("id")), arr), 1);
    }
});

function fillArray() {
    $("input[type='checkbox'][name='idInvoice']:checked").each(function (i, v) {
        arr.push($(v).attr("id"));
    });
}

function downloadInvoices() {
    var url = makeURL(currentPage);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            $('#checkbox').addClass('hidden');
            listOfInvoices = result.invoices;
            maxPage = result.pages;
            setPages();
            fillTableWithInvoices();
            setCheckboxes();
        }
    });
}

function checkInvoice() {
    $.ajax({
        type: "GET",
        url: "/checkInvoice/" + id,
        success: function (result) {
            $("#successSending").show();
            setTimeout(function () {
                $("#successSending").hide();
            }, 5000);
            downloadInvoices();
        }
    });
}

function sendInvoices() {
    if (arr.length == 0){
        $('#exampleModal .modal-title').text("Warning");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>Please select at least 1 invoice</p>");
        $('#exampleModal').modal("show");
        return;
    }
    data = {
        info: arr
    };
    $.ajax({
        type: "POST",
        url: "/company/sendinvoices/",
        data: {info: arr},
        success: function (result) {
            $('#exampleModal .modal-title').text("Succes!");
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-body').append("<p>Invoices have been successfully sent</p>");
            $('#exampleModal').modal("show");
            downloadInvoices();
        }
    });
    arr.length = 0;
}

function sendInvoice(id) {
    $.ajax({
        type: "POST",
        url: "/invoice/" + id + "/send",
        success: function (result) {
            $('#exampleModal .modal-title').text("Succes!");
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-body').append("<p>Invoice has been successfully sent</p>");
            $('#exampleModal').modal("show");
            downloadInvoices();
        }
    });
}

function cancelInvoice(id) {
    $.ajax({
        type: "POST",
        url: "/invoice/" + id + "/cancel",
        success: function (result) {
            $('#exampleModal .modal-title').text("Succes!");
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-body').append("<p>Invoice has been successfully deleted</p>");
            $('#exampleModal').modal("show");
            downloadInvoices();
        }
    });
}

function cancelInvoices() {
    if (arr.length == 0){
        $('#exampleModal .modal-title').text("Warning");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>Please select at least 1 invoice</p>");
        $('#exampleModal').modal("show");
        return;
    }
    data = {
        info: arr
    };
    $.ajax({
        type: "POST",
        url: "/company/cancelinvoices/",
        data: {info: arr},
        success: function (result) {
            $('#exampleModal .modal-title').text("Success!");
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-body').append("<p>Invoices have been successfully deleted</p>");
            $('#exampleModal').modal("show");
            downloadInvoices();
        }
    });
    arr.length = 0;
}

$("th[scopeForSort='sort']").click(function () {
    var field = $(this).attr("field");
    removeTypeOfSort(field);
    if (field === "category") {
        listOfServices.sort(comparatorForCategory);
    }
    if (field === "price") {
        listOfServices.sort(comparatorForPrice);
    }
    if ($(this).attr("typeOfSort") === "asc") {
        listOfServices.reverse();
        setArrowForSort(field, "desc");
        $(this).attr("typeOfSort", "desc");
    } else {
        setArrowForSort(field, "asc");
        $(this).attr("typeOfSort", "asc");
    }
    fillTableWithInvoices();
});

function makeURL(page) {
    var url = "/invoices?page=" + page + "&size=" + size + "&";
    var data = {
        "categoryId": $("#categoryName").val(),

        "order": $("#orderByDueDate").val(),
        "status": $("#invoiceStatus").val(),
        "fromStartDate": $("#fromStartDate").val(),
        "tillStartDate": $("#tillStartDate").val(),
        "fromTillDate": $("#fromTillDate").val(),
        "tillTillDate": $("#tillTillDate").val(),
        "fromDueDate": $("#fromDueDate").val(),
        "tillDueDate": $("#tillDueDate").val(),
        "usersFirstName": $("#usersFirstName").val(),
        "usersLastName": $("#usersLastName").val(),
        "serviceId" : $("#serviceName").val()
    };

    //TODO if
    if ($("#orderByDueDate").val() !== ""){
        data.orderBy = "PAYMENT_DATE";
        data.order=$("#orderByDueDate").val();
    }else{
        data.orderBy = "STATUS";
    }

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
    var paid = 0;
    var unpaid = 0;
    var overdue = 0;
    var dateText;
    for (var i = 0; i < listOfInvoices.length; i++) {
        var row = "<tr>";
        if (listOfInvoices[i].invoiceStatus === "CREATED") {
            $('#checkbox').removeClass('hidden');
            row += "<td><input type=\"checkbox\" name='idInvoice' id=\"" + listOfInvoices[i].invoiceId + "\"></td>";
        } else
            row += "<td></td>";
        row += "<td>" + listOfInvoices[i].invoiceId + "</td>";
        row += "<td>" + listOfInvoices[i].contractId + "</td>";
        row += "<td>" + listOfInvoices[i].userTitle + "</td>";
        row += "<td>" + listOfInvoices[i].serviceTitle + "</td>";
        dateText = moment(listOfInvoices[i].fromDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        dateText = moment(listOfInvoices[i].tillDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        dateText = moment(listOfInvoices[i].paymentDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        switch (listOfInvoices[i].invoiceStatus) {
            case "CREATED":
                row += "<td>-</td>";
                row += "<td>" + listOfInvoices[i].price + " USD</td>";
                unpaid += listOfInvoices[i].price;
                row += "<td>-</td>";
                row += "<td class='text-secondary'><strong>Created</strong></td>";
                break;
            case "PAID":
                row += "<td>" + listOfInvoices[i].price + " USD</td>";
                row += "<td>-</td>";
                row += "<td>-</td>";
                paid += listOfInvoices[i].price;
                row += "<td class='text-success'><strong>Paid</strong></td>";
                break;
            case "OVERDUE":
                row += "<td>-</td>";
                row += "<td>-</td>";
                row += "<td>" + listOfInvoices[i].price + " USD</td>";
                overdue += listOfInvoices[i].price;
                row += "<td class='text-danger'><strong>Overdue</strong></td>";
                break;
            case "SENT":
                row += "<td>-</td>";
                row += "<td>" + listOfInvoices[i].price + " USD</td>";
                unpaid += listOfInvoices[i].price;
                row += "<td>-</td>";
                row += "<td class='text-warning'><strong>Sent</strong></td>";
                break;
            default:
        }
        if (listOfInvoices[i].invoiceStatus === "CREATED") {
            row += "<td><button class=\"btn btn-primary btn-sm send\" style = \"display:inline;width: 55px\" data-id="+ listOfInvoices[i].invoiceId+" >Send</button>" +
                "<button class=\"btn btn-danger btn-sm cancel\" style = \"display:inline;width: 55px\" data-id="+ listOfInvoices[i].invoiceId+ " >Delete</button></td>";
            row += "<td><a class=\"btn btn-secondary btn-sm\" style = \"display:inline;width: 55px\" href=" + "/invoice/" + listOfInvoices[i].invoiceId + "/edit" + ">Edit</a></td>";
        } else {
            row += "<td>-</td>";
            row += "<td>-</td>";
        }
        row += "</tr>";
        $("#tableWithInvoices tbody").append(row);
    }
    var row = "<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td>" +
        "<td>Total per page</td><td>" + paid.toFixed(2) + " USD</td><td>" + unpaid.toFixed(2) + " USD</td><td>" + overdue.toFixed(2) + " USD</td><td></td><td></td><td></td></tr>";
    $("#tableWithInvoices tbody").append(row);
    verifyIfPreviousExists();
    verifyIfNextExists();
}

function comparatorForCategory(a, b) {
    if (a.category < b.category)
        return -1;
    if (a.category > b.category)
        return 1;
    return 0;
}

function comparatorForCompanyName(a, b) {
    if (a.companyName < b.companyName)
        return -1;
    if (a.companyName > b.companyName)
        return 1;
    return 0;
}

function comparatorForPrice(a, b) {
    if (a.price < b.price)
        return -1;
    if (a.price > b.price)
        return 1;
    return 0;
}

function removeTypeOfSort(value) {
    $("th[scopeForSort='sort']").each(function () {
        var field = $(this).attr("field");
        if (field !== value) {
            $(this).attr("typeOfSort", "null");
        }
    });
}

function setArrowForSort(value, type) {
    $("span[scopeForSort='icon']").each(function () {
        var field = $(this).attr("field");
        if (field === value) {
            if (type === "asc") {
                $(this).html(ascArrow);
            } else {
                $(this).html(descArrow);
            }
        } else {
            $(this).html("");
        }
    });
}

function resetInvoiceFilter() {
    $("#orderByDueDate").val("asc");
    $("#companyName").val("");
    $("#categoryName").val("");
    $("#fromStartDate").val("");
    $("#tillStartDate").val("");
    $("#fromTillDate").val("");
    $("#tillTillDate").val("");
    $("#fromDueDate").val("");
    $("#tillDueDate").val("");
    $("#usersFirstName").val("");
    $("#usersLastName").val("");
    $("#serviceName").val("");
    $("#invoiceStatus").val("");
    currentPage = 1;
    downloadInvoices();
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
            $("#balance").addClass("balance");
            $("#balance").text("Balance: " + result.balance.toFixed(2) + " USD");
        }
    });
}

$("#showFilters").click(function () {
    $("#filters").slideToggle();
});

$("#showSimpleFilters").click(function () {
    $("#simpleFilters").slideToggle();
});

$("#showDatesFilters").click(function () {
    $("#dateFilters").slideToggle();
});

function setPages() {
    $("#currentPageButton").html("Page <input type='text' id='currentPage' style='width: 15%; text-align: center' min='1' max='" + maxPage + "'> from " + maxPage);
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
    if (page > maxPage) {
        page = maxPage;
    }
    timer = setTimeout(function () {
        currentPage = page;
        downloadInvoices();
    }, 1000);
});

$("#currentPageButton").click(function () {
    $("#currentPage").focus();
});

function sendInvoicesConfirmation(){
    if (arr.length == 0){
        $('#exampleModal .modal-title').text("Warning");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>Please select at least 1 invoice</p>");
        $('#exampleModal').modal("show");
        return;
    }
    $('#modalSendInvoicesConfirm .modal-body').text("You are going to send "+ arr.length+" invoices to your clients");
    $('#modalSendInvoicesConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalSendInvoicesConfirm .modal-title').text("Confirm");
    $("#modalSendInvoicesConfirm").modal("show");
}

function deleteInvoicesConfirmation(){
    if (arr.length == 0){
        $('#exampleModal .modal-title').text("Warning");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>Please select at least 1 invoice</p>");
        $('#exampleModal').modal("show");
        return;
    }
    $('#modalDeleteInvoicesConfirm .modal-body').text("You are going to delete " + arr.length+" invoices");
    $('#modalDeleteInvoicesConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalDeleteInvoicesConfirm .modal-title').text("Confirm");
    $("#modalDeleteInvoicesConfirm").modal("show");
}

$(document).on("click", ".cancel", function () {
    var invoiceId = $(this).data('id');
    $("#modalDeleteOneInvoiceConfirm #invoiceId").val(invoiceId);
    $('#modalDeleteOneInvoiceConfirm .modal-body').text("You are going to delete an invoice");
    $('#modalDeleteOneInvoiceConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalDeleteOneInvoiceConfirm .modal-title').text("Confirm");
    $("#modalDeleteOneInvoiceConfirm").modal("show");
});

$(document).on("click", ".send", function () {
    var invoiceId = $(this).data('id');
    $("#modalSendOneInvoiceConfirm #invoiceId1").val(invoiceId);
    $('#modalSendOneInvoiceConfirm .modal-body').text("You are going to send an invoice");
    $('#modalSendOneInvoiceConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalSendOneInvoiceConfirm .modal-title').text("Confirm");
    $("#modalSendOneInvoiceConfirm").modal("show");
});

function clickModalYes(modalName){
    if (modalName === 'modalSendInvoicesConfirm'){
        sendInvoices();
    }
    if (modalName === 'modalDeleteInvoicesConfirm'){
        cancelInvoices();
    }
    if (modalName === 'modalDeleteOneInvoiceConfirm'){
        var id = $('#modalDeleteOneInvoiceConfirm #invoiceId').val();
        cancelInvoice(id);
    }
    if (modalName === 'modalSendOneInvoiceConfirm'){
        var id = $('#modalSendOneInvoiceConfirm #invoiceId1').val();
        sendInvoice(id);
    }
}

function clickModalNo(modalName){
    $("#modalConfirm").hide();
}