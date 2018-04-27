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
        var name = $("#company_name").val();
        //TODO add functionality
        if (name === '') {
            displayMessage("Please fill name");
        } else {
            displayMessage(name)
            //saveCategoryInDb(name)
        }
    });
});