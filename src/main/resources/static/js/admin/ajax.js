function deleteCategoryFromDb(value) {
    var resp = $.ajax({
        url: CATEGORY_HOST.concat(value),
        type: 'DELETE', success: function (rs) {
            displayMessage(rs);
            deleteCategoryFromUi(rs)
            displayMessage("success");
        },
        error: function (s) {
            displayMessage("error deleting ");
        }
    });
}

function saveCategoryInDb() {
    request = $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: CATEGORY_HOST,
        data: JSON.stringify({"name": $("#name").val()}),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.CREATED) {
            displayMessage("all ok");
            //check if we are displaying categories now if yes then show it;;
            addCategoryInUi(xhr.responseJSON);
        }
    });
    request.error(function (e) {
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("bad request please contact ");
        } else {
            displayMessage("error , please try it latter");
        }
    })
}