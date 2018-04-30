//TODO change theader
function getCompanyFromDb(status) {

    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/companies?status="+status,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {

            displayMessage("Showing companies ...");
            //check if we are displaying categories now if yes then show it;;
            displayCompanies(xhr.responseJSON);
            //addCategoryInUi(xhr.responseJSON);
        }else{
            displayMessage("There isn't any data");
        }
    });
    request.error(function (e) {
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins");
        } else {
            displayMessage("Error , please try it latter");
        }
    })
}

function updateCompany(name,company){
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: HOST.concat("/admin/companies/").concat(encodeURIComponent(name)),
        data: JSON.stringify(company),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Company Updated");
            //check if we are displaying categories now if yes then show it;;
            editCompanyInUi(name,company);
            $("#editCompany").dialog("close");
        }
    });
    request.error(function (e) {
        console.log(e);
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins ");
        } else {
            displayMessage("Error , please try it latter");
        }
    })

}