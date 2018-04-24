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

function saveCategoryInDb(name) {
    request = $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: CATEGORY_HOST,
        data: JSON.stringify({"name": name}),
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

function changeCategoryInDb(oldCategoryName,newCategoryName){
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: CATEGORY_HOST.concat(oldCategoryName),
        data: JSON.stringify({"name": newCategoryName}),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("all ok");
            //check if we are displaying categories now if yes then show it;;
            editCategoryInUi(oldCategoryName,newCategoryName);
        }
    });
    request.error(function (e) {
        console.log(e);
        $("#input_"+oldCategoryName).val(oldCategoryName);
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("bad request please contact ");
        } else {
            displayMessage("error , please try it latter");
        }
    })
}