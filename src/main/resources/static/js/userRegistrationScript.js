var patternForLogin = /^[A-Za-z\d]{8,}$/;
var patternForPassword = /^(?=.*[A-Z])(?=.*\d)(?=.*[._?!])[A-Za-z\d._?!]{8,}$/;
var patternForMail = /^[a-z0-9._%+]+@[a-z0-9.]+\.[a-z]{2,4}$/;
var successMessage = "<strong>Success!</strong> Looks good!";
var errorMessageLogin = "<strong>Warning!</strong> Login must contain at least 8 chars, that includes letters and numbers."
var errorMessagePassword = "<strong>Warning!</strong> Password must contain at least 8 chars, that includes at least one number, upper case character and symbol.";
var errorMessageRepeatedPassword = "<strong>Warning!</strong> Password must be the same.";
var errorMessageMail = "<strong>Warning!</strong> Insert a valid email address";

$("#login").keyup(function () {
    $("#feedbackLoginFromServer").css('display', 'none');
    validateLogin(this);
})

$("#password").keyup(function () {
    $("#feedbackPasswordFromServer").css('display', 'none');
    validatePassword(this);
});

$("#repeatedPassword").keyup(function () {
    $("#feedbackRepeatedPasswordFromServer").css('display', 'none');
    checkPasswords();
});

$("#email").keyup(function () {
    $("#feedbackEmailFromServer").css('display', 'none');
    validateEmail(this);
});

$("#showPassword").click(function(){
    convertPasswordIntoTextAndInverse("#password", this);
});

$("#showRepeatedPassword").click(function(){
    convertPasswordIntoTextAndInverse("#repeatedPassword", this);
});

$("#resetButton").click(function () {
    toNeutral("#feedbackPassword", "#password");
    toNeutral("#feedbackRepeatedPassword", "#repeatedPassword");
});

$("#registerUser").click(function(event){
    var errors = 0;
    if(validatePassword("#password") == false){
        errors++;
    }
    if(checkPasswords() == false){
        errors++;
    }
    if(validateLogin("#login") == false){
        errors++;
    }
    if(errors > 0){
        event.preventDefault();
    }
});

function validateLogin(input){
    var feedback = "#feedbackLogin";
    var value = $(input).val();
    if(value.match(patternForLogin) !== null){
        toSuccess(feedback, input, successMessage);
        return true;
    }else{
        toFail(feedback, input, errorMessageLogin);
        return false;
    }
}

function validatePassword(input){
    var feedback = "#feedbackPassword";
    var value = $(input).val();
    checkPasswords();
    if(value.match(patternForPassword) !== null){
        toSuccess(feedback, input, successMessage);
        return true;
    }else{
        toFail(feedback, input, errorMessagePassword);
        return false;
    }
}

function validateEmail(input){
    var feedback = "#feedbackEmail";
    var value = $(input).val();
    if(value.match(patternForMail) !== null){
        toSuccess(feedback, input, successMessage);
        return true;
    }else{
        toFail(feedback, input, errorMessageMail);
        return false;
    }
}

function convertPasswordIntoTextAndInverse(input, button) {
    var type = $(input).attr("type");
    if(type == "password"){
        $(input).attr("type", "text");
        $(button).text("Hide");
    }else{
        $(input).attr("type", "password");
        $(button).text("Show");
    }
}

function checkPasswords(){
    var feedback = "#feedbackRepeatedPassword";
    var value = $("#repeatedPassword").val();
    if(value === $("#password").val()){
        toSuccess(feedback, "#repeatedPassword", successMessage);
        return true;
    }else{
        toFail(feedback, "#repeatedPassword", errorMessageRepeatedPassword);
        return false;
    }
}

function toSuccess(feedback, input, message){
    $(feedback).removeClass("invalid-feedback");
    $(feedback).addClass("valid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-invalid");
    $(input).addClass("is-valid");
}

function toFail(feedback, input, message){
    $(feedback).removeClass("valid-feedback");
    $(feedback).addClass("invalid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-valid");
    $(input).addClass("is-invalid");
}

function toNeutral(feedback, input){
    $(feedback).removeClass("invalid-feedback");
    $(feedback).removeClass("valid-feedback");
    $(feedback).text("");
    $(input).removeClass("is-invalid");
    $(input).removeClass("is-valid");
}