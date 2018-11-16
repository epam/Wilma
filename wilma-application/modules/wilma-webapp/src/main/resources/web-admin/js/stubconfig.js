$(document).ready(
		function() {

			$("#nav-stub").addClass("active");
			disableAdminButtons();
			
			// gets the version and sets the version label
			$.get('../../config/public/version', function(data) {
				$("#span-version").text(data.wilmaVersion);
			});

			getFileTypes();

			// event handlers for configuration uploader tabs
			$('#configuration-uploader-tabs a').click(function(e) {
				e.preventDefault();
				$(this).tab('show');
			});

			$('.fileinput-button input[type=file]').fileupload({
				maxFileSize : 500000,
				singleFileUploads : true,
				error : sendError,
				done : function(e, data) {
					if (data.result) {
						fileSendingSuccess(data.result);
						getFileTypes();
					} else {
						sendError();
					}
				}
			});

			function sendError(jqXHR, textStatus, errorThrown) {
				if ($.support.cors) {
					$('#fileupload-result-div .fileupload-result-message')
							.text(jqXHR.responseText);
				} else {
					$('#fileupload-result-div .fileupload-result-message')
							.text("File uploading failed!");
				}
				var alertBlock = $('#fileupload-result-div .alert');
				alertBlock.removeClass("alert-success");
				alertBlock.addClass("alert-error");
				alertBlock.show();
				$('.fileupload').fileupload('clear');
			}

			// handles successfully AJAX request
			function fileSendingSuccess(msg) {
				if ($.support.cors) {
					$('#fileupload-result-div .fileupload-result-message')
							.text(msg);
				} else {
					$('#fileupload-result-div .fileupload-result-message')
							.text("File successfully uploaded!");
				}
				var alertBlock = $('#fileupload-result-div .alert');
				alertBlock.removeClass("alert-error");
				alertBlock.addClass("alert-success");
				alertBlock.show();
				$('.fileupload').fileupload('clear');
			}

			// event handler for alert window hiding
			$('#fileupload-result-div .close').on("click", function() {
				var alertBlock = $('#fileupload-result-div .alert');
				alertBlock.hide();
			});
			
			setSecuritySettings();

		});

function getFileTypes(){
	getDialogDescriptors();
	getSpecificFileTypes('interceptor','#div-interceptor-files',noFilter);
	getSpecificFileTypes('condition-checker','#div-condition-checker-files',noFilter);
	getSpecificFileTypes('template-formatter','#div-template-formatter-files',noFilter);
	getSpecificFileTypes('template','#div-template-files',noFilter);
	getSpecificFileTypes('jar','#div-jar-files',jarFilter);
	getSpecificFileTypes('sequence-handler','#div-sequence-handler-files',noFilter);
}


// gets the dialog descriptors of the actual stub configuration
function getDialogDescriptors() {
	$.get('../../config/public/stubdescriptor?time=' + new Date(), function(data) {
		console.log(data);
		$('#bel').remove();
		var html = '<div id="bel">';
		for( var i = 0; i< data.configs.length; i++){
			html += '<table class="table table-striped table-bordered">';
			if(data.configs[i].active == 'true'){
				html += '<thead>';
            } else {
            	html += '<thead class="table-head-faded">';
            }
				html += '<tr><th>Stub configuration group: ' + data.configs[i].groupname 
                      + '</th><th class="button-group-center"><div class="btn-group" id="btn-group-onoff-stub">';
            if(data.configs[i].active == 'true'){
              	html += '<button class="btn btn-stub active btn-admin" id="btn-stubconfig-on">Enabled</button>'
              		  + '<button class="btn btn-stub btn-admin" id="btn-stubconfig-off" onclick="setStubConfigStatus(false,\'' + data.configs[i].groupname + '\');">Disabled</button>';
            } else {
                html += '<button class="btn btn-stub btn-admin" id="btn-stubconfig-on" onclick="setStubConfigStatus(true,\'' + data.configs[i].groupname + '\');">Enabled</button>'
                      + '<button class="btn btn-stub active red btn-admin" id="btn-stubconfig-off">Disabled</button>';
            }
            html += '</div></th><th class="button-group-right">'
            if(i == 0){
            	if(i == data.configs.length-1){
                    html += '<button class="btn btn-small btn-admin" disabled><i class="icon-chevron-up icon-white"></i></button>'
                          + '<button class="btn btn-small btn-admin" disabled><i class="icon-chevron-down icon-white"></i></button>';
            	} else {
                    html += '<button class="btn btn-small btn-admin" disabled><i class="icon-chevron-up icon-white"></i></button>'
                          + '<button class="btn btn-primary btn-small btn-admin" onclick="setStubConfigOrder(\'down\',\'' + data.configs[i].groupname + '\');" ><i class="icon-chevron-down icon-white"></i></button>'; 
            	}
            } else if(i == data.configs.length-1){
                html += '<button class="btn btn-primary btn-small btn-admin" onclick="setStubConfigOrder(\'up\',\'' + data.configs[i].groupname + '\');" ><i class="icon-chevron-up icon-white"></i></button>'
                      + '<button class="btn btn-small btn-admin" disabled><i class="icon-chevron-down icon-white"></i></button>';	
            }else {
                html += '<button class="btn btn-primary btn-small btn-admin" onclick="setStubConfigOrder(\'up\',\'' + data.configs[i].groupname + '\');" ><i class="icon-chevron-up icon-white"></i></button>'
                      + '<button class="btn btn-primary btn-small btn-admin" onclick="setStubConfigOrder(\'down\',\'' + data.configs[i].groupname + '\');" ><i class="icon-chevron-down icon-white"></i></button>';
            }
            
            html += '</th></thead><tbody>';
            html += '<tr><td style="font-weight:bold;">Dialog descriptor name</td>'
                  + '<td colspan="2" style="font-weight:bold;">Usage</td></tr>';
                
   		    for ( var j = 0; j < data.configs[i].dialogDescriptors.length; j++) {
			      html += '<tr><td>' + data.configs[i].dialogDescriptors[j].Name + '</td><td colspan="2">' + data.configs[i].dialogDescriptors[j].Usage
				    	+ '</td></tr>';
		    }

            if (data.configs[i].sequenceDescriptors.length > 0) {
                html += '<tr><td colspan="3" style="font-weight:bold;">Sequence descriptor name</td></tr>';
                for ( var j = 0; j < data.configs[i].sequenceDescriptors.length; j++) {
                    html += '<tr><td colspan="3">' + data.configs[i].sequenceDescriptors[j].Name + '</td></tr>';
                }
            }

            if (data.configs[i].interceptorDescriptors.length > 0) {
                html += '<tr><td colspan="3" style="font-weight:bold;">Interceptor name</td></tr>';
                for ( var j = 0; j < data.configs[i].interceptorDescriptors.length; j++) {
                    html += '<tr><td colspan="3">' + data.configs[i].interceptorDescriptors[j].Name + '</td></tr>';
                }
            }

   		    html += '<tr><td colspan="2"><button class="btn btn-danger btn-admin" onclick="dropStubConfig(\'' + data.configs[i].groupname + '\');"  >Drop</button></td>'
            + '<td class="button-group-right"><a style="color: #fff;" class="btn btn-primary btn-small" href="../config/public/stub/stubconfig.json?groupname=' + data.configs[i].groupname + '"><i class="icon-download icon-white"></i> Download</a>'
            + '<a style="color: #fff;" class="btn btn-primary btn-small" target="_blank" href="../config/public/stub/stubconfig.json?source=true&groupname=' + data.configs[i].groupname + '">Source</a>'
   		    + '</td></tr></tbody></table>';
		}
		html += '<a style="color: #fff; float: right;" class="btn btn-primary btn-small btn-admin" onclick="saveStubConfigs();">Persist all configuration groups</a>'+
		    '</div>';
		$('#tables').after(html);
	});
}

