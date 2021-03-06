var previousName = "";
var previousSurname = "";
var previousValue = "";
var successMessage = "<strong>Success!</strong> Looks good!";
var patternForPassword = /^(?=.*[A-Z])(?=.*\d)(?=.*[._?!])[A-Za-z\d._?!]{8,}$/;
var errorMessagePassword = "<strong>Warning!</strong> Password must contain at least 8 chars, that includes at least one number, upper case character and symbol.";
var errorMessageRepeatedPassword = "<strong>Warning!</strong> Password must be the same.";

$(document).ready(function () {
    isUnreadMessages();
    downloadBalance();
});

$(document).ajaxStart(function () {
    $("#pleaseWaitDialog").modal('show');
});

$(document).ajaxComplete(function () {
    $("#pleaseWaitDialog").modal('hide');
});

$("a[scopeInThisDoc='update']").click(function () {
    var value = $(this).attr("value");
    hideCurrentButton(value);
    showConfirmButtons(value);
    showDeclineButtons(value);
    restoreSpanText(value);
    transformTextIntoInputs(value);
    previousValue = value;
});

$("a[scopeInThisDoc='decline']").click(function () {
    var value = $(this).attr("value");
    restoreSpanText(value);
    $("a[scopeInThisDoc='confirm'][value='" + value + "']").each(function () {
        $(this).css('display', 'none');
    });
    $("a[scopeInThisDoc='update'][value='" + value + "']").each(function () {
        $(this).css('display', 'inline');
    });
    $(this).css('display', 'none');
});

$("a[scopeInThisDoc='confirm']").click(function () {
    var value = $(this).attr("value");
    setNewValue(value);
    $("a[scopeInThisDoc='decline'][value='" + value + "']").each(function () {
        $(this).css('display', 'none');
    });
    $("a[scopeInThisDoc='update'][value='" + value + "']").each(function () {
        $(this).css('display', 'inline');
    });
    $(this).css('display', 'none');
    updateUser();
});

$("#addMoney").click(function () {
    $("#modalForMoney").modal("show");
});

$("#addAction").click(function () {
    data = {
        "sum": parseFloat($("#money").val())
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/bank/addmoney/",
        data: JSON.stringify(data),
        success: function (rs) {
            console.log(rs);
            var balance = JSON.parse(rs).balance;
            $("#balance").text(balance.toFixed(2) + " USD");
            $("#balanceInCabinet").text(balance.toFixed(2) + " USD");
            $("#modalForMoney").modal("hide");
        }
    });
});

function hideCurrentButton(value) {
    $("a[scopeInThisDoc='update']").each(function () {
        if ($(this).attr("value") === value) {
            $(this).css('display', 'none');
        } else if ($(this).css('display') === 'none') {
            $(this).css('display', 'inline');
        }
    });
}

function showConfirmButtons(value) {
    $("a[scopeInThisDoc='confirm']").each(function () {
        if ($(this).attr("value") === value) {
            $(this).css('display', 'inline');
        } else {
            $(this).css('display', 'none');
        }
    });
}

function showDeclineButtons(value) {
    $("a[scopeInThisDoc='decline']").each(function () {
        if ($(this).attr("value") === value) {
            $(this).css('display', 'inline');
        } else {
            $(this).css('display', 'none');
        }
    });
}

function transformTextIntoInputs(value) {
    $("span[scopeInThisDoc='text'][value='" + value + "']").each(function () {
        var html = $(this).html();
        var input = $("<input id='" + value + "Input' type='text' class='form-control' style='width: 50%; display:inline'/>");
        if (value === "name") {
            previousName = html;
        }
        if (value === "surname") {
            previousSurname = html;
        }
        input.val(html);
        $(this).html(input);
    });
}

function restoreSpanText(value) {
    $("span[scopeInThisDoc='text'][value='" + previousValue + "']").each(function () {
        if (previousValue === "name") {
            $(this).html(previousName);
        }
        if (previousValue === "surname") {
            $(this).html(previousSurname);
        }
    });
}

function setNewValue(value) {
    $("span[scopeInThisDoc='text'][value='" + previousValue + "']").each(function () {
        if (previousValue === "name") {
            $(this).html($("#" + value + "Input").val());
        }
        if (previousValue === "surname") {
            $(this).html($("#" + value + "Input").val());
        }
    });
}

