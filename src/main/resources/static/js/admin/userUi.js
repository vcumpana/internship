function displayUsersInUi(arr) {
    var theadsForUser={
        'Name':null,
        'Surname':null,
        'Username':null,
        'Email':null,
        'User status':null
    };
    addObjectTheads(theadsForUser);
    console.log(arr)
    var tags='';
    var users = [];
    Object.keys(arr).forEach(nr => {
        users.push(arr[nr]["username"]);
        tags += `<tr value="` + arr[nr]["username"] + `">`;
        //Object.keys(arr[nr]).forEach(key => {
        tags += `<td id="company_name">` + arr[nr]['name'] + `</td>`;
        tags += `<td id="company_surname">` + arr[nr]['surname'] + `</td>`;
        tags += `<td id="company_username">` + arr[nr]['username'] + `</td>`;
        tags += `<td id="company_email">` + arr[nr]['email'] + `</td>`;
        tags += `<td id="company_status">` + arr[nr]['status'] + `</td>`;
        //});
        tags += `</tr>`;
    });
    $("#tbody").empty();
    $("#tbody").append(tags);
    users.forEach(function(user){
        $('tr[value="'+user+'"]').on("click",function (tr) {
            //SET value to edit company and open edit
            var status=$('tr[value="'+user+'"] #company_status').text();
            $('#input_company_status input[value="'+status+'"]').prop("checked", true);
            console.log(user);
            console.log("wtf2");
            $("#edit_company_name").text(user);
            console.log("wtf2");
            //$("#input_company_status").text(company);
            $("#editCompany").dialog('option', 'title', "Edit user");
            $("#editCompany").dialog("open");

        });
    })

}

function addObjectTheads(data) {
    var tr = document.createElement("tr");
    Object.keys(data).forEach( key=> {
        var nameTd = document.createElement("th");
        nameTd.appendChild(document.createTextNode(key));
        tr.appendChild(nameTd);
    });
    var thead = $("#thead");
    thead.empty();
    thead.prepend(tr);
}

function editUserInUi(name,data){
    var statusTd=$('tr[value="'+name+'"] #company_status');
    statusTd.text(data['status']);
}