function saveStubConfigs(){
    $.get('../config/admin/stub/save').done( function() {
        console.log('Stub configurations have been saved.');
        $('#fileupload-result-div .fileupload-result-message').text("Saving was successful!");
        var alertBlock = $('#fileupload-result-div .alert');
        alertBlock.removeClass("alert-error");
        alertBlock.addClass("alert-success");
        alertBlock.show();
    }).fail( function() {
        $('#fileupload-result-div .fileupload-result-message').text("Saving has failed!");
        var alertBlock = $('#fileupload-result-div .alert');
        alertBlock.removeClass("alert-success");
        alertBlock.addClass("alert-error");
        alertBlock.show();
    });    
}

function dropStubConfig(groupname){
	$.get('../config/admin/stub/drop?groupname=' + groupname, function(data) {
		console.log(groupname + ' has been deleted.');
		getDialogDescriptors();
		setSecuritySettings();
	});
}

function setStubConfigOrder(direction, groupname){
	if(direction == "up"){
		$.get('../config/admin/stub/changeorder?direction=1&groupname=' + groupname, function(data) {
			console.log(groupname + ' config was moved up by one.');
			getDialogDescriptors();
			setSecuritySettings();
		});
	} else if(direction == "down") {
		$.get('../config/admin/stub/changeorder?direction=-1&groupname=' + groupname, function(data) {
			console.log(groupname + ' config was moved down by one.');
			getDialogDescriptors();
			setSecuritySettings();
		});
	}
}

function setStubConfigStatus(onOff, groupname){
	if(!onOff){
		$.get('../config/admin/stub/changestatus?nextstatus=false&groupname=' + groupname, function(data) {
			console.log(groupname + ' config was enabled');
			getDialogDescriptors();
			setSecuritySettings();
		});
	} else {
		$.get('../config/admin/stub/changestatus?nextstatus=true&groupname=' + groupname, function(data) {
			console.log(groupname + ' config was disabled');
			getDialogDescriptors();
			setSecuritySettings();
		});
	}
}

function disableAdminButtons(){
	$('.fileinput-button').removeClass("enabled");
	$('.fileinput-button').addClass("disabled");
	$('input[type="file"]').prop('disabled', true);
	$('.btn-admin').prop('disabled', true);
}

function setSecuritySettings(){
	$.get('../config/public/adminstatus', function(data) {
		if (!data.adminStatus){
			disableAdminButtons();
		}else{
			$('.fileinput-button').removeClass("disabled");
			$('.fileinput-button').addClass("enabled");
			$('input[type="file"]').prop('disabled', false);
			$('.btn-admin').prop('disabled', false);
		}
	});
}


//GETs the list of the specified type and creates links from each, it can filter the list based on the file names
function getSpecificFileTypes(type, id, fileNameFilter ){
	$.get('../../config/public/stubconfig?type='+type+'&time=' + new Date(), function(
			data) {
		if (data.toString().indexOf("Invalid type") == -1) {
			$(id).html("");
			for ( var i = 0; i < data.files.length; i++) {
				if (fileNameFilter(data.files[i]) ){
					var li = $("<li>");
					var a = $(
							"<a href='../../config/public/stubconfig/" + data.files[i]
									+ "?type="+type+"'>").text(data.files[i]);
					li.append(a);
					$(id).append(li);
				}
			}
		} else {
			console.log(data);
		}
	});
}

function noFilter(fileName){
	return true;
}

function jarFilter(fileName){
	var extension = fileName.slice(-4);
	return extension === ".jar";
}
