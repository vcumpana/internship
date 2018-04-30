var listOfContracts = [];

$(document).ready(function () {
    downloadContracts();
});

function downloadContracts() {
    $.ajax({
        type: "GET",
        url: "/user/contracts",
        success: function (result) {
            listOfContracts = result;
            fillTableWithContracts();
        }
    });
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

$("#activateFilter").click(function () {
    var url = "/user/contracts?";
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
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            listOfContracts = result;
            fillTableWithContracts();
        }
    });
});

function resetContractFilter(){
    $("#status").val("");
    $("#companyName").val("");
    $("#categoryName").val("");
    fillTableWithServices();
}