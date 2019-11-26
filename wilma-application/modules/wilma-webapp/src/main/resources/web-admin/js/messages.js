$(document).ready(function() {

	$("#nav-msg-log").addClass("active");

	// gets the version and sets the version label
	$.get('../config/public/version', function(data) {
		$("#span-version").text(data.wilmaVersion);
	});
	
	// gets logging status and sets the buttons
	$.get('../config/public/logging/status', function(data) {
		if (data.requestLogging && data.responseLogging) {
			$("#span-messages-on").addClass("label-success");
			$("#span-messages-on").removeClass("label-important");
			$("#span-messages-on").html("ON");
		} else {
			$("#span-messages-on").removeClass("label-success");
			$("#span-messages-on").addClass("label-important");
			$("#span-messages-on").html("OFF");
		}
	});
	
	$.get('../config/public/actualload', function(data) {
	    $("#span-messages-filesCount").text(data.deletedFilesCount);
		$("#span-messages-messagesCount").text(data.countOfMessages);
	});	

	// GETs list of message files and creates links from each
	$.get('../config/public/messages', function(data) {
		for ( var i = 0; i < data.files.length; i++) {
			var li = $("<li>");
			var a = $(
					"<a href='../config/public/messages/" + data.files[i]
							+ "'>").text(data.files[i]);
			var btn = $(
					"<a target='_blank' href='../config/public/messages/"
							+ data.files[i] + "?source=true'>").text(
					"[Source]");
			li.append(a);
			li.append("     ");
			li.append(btn);
			$('#div-message-files').append(li);
		}
	});
});
