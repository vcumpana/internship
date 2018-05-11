var HOST="http://localhost:8080";
var STATUS=function(){};
STATUS.OK=200;
STATUS.BAD_REQUEST=400;
STATUS.CREATED=201;
STATUS.ALREADY_REPORTED=208;
STATUS.INTERNATAL_ISSUE=500;
var currentTimeOut;
var futureShowing;
function displayMessage(message) {
    console.log("displayMessage:"+message)
    var x = document.getElementById("snackbar");
    if(currentTimeOut!=null){
        clearTimeout(currentTimeOut);
    }
    if(futureShowing!=null){
        clearTimeout(futureShowing);
    }
    // Add the "show" class to DIV
    x.className = "";
    x.innerText=message;
    futureShowing=setTimeout(function(){
        x.className="show";
        currentTimeOut=setTimeout(
            function(){ x.className = x.className.replace("show", ""); }
            , 3000)}
        , 200);
    // After 3 seconds, remove the show class from DIV
}
//INCLUDE snackbar css if you want to use utils;
$(document).ready(function () {
    var snackbar=`<div id="snackbar" class="hiden"></div>`;
    $("body").append(snackbar);
});