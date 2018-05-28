//TODO change theader
var maxPages;
var currentPage=0;
var currentStatus;
function getUsersFromDb(userStatus) {
    var url;
    if(currentStatus!==userStatus ||currentUser!=="user"){
        url=HOST+"/admin/users?status="+userStatus+"&page="+0;
    }else{
        url=HOST+"/admin/users?status="+userStatus+"&page="+currentPage
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
            if(currentUser!=="user"){
                currentPage=0;
            }
            currentUser="user";
            if(userStatus!=currentStatus){
                currentPage=0;
            }
            maxPages=pages;
            displayMessage("Displaying users..");
            //check if we are displaying categories now if yes then show it;;
            displayUsersInUi(xhr.responseJSON.users);
            selectButton([$("#navbarDropdownMenuLink1"), $(getUsersIdForStatus(userStatus))]);

            currentStatus=userStatus;
            deletePagination();
            if (maxPages > 1) {

                addPages("changeUserPage");
                //addCategoryInUi(xhr.responseJSON);
            }
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
};
function deletePagination(){
    if($("#pages")!=null){
        if($("#pages").parent()!=null){
            $("#pages").parent().remove();
        }
    }
}
function addPages(method){

    var tags=""
    var from=parseInt(((currentPage)/5))*5 +1;
    if(from+4<maxPages){
        to=from+4;
    }else{
        to=maxPages;
    }
    var to;
    if(parseInt(from/5)!==0){
        tags+=`<li class="page-item">
      <a class="page-link" href="#" aria-label="Previous" onclick="`+method+`(`+(from-1)+`)">
        &laquo;
      </a>
    </li>`;
    }
    for(let i=from;i<=to;i++){
        if(i===currentPage+1){
            tags+="<li class=\"page-item\"><a class=\"page-link\">"+i+"</a></li>"
        }else{
            tags+="<li class=\"page-item\"><a class=\"page-link\" href='#' onclick='"+method+`(`+i+")'>"+i+"</a></li>"
        }
    }

    if(parseInt(to/5)!==parseInt(maxPages/5)){
        tags+=` <li class="page-item">
      <a class="page-link" href="#" aria-label="Next" onclick="`+method+`(`+(to+1)+`)">
        &raquo;
      </a>
    </li>`;
    }


    $("table").parent().append("<div class=\"text-center mb-8\">\n" +
        "        <nav id='pages'><div class=\"pagination justify-content-center\">" +
        tags+
        "        </div></nav>\n" +
        "    </div>");

}

function changeUserPage(page){
    currentPage=parseInt(page)-1;
    getUsersFromDb(currentStatus)

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
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins");
        } else {
            displayMessage("Error , please try it latter");
        }
    })

}