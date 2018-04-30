var listOfServices = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";
var currentCompanyName = "";
var currentServiceId;

$(document).ready(function () {
    downloadServices();
});

function downloadServices() {
    $.ajax({
        type: "GET",
        url: "/services",
        success: function (result) {
            listOfServices = result;
            listOfServices.sort(comparatorForCategory);
            fillTableWithServices();
        }
    });
}

$("th[scopeForSort='sort']").click(function () {
    var field = $(this).attr("field");
    removeTypeOfSort(field);
    if (field === "category") {
        listOfServices.sort(comparatorForCategory);
    }
    if (field === "companyName") {
        listOfServices.sort(comparatorForCompanyName);
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
    fillTableWithServices();
});

function fillTableWithServices() {
    $("#tableWithServices tbody").html("");
    for (var i = 0; i < listOfServices.length; i++) {
        var row = "<tr>";
        row += "<td>" + listOfServices[i].category + "</td>";
        row += "<td>" + listOfServices[i].companyName + "</td>";
        row += "<td>" + listOfServices[i].title + "</td>";
        row += "<td>" + listOfServices[i].description + "</td>";
        row += "<td>" + listOfServices[i].price + " MDL</td>";
        row += "<td><i class=\"fa fa-paw filter\" onclick='showServiceInfo(" + listOfServices[i].id + ")'></i></td>";
        row += "</tr>";
        $("#tableWithServices tbody").append(row);
    }
}

function showServiceInfo(id) {
    $("#serviceInfo").modal('show');
    $.ajax({
        type: "GET",
        url: "/services/" + id,
        success: function (result) {
            currentServiceId = id;
            currentCompanyName = result.companyName;
            $("#categoryInModal").text(result.category);
            $("#companyInModal").text(result.companyName);
            $("#titleInModal").text(result.title);
            $("#priceInModal").text(result.price + " MDL");
            $("#descriptionInModal").text(result.description);
        }
    });
}

$("#signContract").click(function () {
    var data = {
        "companyName": currentCompanyName,
        "serviceId": currentServiceId,
        "startDate": $("#startDate").val(),
        "endDate": $("#endDate").val()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/newContract",
        data: JSON.stringify(data),
        success: function () {
            $("#startDate").val("");
            $("#endDate").val("");
            $("#serviceInfo").modal('hide');
            $("#successContract").show();
            window.setTimeout(function () {
                $("#successContract").hide();
            }, 3500);
        }
    });
});

$("#activateFilter").click(function () {
    var data = {
        "min" : $("#minPrice").val(),
        "max" : $("#maxPrice").val(),
        "orderByPrice" : $("#orderByPrice").val(),
        "company" : $("#companyName").val(),
        "category" : $("#categoryName").val()
    };
    console.log(data);
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/services",
        data: JSON.stringify(data),
        success: function (result) {
            console.log(result);
        }
    });
});

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