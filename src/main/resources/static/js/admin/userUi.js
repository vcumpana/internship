function displayUsersInUi(arr) {
    addObjectTheads(arr[0]);
    console.log(arr)
    var tags='';
    var users = [];
    Object.keys(arr).forEach(nr => {
        users.push(arr[nr]["username"]);
        tags += `<tr value="` + arr[nr]["username"] + `">`;
        Object.keys(arr[nr]).forEach(key => {
            tags += `<td id="company_` + key + `">` + arr[nr][key] + `</td>`;
        });

        ;
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
            $('#editCompany #company_name').text(user);
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