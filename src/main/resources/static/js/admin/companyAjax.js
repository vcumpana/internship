//TODO change theader
var currentStatus="";
var currentUser;
function getCompanyFromDb(companyStatus) {
    let url;
    if(currentStatus!==companyStatus||currentUser!=="company"){
        url=HOST+"/admin/companies?status="+companyStatus+"&page="+0;
    }else{
        url=HOST+"/admin/companies?status="+companyStatus+"&page="+currentPage
    }
    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: url,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        let pages = xhr.responseJSON.pages;
        if (status == STATUS.OK) {
            if (pages == 0) {
                displayMessage("There isn't any data");
                return;
            }
            displayMessage("Showing companies ...");
            if(companyStatus!==currentStatus){
                currentPage=0;
            }
            if(currentUser!=="company"){
                currentPage=0;
            }
            currentUser="company";
            maxPages=pages;
            //check if we are displaying categories now if yes then show it;;
            displayCompanies(xhr.responseJSON.companies);
            selectButton([$("#navbarDropdownMenuLink2"), $(getCompanyIdForStatus(companyStatus))]);
            deletePagination();
            currentStatus=companyStatus;
            if (maxPages > 1) {
                addPages("changeCompanyPage");
            }
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

function changeCompanyPage(page){
    currentPage=parseInt(page)-1;
    getCompanyFromDb(currentStatus)
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
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins ");
        } else {
            displayMessage("Error , please try it latter");
        }
    })

}