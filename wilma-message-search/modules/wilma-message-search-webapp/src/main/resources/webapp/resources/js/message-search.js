$(document).ready(function() {
  
  $("#nav-home").addClass("active");
	
	$.get('/version', function(data) {
		$("#span-version").text(data.messageSearchVersion);
	});

    $('#searchButton').on("click", search);

    $('#searchedText').on('keypress', function(event) {
        if (event.which == '13') {
            search(event);
        }
    });

	// event handler for alert window hiding
    $('#search-result-div .close').on("click", function() {
        hideElement('#search-result-div .alert');
    });

	$('#search-info-div .close').on("click", function() {
        hideElement('#search-info-div .alert');
    });

    $('#download-all-button').hide();
});

function hideElement (element) {
    var alertBlock = $(element);
    alertBlock.hide();
}

function search(e) {
	hideElement('#search-info-div .alert');
	hideElement('#search-result-div .alert');
	var searchedText = $('#searchedText').val();
    console.log("send text:" + searchedText);
    if (searchedText != "") {
        $.ajax({
            url : './search',
            type : 'POST',
            data : ({
                searchedText : searchedText
            }),
            success : processSearchResult,
            beforeSend : beforeSendSearchRequest,
            complete : requestComplete,
            statusCode:{
                206: function(){
            	      $('#search-info-div .search-info-message').text("Searching has run with partial index.");
                      var alertBlock = $('#search-info-div .alert');
                      alertBlock.removeClass("alert-success");
                      alertBlock.addClass("alert-info");
                      alertBlock.show();
                },
                400: function(xhr, status){
                    $('#search-result-div .search-result-message').text(xhr.responseText);
                    var alertBlock = $('#search-result-div .alert');
                    alertBlock.removeClass("alert-success");
                    alertBlock.addClass("alert-error");
                    alertBlock.show();
                }
            }
            }).fail( function(xhr, status) {
            $('#search-result-div .search-result-message').text("Searching has failed! Error code:" + xhr.status +" Please contact the developers.");
            var alertBlock = $('#search-result-div .alert');
            alertBlock.removeClass("alert-success");
            alertBlock.addClass("alert-error");
            alertBlock.show();
        });
    } else {
        setNoResultFound();
    }
}

function setNoResultFound() {
    $('#searchResult').html("<td colspan='3'>No files found</td>");
    $('#download-all-button').hide();
}

function processSearchResult(data) {
    hideElement('#search-info-div .alert');
    console.log(data);
    var searchResult = "";
    for (var i = 0; i < data['files'].length; i++) {
        var count = i + 1;
        searchResult += "<tr><td class='span1'>" + count + "</td>";
        for (var j = 0; j < 2; j++) {
            if (data['files'][i][j].indexOf('NOTEXISTS') == -1) {
                searchResult += "<td class='span1'>"
                        + "<a href='/downloadFile?filePath="
                        + data['files'][i][j] + "'>" + data['files'][i][j]
                        + "</a> "
                        + "<a target='_blank' href='/openFile?filePath="
                        + data['files'][i][j] + "'>[Source]</a>" + "</td>";
            } else {
                var pairWithoutPostFix = data['files'][i][j].substring(0,
                        data['files'][i][j].length - 9);
                searchResult += "<td class='span1'>" + pairWithoutPostFix
                        + "</td>";
            }
        }
        searchResult += "</tr>";
    }
    if (searchResult == '') {
        setNoResultFound();
    } else {
        $('#searchResult').html(searchResult);
        $('#download-all-button').show();
    }
}

function beforeSendSearchRequest() {
    $('#searchedText').attr('disabled', 'disabled');
}

function requestComplete() {
    $('#searchedText').removeAttr('disabled');
}