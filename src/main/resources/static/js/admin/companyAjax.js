//TODO change theader
function getCompanyFromDb(companyStatus) {
    console.log(companyStatus);
    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/companies?status="+companyStatus,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {

            displayMessage("Showing companies ...");
            //check if we are displaying categories now if yes then show it;;
            displayCompanies(xhr.responseJSON);
//            $("#navbarDropdownMenuLink2").addClass("btn-dark");
            // $("#navbarDropdownMenuLink2").text(getTextForStatus(companyStatus));
            selectButton([$("#navbarDropdownMenuLink2"),$(getCompanyIdForStatus(companyStatus))]);
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
function getTextForStatus(status){
    if(status==""){
        return " ALL Companies";
    }else if(status=='WAITING'){
        return "Not Verified Companies"
    }else if (status=='DENIED'){
        return "Denied Companies"
    }else{
        return "";
    }
}

function getCompanyIdForStatus(status){
    if(status==""){
        return "#all_companies";
    }else if(status=='WAITING'){
        return "#not_verified_companies"
    }else if (status=='DENIED'){
        return "#denied_companies"
    }else{
        return "";
    }
}

function updateCompany(name,company){
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: "/admin/companies/".concat(encodeURIComponent(name)),
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