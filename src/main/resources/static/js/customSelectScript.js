$(document).click(function () {
    closeAllCustomSelects(event);
});
$(".live-search").on('mousedown', function (event) {
    event.preventDefault();
    if (isThisSelectOpen(this)) {
        return;
    }
    closeAnotherSelects();
    $(this).addClass('active-custom-select');
    var mySelect = createCustomSelect(event.target);
    $(this).after(mySelect);
});

function closeAllCustomSelects(event) {
    $(".custom-select").each(function () {
        if (!$(event.target).hasClass('live-search') && !($(event.target).hasClass('custom-select-input'))) {
            $(this).remove();
            $(".active-custom-select").each(function () {
                $(this).removeClass("active-custom-select");
            });
        }
    });
}

function closeAnotherSelects() {
    $(".active-custom-select").each(function () {
        $(this).removeClass("active-custom-select");
        $(this).next().remove();
    });
}

function createCustomSelect(select) {
    var mySelect = "<div class='custom-select'>";
    mySelect += "<input type='text' class='form-control custom-select-input'>";
    $(select).children("option").each(function () {
        mySelect += "<span value='" + this.value + "' class='custom-select-option'>" + this.text + "</span>";
    });
    mySelect += "</div>";
    return mySelect;
}

function isThisSelectOpen(select) {
    if ($(select).hasClass('active-custom-select')) {
        $(select).removeClass('active-custom-select');
        $(".custom-select").each(function () {
            $(this).remove();
        });
        return true;
    } else {
        return false;
    }
}

$(document).on("click", ".custom-select-option", function () {
    var value = $(this).attr('value');
    $(".active-custom-select").each(function () {
        $(this).val(value);
        $(this).removeClass("active-custom-select");
    });
});

$(document).on("keyup", ".custom-select-input", function () {
    var value = $(this).val().toLowerCase();
    $(".custom-select-option").each(function () {
        var text = $(this).text().toLowerCase();
        if (text.indexOf(value) !== -1) {
            $(this).css('display', 'block');
        } else {
            $(this).css('display', 'none');
        }
    });
})