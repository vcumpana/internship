var currentPage = 0;
var notifications = [];
var noMoreNotifications = false;

$(document).ready(function () {
    loadNotifications();
    isUnreadMessages();
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() == $(document).height() && !noMoreNotifications) {
        loadNotifications();
    }
});

$("#searchNotifications").keyup(function(){
    searchInNotifications();
});

function loadNotifications() {
    $.ajax({
        type: "GET",
        url: "/notification/currentUser?page=" + (currentPage + 1),
        success: function (result) {
            if (result.length > 0) {
                currentPage++;
                notifications = result;
                printNotifications();
            } else {
                noMoreNotifications = true;
            }
        }
    });
}

function printNotifications() {
    for (var i = 0; i < notifications.length; i++) {
        var notification = "<div class='card border-primary mb-3'>";
        notification += "<div class='card-header'>Notification";
        if(notifications[i].status === "UNREAD"){
            notification += "<a class='float-right' href onclick='markAsRead(event, " + notifications[i].id + ")'>Mark as read</a>";
        }
        notification += "</div>";
        notification += "<div class='card-body'>";
        notification += "<p class='card-text'><strong>From: </strong>" + notifications[i].sender + "<br />";
        notification += "<strong>At: </strong>" + notifications[i].dateAndTime + "</p>";
        notification += "<p class='card-text'>" + notifications[i].message + "</p>";
        notification += "</div>";
        notification += "</div>";
        $("#notifications").append(notification);
    }
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

function markAsRead(event,  id){
    event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/notification/markAsRead?notificationId=" + id,
        success: function(){
            event.target.remove();
        }
    });
}

function searchInNotifications(){
    var search = $.trim($("#searchNotifications").val().toLowerCase());
    $(".card").each(function(){
        $(this).find(".card-body").removeHighlight();
        var text = $(this).find(".card-body").text().toLowerCase();
        if(text.indexOf(search) !== -1){
            $(this).find(".card-body").highlight(search);
            $(this).css('display', 'block');
        } else {
            $(this).css('display', 'none');
        }
    });
}

/* SCRIPT FOR HIGHLIGHT FROM INTERNET*/

jQuery.fn.highlight = function(pat) {
    function innerHighlight(node, pat) {
        var skip = 0;
        if (node.nodeType == 3) {
            var pos = node.data.toUpperCase().indexOf(pat);
            pos -= (node.data.substr(0, pos).toUpperCase().length - node.data.substr(0, pos).length);
            if (pos >= 0) {
                var spannode = document.createElement('span');
                spannode.className = 'bg-warning';
                var middlebit = node.splitText(pos);
                var endbit = middlebit.splitText(pat.length);
                var middleclone = middlebit.cloneNode(true);
                spannode.appendChild(middleclone);
                middlebit.parentNode.replaceChild(spannode, middlebit);
                skip = 1;
            }
        }
        else if (node.nodeType == 1 && node.childNodes && !/(script|style)/i.test(node.tagName)) {
            for (var i = 0; i < node.childNodes.length; ++i) {
                i += innerHighlight(node.childNodes[i], pat);
            }
        }
        return skip;
    }
    return this.length && pat && pat.length ? this.each(function() {
        innerHighlight(this, pat.toUpperCase());
    }) : this;
};

jQuery.fn.removeHighlight = function() {
    return this.find("span.bg-warning").each(function() {
        this.parentNode.firstChild.nodeName;
        with (this.parentNode) {
            replaceChild(this.firstChild, this);
            normalize();
        }
    }).end();
};
