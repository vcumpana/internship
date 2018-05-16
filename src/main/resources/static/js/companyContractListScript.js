var listOfContracts = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();
var currentPage = 1;
var size = 10;
var maxPageSize;

$(document).ready(function () {
    downloadContracts();
    isUnreadMessages();
    downloadBalance();
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
    verifyIfPreviousExists();
    verifyIfNextExists();
    $('#select_all').prop("checked", false);});

$("#previousPage").click(function () {
    currentPage--;
    downloadContracts();
    verifyIfPreviousExists();
    verifyIfNextExists();
    $('#select_all').prop("checked", false);});

$('#select_all').change(function() {
    var checkboxes = $(this).closest('form').find(':checkbox');
    checkboxes.prop('checked', $(this).is(':checked'));
});

function fillArray() {
    $("input[type='checkbox'][name='idInvoice']:checked").each(function(i, v) {
        arr.push($(v).attr("id"));
    });
}

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
            listOfContracts = result.contracts;
            maxPageSize=result.pages;
            //  listOfContracts.sort(comparatorForCategory);
            fillTableWithContracts();
            verifyIfPreviousExists();
            verifyIfNextExists();
        }
    });
}

function sendContractsForInvoicesCreation() {
    fillArray();
    data={
         info :arr
    };
    console.log(data);
    $.ajax({
        type: "POST",
        url: "/company/newinvoices/",
        data: {info :arr},
        success: function (result) {
            console.log(result["created"]);
            $('.modal-body').append("<p>Created invoices: " + result.created + " invoice</p>");
            $('.modal-body').append("<p>Skipped contracts: " + result.skipped + " contracts</p><br>");
            $('.modal-body').append("<p>Note: contracts are skipped from creating invoices if there exist issued invoices for current month");
        }
    });
    arr.length = 0;
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
    for (var i = 0; i < listOfContracts.length; i++) {
        var row = "<tr>";
        if (listOfContracts[i].contractStatus === "ACTIVE") {
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
        row += "<td>" + listOfContracts[i].servicePrice + "</td>";
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
            row += "<td><a href=" + "/contract/" + listOfContracts[i].id + "/approve" + ">Approve</a>" + "/" + "<a href=" + "/contract/" + listOfContracts[i].id + "/deny" + ">Deny</a></td>";
            row +="<td></td>";
        } else if (listOfContracts[i].contractStatus === "ACTIVE"){
            row +="<td></td>";
            row += "<td><a href=" + "/contract/"+ listOfContracts[i].id +"/createInvoice" + ">Create invoice</a></td>";
        } else {
            row +="<td></td>";
            row +="<td></td>";
        }
        row += "</tr>";
        $("#tableWithContracts tbody").append(row);
    }
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
    if (currentPage === maxPageSize||maxPageSize===0) {
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
            $("#balance").text(result.balance + " MDL");
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