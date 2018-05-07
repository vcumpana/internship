var page=0;
var size=5;
function getMessages() {
    if(page<0){
        page=0;
    }
    request = $.ajax({
        type: "GET",
        contentType: "application/json; charset=utf-8",
        url: HOST+"/admin/messages?page="+page+"&size="+size,
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Displaying messages..");
            //check if we are displaying categories now if yes then show it;;
            displayMessagesInUi(xhr.responseJSON);

            selectButton([$("#show_messages")]);

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
    console.log(HOST+"/admin/"+path+"/"+id)
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
        console.log(e);
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