var listOfServices = [];
var ascArrow = "<i class=\"fa fa-arrow-down\"></i>";
var descArrow = "<i class=\"fa fa-arrow-up\"></i>";

$(document).ready(function () {
    downloadServices();
    isUnreadMessages();
    downloadBalance();
    if ($('#message').length !== 0){
        $('#exampleModal .modal-title').text("Succes!");
        $('#exampleModal .modal-body').text('');
        $('#exampleModal .modal-body').append("<p>"+$("#message").val()+"</p>");
        $('#exampleModal').modal("show");
    }
});

function downloadServices() {
    $.ajax({
        type: "GET",
        url: "/" + $("#companyName").text() + "/services/",
        success: function (result) {
            listOfServices = result;
            listOfServices.sort(comparatorForCategory);
            fillTableWithServices();
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

function deleteService(id) {
    $.ajax({
        type: "DELETE",
        url: "/service/" + id,
        success: function (result) {
            $('#exampleModal .modal-title').text("Succes!");
            $('#exampleModal .modal-body').text('');
            $('#exampleModal .modal-body').append("<p>Service has been successfully deleted</p>");
            $('#exampleModal').modal("show");
            downloadServices();
        }
    });
}

$(document).on("click", ".cancel", function () {
    var invoiceId = $(this).data('id');
    $("#modalDeleteServiceConfirm #serviceId").val(invoiceId);
    // As pointed out in comments,
    // it is superfluous to have to manually call the modal.
    // $('#addBookDialog').modal('show');
    $('#modalDeleteServiceConfirm .modal-body').text("You are going to delete a service");
    $('#modalDeleteServiceConfirm .modal-body').append("<p>Please confirm!</p><br>");
    $('#modalDeleteServiceConfirm .modal-title').text("Confirm");
    $("#modalDeleteServiceConfirm").modal("show");
});

function clickModalYes() {
        var id = $('#modalDeleteServiceConfirm #serviceId').val();
        deleteService(id);
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

function fillTableWithServices() {
    $("#tableWithServices tbody").html("");
    for (var i = 0; i < listOfServices.length; i++) {
        var row = "<tr>";
        row += "<td>" + listOfServices[i].category.name + "</td>";
        row += "<td>" + listOfServices[i].title + "</td>";
        row += "<td>" + listOfServices[i].description + "</td>";
        row += "<td>" + listOfServices[i].price + " USD</td>";
        row += "<td>" + listOfServices[i].numberOfContracts + "</td>";
        if (listOfServices[i].numberOfContracts == 0){
            row += "<td><a role = \"button\" style = \"display: inline-block;;width: 58px\" class=\"btn btn-info btn-sm send\" href=\"/service/"+ listOfServices[i].id+"/edit\" data-id="+ listOfServices[i].id+" >Edit</a>" +
                "<button class=\"btn btn-danger btn-sm cancel\" style = \"display:inline;width: 58px\" data-id="+ listOfServices[i].id+ " >Delete</button></td>";

        } else {
            row += "<td>-</td>";
        }
        row += "</tr>";
        $("#tableWithServices tbody").append(row);
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