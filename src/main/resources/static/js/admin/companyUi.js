function displayCompanies(arr) {

    addObjectTheads(arr[0]);
    var tags='';
    var comanyNames = [];
    Object.keys(arr).forEach(nr => {
        comanyNames.push(arr[nr]["companyName"]);
        tags += `<tr value="` + arr[nr]["companyName"] + `">`;
        Object.keys(arr[nr]).forEach(key => {
            tags += `<td id="company_` + key + `">` + arr[nr][key] + `</td>`;
        });

        ;
        tags += `</tr>`;
    });
    $("#tbody").empty();
    $("#tbody").append(tags);
    comanyNames.forEach(function(company){
        $('tr[value="'+company+'"]').on("click",function (tr) {
            //SET value to edit company and open edit
            var status=$('tr[value="'+company+'"] #company_status').text();
            console.log($('#input_company_status input[value="'+status+'"]'));
            $('#input_company_status input[value="'+status+'"]').prop("checked", true);
            $("#company_name").text(company);
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