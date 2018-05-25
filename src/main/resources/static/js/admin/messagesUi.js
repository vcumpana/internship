function getUnreadButton(){
    return `<td id="message_mark"  value='mark' class="btn btn-primary" style="vertical-align: central;" onclick="markAsUnread(this)">Mark as unread</td>`;
}

function getReadButton(){
    return `<td id="message_mark" value='mark' class="btn btn-primary" style="vertical-align: center" onclick="markAsRead(this)">Mark as read</td>`;
}

function displayMessagesInUi(data){
    $("#tbody").empty();
    $("#thead").empty();
    if(page<0){
        page=0;
    }
    var theadsForCompany={
        'Email':null,
        'Text':null,
        'Date':null,
        'Mark as Read':null
    };
    addObjectTheads(theadsForCompany);
    arr=data.content;
    var tags='';
    var messageIds = [];
    Object.keys(arr).forEach(nr => {
        messageIds.push(arr[nr]["id"]);
        var aditText="";
        if(arr[nr]["read"]){
            aditText="alert-success"
        }else{
            aditText="alert-warning"
        }

        var dateText=moment(arr[nr]['date']).format("HH:mm DD/MM/YYYY")
        tags += `<tr value="` + arr[nr]["id"] + `" class="main `+aditText+`">`;
        //Object.keys(arr[nr]).forEach(key => {
        tags += `<td id="message_email">` + arr[nr]['fromEmail'] + `</td>`;
        tags += `<td id="message_text">` + arr[nr]['text'] + `</td>`;
        tags += `<td id="message_date">` + dateText + `</td>`;
        if(!arr[nr]['read']) {
            tags += getReadButton();
        }else{
            tags += getUnreadButton();
        }
        //});
        tags += `</tr>`;
    });


    $("#tbody").empty();
    $("#tbody").append(tags);

}

function markAsRead(ele){
    var text=ele.parentNode.getAttribute('value');
    displayMessage("trying to mark as read");
    markAsReadAjax(text);
}

function markAsUnread(ele){
    var text=ele.parentNode.getAttribute('value');
    displayMessage("trying to mark as unread");
    markAsUnreadAjax(text);
}