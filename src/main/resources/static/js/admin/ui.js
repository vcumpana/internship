function addCategoryInUi(category) {
    if (getCurrentTable()!==CATEGORY_TABLE) {
        return;
    }
    console.log(category);
    var categoryName=category['name'];
    var tr = document.createElement("tr");
    tr.id="tr_".concat(categoryName);
    var td = document.createElement("td");
    td.appendChild(document.createTextNode(categoryName));
    tr.appendChild(td);

    var changeTd = document.createElement("td");
    var changeButton=createChangeCategoryButton(categoryName);
    changeTd.appendChild(changeButton);

    var deleteTd = document.createElement("td");
    var deleteButton=createDeleteCategoryButton(categoryName);
    deleteTd.appendChild(deleteButton);

    tr.appendChild(changeTd);
    tr.appendChild(deleteTd);
    var tbody = $("#tbody");
    tbody.prepend(tr);
}

function createDeleteCategoryButton(value){
    var deleteButton=document.createElement("button")
    deleteButton.appendChild(document.createTextNode('Delete'));
    deleteButton.value=value;
    deleteButton.className="btn btn-default";
    deleteButton.id="btn_delete_".concat(value);
    deleteButton.onclick=deleteCategory;
    return deleteButton;
}

function createChangeCategoryButton(value){
    var changeButton=document.createElement("button")
    changeButton.appendChild(document.createTextNode('Change'));
    changeButton.value=value;
    changeButton.className="btn btn-default";
    changeButton.id="btn_change_".concat(value);
    changeButton.onclick=changeCategory;
    return changeButton;
}

function deleteCategoryFromUi(category){
    var categoryName=category[0]['name'];
    displayMessage(categoryName);
    $("#tr_".concat(categoryName)).remove();
}

function createUserThead() {

}

function createCompanyThead() {

}

function getCurrentTable(){
    if(getColumnNr()==3){
        return CATEGORY_TABLE;
    }
    return NOT_FOUND;
}

function getColumnNr() {
    if ($("#thead") != null) {
        if ($("#thead").children() != null) {
            if ($("#thead").children().children() != null) {
                return $("#thead").children().children().length;
            }
        }
        return 0;
    }
}

function createCategoryThead() {
    var tr = document.createElement("tr");
    var nameTd = document.createElement("th");
    nameTd.appendChild(document.createTextNode('name'));

    var changeTd = document.createElement("th");
    changeTd.appendChild(document.createTextNode('change'));

    var deleteTd = document.createElement("th");
    deleteTd.appendChild(document.createTextNode('delete'));

    tr.appendChild(nameTd);
    tr.appendChild(changeTd);
    tr.appendChild(deleteTd);

    var thead = $("#thead");
    thead.empty();
    thead.prepend(tr);
}

var CATEGORY_TABLE=0;
var NOT_FOUND=-1;