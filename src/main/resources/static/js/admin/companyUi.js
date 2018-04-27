function displayCompanies(arr) {

    if (CURRENT_TABLE !== CATEGORY_TABLE) {
        addObjectTheads(arr[0]);
    }
    console.log(arr)
    var tags='';
    var comanyNames = [];
    Object.keys(arr).forEach(nr => {
        comanyNames.push(arr[nr]["companyName"]);
        tags += `<tr id="company_` + arr[nr]["companyName"] + `" value="` + arr[nr]["companyName"] + `">`;
        Object.keys(arr[nr]).forEach(key => {
            tags += `<td id="company_` + key + `_` + arr[nr][key] + `">` + arr[nr][key] + `</td>`;
        });

        ;
        tags += `</tr>`;
    });
    console.log(comanyNames);
    console.log(tags);
    $("#tbody").empty();
    $("#tbody").append(tags);
    comanyNames.forEach(function(company){
        $("#company_"+company).on("click",function () {
            //SET value to edit company and open edit
            $("#company_name").text(company);
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