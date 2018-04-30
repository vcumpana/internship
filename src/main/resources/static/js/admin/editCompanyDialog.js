//Creates Dialog that adds categories
$(document).ready(function () {
    $(function () {
        $("#editCompany").dialog({
            autoOpen: false
        });
        // $("#edit_company").on("click", function () {
        //     $("#edit_company").dialog("open");
        // });
    });
// Validating Form Fields.....
    $("#edit_company").click(function (e) {
        var name = $("#editCompany #company_name").text();
        var status=$("#input_company_status input:checked").val();
        var password=$("#input_company_password").val();
        var repeatedPassword=$("#input_company_repeated_password").val();
        //TODO add functionality
        if (password !== repeatedPassword) {
            displayMessage("Password should match");
        }else {
            var data;
            if(password==''){
                data={'status':status};
            }else{
                data={
                    'status':status,
                    'password':password,
                    'confirmPassword':repeatedPassword
                }
            }
            if($("#editCompany").dialog( "option", "title" )=="Edit user") {
                updateUser(name,data)
            }else {
                updateCompany(name, data);
            }
        }
    });
});