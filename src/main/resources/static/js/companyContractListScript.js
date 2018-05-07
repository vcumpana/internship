var listOfContracts = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();

$(document).ready(function () {
    downloadContracts();
    isUnreadMessages();
});

$('#select_all').change(function() {
    var checkboxes = $(this).closest('form').find(':checkbox');
    checkboxes.prop('checked', $(this).is(':checked'));
});

function fillArray() {
    $("input[type='checkbox'][name='idInvoice']:checked").each(function(i, v) {
        arr.push($(v).attr("id"));
    });
}

function downloadContracts() {
    $.ajax({
        type: "GET",
        url: "/company/contracts/",
        success: function (result) {
            listOfContracts = result.contracts;
            //  listOfContracts.sort(comparatorForCategory);
            fillTableWithContracts();
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
            $("#successCreation").show();
            window.setTimeout(function () {
                $("#successCreation").hide();
            }, 5000);
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
        row += "<td>" + listOfContracts[i].startDate + "</td>";
        row += "<td>" + listOfContracts[i].endDate + "</td>";
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