function updateUser() {
    var surname = $("#surname").text();
    var name = $("#name").text();
    var email = $("#email").text();
    var data = {
        "surname": surname,
        "name": name,
        "email": email
    };
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "/user/selfUpdate/",
        data: JSON.stringify(data),
        success: function () {
            $("#successUpdate").show();
            window.setTimeout(function () {
                $('#successUpdate').hide();
            }, 2300);
            previousName = data.name;
            previousSurname = data.surname;
        }
    })
}

$("#updatePassword").click(function () {
    var errors = 0;
    if (validatePassword("#newPassword") == false) {
        errors++;
    }
    if (checkPasswords() == false) {
        errors++;
    }
    if (errors > 0) {
        return;
    }
    var data = {
        "oldPassword": $("#oldPassword").val(),
        "newPassword": $("#newPassword").val(),
        "repeatedNewPassword": $("#repeatedNewPassword").val()
    };
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "/user/selfUpdatePassword",
        data: JSON.stringify(data),
        statusCode: {
            200: function () {
                $("#successUpdatePassword").show();
                window.setTimeout(function () {
                    $('#successUpdatePassword').hide();
                }, 2300);
                hideForm();
                toNeutral("#feedbackNewPassword", "#newPassword");
                toNeutral("#feedbackRepeatedNewPassword", "#repeatedNewPassword");
            },
            400: function () {
                $("#failUpdatePassword").show();
                window.setTimeout(function () {
                    $('#failUpdatePassword').hide();
                }, 2300);
                console.clear();
            }
        }
    });
});

$("#cancelPassword").click(function () {
    hideForm();
    toNeutral("#feedbackNewPassword", "#newPassword");
    toNeutral("#feedbackRepeatedNewPassword", "#repeatedNewPassword");
});

$("#showPasswordForm").click(function () {
    $("#passwordForm").css("display", "block");
    $("#showPasswordForm").css("display", "none");
    $('html, body').animate({
        scrollTop: $("#passwordForm").offset().top + $("#passwordForm").height()
    }, 2000);
});

function hideForm() {
    $("#oldPassword").val("");
    $("#newPassword").val("");
    $("#repeatedNewPassword").val("");
    $("#passwordForm").css("display", "none");
    $("#showPasswordForm").css("display", "inline");
}

$("#newPassword").keyup(function () {
    validatePassword(this);
});

$("#repeatedNewPassword").keyup(function () {
    checkPasswords();
});

function validatePassword(input) {
    var feedback = "#feedbackNewPassword";
    var value = $(input).val();
    checkPasswords();
    if (value.match(patternForPassword) !== null) {
        toSuccess(feedback, input, successMessage);
        return true;
    } else {
        toFail(feedback, input, errorMessagePassword);
        return false;
    }
}

function checkPasswords() {
    var feedback = "#feedbackRepeatedNewPassword";
    var value = $("#repeatedNewPassword").val();
    if (value === $("#newPassword").val()) {
        toSuccess(feedback, "#repeatedNewPassword", successMessage);
        return true;
    } else {
        toFail(feedback, "#repeatedNewPassword", errorMessageRepeatedPassword);
        return false;
    }
}

function toSuccess(feedback, input, message) {
    $(feedback).removeClass("invalid-feedback");
    $(feedback).addClass("valid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-invalid");
    $(input).addClass("is-valid");
}

function toFail(feedback, input, message) {
    $(feedback).removeClass("valid-feedback");
    $(feedback).addClass("invalid-feedback");
    $(feedback).html(message);
    $(input).removeClass("is-valid");
    $(input).addClass("is-invalid");
}

function toNeutral(feedback, input) {
    $(feedback).removeClass("invalid-feedback");
    $(feedback).removeClass("valid-feedback");
    $(feedback).text("");
    $(input).removeClass("is-invalid");
    $(input).removeClass("is-valid");
}

function isUnreadMessages() {
    $.ajax({
        type: "GET",
        url: "/notification/getNumberOfUnread",
        success: function (result) {
            if (result > 0) {
                $("#unreadMessages").css('display', 'inline');
            }
        }
    });
}

function downloadBalance() {
    $.ajax({
        type: "POST",
        url: "/bank/balance",
        success: function (result) {
            $("#balance").addClass("balance");
            $("#balance").text("Balance: " +result.balance.toFixed(2) + " USD");
            $("#balanceInCabinet").text(result.balance.toFixed(2) + " USD");
        }
    });
}

$("#contact").click(function(){
    window.open("/contact");
});