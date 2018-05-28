function displayCompanies(arr) {
    var theadsForCompany={
        'Company':null,
        'Email':null,
        'Username':null,
        'Bank Account':null,
        'Address':null,
        'Company status':null
    };
    $("#thead").empty();
    $("#thead").append(`<tr><th>Company</th><th>Email</th><th>Username</th><th style="min-width: 150px">Bank Account</th><th style="min-width: 200px">Address</th><th>Status</th></tr>`)
    //addObjectTheads(theadsForCompany);
    var tags='';
    var comanyNames = [];
    Object.keys(arr).forEach(nr => {
        comanyNames.push(arr[nr]["username"]);
    tags += `<tr value="` + arr[nr]["username"] + `">`;
    //Object.keys(arr[nr]).forEach(key => {
    tags += `<td id="company_name">` + arr[nr]['companyName'] + `</td>`;
    tags += `<td id="company_surname">` + arr[nr]['email'] + `</td>`;
    tags += `<td id="company_username">` + arr[nr]['username'] + `</td>`;
        if(arr[nr]['bankAccount']===null||arr[nr]['bankAccount']===0){
            tags += `<td id="company_bankaccount">` + `-` + `</td>`;
        }else{
            tags += `<td id="company_bankaccount">` + arr[nr]['bankAccount'] + `</td>`;
        }
    tags += `<td id="company_address">` + arr[nr]['address'] + `</td>`;
    tags += `<td id="company_status">` + arr[nr]['status'] + `</td>`;
    //});
    tags += `</tr>`;
});

    $("#tbody").empty();
    $("#tbody").append(tags);
    comanyNames.forEach(function(company){
        $('tr[value="'+company+'"]').on("click",function (tr) {
            //SET value to edit company and open edit
            var status=$('tr[value="'+company+'"] #company_status').text();
            $('#input_company_status input[value="'+status+'"]').prop("checked", true);
            $("#edit_company_name").text(company);
            $("#editCompany").dialog('option', 'title', "Edit company");
            //$("#input_company_status").text(company);
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

function editCompanyInUi(name,data){
    var statusTd=$('tr[value="'+name+'"] #company_status');
    statusTd.text(data['status']);
}