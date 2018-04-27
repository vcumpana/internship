//TODO change theader
function getCompanyFromDb(status) {

    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/companies?status="+status,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("all ok");
            //check if we are displaying categories now if yes then show it;;
            displayMessage(xhr.responseJSON);
            displayCompanies(xhr.responseJSON);
            //addCategoryInUi(xhr.responseJSON);
        }
    });
    request.error(function (e) {
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("bad request please contact ");
        } else {
            displayMessage("error , please try it latter");
        }
    })
}