//Creates Dialog that adds categories
$(document).ready(function () {
    $(function () {
        $("#addCategoryDialog").dialog({
            autoOpen: false
        });
        $("#add_categories").on("click", function () {
            $("#addCategoryDialog").dialog("open");
        });
    });
// Validating Form Fields.....
    $("#add_submit").click(function (e) {
        var name = $("#add_name").val();
        if (name === '') {
            displayMessage("Please fill name");
        } else {
            saveCategoryInDb(name)
        }
    });
});