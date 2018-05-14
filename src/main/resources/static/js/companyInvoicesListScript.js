var listOfInvoices = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var arr = new Array();

$(document).ready(function () {
    downloadInvoices();
    downloadBalance();
});

$('#select_all').change(function() {
    var checkboxes = $(this).closest('table').find(':checkbox');
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

function sendInvoice(id) {
    $.ajax({
        type: "GET",
        url: "/invoice/" + id +"/send",
        success: function (result) {
            $("#successSending").show();
            setTimeout(function () {
                $("#successSending").hide();
            }, 5000);
            downloadInvoices();
        }
    });
}

function cancelInvoice(id) {
    $.ajax({
        type: "GET",
        url: "/invoice/" + id +"/cancel",
        success: function (result) {
            $("#successCanceling").show();
            setTimeout(function () {
                $("#successCanceling").hide();
            }, 5000);
            downloadInvoices();
        }
    });
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
    var dateText;
    for (var i = 0; i < listOfInvoices.length; i++) {
        var row = "<tr>";
        if (listOfInvoices[i].invoiceStatus === "CREATED"){
            row +="<td><input type=\"checkbox\" name='idInvoice' id=\"" + listOfInvoices[i].invoiceId + "\"></td>";
        } else
            row +="<td></td>";
        row += "<td>" + listOfInvoices[i].invoiceId + "</td>";
        row += "<td>" + listOfInvoices[i].contractId + "</td>";
        row += "<td>" + listOfInvoices[i].userTitle + "</td>";
        row += "<td>" + listOfInvoices[i].price + " MDL</td>";
        row += "<td>" + listOfInvoices[i].serviceTitle + "</td>";
        dateText=moment(listOfInvoices[i].fromDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        dateText=moment(listOfInvoices[i].tillDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        dateText=moment(listOfInvoices[i].paymentDate).format("DD/MM/YYYY");
        row += "<td>" + dateText + "</td>";
        switch (listOfInvoices[i].invoiceStatus){
            case "CREATED":
                row += "<td class='text-secondary'><strong>Created</strong></td>";
                break;
            case "PAID":
                row += "<td class='text-success'><strong>Paid</strong></td>";
                break;
            case "OVERDUE":
                row += "<td class='text-danger'><strong>Overdue</strong></td>";
                break;
            case "SENT":
                row += "<td class='text-warning'><strong>Sent</strong></td>";
                break;
            default:
        }
        if (listOfInvoices[i].invoiceStatus === "CREATED") {
            row += "<td><button onclick=\"sendInvoice("+listOfInvoices[i].invoiceId+")\">Send</button><button onclick=\"cancelInvoice("+listOfInvoices[i].invoiceId+")\">Cancel</button></td>";
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

function downloadBalance(){
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").text(result.balance + " MDL");
        }
    });
}