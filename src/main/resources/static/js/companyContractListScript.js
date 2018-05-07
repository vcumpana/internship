var listOfContracts = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";

$(document).ready(function () {
    downloadContracts();
});

function downloadContracts() {
    $.ajax({
        type: "GET",
        url: "/company/contracts",
        success: function (result) {
            listOfContracts = result.contracts;
          //  listOfContracts.sort(comparatorForCategory);
            fillTableWithContracts();
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
    for (var i = 0; i < listOfContracts.length; i++) {
        var row = "<tr>";
        row += "<td>" + listOfContracts[i].id+ "</td>";
        row += "<td>" + listOfContracts[i].serviceTitle + "</td>";
        row += "<td>" + listOfContracts[i].categoryName + "</td>";
        row += "<td>" + listOfContracts[i].startDate + "</td>";
        row += "<td>" + listOfContracts[i].endDate + "</td>";
        row += "<td>" + listOfContracts[i].companyName + "</td>";
        row += "<td>" + listOfContracts[i].servicePrice + "</td>";
        row += "<td>" + listOfContracts[i].contractStatus + "</td>";
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