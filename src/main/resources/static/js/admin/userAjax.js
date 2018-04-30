//TODO change theader
function getUsersFromDb(status) {

    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/users?status="+status,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Displaying users..");
            //check if we are displaying categories now if yes then show it;;
            displayUsersInUi(xhr.responseJSON);
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

function updateUser(name,data){
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: HOST.concat("/admin/users/").concat(encodeURIComponent(name)),
        data: JSON.stringify(data),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("User Updated");
            //check if we are displaying categories now if yes then show it;;
            editUserInUi(name,data);
            $("#editCompany").dialog("close");
        }
    });
    request.error(function (e) {
        console.log(e);
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins");
        } else {
            displayMessage("Error , please try it latter");
        }
    })

}