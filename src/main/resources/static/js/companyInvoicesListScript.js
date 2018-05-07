var listOfInvoices = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();

$(document).ready(function () {
    downloadInvoices();
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

function downloadInvoices() {
    $.ajax({
        type: "GET",
        url: "/invoices/",
        success: function (result) {
            listOfInvoices = result.invoices;
            //  listOfContracts.sort(comparatorForCategory);
            fillTableWithInvoices();
        }
    });
}

function sendInvoices() {
    fillArray();
    data={
         info :arr
    };
    console.log(data);
    $.ajax({
        type: "POST",
        url: "/company/sendinvoices/",
        data: {info :arr},
        success: function (result) {
            $("#successSending").show();
            window.setTimeout(function () {
                $("#successSending").hide();
            }, 5000);
            downloadInvoices();
        }
    });
    arr.length = 0;
}

function cancelInvoices() {
    fillArray();
    data={
        info :arr
    };
    console.log(data);
    $.ajax({
        type: "POST",
        url: "/company/cancelinvoices/",
        data: {info :arr},
        success: function (result) {
            $("#successCancel").show();
            window.setTimeout(function () {
                $("#successCancel").hide();
            }, 5000);
            downloadInvoices();
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
    fillTableWithInvoices();
});

function fillTableWithInvoices() {
    $("#tableWithInvoices tbody").html("");
    for (var i = 0; i < listOfInvoices.length; i++) {
        var row = "<tr>";
        if (listOfInvoices[i].invoiceStatus === "CREATED"){
            row +="<td><input type=\"checkbox\" name='idInvoice' id=\"" + listOfInvoices[i].invoiceId + "\"></td>";
        } else
            row +="<td></td>";
        row += "<td>" + listOfInvoices[i].invoiceId+ "</td>";
        row += "<td>" + listOfInvoices[i].userTitle + "</td>";
        row += "<td>" + listOfInvoices[i].price + "</td>";
        row += "<td>" + listOfInvoices[i].serviceTitle + "</td>";
        row += "<td>" + listOfInvoices[i].fromDate + "</td>";
        row += "<td>" + listOfInvoices[i].tillDate + "</td>";
        row += "<td>" + listOfInvoices[i].paymentDate + "</td>";
        row += "<td>" + listOfInvoices[i].invoiceStatus + "</td>";
        if (listOfInvoices[i].invoiceStatus === "CREATED") {
            row += "<td><a href=" + "/invoice/" + listOfInvoices[i].invoiceId + "/send" + ">Send</a>" + "/" + "<a href=" + "/invoice/" + listOfInvoices[i].invoiceId + "/cancel" + ">Cancel</a></td>";
            row += "<td><a href=" + "/invoice/"+ listOfInvoices[i].invoiceId +"/edit" + ">Edit</a></td>";
        } else {
            row +="<td></td>";
            row +="<td></td>";
        }
        row += "</tr>";
        $("#tableWithInvoices tbody").append(row);
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