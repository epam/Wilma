$(document).ready(function() {

	$("#nav-home").addClass("active");
	$(".btn").prop('disabled', true);

	// gets the version and sets the version label
	$.get('../config/public/version', function(data) {
		$("#span-version").text(data.wilmaVersion);
	});

	// gets logging status and sets the buttons
	$.get('../config/public/logging/status', function(data) {
		if (data.requestLogging && data.responseLogging) {
			$("#btn-msglog-on").addClass("active");
		} else {
			$("#btn-msglog-off").addClass("active");
		}
	});

	// event handlers of message logging switch on/off buttons
	$("#btn-msglog-on").on("click", function(event) {
		$.get('../config/admin/logging/on', function(data) {
			console.log('Message logging turned on.');
		});
	});
	$("#btn-msglog-off").on("click", function(event) {
		$.get('../config/admin/logging/off', function(data) {
			console.log('Message logging turned off.');
		});
	});

	// GETs maintainer properties
	$.get('../config/public/maintainer', function(data) {
		$("#span-maintainer-method").html(data.maintainerMethod);
		$("#span-cron-expression").html(data.cronExpression);
		if (data.maintainerMethod === "filelimit") {
			$("#span-method-property").text(data.fileLimit);
		} else {
			$("#span-method-property").html(data.timeLimit);
		}
	});

	// gets operation mode status and sets the buttons
	$.get('../config/public/switch/status', function(data) {
		if (data.proxyMode) {
			$("#btn-switch-proxy").addClass("active");
		} else if (data.stubMode) {
			$("#btn-switch-stub").addClass("active");
		} else {
			$("#btn-switch-wilma").addClass("active");
		}
	});

	// event handlers of operation mode switch buttons
	$("#btn-switch-proxy").on("click", function(event) {
		$.get('../config/admin/switch/proxy', function(data) {
			console.log('The application is in proxy mode now.');
		});
	});
	$("#btn-switch-stub").on("click", function(event) {
		$.get('../config/admin/switch/stub', function(data) {
			console.log('The application is in stub mode now.');
		});
	});
	$("#btn-switch-wilma").on("click", function(event) {
		$.get('../config/admin/switch/wilma', function(data) {
			console.log('The application is in wilma mode now.');
		});
	});

	// gets interceptor usage status and sets the buttons
	$.get('../config/public/interceptor/status', function(data) {
		if (data.requestInterceptor && data.responseInterceptor) {
			$("#btn-interceptor-on").addClass("active");
		} else {
			$("#btn-interceptor-off").addClass("active");
		}
	});
	

	// event handlers of interceptor switch on/off buttons
	$("#btn-interceptor-on").on("click", function(event) {
		$.get('../config/admin/interceptor/on', function(data) {
			console.log('Interceptor usage is turned on.');
		});
	});
	$("#btn-interceptor-off").on("click", function(event) {
		$.get('../config/admin/interceptor/off', function(data) {
			console.log('Interceptor usage is turned off.');
		});
	});
	
	$.get('../config/public/localhost/status', function(data) {
		if (data.localhostMode) {
			$("#btn-localhost-blocking-on").addClass("active");
		} else {
			$("#btn-localhost-blocking-off").addClass("active");
		}
	});

	// event handlers of localhost mode on/off buttons
	$("#btn-localhost-blocking-on").on("click", function(event) {
		$.get('../config/admin/localhost/on', function(data) {
			console.log('Localhost blocking is turned on.');
		});
	});
	$("#btn-localhost-blocking-off").on("click", function(event) {
		$.get('../config/admin/localhost/off', function(data) {
			console.log('Localhost blocking is turned off.');
		});
	});
	
	$.get('../config/public/sequence/status', function(data) {
		if (data.sequenceHandlingUsage) {
			$("#btn-sequence-handling-on").addClass("active");
		} else {
			$("#btn-sequence-handling-off").addClass("active");
		}
	});
	
	// event handlers of sequence handling on/off buttons
	$("#btn-sequence-handling-on").on("click", function(event) {
		$.get('../config/admin/sequence/on', function(data) {
			console.log('Sequence handling is turned on.');
		});
	});
	$("#btn-sequence-handling-off").on("click", function(event) {
		$.get('../config/admin/sequence/off', function(data) {
			console.log('Sequence handling is turned off.');
		});
	});


	$.get('../config/public/adminstatus', function(data) {
		if (!data.adminStatus){
			$(".btn").prop('disabled', true);
		}else{
			$(".btn").prop('disabled', false);
		}
	});
});
