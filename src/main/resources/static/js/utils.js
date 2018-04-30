var HOST="http://localhost:8080";
var STATUS=function(){};
STATUS.OK=200;
STATUS.BAD_REQUEST=400;
STATUS.CREATED=201;
STATUS.ALREADY_REPORTED=208;
STATUS.INTERNATAL_ISSUE=500;
function displayMessage(message) {
    var x = document.getElementById("snackbar");
    // Add the "show" class to DIV
    x.className = "show";
    x.innerText=message;
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
//INCLUDE snackbar css if you want to use utils;
$(document).ready(function () {
    var snackbar=`<div id="snackbar">Default Message</div>`;
    $("body").append(snackbar);
});