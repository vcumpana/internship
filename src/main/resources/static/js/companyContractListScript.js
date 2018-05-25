var listOfContracts = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();
var currentPage = 1;
var size = 10;
var maxPage;
var chekedAll = false;
var timer = null;

$(document).ready(function () {
    downloadContracts();
    isUnreadMessages();
    downloadBalance();
    chekedAll = false;
});

$( document ).ajaxStart(function() {
    $( "#pleaseWaitDialog" ).modal('show');
});

$( document ).ajaxComplete(function() {
    $( "#pleaseWaitDialog" ).modal('hide');
});

$("#activateFilter").click(function (event) {
    event.preventDefault();
    currentPage = 1;
    downloadContracts();
});

$("#nextPage").click(function () {
    currentPage++;
    downloadContracts();
});

$("#previousPage").click(function () {
    currentPage--;
    downloadContracts();
});

$('#select_all').change(function() {
    if (chekedAll == false){
        chekedAll = true;
        getAllContractsIds();
    } else {
        arr.length = 0;
        chekedAll = false;
    }
    var checkboxes = $(this).closest('form').find(':checkbox');
    checkboxes.prop('checked', $(this).is(':checked'));
});

function makeURL(page) {
    var url = "/company/contracts?page=" + page + "&size=" + size + "&";
    var data = {
        "categoryId": $("#categoryName").val(),
        "orderByEndDate": $("#orderByEndDate").val(),
        "status": $("#contractStatus").val(),
        "fromStartDate": $("#fromStartDate").val(),
        "tillStartDate": $("#tillStartDate").val(),
        "fromEndDate": $("#fromTillDate").val(),
        "tillEndDate": $("#tillTillDate").val(),
        "usersFirstName": $("#usersFirstName").val(),
        "usersLastName": $("#usersLastName").val(),
        "serviceId" : $("#serviceName").val()
    };
    for (key in data) {
        if (data[key] !== "") {
            url += key + "=" + data[key] + "&";
        }
    }
    url = url.substring(0, url.length - 1);
    return url;
}
function downloadContracts() {
    var url = makeURL(currentPage);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            $('#checkbox').addClass('hidden');
            listOfContracts = result.contracts;
            maxPage=result.pages;
            setPages();
            //  listOfContracts.sort(comparatorForCategory);
            fillTableWithContracts();
            setCheckboxes();
        }
    });
}

function getAllContractsIds() {
    $.ajax({
        type: "GET",
        url: "/company/contracts/allIds",
        success: function (result) {
            arr = [];
            arr = $.merge(arr, result);
            //  listOfContracts.sort(comparatorForCategory);
        }
    });
}

function sendContractsForInvoicesCreation() {
    data={
         info :arr
    };
    $.ajax({
        type: "POST",
        url: "/company/newinvoices/",
        data: {info :arr},
        success: function (result) {
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-title').text("Invoice creation report");
            $('#exampleModal .modal-body').append("<p>Created invoices: " + result.created + " invoice</p>");
            $('#exampleModal .modal-body').append("<p>Skipped contracts: " + result.skipped + " contracts</p><br>");
            $('#exampleModal .modal-body').append("<p>Note: contracts are skipped from creating invoices if there exist issued invoices for current month or the contract has the status other than Active");
            $("#exampleModal").modal("show");
        }

    });
}

$("th[scopeForSort='sort']").click(function () {
    var field = $(this).attr("field");
    removeTypeOfSort(field);
    if(field === "category"){
        listOfServices.sort(comparatorForCategory);
    }
    if(field === "price"){
        listOfServices.sort(comparatorForPrice);
    }
    if($(this).attr("typeOfSort") === "asc"){
        listOfServices.reverse();
        setArrowForSort(field, "desc");
        $(this).attr("typeOfSort", "desc");
    } else {
        setArrowForSort(field, "asc");
        $(this).attr("typeOfSort", "asc");
    }
    fillTableWithServices();
});

