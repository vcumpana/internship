function getUnreadButton(){
    return `<td id="message_mark"  value='mark' class="btn btn-primary" style="vertical-align: central;" onclick="markAsUnread(this)">Mark as unread</td>`;
}

function getReadButton(){
    return `<td id="message_mark" value='mark' class="btn btn-primary" style="vertical-align: center" onclick="markAsRead(this)">Mark as read</td>`;
}

function displayMessagesInUi(data){
    $("#tbody").empty();
    $("#thead").empty();
    console.log(data);

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
    console.log(arr)
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

        var dateText=moment(arr[nr]['date']).format("hh:mm DD/MM/YYYY")
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

    $("#tbody").append(`<tr>
                                    <td></td>
                                    <td><button class=\"btn btn-primary btn-lg disabled\" id="previousPage" onclick="prevMessagePage()" >Previous</button></td>
                                    <td><button class=\"btn btn-primary btn-lg disabled\" id="nextPage" onclick="nextMessagePage()">Next</button></td>
                               <td></td></tr>
                           `);
    if(page==0){
        $("#previousPage").attr('disabled','disabled');
    }else{
        $("#previousPage").removeClass('disabled')
    }

    if(page>=data.totalPages-1){
        $("#nextPage").attr('disabled','disabled');
    }else{
        $("#nextPage").removeClass('disabled')
    }
}

function markAsRead(ele){
    console.log(ele.parentNode);
    var text=ele.parentNode.getAttribute('value');
    displayMessage("trying to mark as read");
    markAsReadAjax(text);
}

function markAsUnread(ele){
    console.log(ele.parentNode);
    var text=ele.parentNode.getAttribute('value');
    displayMessage("trying to mark as unread");
    markAsUnreadAjax(text);
}