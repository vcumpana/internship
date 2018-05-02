var listOfContracts = [];
var currentPage = 1;
var size = 10;

$(document).ready(function () {
    getDataForTable();
});

$("#activateFilter").click(function () {
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
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            listOfContracts = result;
            fillTableWithContracts();
        }
    });
    verifyIfPreviousExists();
    verifyIfNextExists();
}

function makeURL(page){
    var url = "/user/contracts?page=" + page + "&size=" + size+"&";
    var data = {
        "status" : $("#status").val(),
        "companyId" : $("#companyName").val(),
        "categoryId" : $("#categoryName").val()
    };
    for(key in data){
        if(data[key] !== ""){
            url += key + "=" + data[key] + "&";
        }
    }
    url = url.substring(0, url.length - 1);
    return url;
}

function fillTableWithContracts() {
    $("#tableWithContracts tbody").html("");
    for (var i = 0; i < listOfContracts.length; i++) {
        var row = "<tr>";
        row += "<td>" + listOfContracts[i].categoryName + "</td>";
        row += "<td>" + listOfContracts[i].companyName + "</td>";
        row += "<td>" + listOfContracts[i].serviceTitle + "</td>";
        row += "<td>" + listOfContracts[i].servicePrice + " MDL</td>";
        row += "<td>" + listOfContracts[i].startDate + "</td>";
        row += "<td>" + listOfContracts[i].endDate + "</td>";
        var status = listOfContracts[i].contractStatus;
        if(status === "SIGNEDBYCLIENT"){
            row += "<td class='text-warning'><strong>Waiting</strong></td>";
        } else if (status === "ACTIVE"){
            row += "<td class='text-success'><strong>Active</strong></td>";
        } else {
            row += "<td class='text-danger'><strong>Inactive</strong></td>";
        }
        row += "<td><i class=\"fa fa-paw filter\" onclick='showServiceInfo(" + listOfContracts[i].id + ")'></i></td>";
        row += "</tr>";
        $("#tableWithContracts tbody").append(row);
    }
}

function resetContractFilter(){
    $("#status").val("");
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
    var url = makeURL(currentPage + 1);
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            if (result.length === 0) {
                $("#nextPage").addClass("disabled");
                $("#nextPage").attr("disabled", true);
            } else {
                $("#nextPage").removeClass("disabled");
                $("#nextPage").attr("disabled", false);
            }
        }
    });
}