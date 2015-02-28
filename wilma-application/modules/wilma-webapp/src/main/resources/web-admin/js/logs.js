$(document).ready(function() {

    $("#nav-wilma-log").addClass("active");

    // gets the version and sets the version label
    $.get('../config/public/version', function(data) {
        $("#span-version").text(data.wilmaVersion);
    });

    // GETs list of message files and creates links from each
    $.get('../config/public/logs', function(data) {
        var filesLenght = data.files.length;
        if (filesLenght > 0) {
            appendNewLogFileRow(data, 0);
        }
        for (var i = filesLenght - 1; i > 0; i--) {
            appendNewLogFileRow(data, i);
        }
    });

    $.get('../config/public/actualload', function(data) {
        $("#span-messages-logger-queue-info").text(data.loggerQueueSize);
        $("#span-messages-response-queue-info").text(data.responseQueueSize);
    });
});

function appendNewLogFileRow(data, i) {
    var li = $("<li>");
    var a = $("<a href='../config/public/logs/" + data.files[i] + "'>").text(data.files[i]);
    var btn = $("<a target='_blank' href='../config/public/logs/" + data.files[i]
                      + "?source=true'>").text("[Source]");
    li.append(a);
    li.append("     ");
    li.append(btn);
    $('#div-log-files').append(li);
}
