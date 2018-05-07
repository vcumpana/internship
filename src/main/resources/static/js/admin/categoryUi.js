function addCategoryInUi(category) {
    if (getCurrentTable()!==CATEGORY_TABLE) {
        return;
    }
    console.log(category);
    var categoryName=category['name'];
    var tr = document.createElement("tr");
    tr.id="tr_".concat(categoryName);
    var td = document.createElement("td");
    var input=document.createElement("input");
    input.value=categoryName;
    input.className="form-control btn-default";
    input.id="input_".concat(categoryName);
    td.appendChild(input);
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
    $("#tr_".concat(categoryName)).remove();
}

function editCategoryInUi(oldName,newName) {
    deleteFunc=getButton(oldName).onclick;
    changeValuesAndIds("tr",oldName,newName);
    changeValuesAndIds("input",oldName,newName);
    changeValuesAndIds("btn_change",oldName,newName);
    changeValuesAndIds("btn_delete",oldName,newName);
    getButton("btn_delete",newName).on("click",deleteCategory);
    getButton("btn_change",newName).on("click",changeCategory);
}

function changeValuesAndIds(tag,oldValue,newValue){
    $("#"+tag+"_"+oldValue).prop("value",newValue);
    $("#"+tag+"_"+oldValue).prop("id",tag+"_"+newValue);
}

function getButton(prefix,id){
    return $("#"+prefix+"_"+id);
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
    nameTd.appendChild(document.createTextNode('Service Categories'));

    var changeTd = document.createElement("th");
    changeTd.appendChild(document.createTextNode('Edit'));
    changeTd.classList=['pl-4'];
    var deleteTd = document.createElement("th");
    deleteTd.appendChild(document.createTextNode('Drop'));
    deleteTd.classList=['pl-4'];

    tr.appendChild(nameTd);
    tr.appendChild(changeTd);
    tr.appendChild(deleteTd);

    var thead = $("#thead");
    thead.empty();
    thead.prepend(tr);
}

var CATEGORY_TABLE=0;
var NOT_FOUND=-1;
function getCurrentTable(){
    if(getColumnNr()==3){
        return CATEGORY_TABLE;
    }
}