function fillTableWithContracts() {
    $("#tableWithContracts tbody").html("");
    var dateText;
    var today = new Date();
    for (var i = 0; i < listOfContracts.length; i++) {
        var row = "<tr>";
        if (listOfContracts[i].availForInvoice === true) {
            $('#checkbox').removeClass('hidden');
            row += "<td><input type=\"checkbox\" name='idInvoice' id=\"" + listOfContracts[i].id + "\"></td>";
        } else
            row +="<td></td>";
        //row +="<td><input type=\"checkbox\"></td>";
        row += "<td>" + listOfContracts[i].id+ "</td>";
        row += "<td>" + listOfContracts[i].serviceTitle + "</td>";
        row += "<td>" + listOfContracts[i].categoryName + "</td>";
        dateText=moment(listOfContracts[i].startDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        dateText=moment(listOfContracts[i].endDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        row += "<td>" + listOfContracts[i].fullName + "</td>";
        row += "<td>" + listOfContracts[i].servicePrice + " USD</td>";
        switch (listOfContracts[i].contractStatus){
            case "SIGNEDBYCLIENT":
                row += "<td class='text-warning'><strong>Waiting</strong></td>";
                break;
            case "ACTIVE":
                row += "<td class='text-success'><strong>Active</strong></td>";
                break;
            case "DENIED":
                row += "<td class='text-danger'><strong>Denied</strong></td>";
                break;
            case "EXPIRED":
                row += "<td class='text-dark'><strong>Expired</strong></td>";
                break;
            case "CANCELED":
                row += "<td class='text-danger'><strong>Canceled</strong></td>";
                break;
            default:
        }
        if (listOfContracts[i].contractStatus === "SIGNEDBYCLIENT") {
            row += "<td><a href=\"" + "/contract/" + listOfContracts[i].id + "/approve\" "
                + " role=\"button\" class=\"btn btn-warning btn-sm \" style = \"display:inline;width: 71px\">Approve</a>"
                + "<a href=" + "/contract/" + listOfContracts[i].id + "/deny"
                + " role=\"button\" class=\"btn btn-danger btn-sm \" style = \"display:inline;width: 71px\">Deny</a></td>";
            row +="<td>-</td>";
        } else if (listOfContracts[i].contractStatus === "ACTIVE" && listOfContracts[i].availForInvoice === true){
            row +="<td>-</td>";
            row += "<td><a href=" + "/contract/"+ listOfContracts[i].id +"/createInvoice"
                + " role=\"button\" class=\"btn btn-success btn-sm \" >Create invoice</a></td>";
        } else {
            row +="<td>-</td>";
            row +="<td>-</td>";
        }
        row += "</tr>";
        $("#tableWithContracts tbody").append(row);
    }
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

function comparatorForPrice(a, b){
    if (a.price < b.price)
        return -1;
    if (a.price > b.price)
        return 1;
    return 0;
}

function removeTypeOfSort(value){
    $("th[scopeForSort='sort']").each(function () {
        var field = $(this).attr("field");
        if(field !== value){
            $(this).attr("typeOfSort", "null");
        }
    });
}

function setArrowForSort(value, type) {
    $("span[scopeForSort='icon']").each(function () {
        var field = $(this).attr("field");
        if(field === value){
            if(type === "asc") {
                $(this).html(ascArrow);
            } else {
                $(this).html(descArrow);
            }
        } else {
            $(this).html("");
        }
    });
}

function resetContractFilter() {
    $("#contractStatus").val(""),
    $("#orderByStartDate").val("asc");
    $("#categoryName").val("");
    $("#serviceName").val("")
    $("#fromStartDate").val("");
    $("#tillStartDate").val("");
    $("#fromTillDate").val("");
    $("#tillTillDate").val("");
    $("#usersFirstName").val("");
    $("#usersLastName").val("");
    currentPage = 1;
    downloadContracts();
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
    if (currentPage === maxPage||maxPage===0) {
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

function downloadBalance(){
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").text(result.balance + " USD");
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
        downloadContracts();
    }, 1000);
});

$("#currentPageButton").click(function () {
    $("#currentPage").focus();
});

function createInvoicesConfirmation(){
    if (arr.length == 0){
        $('#exampleModal .modal-title').text("Warning");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>Please select at least 1 contract</p>");
        $("#exampleModal").modal("show");

        return;
    }
    $('#modalCreateInvoicesConfirm .modal-body').text("You are going to create invoices on all selected contracts.");
    $('#modalCreateInvoicesConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalCreateInvoicesConfirm .modal-title').text("Confirm");
    $("#modalCreateInvoicesConfirm").modal("show");
}

function clickModalYes(){
    sendContractsForInvoicesCreation();
}
