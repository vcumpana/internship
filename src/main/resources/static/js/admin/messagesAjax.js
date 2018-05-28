var page=0;
var size=10;
function getMessages() {
    if(page<0){
        page=0;
    }
    let url;
    if(currentUser!=="message"){
        url=HOST+"/admin/messages?page="+0+"&size="+size;
    }else{
        url=HOST+"/admin/messages?page="+currentPage+"&size="+size;
    }
    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: url,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        let pages = xhr.responseJSON.totalPages;
        if (status == STATUS.OK) {
            displayMessage("Displaying messages..");
            //check if we are displaying categories now if yes then show it;;
            deletePagination();
            if(currentUser!=="message"){
                currentPage=0;
            }
            currentUser="message";
            maxPages=pages;
            displayMessagesInUi(xhr.responseJSON);

            selectButton([$("#show_messages")]);
            if (maxPages > 1) {

                addPages("displayMessagesPage");
                //addCategoryInUi(xhr.responseJSON);
            }

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

function displayMessagesPage(page){
    currentPage=parseInt(page)-1;
    getMessages()
}

function markAsReadAjax(id){
    markAsReadAjax(id,true);
}

function markAsUnreadAjax(id){
    markAsReadAjax(id,false);
}


function markAsReadAjax(id,read){
    var path="markAsRead";
    var difMessage="read";
    if(read===false){
        path="markAsUnread";
        difMessage="unread";
    }
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/"+path+"/"+id,
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Message was marked as "+difMessage);
            $("tr[value=" + id + "] td[value='mark']").remove();

            if(read===false) {
                $("tr[value=" + id + "]").first().removeClass('alert-success');
                $("tr[value=" + id + "]").first().addClass('alert-warning');
                $("tr[value=" + id + "]").first().append(getReadButton())
            }else{
                $("tr[value=" + id + "]").first().addClass('alert-success');
                $("tr[value=" + id + "]").first().removeClass('alert-warning');
                $("tr[value=" + id + "]").first().append(getUnreadButton())
            }
            //check if we are displaying categories now if yes then show it;;
            //displayMessagesInUi(xhr.responseJSON);

            //addCategoryInUi(xhr.responseJSON);
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

function prevMessagePage(){
    page--;
    getMessages();
}

function nextMessagePage(){
    page++;
    getMessages();
}