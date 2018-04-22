var HOST="http://localhost:8080";
var CATEGORY_HOST=HOST+"/category/";
var STATUS=function(){};
STATUS.OK=200;
STATUS.BAD_REQUEST=400;
STATUS.CREATED=201;
STATUS.ALREADY_REPORTED=208;
STATUS.INTERNATAL_ISSUE=500;
function displayMessage(message) {
    console.log(message);
}