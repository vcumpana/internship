//TODO change theader
function getUsersFromDb(userStatus) {

    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/users?status="+userStatus,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Displaying users..");
            //check if we are displaying categories now if yes then show it;;
            displayUsersInUi(xhr.responseJSON);
            selectButton([$("#navbarDropdownMenuLink1"),$(getUsersIdForStatus(userStatus))]);
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

function getUsersIdForStatus(status){
    if(status==""){
        return "#all_users";
    }else if(status=='WAITING'){
        return "#not_verified_users"
    }else if (status=='DENIED'){
        return "#denied_users"
    }else{
        return "";
    }
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