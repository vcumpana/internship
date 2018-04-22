//Creates Dialog that adds categories
$(document).ready(function () {
    $(function () {
        $("#dialog").dialog({
            autoOpen: false
        });
        $("#add_categories").on("click", function () {
            $("#dialog").dialog("open");
        });
    });
// Validating Form Fields.....
    $("#submit").click(function (e) {
        var name = $("#name").val();
        if (name === '') {
            displayMessage("Please fill name");
        } else {
            saveCategoryInDb()
        }
    });
});