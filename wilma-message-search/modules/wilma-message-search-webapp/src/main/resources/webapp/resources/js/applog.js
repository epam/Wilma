$(document).ready(function() {
  $("#nav-application-log").addClass("active");
  
  $.get('/version', function(data) {
      $("#span-version").text(data.messageSearchVersion);
  });
  
  $.get('/loadinformation', function(data) {
      $("#span-queue-info").text(data.loadInformation);
  });
    
  $.get('/logs', function(data) {
	data.files.sort();
    var firstElement = data.files.shift();
    data.files = data.files.reverse();
    data.files.unshift(firstElement);
    for (var i = 0; i < data.files.length; i++) {
      var li = $("<li>");
      var a = $("<a href='logs/" + data.files[i] + "'>").text(data.files[i]);
      var btn = $("<a target='_blank' href='logs/" + data.files[i]
          + "?source=true'>").text("[Source]");
      li.append(a);
      li.append("     ");
      li.append(btn);
      $('#div-log-files').append(li);
    }
  });
});