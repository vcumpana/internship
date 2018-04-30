function deleteCategoryFromDb(value) {
    var resp = $.ajax({
        url: HOST.concat("/admin/category/").concat(value),
        type: 'DELETE', success: function (rs) {
            deleteCategoryFromUi(rs)
            displayMessage("Category Deleted");
        },
        error: function (s) {
            displayMessage("Can't delete,most it's like already used");
        }
    });
}

function saveCategoryInDb(name) {
    request = $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: HOST.concat("/admin/category"),
        data: JSON.stringify({"name": name}),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.CREATED) {
            displayMessage("Category Saved");
            //check if we are displaying categories now if yes then show it;;
            addCategoryInUi(xhr.responseJSON);
        }
    });
    request.error(function (e) {
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact Admins");
        } else {
            displayMessage("Error , please try it latter");
        }
    })
}

function getAllCategoriesFromDb(){

    var resp = $.ajax({
        url: HOST.concat("/category"),
        type: 'GET', success: function (rs) {
            createCategoryThead();
            $("#tbody").empty();
            rs.forEach(function(category){
                addCategoryInUi(category);
            });
            displayMessage("Showing categories ...");
        },
        error: function (s) {
            displayMessage("Error retrieving categories");
        }
    });

}
function changeCategoryInDb(oldCategoryName, newCategoryName) {
    request = $.ajax({
        type: "PUT",
        contentType: "application/json; charset=utf-8",
        url: HOST.concat("/admin/category/").concat(oldCategoryName),
        data: JSON.stringify({"name": newCategoryName}),
        dataType: "json"
    });
    request.done(function (response, textStatus, xhr) {
        status = xhr.status;
        if (status == STATUS.OK) {
            displayMessage("Name Changed");
            //check if we are displaying categories now if yes then show it;;
            editCategoryInUi(oldCategoryName, newCategoryName);
        }
    });
    request.error(function (e) {
        console.log(e);
        $("#input_" + oldCategoryName).val(oldCategoryName);
        status = e.status;
        if (status == STATUS.BAD_REQUEST) {
            displayMessage("Bad request please contact admins");
        } else {
            displayMessage("Error , please try it latter");
        }
    })
}