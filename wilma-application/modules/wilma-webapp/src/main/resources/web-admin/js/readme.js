$(document).ready(function() {

	$("#nav-guide").addClass("active");

	// gets the version and sets the version label
	$.get('../config/public/version', function(data) {
		$("#span-version").text(data.wilmaVersion);
	});

	// GETs the wilma readme url and the text of the url
	$.get('../config/public/readme', function(data) {
			var a = $(
					"<a target='_blank'  href='" + data.readmeUrl
							+ "'>").text(data.readmeText);
			$('#readme').append(a);
	});
});
